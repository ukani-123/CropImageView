package com.example.cropimageview.Helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewCompat;

import com.example.cropimageview.R;

import java.util.Random;
import java.util.Stack;




public class SplashView extends AppCompatImageView {
    private PointF midPoint = new PointF();
    private Sticker sticker;
    @StickerView.ActionMode
    private int currentMode = StickerView.ActionMode.NONE;
    private float oldDistance = 0f;
    private float oldRotation = 0f;
    private final float[] tmp = new float[2];
    private final float[] point = new float[2];
    private final Matrix downMatrix = new Matrix();
    private final Matrix moveMatrix = new Matrix();
    private final PointF currentCenterPoint = new PointF();
    public static final int SHAPE = 0;
    public static final int DRAW = 1;
    public int currentSplashMode = SHAPE;
    private Paint mDrawPaint, paintCircle;
    private Path mPath;
    private int brushBitmapSize = 100;
    private float mTouchX, mTouchY;
    private Stack<LinePath> mPoints = new Stack<>();
    private Stack<LinePath> lstPoints = new Stack<>();
    private Stack<LinePath> mRedoPaths = new Stack<>();
    private float currentX;
    private float currentY;
    private boolean showTouchIcon = false;
    private boolean isLock = false;
    private Bitmap bitmap;

    public void setCurrentSplashMode(int currentSplashMode) {
        this.currentSplashMode = currentSplashMode;
    }

    public void setLock(boolean isLock) {
        this.isLock = isLock;
    }

    public SplashView(Context context) {
        super(context);
        init();
    }

