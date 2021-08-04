package com.bebo.manga.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bebo.manga.Common.Common;
import com.bebo.manga.Interface.IRecyclerItemClickListener;
import com.bebo.manga.R;
import com.bebo.manga.ViewComicActivity;
import com.bebo.manga.model.Chapter;
import com.bebo.manga.model.Comic;
import com.bebo.manga.model.DownloadItem;
import com.bebo.manga.service.ComicDownloader;
import com.bebo.manga.service.SeeData;

import java.util.ArrayList;
import java.util.List;

public class MyChapterAdapter extends RecyclerView.Adapter<MyChapterAdapter.MyViewHolder> {
    Context context;
    List<Chapter> chapterList;
    LayoutInflater inflater;

    public MyChapterAdapter(Context context, List<Chapter> chapterList)
    {
        this.context = context;
        this.chapterList = chapterList;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.chapter_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txt_chapter_numb.setText(chapterList.get(position).Name);
        //check for comic offline or not
        if(Common.comicOfflineList != null)
        {
            for (Comic comic : Common.comicOfflineList)
            {
                for (Chapter chapter : comic.Chapters)
                {
                    if(chapter.Name.equals(chapterList.get(position).Name))
                    {
                        holder.btn_chapter_download.setBackground(null);
                        holder.btn_chapter_download.setClickable(false);
                    }
                }
            }
        }
        //check if this chapter download or not
        if(Common.downloadItems != null){
            for (DownloadItem downloadItem : Common.downloadItems)
            {
                if(downloadItem.DChapter.Name.equals(chapterList.get(position).Name)){
                    holder.btn_chapter_download.setBackground(null);
                    holder.btn_chapter_download.setClickable(false);
                    holder.btn_chapter_download.setBackground(holder.btn_chapter_download.getContext().getDrawable(R.drawable.ic_baseline_access_time_24));
                }
            }
        }
        Common.SEE = SeeData.Load(context);

        if(Common.SEE != null) {
            for (String string : Common.SEE) {
                if (string.equals(Common.comicSelected.Name + Common.comicSelected.Chapters.get(position).Name)) {
                    holder.btn_chapter_see.setChecked(true);
                }
            }
        }


        holder.btn_chapter_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComicDownloader.deleteChapter(Common.comicSelected.Chapters.get(position).Name);
            }
        });

        holder.btn_chapter_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.downloadItems.add(new DownloadItem(Common.comicSelected, Common.comicSelected.Chapters.get(position), 0, 0));
                holder.btn_chapter_download.setBackground(null);
                holder.btn_chapter_download.setClickable(false);
                holder.btn_chapter_download.setBackground(context.getDrawable(R.drawable.ic_baseline_access_time_24));
            }
        });

        holder.btn_chapter_see.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    if(Common.SEE != null)
                    {
                        Common.SEE.add(Common.comicSelected.Name + Common.comicSelected.Chapters.get(position).Name);
                        SeeData.Save(Common.SEE, context);

                    }else {
                        List<String> strings = new ArrayList<>();
                        strings.add(Common.comicSelected.Name + Common.comicSelected.Chapters.get(position).Name);
                        Common.SEE = strings;
                        SeeData.Save(Common.SEE, context);
                    }
                }else {
                    if(Common.SEE != null)
                    {
                        Common.SEE.remove(Common.comicSelected.Name + Common.comicSelected.Chapters.get(position).Name);
                        SeeData.Save(Common.SEE, context);

                    }
                }
            }
        });


        holder.setRecyclerItemClickListener(new IRecyclerItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Common.chapterSelected = chapterList.get(position);
                Common.chapterIndex = position;
                Intent intent = new Intent(context, ViewComicActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chapterList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_chapter_numb;
        Button btn_chapter_download;
        Button btn_chapter_delete;
        ToggleButton btn_chapter_see;
        IRecyclerItemClickListener recyclerItemClickListener;

        public void setRecyclerItemClickListener(IRecyclerItemClickListener recyclerItemClickListener)
        {
            this.recyclerItemClickListener = recyclerItemClickListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_chapter_numb = (TextView)itemView.findViewById(R.id.txt_chapter_numb);
            btn_chapter_download = (Button)itemView.findViewById(R.id.btn_chapter_download);
            btn_chapter_delete = (Button)itemView.findViewById(R.id.btn_chapter_delete);
            btn_chapter_see = (ToggleButton)itemView.findViewById(R.id.btn_chapter_see);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            recyclerItemClickListener.onClick(v, getAdapterPosition());
        }
    }
}
