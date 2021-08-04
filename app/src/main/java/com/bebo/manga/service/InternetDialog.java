package com.bebo.manga.service;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;

import com.bebo.manga.R;

public class InternetDialog {
    public static void show(Context context, Activity activity)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Please connect to internet");
        alertDialog.setIcon(R.drawable.ic_baseline_wifi_off_24);
        LayoutInflater infl = activity.getLayoutInflater();
        View internet_layout = infl.inflate(R.layout.dialog_internet, null);
        alertDialog.setView(internet_layout);
        alertDialog.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
