package com.bebo.manga;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bebo.manga.Common.Common;
import com.bebo.manga.Interface.IBannerLoadDone;
import com.bebo.manga.Interface.IComicLoadDone;
import com.bebo.manga.Interface.IMixLoadDone;
import com.bebo.manga.adapter.MyComicAdapter;
import com.bebo.manga.adapter.MyMixAdapter;
import com.bebo.manga.adapter.MySliderAdapter;
import com.bebo.manga.model.Comic;
import com.bebo.manga.model.Mix;
import com.bebo.manga.model.ProfileSetting;
import com.bebo.manga.service.CheckInternetConnection;
import com.bebo.manga.service.PicassoLoadingService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import ss.com.bannerslider.Slider;

import static android.content.Context.MODE_PRIVATE;


public class HomeFragment extends Fragment implements IBannerLoadDone, IComicLoadDone, IMixLoadDone {

    Slider slider;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recycler_comic;
    RecyclerView recycler_mix;
    //TextView txt_comic;
    //DataBase
    DatabaseReference banners, comics, mixes;

    //Listener
    IBannerLoadDone bannerLoadDone;
    IComicLoadDone comicLoadDone;
    IMixLoadDone mixLoadDone;

    android.app.AlertDialog alertDialog;

    public static final int REQUEST_CODE = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        //Init Database
        banners = FirebaseDatabase.getInstance().getReference("Banners");
        comics = FirebaseDatabase.getInstance().getReference("Comic");
        mixes = FirebaseDatabase.getInstance().getReference("Mix");
        //Init
        bannerLoadDone = this;
        comicLoadDone = this;
        mixLoadDone = this;

        slider = (Slider) view.findViewById(R.id.slider);
        Slider.init(new PicassoLoadingService());

        Common.profileSetting = loadProfileSettingData();

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_to_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.app_gr_1, R.color.app_gr_2);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                CheckInternetConnection.check(getContext());
                loadBanner();
                loadComic();
                loadMix();
            }
        });

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                CheckInternetConnection.check(getContext());
                if(Common.profileSetting != null)
                {
                    if(Common.comicList == null || Common.profileSetting.autoRefresh)
                    {
                        loadBanner();
                        loadComic();
                        loadMix();
                    }else {
                        onBannerLoadDoneListener(Common.bannerList);
                        recycler_comic.setAdapter(new MyComicAdapter(getContext(), Common.comicList, false, true));
                    }
                }else {
                    if(Common.comicList == null)
                    {
                        loadBanner();
                        loadComic();
                        loadMix();
                    }else {
                        onBannerLoadDoneListener(Common.bannerList);
                        recycler_comic.setAdapter(new MyComicAdapter(getContext(), Common.comicList, false, true));
                    }
                }
            }
        });

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recycler_comic = (RecyclerView) view.findViewById(R.id.recycler_comic);
        recycler_comic.setHasFixedSize(true);
        recycler_comic.setLayoutManager(new GridLayoutManager(view.getContext(), 1));
        recycler_comic.setLayoutManager(horizontalLayoutManager);

        LinearLayoutManager horizontalLayoutManagerMix = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recycler_mix = (RecyclerView) view.findViewById(R.id.recycler_mix);
        recycler_mix.setHasFixedSize(true);
        recycler_mix.setLayoutManager(new GridLayoutManager(view.getContext(), 1));
        recycler_mix.setLayoutManager(horizontalLayoutManagerMix);
        //txt_comic = (TextView)findViewById(R.id.txt_comic);

        verifyPermission();
        loadProfileSettingData();
        Common.comicOfflineList = loadData();
        return view;
    }

    private ProfileSetting loadProfileSettingData()
    {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("shared preferences profile", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("profile setting", null);
        Type type = new TypeToken<ProfileSetting>() {}.getType();
        return gson.fromJson(json, type);
    }

    private List<Comic> loadData()
    {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("download", null);
        Type type = new TypeToken<List<Comic>>() {}.getType();
        return gson.fromJson(json, type);
    }

    private void verifyPermission(){
        String[] permissions  = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET};
        if(ContextCompat.checkSelfPermission(getContext().getApplicationContext(), permissions[0]) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getContext().getApplicationContext(), permissions[1]) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getContext().getApplicationContext(), permissions[2]) == PackageManager.PERMISSION_GRANTED){

        }else {
            requestPermissions(permissions, REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        verifyPermission();
    }

    private void loadBanner()
    {
        banners.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> bannerList = new ArrayList<>();
                for(DataSnapshot bannerSnapShot:snapshot.getChildren())
                {
                    String image = bannerSnapShot.getValue(String.class);
                    bannerList.add(image);
                }
                Common.bannerList = bannerList;
                bannerLoadDone.onBannerLoadDoneListener(bannerList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadComic()
    {
        //Show Dialog
        if(Common.online)
        {
            alertDialog = new SpotsDialog.Builder().setContext(getContext())
                    .setCancelable(false)
                    .setMessage("Please wait...")
                    .build();
            if(!swipeRefreshLayout.isRefreshing())
                alertDialog.show();
        }

        comics.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final List<Comic> comic_load = new ArrayList<>();
                for(DataSnapshot comicSnapShot:snapshot.getChildren())
                {
                    Comic comic = comicSnapShot.getValue(Comic.class);
                    comic_load.add(comic);
                }
                comicLoadDone.onComicLoadDoneListener(comic_load);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void loadMix()
    {
        mixes.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final List<Mix> mix_load = new ArrayList<>();
                for(DataSnapshot mixSnapShot:snapshot.getChildren())
                {
                    Mix mix = mixSnapShot.getValue(Mix.class);
                    mix_load.add(mix);
                }
                mixLoadDone.onMixLoadDoneListener(mix_load);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBannerLoadDoneListener(List<String> banners) {
        slider.setAdapter(new MySliderAdapter(banners));
    }

    @Override
    public void onComicLoadDoneListener(List<Comic> comicList) {
        Common.comicList = comicList;

        recycler_comic.setAdapter(new MyComicAdapter(getContext(), comicList, false, true));

        if(!swipeRefreshLayout.isRefreshing())
            alertDialog.dismiss();
    }

    @Override
    public void onMixLoadDoneListener(List<Mix> mixList)
    {
        Common.mixList = mixList;

        recycler_mix.setAdapter(new MyMixAdapter(getContext(), mixList));

        if(!swipeRefreshLayout.isRefreshing())
            alertDialog.dismiss();
    }
}
