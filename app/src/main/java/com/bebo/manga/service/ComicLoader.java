package com.bebo.manga.service;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bebo.manga.Common.Common;
import com.bebo.manga.model.Comic;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ComicLoader{
    public static List<Comic> comicList = new ArrayList<>();

    public static void LoadComicsFormServer(Context context)
    {
        DatabaseReference comics = FirebaseDatabase.getInstance().getReference("Comic");

        comics.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final List<Comic> comic_load = new ArrayList<>();
                for(DataSnapshot comicSnapShot:snapshot.getChildren())
                {
                    Comic comic = comicSnapShot.getValue(Comic.class);
                    comic_load.add(comic);
                }
                Common.comicList = comicList;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
