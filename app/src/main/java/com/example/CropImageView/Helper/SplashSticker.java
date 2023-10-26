package com.example.CropImageView.Helper;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
public class SplashSticker extends Sticker {
    private Bitmap bitmapXor;
    private Bitmap bitmapOver;
    private Paint xor;
    private Paint over;
    public SplashSticker(Bitmap drawableXor, Bitmap drawableOver) {
        xor = new Paint();
        xor.setDither(false);
        xor.setAntiAlias(false);
        xor.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));
        over = new Paint();
        over.setDither(false);
        over.setAntiAlias(false);
        over.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        this.bitmapXor = drawableXor;
        this.bitmapOver = drawableOver;
    }
    private Bitmap getBitmapOver() {
        return this.bitmapOver;
    }
    private Bitmap getBitmapXor() {
        return this.bitmapXor;
    }
    @NonNull
    @Override
    public Drawable getDrawable() {
        return null;//drawableOver;
    }
    @Override
    public SplashSticker setDrawable(@NonNull Drawable drawable) {
//        this.drawableOver = drawable;
        return this;
    }
    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawBitmap(getBitmapXor(), getMatrix(), xor);
        canvas.drawBitmap(getBitmapOver(), getMatrix(), over);
    }
    @NonNull
    @Override
    public SplashSticker setAlpha(@IntRange(from = 0, to = 255) int alpha) {
        return this;
    }
    @Override
    public int getAlpha() {
        return 1;
    }
    @Override
    public int getWidth() {
        return bitmapXor.getWidth();
    }
    @Override
    public int getHeight() {
        return bitmapOver.getHeight();
    }
    @Override
    public void release() {
        super.release();
        xor = null;
        over = null;
        if (bitmapXor != null)
            bitmapXor.recycle();
        bitmapXor = null;
        if (bitmapOver != null)
            bitmapOver.recycle();
        bitmapOver = null;
    }
}
