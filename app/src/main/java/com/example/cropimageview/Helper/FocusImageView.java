package com.example.cropimageview.Helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.util.AttributeSet;

public class FocusImageView extends androidx.appcompat.widget.AppCompatImageView {
    private float focus = 1f;

    public FocusImageView(Context context) {
        super(context);
    }

    public FocusImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FocusImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setFocus(float focus) {
        this.focus = focus;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getDrawable() != null) {
            Bitmap bitmap = getBitmapFromDrawable(getDrawable());
            if (bitmap != null) {
                Bitmap focusedBitmap = applyFocus(bitmap, focus);
                canvas.drawBitmap(focusedBitmap, 0, 0, null);
            } else {
                super.onDraw(canvas);
            }
        } else {
            super.onDraw(canvas);
        }
    }

    private Bitmap applyFocus(Bitmap source, float focus) {
        float positiveFocus = focus > 0 ? focus : 0;
        float negativeFocus = focus < 0 ? -focus : 0;

        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.set(new float[]{
            1 - positiveFocus, 0, 0, 0, 0,
            0, 1 - positiveFocus, 0, 0, 0,
            0, 0, 1 - negativeFocus, 0, 0,
            0, 0, 0, 1, 0
        });

        Bitmap focusedBitmap = Bitmap.createBitmap(source.getWidth(), source.getHeight(), source.getConfig());
        Canvas canvas = new Canvas(focusedBitmap);
        canvas.drawBitmap(source, 0, 0, new android.graphics.Paint());
        android.graphics.Paint paint = new android.graphics.Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(focusedBitmap, 0, 0, paint);

        return focusedBitmap;
    }

    private Bitmap getBitmapFromDrawable(android.graphics.drawable.Drawable drawable) {
        if (drawable instanceof android.graphics.drawable.BitmapDrawable) {
            return ((android.graphics.drawable.BitmapDrawable) drawable).getBitmap();
        } else {
            return null;
        }
    }
}