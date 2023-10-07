package com.example.cropimageview.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.cropimageview.Fragment.StickerFragment;
import com.example.cropimageview.Helper.DataBinder;
import com.example.cropimageview.model.StickerPaths;

import java.util.List;

public class pagerAdapter extends FragmentPagerAdapter {
    Context context;
    List<StickerPaths> stickerCat;

    public pagerAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        stickerCat = DataBinder.getStickerPathsList();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return StickerFragment.getInstance(stickerCat.get(position).getPaths());
    }

    @Override
    public int getCount() {
        return stickerCat.size();
    }

    public CharSequence getPageTitle(int position) {
        return stickerCat.get(position).getCateName();
    }
}
