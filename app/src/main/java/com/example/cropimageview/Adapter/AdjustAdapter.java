package com.example.cropimageview.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cropimageview.Interface.Adjust;
import com.example.cropimageview.R;
import java.util.ArrayList;
public class AdjustAdapter extends RecyclerView.Adapter<AdjustAdapter.ViewHolder> {
    Context context;
    ArrayList<Integer> adjustList;
    int selectedItem = -1;
    Adjust adjust;
    public AdjustAdapter(Context context, ArrayList<Integer> adjustList, Adjust adjust) {
        this.context = context;
        this.adjustList = adjustList;
        this.adjust = adjust;
    }

    @NonNull
    @Override
    public AdjustAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.adjust_rcv,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdjustAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.imageView.setImageResource(adjustList.get(position));
        if (position == selectedItem) {
            holder.imageView.setBackgroundResource(R.drawable.shape);
        } else {
            holder.imageView.setBackgroundResource(0);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adjust.onClickAdjust(position);

                int previousSelectedItem = selectedItem;
                selectedItem = position;
                notifyItemChanged(previousSelectedItem);
                notifyItemChanged(selectedItem);
            }
        });
    }
    @Override
    public int getItemCount() {
        return adjustList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageAdjustView);
        }
    }
}
