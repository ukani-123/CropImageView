package com.example.cropimageview.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cropimageview.Interface.Filter;
import com.example.cropimageview.R;

import java.util.ArrayList;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {

    Context context;
    ArrayList<ColorFilter> colorFilterArrayList;
    Bitmap bitmap;
    int selectedItem = -1;
    Filter filter;


    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        notifyDataSetChanged();
    }

    public FilterAdapter(Context context, ArrayList<ColorFilter> colorFilterArrayList, Filter filter) {
        this.context = context;

        this.colorFilterArrayList = colorFilterArrayList;
        this.filter = filter;
    }

    @NonNull
    @Override
    public FilterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.rcv_ffilter,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull FilterAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.imageView.setImageBitmap(bitmap);
        holder.imageView.setColorFilter(colorFilterArrayList.get(position));
        if (position == selectedItem) {
            holder.imageView.setBackgroundResource(R.drawable.shape);
        } else {
            holder.imageView.setBackgroundResource(0);
        }



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter.onClickItem(colorFilterArrayList.get(position));
                int previousSelectedItem = selectedItem;
                selectedItem = position;
                notifyItemChanged(previousSelectedItem);
                notifyItemChanged(selectedItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return colorFilterArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
