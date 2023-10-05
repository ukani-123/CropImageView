package com.example.cropimageview.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cropimageview.R;

import java.util.List;

public class bdayAdapter extends RecyclerView.Adapter<bdayAdapter.ViewHolder> {

    List<String> paths;
    Context context;

    public bdayAdapter(List<String> paths, Context context) {
        this.paths = paths;
        this.context = context;
    }

    @NonNull
    @Override
    public bdayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.rcv_bday,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull bdayAdapter.ViewHolder holder, int position) {

        holder.imageView.setImageDrawable(Drawable.createFromPath(paths.get(position)));

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
