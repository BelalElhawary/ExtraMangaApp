package com.bebo.manga.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.audiofx.BassBoost;

import androidx.annotation.NonNull;

import com.bebo.manga.Common.Common;
import com.bebo.manga.model.ProfileSetting;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class SettingData {

    public static void Save(ProfileSetting profileSetting, Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences profile", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        Common.profileSetting = SettingData.Load(context);
        String json = gson.toJson(profileSetting);
        editor.putString("profile setting", json);
        editor.apply();
    }

    public static ProfileSetting Load(Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences profile", context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("profile setting", null);
        Type type = new TypeToken<ProfileSetting>() {}.getType();
        return gson.fromJson(json, type);
    }
}
