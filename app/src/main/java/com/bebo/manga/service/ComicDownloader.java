package com.bebo.manga.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import com.bebo.manga.Common.Common;
import com.bebo.manga.R;
import com.bebo.manga.model.Chapter;
import com.bebo.manga.model.Comic;
import com.bebo.manga.model.DownloadItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class ComicDownloader{

    public static Context context;
    public static int max = 0;
    public static int progress = 0;
    public static DownloadItem item;
    public static void DownloadChapter(DownloadItem It)
    {
        item = It;
        List<Chapter> chapters = new ArrayList<>();
        if (Common.comicOfflineList != null) {
                if (Common.comicOfflineList.size() > 0) {
                    for (int i = 0; i < Common.comicOfflineList.size(); i++) {
                        if (Common.comicOfflineList.get(i).Name.equals(It.DComic.Name)) {
                            for (int r = 0; r < Common.comicOfflineList.get(i).Chapters.size(); r++) {
                                if (Common.comicOfflineList.get(i).Chapters.get(r).Name.equals(It.DChapter.Name)) {
                                    return;
                                }
                            }
                            Common.comicOfflineList.get(i).Chapters.add(DownloadImages(It.DChapter, It.DComic.Name));
                            return;
                        }
                    }
                    chapters.add(DownloadImages(It.DChapter, It.DComic.Name));
                    try {
                        Comic comicOff = It.DComic;
                        comicOff.Image = saveImageToExternal(It.DComic.Name + " Cover", getBitmapFromURL(It.DComic.Image));
                        comicOff.Chapters = chapters;
                        Common.comicOfflineList.add(comicOff);
                    } catch (Exception e) {
                        Log.d("ERROR", String.valueOf(e));
                    }
                } else {
                    chapters.add(DownloadImages(It.DChapter, It.DComic.Name));
                    try {
                        Comic comicOff = It.DComic;
                        comicOff.Image = saveImageToExternal(It.DComic.Name + " Cover", getBitmapFromURL(It.DComic.Image));
                        comicOff.Chapters = chapters;
                        Common.comicOfflineList.add(comicOff);
                    } catch (Exception e) {
                        Log.d("ERROR", String.valueOf(e));
                    }
                }
            } else {
            Common.comicOfflineList = new ArrayList<>();
            chapters.add(DownloadImages(It.DChapter, It.DComic.Name));
            try {
                Comic comicOff = It.DComic;
                comicOff.Image = saveImageToExternal(It.DComic.Name + " Cover", getBitmapFromURL(It.DComic.Image));
                comicOff.Chapters = chapters;
                Common.comicOfflineList.add(comicOff);
            } catch (Exception e) {
                Log.d("ERROR", String.valueOf(e));
            }
        }
    }

    public static Chapter DownloadImages(Chapter chapter, String comicName)
    {
        Integer notificationID = 100;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder notificationBuilder = new Notification.Builder(context.getApplicationContext());
        notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.ic_baseline_arrow_circle_down_24)
                .setContentTitle("Download " + item.DComic.Name)
                .setContentText(item.DChapter.Name)
                .setProgress(20, ComicDownloader.progress, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String channelId = "Your_channel_id";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
            notificationBuilder.setChannelId(channelId);
        }
        Notification notification = notificationBuilder.build();
        notificationManager.notify(notificationID, notification);
        int op = 0;
        List<String> strings = new ArrayList<>();
        max = chapter.Links.size();

        for(int i = 0; i < Common.downloadItems.size(); i++)
        {
            if(Common.downloadItems.get(i) == item)
            {
                op = i;
            }
            break;
        }
        Common.downloadItems.get(op).max = max;
        for(int i = 0; i < chapter.Links.size(); i++)
        {
                progress = i;
                Common.downloadItems.get(op).progress = progress;
                notificationBuilder.setProgress(max, ComicDownloader.progress, false);
                notification = notificationBuilder.build();
                notificationManager.notify(notificationID, notification);
                try {
                    strings.add(saveImageToExternal(comicName + "_" + chapter.Name + "_", getBitmapFromURL(chapter.Links.get(i))));
                    Log.d("TAG TEST TEST ", comicName + "_" + chapter.Name + "_");
                } catch (Exception e) {
                    Log.d("DownloadImagesL103", String.valueOf(e));
                }
        }
        Chapter chapter1 = chapter;
        chapter1.Links =strings;
        notificationManager.cancelAll();
        return chapter1;
    }
    public static Bitmap getBitmapFromURL(String src) {
            try {
                java.net.URL url = new java.net.URL(src);
                HttpsURLConnection connection = (HttpsURLConnection) url
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                myBitmap = myBitmap.copy(Bitmap.Config.ARGB_8888, true);
                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
    }
    public static String saveImageToExternal(String imgName, Bitmap bm) throws IOException {
        {
            ContextWrapper cw = new ContextWrapper(context);
            // path to /data/data/yourApp/app_data/imageDir
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            // Create imageDir
            File mypath = new File(directory, imgName + ".webp");

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(mypath);
                // Use the compress method on the BitMap object to write image to the OutputStream
                if(Common.profileSetting != null)
                {
                    bm.compress(Bitmap.CompressFormat.WEBP, Common.profileSetting.ImageCompress, fos);
                }else {
                    bm.compress(Bitmap.CompressFormat.WEBP, 100, fos);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //Log.d("LINE_152", mypath.getPath());
            return mypath.getPath();
        }
    }

    public static void deleteChapter(String path)
    {
        try {
            new File(path).delete();
        }catch (Exception e)
        {
            Log.e("delete error", String.valueOf(e));
        }
    }
}
