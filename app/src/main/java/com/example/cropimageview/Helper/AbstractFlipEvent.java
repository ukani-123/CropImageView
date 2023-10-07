package com.example.cropimageview.Helper;

import android.view.MotionEvent;

import com.example.cropimageview.Interface.StickerIconEvent;

public abstract class AbstractFlipEvent implements StickerIconEvent {

  @Override public void onActionDown(StickerView stickerView, MotionEvent event) {

  }

  @Override public void onActionMove(StickerView stickerView, MotionEvent event) {

  }

  @Override public void onActionUp(StickerView stickerView, MotionEvent event) {
    stickerView.flipCurrentSticker(getFlipDirection());
  }

  @StickerView.Flip protected abstract int getFlipDirection();
}
