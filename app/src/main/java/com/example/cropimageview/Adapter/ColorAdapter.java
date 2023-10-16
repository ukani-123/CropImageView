package com.example.cropimageview.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cropimageview.Interface.ColorInter;
import com.example.cropimageview.R;

import java.util.ArrayList;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ViewHolder> {
    Context context;
    ArrayList<Integer> colorList;
    ColorInter color;

    public ColorAdapter(Context context, ArrayList<Integer> colorList, ColorInter color) {
        this.context = context;
        this.colorList = colorList;
        this.color = color;
    }

    @NonNull
    @Override
    public ColorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_color_item,parent,false));
    }
    @Override

    public void onBindViewHolder(@NonNull ColorAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.imageView.setBackgroundColor(colorList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                color.onclickColor(position);

            }
        });

    }
    @Override
    public int getItemCount() {
        return colorList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgColor);
        }
    }
}