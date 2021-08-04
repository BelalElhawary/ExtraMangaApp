package com.bebo.manga;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bebo.manga.Common.Common;
import com.bebo.manga.adapter.MyComicAdapter;
import com.bebo.manga.adapter.MyDownloadAdapter;
import com.bebo.manga.model.Comic;
import com.bebo.manga.service.DownloadData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class DownloadFragment extends Fragment {
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_download, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.downloads);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));

        if(DownloadData.LoadComic(getContext()) != null)
        {
            recyclerView.setAdapter(new MyComicAdapter(getContext(), DownloadData.LoadComic(getContext()), true, false));
        }
        return view;
    }


}
