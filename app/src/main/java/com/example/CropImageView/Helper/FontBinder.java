package com.example.CropImageView.Helper;

import android.content.Context;

import com.example.CropImageView.model.FontPaths;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FontBinder {

    public static FontPaths listAssetFiles(Context c, String rootPath) {
        List<String> files = new ArrayList<>();
        try {
            String[] Paths = c.getAssets().list(rootPath);
            if (Paths.length > 0) {
                FontPaths fontPaths = new FontPaths(Paths);
                return fontPaths;
                /*for (String file : Paths) {

                    String filename = rootPath + (rootPath.isEmpty() ? "" : "/") + file;
                    String[] tmp = c.getAssets().list(filename);
                    if(tmp.length>0) {
                        FontPaths fontPaths = new FontPaths(file, new ArrayList<>());
                        fontPaths.getPaths().addAll(listAssetFiles(c, filename));
                        fontPathList.add(fontPaths);
                    }else {
                        files.add(filename);
                    }
                }*/
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
