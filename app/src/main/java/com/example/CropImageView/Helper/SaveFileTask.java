package com.example.CropImageView.Helper;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;

import com.example.CropImageView.Activity.MainActivity;
import com.example.cropimageview.R;


public abstract class SaveFileTask extends AsyncTask<Void, Void, Void> {
    String savePath;
    Activity activity;
    View layout;
    Dialog dialog;
    Bitmap bitmap;

    public SaveFileTask(Activity activity, View layout) {
        this.activity = activity;
        this.layout = layout;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog_loader);
        dialog.getWindow().setBackgroundDrawableResource(R.color.white);
        dialog.show();
        layout.setDrawingCacheEnabled(true);
        bitmap = layout.getDrawingCache();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        savePath = MainActivity.saveToAlbum(bitmap, activity);
        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
        dialog.dismiss();
        layout.setDrawingCacheEnabled(false);
        OnSaveFile(savePath);
    }

    public abstract void OnSaveFile(String path);
}
