package com.example.CropImageView.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.CropImageView.Interface.Tools;
import com.example.CropImageView.model.Tool;
import com.example.cropimageview.R;

import java.util.ArrayList;

public class ToolsAdapter extends RecyclerView.Adapter<ToolsAdapter.ViewHolder> {
    Context context;
    ArrayList<Tool> toolsList;
    int selectedItem = -1;
    Tools tools;
    public ToolsAdapter(Context context, ArrayList<Tool> toolsList, Tools tools) {
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
        holder.imageView.setImageResource(toolsList.get(position).getIcon());
        holder.txtTool.setText(toolsList.get(position).getName());

        if (position == selectedItem) {
            holder.itemView.setBackgroundResource(R.drawable.shape);
        } else {
            holder.itemView.setBackgroundResource(0);
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
        TextView txtTool;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageToolsView);
            txtTool = itemView.findViewById(R.id.txtTool);
        }
    }
}
