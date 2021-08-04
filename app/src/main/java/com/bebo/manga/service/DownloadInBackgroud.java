package com.bebo.manga.service;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.bebo.manga.Common.Common;

public class DownloadInBackgroud extends Service {

    private static final String TAG = "DownloadInBackgroud";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(Common.downloadItems != null)
                    DownloadData.SaveComic(Common.downloadItems, getBaseContext());
            }
        }).start();
        return START_STICKY;
    }
}
