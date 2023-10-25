package com.example.CropImageView.Helper;

import android.view.MotionEvent;

import com.example.CropImageView.Interface.StickerIconEvent;
public class DeleteIconEvent implements StickerIconEvent {
  @Override public void onActionDown(StickerView stickerView, MotionEvent event) {

  }

  @Override public void onActionMove(StickerView stickerView, MotionEvent event) {

  }

  @Override public void onActionUp(StickerView stickerView, MotionEvent event) {
    stickerView.removeCurrentSticker();
  }
}
