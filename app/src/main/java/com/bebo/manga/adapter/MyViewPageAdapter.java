package com.bebo.manga.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bebo.manga.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MyViewPageAdapter extends PagerAdapter {
    Context context;
    List<String> imageLinks;
    LayoutInflater inflater;

    public MyViewPageAdapter(Context context, List<String> imageLinks)
    {
        this.context = context;
        this.imageLinks = imageLinks;
        inflater = LayoutInflater.from(context);
    }



    public Bitmap getBitmapFromURL(String src) {
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

    public Bitmap getBitmapFromURL2(String src) {
        try {
            URL url = new URL(src);
            URLConnection conexion = url.openConnection();
            conexion.connect();
            InputStream input = new BufferedInputStream(url.openStream());
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            myBitmap = myBitmap.copy(Bitmap.Config.ARGB_8888, true);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int getCount() {
        return imageLinks.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View image_layout = inflater.inflate(R.layout.view_page_item, container, false);
        PhotoView page_image = (PhotoView)image_layout.findViewById(R.id.page_image);
        page_image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Picasso.get().load(imageLinks.get(position)).into(page_image);
        container.addView(image_layout);
        return image_layout;
    }
}
