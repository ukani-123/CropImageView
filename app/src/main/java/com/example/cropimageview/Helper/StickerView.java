package com.example.cropimageview.Helper;

import static com.example.cropimageview.Helper.SplashView.dpToPx;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.RelativeLayout;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewCompat;

import com.example.cropimageview.R;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StickerView extends RelativeLayout {

    private boolean showIcons;
    private boolean showBorder;
    private boolean bringToFrontCurrentSticker;
    //private boolean shapeType;

    @IntDef({
            ActionMode.NONE, ActionMode.DRAG, ActionMode.ZOOM_WITH_TWO_FINGER, ActionMode.ICON,
            ActionMode.CLICK
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface ActionMode {
        int NONE = 0;
        int DRAG = 1;
        int ZOOM_WITH_TWO_FINGER = 2;
        int ICON = 3;
        int CLICK = 4;
    }

    @IntDef(flag = true, value = {FLIP_HORIZONTALLY, FLIP_VERTICALLY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Flip {
    }

    private static final String TAG = "StickerView";

    private static final int DEFAULT_MIN_CLICK_DELAY_TIME = 200;

    public static final int FLIP_HORIZONTALLY = 1;
    public static final int FLIP_VERTICALLY = 1 << 1;

    private final List<Sticker> stickers = new ArrayList<>();

    private final Paint borderPaint = new Paint();
    private final Paint linePaint = new Paint();
    private final RectF stickerRect = new RectF();

    private final Matrix sizeMatrix = new Matrix();
    private final Matrix downMatrix = new Matrix();
    private final Matrix moveMatrix = new Matrix();

    // region storing variables
    private final float[] bitmapPoints = new float[8];
    private final float[] bounds = new float[8];
    private final float[] point = new float[2];
    private final PointF currentCenterPoint = new PointF();
    private final float[] tmp = new float[2];
    private PointF midPoint = new PointF();
    private boolean drawCirclePoint = false;
    private boolean onMoving = false;
    private float currentMoveingX, currentMoveingY;
    private Paint paintCircle;
    private int circleRadius;

    // endregion
    private int touchSlop;

    //private BitmapStickerIcon currentIcon;
    //the first point down position
    private float downX;
    private float downY;

    private float oldDistance = 0f;
    private float oldRotation = 0f;

    @ActionMode
    private int currentMode = ActionMode.NONE;

    private Sticker handlingSticker;
    private Sticker lastHandlingSticker;

    private boolean locked;
    private boolean constrained;

    private OnStickerOperationListener onStickerOperationListener;

    private long lastClickTime = 0;
    private int minClickDelayTime = DEFAULT_MIN_CLICK_DELAY_TIME;

    public StickerView(Context context) {
        this(context, null);
    }

    public StickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paintCircle = new Paint();
        paintCircle.setAntiAlias(true);
        paintCircle.setDither(true);
        paintCircle.setColor(getContext().getResources().getColor(R.color.black));
        paintCircle.setStrokeWidth(dpToPx(getContext(), 2));
        paintCircle.setStyle(Paint.Style.STROKE);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        TypedArray a = null;
        try {
            a = context.obtainStyledAttributes(attrs, R.styleable.StickerView);
            showIcons = a.getBoolean(R.styleable.StickerView_showIcons, false);
            showBorder = a.getBoolean(R.styleable.StickerView_showBorder, false);
            bringToFrontCurrentSticker =
                    a.getBoolean(R.styleable.StickerView_bringToFrontCurrentSticker, false);

            borderPaint.setAntiAlias(true);
            borderPaint.setColor(a.getColor(R.styleable.StickerView_borderColor, Color.WHITE));
            borderPaint.setAlpha(a.getInteger(R.styleable.StickerView_borderAlpha, 255));
            configDefaultIcons();
        } finally {
            if (a != null) {
                a.recycle();
            }
        }

    }
    public Matrix getSizeMatrix() {
        return sizeMatrix;
    }

    public Matrix getDownMatrix() {
        return downMatrix;
    }

    public Matrix getMoveMatrix() {
        return moveMatrix;
    }

    public List<Sticker> getStickers() {
        return stickers;
    }

    public void configDefaultIcons() {
       /* BitmapStickerIcon deleteIcon = new BitmapStickerIcon(
                ContextCompat.getDrawable(getContext(), R.drawable.border_cancel_icon),
                BitmapStickerIcon.LEFT_TOP, BitmapStickerIcon.REMOVE);
        deleteIcon.setIconEvent(new DeleteIconEvent());
        BitmapStickerIcon zoomIcon = new BitmapStickerIcon(
                ContextCompat.getDrawable(getContext(), R.drawable.border_move_icon),
                BitmapStickerIcon.RIGHT_BOTOM, BitmapStickerIcon.ZOOM);
        zoomIcon.setIconEvent(new ZoomIconEvent());
        BitmapStickerIcon flipIcon = new BitmapStickerIcon(
                ContextCompat.getDrawable(getContext(), R.drawable.border_flip_icon),
                BitmapStickerIcon.RIGHT_TOP, BitmapStickerIcon.FLIP);
        flipIcon.setIconEvent(new FlipHorizontallyEvent());

        BitmapStickerIcon editIcon = new BitmapStickerIcon(
                ContextCompat.getDrawable(getContext(), R.drawable.border_edit_icon),
                BitmapStickerIcon.LEFT_BOTTOM, BitmapStickerIcon.EDIT);
        editIcon.setIconEvent(new FlipHorizontallyEvent());

        icons.clear();
        icons.add(deleteIcon);
        icons.add(zoomIcon);
        icons.add(flipIcon);
        icons.add(editIcon);*/
    }

    /**
     * Swaps sticker at layer [[oldPos]] with the one at layer [[newPos]].
     * Does nothing if either of the specified layers doesn't exist.
     */
    public void swapLayers(int oldPos, int newPos) {
        if (stickers.size() >= oldPos && stickers.size() >= newPos) {
            Collections.swap(stickers, oldPos, newPos);
            invalidate();
        }
    }

    public void setHandlingSticker(Sticker handlingSticker) {
        this.lastHandlingSticker = this.handlingSticker;
        this.handlingSticker = handlingSticker;
        invalidate();
    }

    public void showLastHandlingSticker() {
        if (this.lastHandlingSticker != null && !this.lastHandlingSticker.isShow()) {
            this.lastHandlingSticker.setShow(true);
            invalidate();
        }
    }

    /**
     * Sends sticker from layer [[oldPos]] to layer [[newPos]].
     * Does nothing if either of the specified layers doesn't exist.
     */
    public void sendToLayer(int oldPos, int newPos) {
        if (stickers.size() >= oldPos && stickers.size() >= newPos) {
            Sticker s = stickers.get(oldPos);
            stickers.remove(oldPos);
            stickers.add(newPos, s);
            invalidate();
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            stickerRect.left = left;
            stickerRect.top = top;
            stickerRect.right = right;
            stickerRect.bottom = bottom;
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (drawCirclePoint && onMoving) {
            canvas.drawCircle(downX, downY, circleRadius, paintCircle);
            canvas.drawLine(downX, downY, currentMoveingX, currentMoveingY, paintCircle);
        }
        drawStickers(canvas);
    }

    public void setCircleRadius(int circleRadius) {
        this.circleRadius = circleRadius;
    }

    protected void drawStickers(Canvas canvas) {

        for (int i = 0; i < stickers.size(); i++) {
            Sticker sticker = stickers.get(i);
            if (sticker != null && sticker.isShow()) {
                sticker.draw(canvas);
            }
        }

        if (handlingSticker != null && !locked && (showBorder || showIcons)) {
            getStickerPoints(handlingSticker, bitmapPoints);
            float x1 = bitmapPoints[0];
            float y1 = bitmapPoints[1];
            float x2 = bitmapPoints[2];
            float y2 = bitmapPoints[3];
            float x3 = bitmapPoints[4];
            float y3 = bitmapPoints[5];
            float x4 = bitmapPoints[6];
            float y4 = bitmapPoints[7];

            if (showBorder) {
                canvas.drawLine(x1, y1, x2, y2, borderPaint);
                canvas.drawLine(x1, y1, x3, y3, borderPaint);
                canvas.drawLine(x2, y2, x4, y4, borderPaint);
                canvas.drawLine(x4, y4, x3, y3, borderPaint);
            }

            //draw icons
           /* if (showIcons) {
                float rotation = calculateRotation(x4, y4, x3, y3);
                for (int i = 0; i < icons.size(); i++) {
                    BitmapStickerIcon icon = icons.get(i);
                    switch (icon.getPosition()) {
                        case BitmapStickerIcon.LEFT_TOP:
                            configIconMatrix(icon, x1, y1, rotation);
                            icon.draw(canvas, borderPaint);
                            break;

                        case BitmapStickerIcon.RIGHT_TOP:
                            if ((handlingSticker instanceof TextSticker && icon.getTag().equals(BitmapStickerIcon.EDIT)) || (handlingSticker instanceof DrawableSticker && icon.getTag().equals(BitmapStickerIcon.FLIP))) {
                                configIconMatrix(icon, x2, y2, rotation);
                                icon.draw(canvas, borderPaint);
                            }
                            break;

                        case BitmapStickerIcon.LEFT_BOTTOM:
                            if (handlingSticker instanceof BeautySticker) {
                                BeautySticker beautySticker = (BeautySticker) handlingSticker;
                                if (beautySticker.getType() == BeautySticker.BUST_0) {
                                    configIconMatrix(icon, x3, y3, rotation);
                                    icon.draw(canvas, borderPaint);
                                }
                            } else {
                                configIconMatrix(icon, x3, y3, rotation);
                                icon.draw(canvas, borderPaint);
                            }
                            break;

                        case BitmapStickerIcon.RIGHT_BOTOM:
                            if ((handlingSticker instanceof TextSticker && icon.getTag().equals(BitmapStickerIcon.ZOOM)) || (handlingSticker instanceof DrawableSticker && icon.getTag().equals(BitmapStickerIcon.ZOOM))) {
                                configIconMatrix(icon, x4, y4, rotation);
                                icon.draw(canvas, borderPaint);
                            } else if (handlingSticker instanceof BeautySticker) {
                                BeautySticker beautySticker = (BeautySticker) handlingSticker;
                                if (beautySticker.getType() == BeautySticker.BUST_1) {
                                    configIconMatrix(icon, x4, y4, rotation);
                                    icon.draw(canvas, borderPaint);
                                } else if (beautySticker.getType() == BeautySticker.HIP_1 || beautySticker.getType() == BeautySticker.HIP_2 || beautySticker.getType() == BeautySticker.FACE) {
                                    configIconMatrix(icon, x4, y4, rotation);
                                    icon.draw(canvas, borderPaint);
                                }
                            }
                            break;
                    }
                }
            }*/
        }
        invalidate();
    }

    /*protected void configIconMatrix(@NonNull BitmapStickerIcon icon, float x, float y,
                                    float rotation) {
        icon.setX(x);
        icon.setY(y);
        icon.getMatrix().reset();

        icon.getMatrix().postRotate(rotation, icon.getWidth() / 2, icon.getHeight() / 2);
        icon.getMatrix().postTranslate(x - icon.getWidth() / 2, y - icon.getHeight() / 2);
    }*/

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (locked) return super.onInterceptTouchEvent(ev);

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                downY = ev.getY();

                return /*findCurrentIconTouched() != null ||*/ findHandlingSticker() != null;
        }

        return super.onInterceptTouchEvent(ev);
    }

    public void setDrawCirclePoint(boolean drawCirclePoint) {
        this.drawCirclePoint = drawCirclePoint;
        onMoving = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (locked) {
            return super.onTouchEvent(event);
        }

        int action = MotionEventCompat.getActionMasked(event);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (!onTouchDown(event)) {
                    if (onStickerOperationListener == null)
                        return false;
                    onStickerOperationListener.onStickerTouchOutside();
                    invalidate();
                    if (!drawCirclePoint)
                        return false;
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDistance = calculateDistance(event);
                oldRotation = calculateRotation(event);

                midPoint = calculateMidPoint(event);

                if (handlingSticker != null && isInStickerArea(handlingSticker, event.getX(1),
                        event.getY(1)) /*&& findCurrentIconTouched() == null*/) {
                    currentMode = ActionMode.ZOOM_WITH_TWO_FINGER;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                handleCurrentMode(event);
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                onTouchUp(event);
                break;

            case MotionEvent.ACTION_POINTER_UP:
                if (currentMode == ActionMode.ZOOM_WITH_TWO_FINGER && handlingSticker != null) {
                    if (onStickerOperationListener != null) {
                        onStickerOperationListener.onStickerZoomFinished(handlingSticker);
                    }
                }
                currentMode = ActionMode.NONE;
                break;
        }

        return true;
    }

    /**
     * @param event MotionEvent received from {@link #onTouchEvent)
     * @return true if has touch something
     */
    protected boolean onTouchDown(@NonNull MotionEvent event) {
        currentMode = ActionMode.DRAG;

        downX = event.getX();
        downY = event.getY();
        onMoving = true;
        currentMoveingX = event.getX();
        currentMoveingY = event.getY();

        midPoint = calculateMidPoint();
        oldDistance = calculateDistance(midPoint.x, midPoint.y, downX, downY);
        oldRotation = calculateRotation(midPoint.x, midPoint.y, downX, downY);

        //currentIcon = findCurrentIconTouched();
        /*if (currentIcon != null) {
            currentMode = ActionMode.ICON;
            currentIcon.onActionDown(this, event);
        } else {*/
            handlingSticker = findHandlingSticker();
        //}

        if (handlingSticker != null) {
            downMatrix.set(handlingSticker.getMatrix());
            if (bringToFrontCurrentSticker) {
                stickers.remove(handlingSticker);
                stickers.add(handlingSticker);
            }
            if (onStickerOperationListener != null) {
                onStickerOperationListener.onStickerTouchedDown(handlingSticker);
            }
        }

        if (drawCirclePoint) {
            onStickerOperationListener.onTouchDownForBeauty(currentMoveingX, currentMoveingY);
            invalidate();
            return true;
        }

        /*if (currentIcon == null && handlingSticker == null) {
            return false;
        }*/
        invalidate();
        return true;
    }

    protected void onTouchUp(@NonNull MotionEvent event) {
        long currentTime = SystemClock.uptimeMillis();
        onMoving = false;
        if (drawCirclePoint)
            onStickerOperationListener.onTouchUpForBeauty(event.getX(), event.getY());
        /*if (currentMode == ActionMode.ICON && currentIcon != null && handlingSticker != null) {
            currentIcon.onActionUp(this, event);
        }*/

        if (currentMode == ActionMode.DRAG
                && Math.abs(event.getX() - downX) < touchSlop
                && Math.abs(event.getY() - downY) < touchSlop
                && handlingSticker != null) {
            currentMode = ActionMode.CLICK;
            if (onStickerOperationListener != null) {
                onStickerOperationListener.onStickerClicked(handlingSticker);
            }
            if (currentTime - lastClickTime < minClickDelayTime) {
                if (onStickerOperationListener != null) {
                    onStickerOperationListener.onStickerDoubleTapped(handlingSticker);
                }
            }
        }

        if (currentMode == ActionMode.DRAG && handlingSticker != null) {
            if (onStickerOperationListener != null) {
                onStickerOperationListener.onStickerDragFinished(handlingSticker);
            }
        }

        currentMode = ActionMode.NONE;
        lastClickTime = currentTime;
    }

    protected void handleCurrentMode(@NonNull MotionEvent event) {
        switch (currentMode) {
            case ActionMode.NONE:
            case ActionMode.CLICK:
                break;
            case ActionMode.DRAG:
                currentMoveingX = event.getX();
                currentMoveingY = event.getY();
                if (drawCirclePoint) {
                    onStickerOperationListener.onTouchDragForBeauty(currentMoveingX, currentMoveingY);
                }
                if (handlingSticker != null) {
                    moveMatrix.set(downMatrix);
                    /*if (handlingSticker instanceof BeautySticker) {
                        BeautySticker beautySticker = (BeautySticker) handlingSticker;
                        if (beautySticker.getType() == BeautySticker.TALL_1 || beautySticker.getType() == BeautySticker.TALL_2) {
                            moveMatrix.postTranslate(0, event.getY() - downY);
                        } else {
                            moveMatrix.postTranslate(event.getX() - downX, event.getY() - downY);
                        }
                    } else*/
                        moveMatrix.postTranslate(event.getX() - downX, event.getY() - downY);
                    handlingSticker.setMatrix(moveMatrix);
                    if (constrained) {
                        constrainSticker(handlingSticker);
                    }
                }
                break;
            case ActionMode.ZOOM_WITH_TWO_FINGER:
                if (handlingSticker != null) {
                    float newDistance = calculateDistance(event);
                    float newRotation = calculateRotation(event);

                    moveMatrix.set(downMatrix);
                    moveMatrix.postScale(newDistance / oldDistance, newDistance / oldDistance, midPoint.x,
                            midPoint.y);
                    moveMatrix.postRotate(newRotation - oldRotation, midPoint.x, midPoint.y);
                    handlingSticker.setMatrix(moveMatrix);
                }
                break;

            case ActionMode.ICON:
                /*if (handlingSticker != null && currentIcon != null) {
                    currentIcon.onActionMove(this, event);
                }*/
                break;
        }
    }

    public void zoomAndRotateCurrentSticker(@NonNull MotionEvent event) {
        zoomAndRotateSticker(handlingSticker, event);
    }

    public void alignHorizontally() {
        moveMatrix.set(downMatrix);
        moveMatrix.postRotate(-getCurrentSticker().getCurrentAngle(), midPoint.x, midPoint.y);
        handlingSticker.setMatrix(moveMatrix);
    }

    public void zoomAndRotateSticker(@Nullable Sticker sticker, @NonNull MotionEvent event) {
        if (sticker != null) {
            /*if (sticker instanceof BeautySticker) {
                BeautySticker beautySticker = (BeautySticker) sticker;
                if (beautySticker.getType() == BeautySticker.TALL_1 || beautySticker.getType() == BeautySticker.TALL_2)
                    return;
            }*/
            float newDistance;
            //TODO sticker
          /*  if (sticker instanceof TextSticker)
                newDistance = oldDistance;
            else*/
            newDistance = calculateDistance(midPoint.x, midPoint.y, event.getX(), event.getY());
            float newRotation = calculateRotation(midPoint.x, midPoint.y, event.getX(), event.getY());

            moveMatrix.set(downMatrix);
            moveMatrix.postScale(newDistance / oldDistance, newDistance / oldDistance, midPoint.x,
                    midPoint.y);
            /*if (!(sticker instanceof BeautySticker))
                moveMatrix.postRotate(newRotation - oldRotation, midPoint.x, midPoint.y);*/
            handlingSticker.setMatrix(moveMatrix);
        }
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

   /* @Nullable
    protected BitmapStickerIcon findCurrentIconTouched() {
        for (BitmapStickerIcon icon : icons) {
            float x = icon.getX() - downX;
            float y = icon.getY() - downY;
            float distance_pow_2 = x * x + y * y;
            if (distance_pow_2 <= Math.pow(icon.getIconRadius() + icon.getIconRadius(), 2)) {
                return icon;
            }
        }

        return null;
    }*/

    /**
     * find the touched Sticker
     **/
    @Nullable
    protected Sticker findHandlingSticker() {
        for (int i = stickers.size() - 1; i >= 0; i--) {
            if (isInStickerArea(stickers.get(i), downX, downY)) {
                return stickers.get(i);
            }
        }
        return null;
    }

    protected boolean isInStickerArea(@NonNull Sticker sticker, float downX, float downY) {
        tmp[0] = downX;
        tmp[1] = downY;
        return sticker.contains(tmp);
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
        if (handlingSticker == null) {
            midPoint.set(0, 0);
            return midPoint;
        }
        handlingSticker.getMappedCenterPoint(midPoint, point, tmp);
        return midPoint;
    }

    /**
     * calculate rotation in line with two fingers and x-axis
     **/
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

    /**
     * calculate Distance in two fingers
     **/
    protected float calculateDistance(@Nullable MotionEvent event) {
        if (event == null || event.getPointerCount() < 2) {
            return 0f;
        }
        return calculateDistance(event.getX(0), event.getY(0), event.getX(1), event.getY(1));
    }

    protected float calculateDistance(float x1, float y1, float x2, float y2) {
        double x = x1 - x2;
        double y = y1 - y2;

        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * Sticker's drawable will be too bigger or smaller
     * This method is to transform it to fit
     * step 1：let the center of the sticker image is coincident with the center of the View.
     * step 2：Calculate the zoom and zoom
     **/
    protected void transformSticker(@Nullable Sticker sticker) {
        if (sticker == null) {
            Log.e(TAG, "transformSticker: the bitmapSticker is null or the bitmapSticker bitmap is null");
            return;
        }

      /*  sizeMatrix.reset();

        float width = getWidth();
        float height = getHeight();
        float stickerWidth = sticker.getWidth();
        float stickerHeight = sticker.getHeight();
        //step 1
        float offsetX = (width - stickerWidth) / 2;
        float offsetY = (height - stickerHeight) / 2;

        sizeMatrix.postTranslate(offsetX, offsetY);

        //step 2
        float scaleFactor;
        if (width < height) {
            scaleFactor = width / stickerWidth;
        } else {
            scaleFactor = height / stickerHeight;
        }

        sizeMatrix.postScale(scaleFactor / 2f, scaleFactor / 2f, width / 2f, height / 2f);

        sticker.getMatrix().reset();
        sticker.setMatrix(sizeMatrix);

        invalidate();*/

        sizeMatrix.set(sticker.getMatrix());
        float stickerWidth = sticker.getCurrentWidth();
        float stickerHeight = sticker.getCurrentHeight();
        float translateX = sticker.getMatrixValue(Matrix.MTRANS_X);
        float translateY = sticker.getMatrixValue(Matrix.MTRANS_Y);

        // move the origin to center of the sticker to make scaling easier
        sizeMatrix.postTranslate(-translateX - stickerWidth / 2f, -translateY - stickerHeight / 2f);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        for (int i = 0; i < stickers.size(); i++) {
            Sticker sticker = stickers.get(i);
            if (sticker != null) {
                transformSticker(sticker);
            }
        }
    }

    public void flipCurrentSticker(int direction) {
        flip(handlingSticker, direction);
    }

    public void flip(@Nullable Sticker sticker, @Flip int direction) {
        if (sticker != null) {
            sticker.getCenterPoint(midPoint);
            if ((direction & FLIP_HORIZONTALLY) > 0) {
                sticker.getMatrix().preScale(-1, 1, midPoint.x, midPoint.y);
                sticker.setFlippedHorizontally(!sticker.isFlippedHorizontally());
            }
            if ((direction & FLIP_VERTICALLY) > 0) {
                sticker.getMatrix().preScale(1, -1, midPoint.x, midPoint.y);
                sticker.setFlippedVertically(!sticker.isFlippedVertically());
            }

            if (onStickerOperationListener != null) {
                onStickerOperationListener.onStickerFlipped(sticker);
            }

            invalidate();
        }
    }

    public boolean replace(@Nullable Sticker sticker) {
        return replace(sticker, true);
    }

    public Sticker getLastHandlingSticker() {
        return lastHandlingSticker;
    }

    public boolean replace(@Nullable Sticker sticker, boolean needStayState) {

        if (handlingSticker == null)
            handlingSticker = lastHandlingSticker;

        if (handlingSticker != null && sticker != null) {
            float width = getWidth();
            float height = getHeight();
            if (needStayState) {
                sticker.setMatrix(handlingSticker.getMatrix());
                sticker.setFlippedVertically(handlingSticker.isFlippedVertically());
                sticker.setFlippedHorizontally(handlingSticker.isFlippedHorizontally());
            } else {
                handlingSticker.getMatrix().reset();
                // reset scale, angle, and put it in center
                float offsetX = (width - handlingSticker.getWidth()) / 2f;
                float offsetY = (height - handlingSticker.getHeight()) / 2f;
                sticker.getMatrix().postTranslate(offsetX, offsetY);

                float scaleFactor;
                if (width < height) {
                   /* if (handlingSticker instanceof TextSticker)
                        scaleFactor = width / handlingSticker.getWidth();
                    else*/
                        scaleFactor = width / handlingSticker.getDrawable().getIntrinsicWidth();
                } else {
                    /*if (handlingSticker instanceof TextSticker)
                        scaleFactor = height / handlingSticker.getHeight();
                    else*/
                        scaleFactor = height / handlingSticker.getDrawable().getIntrinsicHeight();
                }
                sticker.getMatrix().postScale(scaleFactor / 2f, scaleFactor / 2f, width / 2f, height / 2f);
            }
            int index = stickers.indexOf(handlingSticker);
            stickers.set(index, sticker);
            handlingSticker = sticker;

            invalidate();
            return true;
        } else {
            return false;
        }
    }

    public boolean remove(@Nullable Sticker sticker) {
        if (stickers.contains(sticker)) {
            stickers.remove(sticker);
            if (onStickerOperationListener != null) {
                onStickerOperationListener.onStickerDeleted(sticker);
            }
            if (handlingSticker == sticker) {
                handlingSticker = null;
            }
            invalidate();

            return true;
        } else {
            Log.d(TAG, "remove: the sticker is not in this StickerView");

            return false;
        }
    }

    public boolean removeCurrentSticker() {
        return remove(handlingSticker);
    }

    public void removeAllStickers() {
        stickers.clear();
        if (handlingSticker != null) {
            handlingSticker.release();
            handlingSticker = null;
        }
        invalidate();
    }

    @NonNull
    public StickerView addSticker(@NonNull Sticker sticker) {
        return addSticker(sticker, Sticker.Position.CENTER);
    }

    public StickerView addSticker(@NonNull final Sticker sticker,
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
        return this;
    }

    protected void addStickerImmediately(@NonNull Sticker sticker, @Sticker.Position int position) {
        setStickerPosition(sticker, position);

        sticker.getMatrix().postScale(1, 1, getWidth(), getHeight());

        handlingSticker = sticker;
        stickers.add(sticker);
        if (onStickerOperationListener != null) {
            onStickerOperationListener.onStickerAdded(sticker);
        }
        invalidate();
    }

    protected void setStickerPosition(@NonNull Sticker sticker, @Sticker.Position int position) {
        float width = getWidth();
        float height = getHeight();
        float offsetX = width - sticker.getWidth();
        float offsetY = height - sticker.getHeight();

       /* if (sticker instanceof BeautySticker) {
            BeautySticker beautySticker = (BeautySticker) sticker;
            offsetY /= 2f;
            if (beautySticker.getType() == BeautySticker.BUST_0) {
                offsetX = offsetX / 3f;
            } else if (beautySticker.getType() == BeautySticker.BUST_1) {
                offsetX = offsetX * 2 / 3f;
            } else if (beautySticker.getType() == BeautySticker.HIP_1) {
                offsetX = offsetX / 2;
            } else if (beautySticker.getType() == BeautySticker.FACE) {
                offsetX = offsetX / 2;
            } else if (beautySticker.getType() == BeautySticker.TALL_1) {
                offsetX = offsetX / 2;
                offsetY = offsetY * 2f / 3f;
            } else if (beautySticker.getType() == BeautySticker.TALL_2) {
                offsetX = offsetX / 2;
                offsetY = offsetY * 3f / 2f;
            }
        } else {*/
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
       // }

        sticker.getMatrix().postTranslate(offsetX, offsetY);
    }

    public void editTextSticker() {
        onStickerOperationListener.onStickerDoubleTapped(handlingSticker);
    }

    @NonNull
    public float[] getStickerPoints(@Nullable Sticker sticker) {
        float[] points = new float[8];
        getStickerPoints(sticker, points);
        return points;
    }

    public void getStickerPoints(@Nullable Sticker sticker, @NonNull float[] dst) {
        if (sticker == null) {
            Arrays.fill(dst, 0);
            return;
        }
        sticker.getBoundPoints(bounds);
        sticker.getMappedPoints(dst, bounds);
    }

    /*public void save(@NonNull File file) {
        try {
            StickerUtils.saveImageToGallery(file, createBitmap());
            StickerUtils.notifySystemGallery(getContext(), file);
        } catch (IllegalArgumentException | IllegalStateException ignored) {
            //
        }
    }*/

    @NonNull
    public Bitmap createBitmap() throws OutOfMemoryError {
        handlingSticker = null;
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        this.draw(canvas);
        return bitmap;
    }

    public int getStickerCount() {
        return stickers.size();
    }

    public boolean isNoneSticker() {
        return getStickerCount() == 0;
    }

    @NonNull
    public StickerView setLocked(boolean locked) {
        this.locked = locked;
        invalidate();
        return this;
    }

    public boolean isLocked() {
        return locked;
    }

    @NonNull
    public StickerView setMinClickDelayTime(int minClickDelayTime) {
        this.minClickDelayTime = minClickDelayTime;
        return this;
    }

    public int getMinClickDelayTime() {
        return minClickDelayTime;
    }

    public boolean isConstrained() {
        return constrained;
    }

    @NonNull
    public StickerView setConstrained(boolean constrained) {
        this.constrained = constrained;
        postInvalidate();
        return this;
    }

    @NonNull
    public StickerView setOnStickerOperationListener(
            @Nullable OnStickerOperationListener onStickerOperationListener) {
        this.onStickerOperationListener = onStickerOperationListener;
        return this;
    }

    @Nullable
    public OnStickerOperationListener getOnStickerOperationListener() {
        return onStickerOperationListener;
    }

    @Nullable
    public Sticker getCurrentSticker() {
        return handlingSticker;
    }

  /*  @NonNull
    public List<BitmapStickerIcon> getIcons() {
        return icons;
    }

    public void setIcons(@NonNull List<BitmapStickerIcon> icons) {
        this.icons.clear();
        this.icons.addAll(icons);
        invalidate();
    }*/

    public interface OnStickerOperationListener {
        void onStickerAdded(@NonNull Sticker sticker);

        void onStickerClicked(@NonNull Sticker sticker);

        void onStickerDeleted(@NonNull Sticker sticker);

        void onStickerTouchOutside();

        void onStickerDragFinished(@NonNull Sticker sticker);

        void onStickerTouchedDown(@NonNull Sticker sticker);

        void onStickerZoomFinished(@NonNull Sticker sticker);

        void onStickerFlipped(@NonNull Sticker sticker);

        void onStickerDoubleTapped(@NonNull Sticker sticker);

        void onTouchDownForBeauty(float x, float y);

        void onTouchDragForBeauty(float x, float y);

        void onTouchUpForBeauty(float x, float y);
    }

   
}
