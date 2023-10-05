package com.example.cropimageview.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.cropimageview.Fragment.StickerFragment;

public class pagerAdapter extends FragmentPagerAdapter {
    Context context;
    public pagerAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) return new StickerFragment();
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
    public CharSequence getPageTitle(int position) {
        if (position == 0) return "Birthday";
        else return "Emoji";
    }
}
