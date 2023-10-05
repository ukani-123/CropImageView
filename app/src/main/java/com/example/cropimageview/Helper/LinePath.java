package com.example.cropimageview.Helper;

import android.graphics.Paint;
import android.graphics.Path;

public  class LinePath {
        private Paint mDrawPaint;
        private Path mDrawPath;

        public LinePath(Path drawPath, Paint drawPaints) {
            mDrawPaint = new Paint(drawPaints);
            mDrawPath = new Path(drawPath);

        }

        public Paint getDrawPaint() {
            return mDrawPaint;
        }

        public Path getDrawPath() {
            return mDrawPath;
        }
    }