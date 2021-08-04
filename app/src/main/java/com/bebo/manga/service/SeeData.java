package com.bebo.manga.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.bebo.manga.Common.Common;
import com.bebo.manga.model.ProfileSetting;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class SeeData {

    public static void Save(List<String> strings, Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences see", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        Common.SEE = SeeData.Load(context);
        String json = gson.toJson(strings);
        editor.putString("see", json);
        editor.apply();
    }

    public static List<String> Load(Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences see", context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("see", null);
        Type type = new TypeToken<List<String>>() {}.getType();
        return gson.fromJson(json, type);
    }
}
