package com.example.cropimageview;

import java.util.ArrayList;

public class paths {

    String cateName;
    ArrayList<String> paths;

    public paths(String cateName) {
        this.cateName = cateName;
    }

    public String getCateName() {
        return cateName;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }

    public ArrayList<String> getPaths() {
        return paths;
    }

    public void setPaths(ArrayList<String> paths) {
        this.paths = paths;
    }
}
