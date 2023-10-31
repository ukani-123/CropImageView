package com.example.CropImageView.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.CropImageView.Fragment.StickerFragment;
import com.example.CropImageView.Helper.DataBinder;
import com.example.CropImageView.model.StickerPaths;

import java.util.ArrayList;
import java.util.List;

public class PagerAdapter extends FragmentPagerAdapter {
    Context context;
    List<StickerPaths> stickerCat = new ArrayList<>();

    public PagerAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        stickerCat.clear();
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
