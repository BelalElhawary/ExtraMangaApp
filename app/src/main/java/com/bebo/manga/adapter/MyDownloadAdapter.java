package com.bebo.manga.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bebo.manga.Common.Common;
import com.bebo.manga.R;
import com.bebo.manga.model.Comic;
import com.bebo.manga.model.DownloadItem;
import com.bebo.manga.service.ComicDownloader;

import java.util.List;

public class MyDownloadAdapter extends RecyclerView.Adapter<MyDownloadAdapter.MyViewHolder> {
    Context context;
    List<DownloadItem> downloadItems;
    LayoutInflater inflater;


    public MyDownloadAdapter(Context context, List<DownloadItem> downloadItems)
    {
        this.context = context;
        this.downloadItems = downloadItems;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyDownloadAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        itemView = inflater.inflate(R.layout.download_manager_item, parent, false);
        return new MyDownloadAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyDownloadAdapter.MyViewHolder holder, int position) {
        holder.textView.setText(downloadItems.get(position).DComic.Name+downloadItems.get(position).DChapter.Name);
        if(position == 0)
        {
            holder.progressBar.setMax(downloadItems.get(position).max);
            holder.progressBar.setProgress(downloadItems.get(position).progress);
            holder.textView1.setText(new StringBuilder("(").append(downloadItems.get(position).progress).append("/").append(downloadItems.get(position).max).append(")"));
        }
    }

    @Override
    public int getItemCount() {
        return downloadItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        TextView textView1;
        SeekBar progressBar;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = (TextView)itemView.findViewById(R.id.download_manager_chapter_name);
            textView1 = (TextView)itemView.findViewById(R.id.download_manager_chapter_progress_detail);
            progressBar = (SeekBar)itemView.findViewById(R.id.download_manager_chapter_progress);
        }
    }
}
