package com.example.cropimageview.model;

import java.util.ArrayList;
import java.util.List;

public class StickerPaths {

    String cateName;
    List<String> paths;

    public StickerPaths(String cateName, List<String> paths) {
        this.cateName = cateName;
        this.paths = paths;
    }

    public String getCateName() {
        return cateName;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }

    public List<String> getPaths() {
        return paths;
    }

    public void setPaths(ArrayList<String> paths) {
        this.paths = paths;
    }
}
