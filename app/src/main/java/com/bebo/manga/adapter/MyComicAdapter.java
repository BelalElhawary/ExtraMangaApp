package com.bebo.manga.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bebo.manga.ComicDetails;
import com.bebo.manga.Common.Common;
import com.bebo.manga.Interface.IRecyclerItemClickListener;
import com.bebo.manga.R;
import com.bebo.manga.model.Comic;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyComicAdapter extends RecyclerView.Adapter<MyComicAdapter.MyViewHolder> {

    Context context;
    List<Comic> comicList;
    LayoutInflater inflater;
    boolean vertical = true;
    boolean online = true;

    public MyComicAdapter(Context context, List<Comic> comicList, boolean vertical, boolean online)
    {
        this.context = context;
        this.comicList = comicList;
        this.vertical = vertical;
        this.online = online;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        if (vertical) { itemView = inflater.inflate(R.layout.comic_item, parent, false); }
        else { itemView = inflater.inflate(R.layout.comic_item_h, parent, false); }
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            if (online) {Picasso.get().load(comicList.get(position).Image).error(R.drawable.error_load_image).into(holder.image_comic);}
            else {
                Bitmap bitmap = BitmapFactory.decodeFile(comicList.get(position).Image);
                holder.image_comic.setImageBitmap(bitmap);
            }
            holder.name_comic.setText(comicList.get(position).Name);
            holder.trend_comic.setText(String.valueOf(comicList.get(position).Trend));

            if (holder.category_comic != null) { holder.category_comic.setText(comicList.get(position).Category.get(position)); }

            holder.setRecyclerItemClickListener(new IRecyclerItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Common.comicSelected = comicList.get(position);
                Intent intent = new Intent(context, ComicDetails.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        if (comicList != null) { return comicList.size(); }
        else { return 0; }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name_comic;
        TextView category_comic;
        TextView trend_comic;
        ImageView image_comic;

        IRecyclerItemClickListener recyclerItemClickListener;
        public void setRecyclerItemClickListener(IRecyclerItemClickListener recyclerItemClickListener) { this.recyclerItemClickListener = recyclerItemClickListener; }
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image_comic = (ImageView)itemView.findViewById(R.id.image_comic);
            name_comic = (TextView)itemView.findViewById(R.id.name_comic);
            category_comic = (TextView)itemView.findViewById(R.id.category_comic);
            trend_comic = (TextView)itemView.findViewById(R.id.trend_comic);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            recyclerItemClickListener.onClick(v, getAdapterPosition());
        }
    }
}
