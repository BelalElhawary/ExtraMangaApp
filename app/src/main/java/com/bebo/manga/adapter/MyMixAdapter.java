package com.bebo.manga.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bebo.manga.R;
import com.bebo.manga.model.Mix;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyMixAdapter extends RecyclerView.Adapter<MyMixAdapter.MyViewHolder> {

    Context context;
    List<Mix> mixList;
    LayoutInflater inflater;

    public MyMixAdapter(Context context, List<Mix> mixList)
    {
        this.context = context;
        this.mixList = mixList;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        itemView = inflater.inflate(R.layout.mix_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        /*
        for(int i = 0; i < holder.images_mix.size(); i++)
        {
            Picasso.get().load(mixList.get(position).Image.get(i)).into(holder.images_mix.get(i));
        }
        */
        Picasso.get().load(mixList.get(position).Image.get(0)).error(R.drawable.error_load_image).into(holder.image0_mix);
        Picasso.get().load(mixList.get(position).Image.get(1)).error(R.drawable.error_load_image).into(holder.image1_mix);
        Picasso.get().load(mixList.get(position).Image.get(2)).error(R.drawable.error_load_image).into(holder.image2_mix);
        Picasso.get().load(mixList.get(position).Image.get(3)).error(R.drawable.error_load_image).into(holder.image3_mix);
        Picasso.get().load(mixList.get(position).Image.get(4)).error(R.drawable.error_load_image).into(holder.image4_mix);
        holder.name_mix.setText(mixList.get(position).Name);
    }

    @Override
    public int getItemCount() {
        return mixList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name_mix;
        ImageView image0_mix;
        ImageView image1_mix;
        ImageView image2_mix;
        ImageView image3_mix;
        ImageView image4_mix;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name_mix = (TextView)itemView.findViewById(R.id.name_mix);
            image0_mix = (ImageView)itemView.findViewById(R.id.image0_mix);
            image1_mix = (ImageView)itemView.findViewById(R.id.image1_mix);
            image2_mix = (ImageView)itemView.findViewById(R.id.image2_mix);
            image3_mix = (ImageView)itemView.findViewById(R.id.image3_mix);
            image4_mix = (ImageView)itemView.findViewById(R.id.image4_mix);

        }
    }
}
