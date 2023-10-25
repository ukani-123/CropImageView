package com.example.CropImageView.Helper;

import android.content.Context;

import com.example.CropImageView.model.StickerPaths;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataBinder {

    public static List<StickerPaths> stickerPathsList = new ArrayList<>();

    public static List<StickerPaths> getStickerPathsList() {
        return stickerPathsList;
    }

    public static List<String> listAssetFiles(Context c, String rootPath) {
        List<String> files = new ArrayList<>();
        try {
            String[] Paths = c.getAssets().list(rootPath);
            if (Paths.length > 0) {
                for (String file : Paths) {

                    String filename = rootPath + (rootPath.isEmpty() ? "" : "/") + file;
                    String[] tmp = c.getAssets().list(filename);
                    if(tmp.length>0) {
                        StickerPaths stickerPaths = new StickerPaths(file, new ArrayList<>());
                        stickerPaths.getPaths().addAll(listAssetFiles(c, filename));
                        stickerPathsList.add(stickerPaths);
                    }else {
                        files.add(filename);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return files;
    }

}

