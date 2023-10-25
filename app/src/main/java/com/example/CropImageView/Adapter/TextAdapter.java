package com.example.CropImageView.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.CropImageView.Interface.TextInter;
import com.example.cropimageview.R;

import java.util.ArrayList;
public class TextAdapter extends RecyclerView.Adapter<TextAdapter.ViewHolder> {
    ArrayList<Integer> textList;
    Context context;
    int selectedItem = -1;
    TextInter text;
    public TextAdapter(ArrayList<Integer> textList, Context context, TextInter text) {
        this.textList = textList;
        this.context = context;
        this.text = text;
    }
    @NonNull
    @Override
    public TextAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.text_adapter_item,parent,false));
    }
    @Override
    public void onBindViewHolder(@NonNull TextAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.imageView.setImageResource(textList.get(position));
        if (position == selectedItem) {
            holder.imageView.setBackgroundResource(R.drawable.shape);
        } else {
            holder.imageView.setBackgroundResource(0);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text.onClickText(position);
            }
        });

    }
    @Override
    public int getItemCount() {
        return textList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.txtImage);
        }
    }
}
