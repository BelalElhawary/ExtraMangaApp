package com.bebo.manga.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.bebo.manga.Common.Common;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ComicData {

    public static void SaveTrend(List<String> trends, Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences trend", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(trends);
        editor.putString("trend", json);
        editor.apply();
    }

    public static List<String> LoadTrend(Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences trend", context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("trend", null);
        Type type = new TypeToken<List<String>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public static void SaveFavorite(List<String> fav, Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences fav", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(fav);
        editor.putString("fav", json);
        editor.apply();
    }

    public static List<String> LoadFavorite(Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences fav", context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("fav", null);
        Type type = new TypeToken<List<String>>() {}.getType();
        return gson.fromJson(json, type);
    }
}
