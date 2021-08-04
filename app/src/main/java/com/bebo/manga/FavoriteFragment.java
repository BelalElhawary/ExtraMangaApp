package com.bebo.manga;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bebo.manga.Common.Common;
import com.bebo.manga.adapter.MyComicAdapter;
import com.bebo.manga.adapter.MyDownloadAdapter;
import com.bebo.manga.model.Comic;
import com.bebo.manga.service.CheckInternetConnection;
import com.bebo.manga.service.InternetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class FavoriteFragment extends Fragment {
    RecyclerView recyclerView;
    MyComicAdapter comicAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_favorite, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.downloads);
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_to_refresh);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));

        CheckInternetConnection.check(getContext());

        swipeRefreshLayout.setColorSchemeResources(R.color.app_gr_1, R.color.app_gr_2);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                if(Common.online){ LoadFavoriteList(); }else { InternetDialog.show(getContext(), getActivity()); }
            }
        });

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if(Common.online){ LoadFavoriteList(); }else { InternetDialog.show(getContext(), getActivity()); }
            }
        });


        return view;
    }

    void LoadFavoriteList()
    {
        if(loadFavData() != null) {
            List<Comic> comics = new ArrayList<>();
            List<String> strings = loadFavData();
            for (int i = 0; i < strings.size(); i++) {
                for (int j = 0; j < Common.comicList.size(); j++) {
                    if (Common.comicList.get(j).Name.equals(strings.get(i))) {
                        comics.add(Common.comicList.get(j));
                    }
                }
            }
            comicAdapter = new MyComicAdapter(getContext(), comics, true, Common.online);
            comicAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(comicAdapter);
        }
    }

    private List<String> loadFavData()
    {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("shared preferences fav", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("fav", null);
        Type type = new TypeToken<List<String>>() {}.getType();
        return gson.fromJson(json, type);
    }


}
