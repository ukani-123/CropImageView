package com.example.cropimageview.Interface;

import android.view.MotionEvent;

import com.example.cropimageview.Helper.StickerView;

/**
 * @author wupanjie
 */

public interface StickerIconEvent {
  void onActionDown(StickerView stickerView, MotionEvent event);

  void onActionMove(StickerView stickerView, MotionEvent event);

  void onActionUp(StickerView stickerView, MotionEvent event);
}
