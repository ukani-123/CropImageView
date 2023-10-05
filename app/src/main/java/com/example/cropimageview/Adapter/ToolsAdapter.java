package com.example.cropimageview.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cropimageview.R;
import com.example.cropimageview.Interface.Tools;

import java.util.ArrayList;

public class ToolsAdapter extends RecyclerView.Adapter<ToolsAdapter.ViewHolder> {
    Context context;
    ArrayList<Integer> toolsList;
    int selectedItem = -1;
    Tools tools;
    public ToolsAdapter(Context context, ArrayList<Integer> toolsList, Tools tools) {
        this.context = context;
        this.toolsList = toolsList;
        this.tools = tools;
    }
    @NonNull
    @Override
    public ToolsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.rcv_tools,parent,false));
    }
    @Override
    public void onBindViewHolder(@NonNull ToolsAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.imageView.setImageResource(toolsList.get(position));
        if (position == selectedItem) {
            holder.imageView.setBackgroundResource(R.drawable.shape);
        } else {
            holder.imageView.setBackgroundResource(0);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tools.onClickTool(position);

                int previousSelectedItem = selectedItem;
                selectedItem = position;
                notifyItemChanged(previousSelectedItem);
                notifyItemChanged(selectedItem);
            }
        });

    }
    @Override
    public int getItemCount() {
        return toolsList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageToolsView);
        }
    }
}
