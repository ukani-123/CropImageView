package com.example.cropimageview.Helper;

import android.content.Context;

import com.example.cropimageview.model.StickerPaths;

import java.io.File;
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
                    String path = rootPath + "/" + file;
                    if (new File(path).isDirectory()) {
                        StickerPaths stickerPaths = new StickerPaths(file, new ArrayList<>());
                        stickerPaths.getPaths().addAll(listAssetFiles(c, path));
                        stickerPathsList.add(stickerPaths);
                    } else {
                        files.add(path);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return files;
    }

}
