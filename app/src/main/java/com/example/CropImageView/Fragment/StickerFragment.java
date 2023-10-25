package com.example.CropImageView.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.CropImageView.Adapter.bdayAdapter;
import com.example.CropImageView.Interface.ClickSticker;
import com.example.CropImageView.Interface.StickerClick;
import com.example.cropimageview.R;

import java.util.ArrayList;
import java.util.List;

public class StickerFragment extends Fragment {

    RecyclerView recyclerView;
    List<String> stickerList = new ArrayList<>();
    static ClickSticker clickSticker;

    public static void setClickSticker(ClickSticker clickSticker) {
        StickerFragment.clickSticker = clickSticker;
    }

    public static StickerFragment getInstance(List<String> data) {
        StickerFragment stickerFragment = new StickerFragment();
        stickerFragment.stickerList = data;
        return stickerFragment;
    }

    public StickerFragment() {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.stickerRcv);
        recyclerView.setAdapter(new bdayAdapter(stickerList, getContext(), new StickerClick() {
            @Override
            public void onStickerClick(String path) {
                clickSticker.onClickSticker(path);
            }
        }));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stickerr, container, false);
    }
}