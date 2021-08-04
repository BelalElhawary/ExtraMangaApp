package com.bebo.manga;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bebo.manga.Common.Common;
import com.bebo.manga.adapter.MyCategoryAdapter;
import com.bebo.manga.service.ComicData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ComicDetails extends AppCompatActivity {
    ImageView imageView;
    ImageView imageViewBanner;
    TextView name;
    TextView wr;
    TextView sum;
    RecyclerView recycler_category;
    Button chapters1;

    ToggleButton trend;
    ToggleButton favorite;

    int TrendValue = 0 ;


    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic_details);

        Common.TREND = ComicData.LoadTrend(getBaseContext());
        Common.FAVORITE = ComicData.LoadFavorite(getBaseContext());

        imageView = (ImageView)findViewById(R.id.det_img);
        imageViewBanner = (ImageView)findViewById(R.id.det_banner);
        name = (TextView)findViewById(R.id.det_name);
        wr = (TextView)findViewById(R.id.det_wr);
        sum = (TextView)findViewById(R.id.det_sum);
        chapters1 = (Button)findViewById(R.id.btn_chapters1);


        if(Common.online)
        {
            Picasso.get().load(Common.comicSelected.Image).into(imageView);
            Picasso.get().load(Common.comicSelected.Image).into(imageViewBanner);
        }else {
            Bitmap bitmap = BitmapFactory.decodeFile(Common.comicSelected.Image);
            imageView.setImageBitmap(bitmap);
            imageViewBanner.setImageBitmap(bitmap);
        }
        name.setText(Common.comicSelected.Name);
        wr.setText(Common.comicSelected.Writer);
        sum.setText(Common.comicSelected.Summary);

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recycler_category = (RecyclerView) findViewById(R.id.recycler_category);
        recycler_category.setHasFixedSize(true);
        recycler_category.setLayoutManager(new GridLayoutManager(this, 1));
        recycler_category.setLayoutManager(horizontalLayoutManager);
        MyCategoryAdapter categoryAdapter = new MyCategoryAdapter(this.getBaseContext(), Common.comicSelected.Category);
        recycler_category.setAdapter(categoryAdapter);

        chapters1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartAct();
            }
        });


        if(Common.online) {
            databaseReference = FirebaseDatabase.getInstance().getReference("Comic");
            trend = (ToggleButton) findViewById(R.id.trend);
            favorite = (ToggleButton) findViewById(R.id.favorite);
            Common.TREND = ComicData.LoadTrend(getBaseContext());
            if (Common.TREND != null) {
                for (String string : Common.TREND) {
                    if (Common.comicSelected.Name.equals(string)) {
                        trend.setChecked(true);
                        break;
                    }
                }
                trend.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Common.TREND = ComicData.LoadTrend(getBaseContext());
                        if(trend.isChecked())
                        {
                            List<String> strings = Common.TREND ;
                            strings.add(Common.comicSelected.Name);
                            for(int i = 0; i < Common.comicList.size(); i++)
                            {
                                if(Common.comicList.get(i).Name.equals(Common.comicSelected.Name))
                                {
                                    DatabaseReference scoreRef = databaseReference.child(String.valueOf(i)).child("Trend");;
                                    scoreRef.runTransaction(new Transaction.Handler() {
                                        @Override
                                        public Transaction.Result doTransaction(MutableData mutableData) {
                                            Integer score = mutableData.getValue(Integer.class);
                                            if (score == null) {
                                                return Transaction.success(mutableData);
                                            }
                                            mutableData.setValue(score + 1);
                                            return Transaction.success(mutableData);
                                        }
                                        @Override
                                        public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {}
                                    });
                                    ComicData.SaveTrend(strings, getBaseContext());
                                    break;
                                }
                            }
                        }else {
                            List<String> strings = Common.TREND;
                            strings.remove(Common.comicSelected.Name);
                            for(int i = 0; i < Common.comicList.size(); i++)
                            {
                                if(Common.comicList.get(i).Name.equals(Common.comicSelected.Name))
                                {
                                    DatabaseReference scoreRef = databaseReference.child(String.valueOf(i)).child("Trend");;
                                    scoreRef.runTransaction(new Transaction.Handler() {
                                        @Override
                                        public Transaction.Result doTransaction(MutableData mutableData) {
                                            Integer score = mutableData.getValue(Integer.class);
                                            if (score == null) {
                                                return Transaction.success(mutableData);
                                            }
                                            mutableData.setValue(score - 1);
                                            return Transaction.success(mutableData);
                                        }
                                        @Override
                                        public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {}
                                    });
                                    ComicData.SaveTrend(strings, getBaseContext());
                                    break;
                                }
                            }
                        }
                    }
                });
            }
            else{
                Common.TREND = new ArrayList<>();
                trend.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(trend.isChecked())
                        {
                            List<String> strings = Common.TREND;
                            strings.add(Common.comicSelected.Name);
                            for(int i = 0; i < Common.comicList.size(); i++)
                            {
                                if(Common.comicList.get(i).Name.equals(Common.comicSelected.Name))
                                {
                                    DatabaseReference scoreRef = databaseReference.child(String.valueOf(i)).child("Trend");;
                                    scoreRef.runTransaction(new Transaction.Handler() {
                                        @Override
                                        public Transaction.Result doTransaction(MutableData mutableData) {
                                            Integer score = mutableData.getValue(Integer.class);
                                            if (score == null) {
                                                return Transaction.success(mutableData);
                                            }
                                            mutableData.setValue(score + 1);
                                            return Transaction.success(mutableData);
                                        }
                                        @Override
                                        public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {}
                                    });
                                    ComicData.SaveTrend(strings, getBaseContext());
                                    break;
                                }
                            }
                        }else {
                            List<String> strings = ComicData.LoadTrend(getBaseContext());
                            strings.remove(Common.comicSelected.Name);
                            for(int i = 0; i < Common.comicList.size(); i++)
                            {
                                if(Common.comicList.get(i).Name.equals(Common.comicSelected.Name))
                                {
                                    DatabaseReference scoreRef = databaseReference.child(String.valueOf(i)).child("Trend");;
                                    scoreRef.runTransaction(new Transaction.Handler() {
                                        @Override
                                        public Transaction.Result doTransaction(MutableData mutableData) {
                                            Integer score = mutableData.getValue(Integer.class);
                                            if (score == null) {
                                                return Transaction.success(mutableData);
                                            }
                                            mutableData.setValue(score - 1);
                                            return Transaction.success(mutableData);
                                        }
                                        @Override
                                        public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {}
                                    });
                                    ComicData.SaveTrend(strings, getBaseContext());
                                    break;
                                }
                            }
                        }
                    }
                });
            }
            Common.FAVORITE = ComicData.LoadFavorite(getBaseContext());
            if(Common.FAVORITE != null){
                for (String string : Common.FAVORITE) {
                    if (Common.comicSelected.Name.equals(string)) {
                        favorite.setChecked(true);
                        break;
                    }
                }
                favorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Common.FAVORITE = ComicData.LoadFavorite(getBaseContext());
                        if(favorite.isChecked())
                        {
                            List<String> strings = Common.FAVORITE ;
                            strings.add(Common.comicSelected.Name);
                            ComicData.SaveFavorite(strings, getBaseContext());
                        }else {
                            List<String> strings = Common.FAVORITE;
                            strings.remove(Common.comicSelected.Name);
                            ComicData.SaveFavorite(strings, getBaseContext());
                        }
                    }
                });
            }
            else {
                Common.FAVORITE = new ArrayList<>();
                favorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(favorite.isChecked())
                        {
                            List<String> strings = Common.FAVORITE ;
                            strings.add(Common.comicSelected.Name);
                            ComicData.SaveFavorite(strings, getBaseContext());
                        }else {
                            List<String> strings = Common.FAVORITE;
                            strings.remove(Common.comicSelected.Name);
                            ComicData.SaveFavorite(strings, getBaseContext());
                        }
                    }
                });
            }
        }
    }
    private void StartAct()
    {
        Intent intent = new Intent(this, ChaptersActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }
}