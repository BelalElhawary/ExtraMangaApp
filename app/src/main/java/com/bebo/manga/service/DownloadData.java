package com.bebo.manga.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.bebo.manga.Common.Common;
import com.bebo.manga.model.Comic;
import com.bebo.manga.model.DownloadItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class DownloadData {


    public static List<Comic> LoadComic(Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("download", null);
        Type type = new TypeToken<List<Comic>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public static void SaveComic(List<DownloadItem> ints, Context context){
        if(Common.downloadItems.size() <= 0)
            return;
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        Common.comicOfflineList = DownloadData.LoadComic(context);
        ComicDownloader.context = context.getApplicationContext();
        ComicDownloader.DownloadChapter(ints.get(0));
        Common.downloadItems.remove(0);
        String json = gson.toJson(Common.comicOfflineList);
        editor.putString("download", json);
        editor.apply();
        DownloadData.SaveComic(Common.downloadItems, context);
    }

    public static List<DownloadItem> LoadDownloadItems(Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences download item", context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("download item", null);
        Type type = new TypeToken<List<DownloadItem>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public static void SaveDownloadItems(List<DownloadItem> ints, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences download item", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(ints);
        editor.putString("download item", json);
        editor.apply();
    }
}