    public SplashView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public SplashView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        setBitmap(bm);
    }

    public void setBitmap(Bitmap bm)    {
        bitmap = bm;
    }

    private void init() {
        mDrawPaint = new Paint();
        mDrawPaint.setAntiAlias(true);
        mDrawPaint.setDither(true);
        mDrawPaint.setStyle(Paint.Style.FILL);
        mDrawPaint.setStrokeJoin(Paint.Join.ROUND);
        mDrawPaint.setStrokeCap(Paint.Cap.ROUND);
        mDrawPaint.setStrokeWidth(brushBitmapSize);
        mDrawPaint.setMaskFilter(new BlurMaskFilter(20F, BlurMaskFilter.Blur.NORMAL));
        mDrawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        mDrawPaint.setStyle(Paint.Style.STROKE);

        paintCircle = new Paint();
        paintCircle.setAntiAlias(true);
        paintCircle.setDither(true);
        paintCircle.setColor(getContext().getResources().getColor(R.color.black));
        paintCircle.setStrokeWidth(dpToPx(getContext(), 2));
        paintCircle.setStyle(Paint.Style.STROKE);
        mPath = new Path();

    }

    public void updateBrush() {
        mPath = new Path();
        mDrawPaint.setAntiAlias(true);
        mDrawPaint.setDither(true);
        mDrawPaint.setStyle(Paint.Style.FILL);
        mDrawPaint.setStrokeJoin(Paint.Join.ROUND);
        mDrawPaint.setStrokeCap(Paint.Cap.ROUND);
        mDrawPaint.setStrokeWidth(brushBitmapSize);
        mDrawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        mDrawPaint.setStyle(Paint.Style.STROKE);
        showTouchIcon = false;
        invalidate();
    }

    @NonNull
    public void addSticker(@NonNull Sticker sticker) {
        addSticker(sticker, Sticker.Position.CENTER);
    }

    public void addSticker(@NonNull final Sticker sticker,
                           final @Sticker.Position int position) {
        if (ViewCompat.isLaidOut(this)) {
            addStickerImmediately(sticker, position);
        } else {
            post(new Runnable() {
                @Override
                public void run() {
                    addStickerImmediately(sticker, position);
                }
            });
        }
    }

    protected void addStickerImmediately(@NonNull Sticker sticker, @Sticker.Position int position) {
        this.sticker = sticker;
        setStickerPosition(sticker, position);
        invalidate();
    }

    protected void setStickerPosition(@NonNull Sticker sticker, @Sticker.Position int position) {
        float width = getWidth();
        float height = getHeight();

        float scale;
        if (width > height)
            scale = (height * 4 / 5) / sticker.getHeight();
        else
            scale = (width * 4 / 5) / sticker.getWidth();

        midPoint.set(0, 0);
        downMatrix.reset();
        moveMatrix.set(downMatrix);
        moveMatrix.postScale(scale, scale);
        int degree = new Random().nextInt(20) - 10;
        moveMatrix.postRotate(degree, midPoint.x, midPoint.y);

        float offsetX = width - (int) (sticker.getWidth() * scale);
        float offsetY = height - (int) (sticker.getHeight() * scale);
        if ((position & Sticker.Position.TOP) > 0) {
            offsetY /= 4f;
        } else if ((position & Sticker.Position.BOTTOM) > 0) {
            offsetY *= 3f / 4f;
        } else {
            offsetY /= 2f;
        }
        if ((position & Sticker.Position.LEFT) > 0) {
            offsetX /= 4f;
        } else if ((position & Sticker.Position.RIGHT) > 0) {
            offsetX *= 3f / 4f;
        } else {
            offsetX /= 2f;
        }
        moveMatrix.postTranslate(offsetX, offsetY);
        sticker.setMatrix(moveMatrix);
    }

    @SuppressLint("CanvasSize")
    @Override
    protected void onDraw(Canvas canvas) {
        if (bitmap == null || bitmap.isRecycled())
            return;
        super.onDraw(canvas);
        if (currentSplashMode == SHAPE) {
            drawStickers(canvas);
        } else {
            for (LinePath path : mPoints) {
                canvas.drawPath(path.getDrawPath(), path.getDrawPaint());
            }
            canvas.drawPath(mPath, mDrawPaint);
            if (showTouchIcon) {
                canvas.drawCircle(currentX, currentY, brushBitmapSize / 2, paintCircle);
            }

        }
    }

    protected void drawStickers(Canvas canvas) {

        if (sticker != null && sticker.isShow()) {
            sticker.draw(canvas);
        }

        invalidate();
    }

    protected float calculateDistance(float x1, float y1, float x2, float y2) {
        double x = x1 - x2;
        double y = y1 - y2;

        return (float) Math.sqrt(x * x + y * y);
    }

    protected float calculateDistance(@Nullable MotionEvent event) {
        if (event == null || event.getPointerCount() < 2) {
            return 0f;
        }
        return calculateDistance(event.getX(0), event.getY(0), event.getX(1), event.getY(1));
    }

    protected float calculateRotation(@Nullable MotionEvent event) {
        if (event == null || event.getPointerCount() < 2) {
            return 0f;
        }
        return calculateRotation(event.getX(0), event.getY(0), event.getX(1), event.getY(1));
    }

    protected float calculateRotation(float x1, float y1, float x2, float y2) {
        double x = x1 - x2;
        double y = y1 - y2;
        double radians = Math.atan2(y, x);
        return (float) Math.toDegrees(radians);
    }

    @NonNull
    protected PointF calculateMidPoint(@Nullable MotionEvent event) {
        if (event == null || event.getPointerCount() < 2) {
            midPoint.set(0, 0);
            return midPoint;
        }
        float x = (event.getX(0) + event.getX(1)) / 2;
        float y = (event.getY(0) + event.getY(1)) / 2;
        midPoint.set(x, y);
        return midPoint;
    }

    @NonNull
    protected PointF calculateMidPoint() {
        sticker.getMappedCenterPoint(midPoint, point, tmp);
        return midPoint;
    }

    protected boolean isInStickerArea(@NonNull Sticker sticker, float downX, float downY) {
        tmp[0] = downX;
        tmp[1] = downY;
        return sticker.contains(tmp);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isLock) {
            try {
                int action = MotionEventCompat.getActionMasked(event);
                float x = event.getX();
                float y = event.getY();
                currentX = x;
                currentY = y;
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        if (!onTouchDown(x, y)) {
                            invalidate();
                            return false;
                        }
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        oldDistance = calculateDistance(event);
                        oldRotation = calculateRotation(event);

                        midPoint = calculateMidPoint(event);

                        if (sticker != null && isInStickerArea(sticker, event.getX(1),
                                event.getY(1))) {
                            currentMode = StickerView.ActionMode.ZOOM_WITH_TWO_FINGER;
                        }
                        break;

                    case MotionEvent.ACTION_MOVE:
                        handleCurrentMode(x, y, event);
                        invalidate();
                        break;

                    case MotionEvent.ACTION_UP:
                        onTouchUp(event);
                        break;

                    case MotionEvent.ACTION_POINTER_UP:
                        currentMode = StickerView.ActionMode.NONE;
                        break;
                }


            } catch (Exception e) {

            }
            return true;
        }

        return false;
    }

    protected void constrainSticker(@NonNull Sticker sticker) {
        float moveX = 0;
        float moveY = 0;
        int width = getWidth();
        int height = getHeight();
        sticker.getMappedCenterPoint(currentCenterPoint, point, tmp);
        if (currentCenterPoint.x < 0) {
            moveX = -currentCenterPoint.x;
        }

        if (currentCenterPoint.x > width) {
            moveX = width - currentCenterPoint.x;
        }

        if (currentCenterPoint.y < 0) {
            moveY = -currentCenterPoint.y;
        }

        if (currentCenterPoint.y > height) {
            moveY = height - currentCenterPoint.y;
        }

        sticker.getMatrix().postTranslate(moveX, moveY);
    }


    protected synchronized void handleCurrentMode(float x, float y, MotionEvent event) {
        if (currentSplashMode == SHAPE) {
            switch (currentMode) {
                case StickerView.ActionMode.NONE:
                case StickerView.ActionMode.CLICK:
                    break;
                case StickerView.ActionMode.DRAG:
                    if (sticker != null) {
                        moveMatrix.set(downMatrix);
                        moveMatrix.postTranslate(event.getX() - mTouchX, event.getY() - mTouchY);
                        sticker.setMatrix(moveMatrix);
                    }
                    break;
                case StickerView.ActionMode.ZOOM_WITH_TWO_FINGER:
                    if (sticker != null) {
                        float newDistance = calculateDistance(event);
                        float newRotation = calculateRotation(event);

                        moveMatrix.set(downMatrix);
                        moveMatrix.postScale(newDistance / oldDistance, newDistance / oldDistance, midPoint.x,
                                midPoint.y);
                        moveMatrix.postRotate(newRotation - oldRotation, midPoint.x, midPoint.y);

                        sticker.setMatrix(moveMatrix);
                    }
                    break;
            }
        } else {
            mPath.quadTo(mTouchX, mTouchY, (x + mTouchX) / 2, (y + mTouchY) / 2);
            mTouchX = x;
            mTouchY = y;
        }
    }

    public void setBrushBitmapSize(int brushBitmapSize) {
        this.brushBitmapSize = brushBitmapSize;
        mDrawPaint.setStrokeWidth(brushBitmapSize);

        showTouchIcon = true;
        currentX = getWidth() / 2;
        currentY = getHeight() / 2;
        invalidate();
    }
    public void removeTouchIcon(){
        showTouchIcon=false;
        invalidate();
    }
    @Nullable
    protected Sticker findHandlingSticker() {
        if (isInStickerArea(sticker, mTouchX, mTouchY)) {
            return sticker;
        }
        return null;
    }

    protected boolean onTouchDown(float x, float y) {
        currentMode = StickerView.ActionMode.DRAG;
        mTouchX = x;
        mTouchY = y;
        currentX = x;
        currentY = y;

        if (currentSplashMode == SHAPE) {
            midPoint = calculateMidPoint();
            oldDistance = calculateDistance(midPoint.x, midPoint.y, mTouchX, mTouchY);
            oldRotation = calculateRotation(midPoint.x, midPoint.y, mTouchX, mTouchY);
            Sticker handlingSticker = findHandlingSticker();
            if (handlingSticker != null) {
                downMatrix.set(sticker.getMatrix());
            }

            if (handlingSticker == null) {
                return false;
            }
        } else {
            showTouchIcon = true;
            mRedoPaths.clear();
            mPath.reset();
            mPath.moveTo(x, y);
        }
        invalidate();
        return true;
    }

    protected void onTouchUp(@NonNull MotionEvent event) {
        showTouchIcon = false;
        if (currentSplashMode == SHAPE) {
            currentMode = StickerView.ActionMode.NONE;
        } else {
            LinePath linePath = new LinePath(mPath, mDrawPaint);
            mPoints.push(linePath);
            lstPoints.push(linePath);
            mPath = new Path();
        }
        invalidate();
    }
    public void eraseAll() {
        if (!mPoints.empty()) {
            mPoints.clear();
            mRedoPaths.clear();
            lstPoints.clear();
            invalidate();
        }
    }
    public void removeLastItem(int size) {
        for (int i = 1; size >= i; i++) {
            if (!lstPoints.empty()) {
                lstPoints.remove(lstPoints.size() - 1);
                invalidate();
            }
            if (!mRedoPaths.empty()) {
                mRedoPaths.remove(mRedoPaths.size() - 1);
                invalidate();
            }
            if (!mPoints.empty()) {
                mPoints.remove(mPoints.size() - 1);
                invalidate();
            }
        }
    }

    public int getSizeOfPaths() {
        return mPoints.size();
    }

    public boolean undo() {
        if (!lstPoints.empty()) {
            LinePath pop = lstPoints.pop();
            mRedoPaths.push(pop);
                mPoints.remove(pop);
            invalidate();
        }
        return !lstPoints.empty();
    }

    public boolean redo() {
        if (!mRedoPaths.empty()) {
            LinePath pop = mRedoPaths.pop();
            mPoints.push(pop);
            lstPoints.push(pop);
            invalidate();
        }

        return !mRedoPaths.empty();
    }

    public Sticker getSticker() {
        return sticker;
    }

    public Bitmap getBitmap(Bitmap originalBitmap) {

        //Bitmap myCanvasBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        //Canvas canvas = new Canvas(bitmap);
        int width = getWidth();
        int height = getHeight();
        Bitmap bmpMonochrome = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmpMonochrome);
        canvas.drawBitmap(bitmap, null, new RectF(0, 0, width, height), null);
        if (currentSplashMode == SHAPE) {
            drawStickers(canvas);
        } else {
            for (LinePath path : mPoints)
                canvas.drawPath(path.getDrawPath(), path.getDrawPaint());
        }

        Bitmap bmpMonochrome1 = Bitmap.createBitmap(originalBitmap.getWidth(), originalBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas1 = new Canvas(bmpMonochrome1);
        canvas1.drawBitmap(originalBitmap, null, new RectF(0, 0, originalBitmap.getWidth(), originalBitmap.getHeight()), null);
        canvas1.drawBitmap(bmpMonochrome, null, new RectF(0, 0, originalBitmap.getWidth(), originalBitmap.getHeight()), null);

        return bmpMonochrome1;

    }

    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) (dp * displayMetrics.density);
    }

}
