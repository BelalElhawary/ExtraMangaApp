package com.bebo.manga;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bebo.manga.Common.Common;
import com.bebo.manga.adapter.MyDownloadAdapter;
import com.bebo.manga.service.DownloadInBackgroud;

public class DownloadManagerActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Button button;
    Button button1;
    Intent intent;

    MyDownloadAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_manager);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_download);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setAdapter(new MyDownloadAdapter(this, Common.downloadItems));
        button = (Button)findViewById(R.id.download);
        button1 = (Button)findViewById(R.id.stop_download);
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                recyclerView.setAdapter(new MyDownloadAdapter(getBaseContext(), Common.downloadItems));
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(runnable);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Down();
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Stop();
            }
        });
        com.squareup.pollexor.Thumbor thumbor;
    }

    void Down()
    {
        new Thread(new Runnable() {

            @Override
            public void run() {
                intent = new Intent(getApplicationContext(), DownloadInBackgroud.class);
                startService(intent);

            }
        }).start();
        Common.pause = false;
        Log.d("TEST TESTT TSETT", String.valueOf(Common.pause));
    }

    void Stop()
    {
        intent = new Intent(getApplicationContext(), DownloadInBackgroud.class);
        stopService(intent);
        Common.pause = true;
    }

}
