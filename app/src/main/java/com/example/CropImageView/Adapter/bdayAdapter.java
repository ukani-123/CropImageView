package com.example.CropImageView.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.CropImageView.Interface.StickerClick;
import com.example.cropimageview.R;

import java.util.List;

public class bdayAdapter extends RecyclerView.Adapter<bdayAdapter.ViewHolder> {

    List<String> paths;
    Context context;
    StickerClick stickerClick;
    public bdayAdapter(List<String> paths, Context context, StickerClick stickerClick) {
        this.paths = paths;
        this.context = context;
        this.stickerClick = stickerClick;
    }

    @NonNull
    @Override
    public bdayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return  new ViewHolder(LayoutInflater.from(context).inflate(R.layout.rcv_bday,parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context).load("file:///android_asset/"+paths.get(position).toString()).into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stickerClick.onStickerClick(paths.get(position).toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return paths.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageSticker);
        }
    }
}
