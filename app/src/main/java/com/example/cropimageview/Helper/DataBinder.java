package com.example.cropimageview.Helper;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataBinder {

    public static List<String> listAssetFiles(Context c, String rootPath) {
        List<String> files =new ArrayList<>();
        try {
            String [] Paths = c.getAssets().list(rootPath);
            if (Paths.length > 0) {
                // This is a folder
                for (String file : Paths) {
                    String path = rootPath + "/" + file;
                    if (new File(path).isDirectory())
                        files.addAll(listAssetFiles(c,path));
                    else files.add(path);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return files;
    }

}
