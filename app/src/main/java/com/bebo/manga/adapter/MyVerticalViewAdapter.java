package com.bebo.manga.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bebo.manga.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyVerticalViewAdapter extends RecyclerView.Adapter<MyVerticalViewAdapter.MyViewHolder> {

    Context context;
    List<String> linksList;
    LayoutInflater inflater;
    boolean online = true;


    public MyVerticalViewAdapter(Context context, List<String> linksList, boolean online)
    {
        this.context = context;
        this.linksList = linksList;
        this.online = online;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        itemView = inflater.inflate(R.layout.view_page_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(online)
        {Picasso.get().load(linksList.get(position)).error(R.drawable.error_load_image).into(holder.image_view);}
        else
        {
            Bitmap bitmap = BitmapFactory.decodeFile(linksList.get(position));
            holder.image_view.setImageBitmap(bitmap);
        }
    }


    @Override
    public int getItemCount() {
        return linksList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        PhotoView image_view;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image_view = (PhotoView)itemView.findViewById(R.id.page_image);
            image_view.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            image_view.setAdjustViewBounds(true);
        }
    }
}