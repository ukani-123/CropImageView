package com.example.CropImageView.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.CropImageView.Helper.FontBinder;
import com.example.CropImageView.Interface.FontClick;
import com.example.CropImageView.model.FontPaths;
import com.example.cropimageview.R;
public class FontAdapter extends RecyclerView.Adapter<FontAdapter.ViewHolder> {
    Context context;
    FontClick fontClick;
    FontPaths fontPaths;
    public FontAdapter(Context context,FontClick fontClick) {
        this.context = context;
        this.fontClick = fontClick;
        fontPaths = FontBinder.listAssetFiles(context,"Fonts");
        //Log.d("Font", "FontAdapter: "+FontBinder.getFontPathList());
    }
    @NonNull
    @Override
    public FontAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.font_adapter_item, parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull FontAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.textView.setText("Font");
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "Fonts/" + fontPaths.getPaths()[position]);
        holder.textView.setTypeface(typeface);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fontClick.onFontClick(typeface);
            }
        });
    }
    @Override
    public int getItemCount() {
        return fontPaths.getPaths().length;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.txtFont);
        }
    }
}