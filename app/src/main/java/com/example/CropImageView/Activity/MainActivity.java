package com.example.CropImageView.Activity;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.commit451.nativestackblur.NativeStackBlur;
import com.example.CropImageView.Adapter.AdjustAdapter;
import com.example.CropImageView.Adapter.ColorAdapter;
import com.example.CropImageView.Adapter.FilterAdapter;
import com.example.CropImageView.Adapter.FontAdapter;
import com.example.CropImageView.Adapter.TextAdapter;
import com.example.CropImageView.Adapter.ToolsAdapter;
import com.example.CropImageView.Adapter.pagerAdapter;
import com.example.CropImageView.Fragment.FullScreenEditTextFragment;
import com.example.CropImageView.Fragment.StickerFragment;
import com.example.CropImageView.Helper.BitmapStickerIcon;
import com.example.CropImageView.Helper.DataBinder;
import com.example.CropImageView.Helper.DeleteIconEvent;
import com.example.CropImageView.Helper.DrawableSticker;
import com.example.CropImageView.Helper.FlipHorizontallyEvent;
import com.example.CropImageView.Helper.SplashSticker;
import com.example.CropImageView.Helper.SplashView;
import com.example.CropImageView.Helper.StickerView;
import com.example.CropImageView.Helper.TextSticker;
import com.example.CropImageView.Helper.ZoomIconEvent;
import com.example.CropImageView.Interface.Adjust;
import com.example.CropImageView.Interface.ClickSticker;
import com.example.CropImageView.Interface.ColorInter;
import com.example.CropImageView.Interface.Filter;
import com.example.CropImageView.Interface.FontClick;
import com.example.CropImageView.Interface.TextInter;
import com.example.CropImageView.model.AdjustModel;
import com.example.CropImageView.model.TextTool;
import com.example.CropImageView.model.Tool;
import com.example.cropimageview.R;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity implements ClickSticker, SeekBar.OnSeekBarChangeListener {
    RecyclerView subRecyclerView, colorRecyclerView, fontRecyclerView;
    Bitmap mOriginalBitmap;
    LinearLayout filter, adjust, sticker, text;
    SeekBar sbBrightness, sbContrast, sbSaturation, sbSharp, sbTemp;
    ToolsAdapter toolsAdapter;
    ConstraintLayout stickerLayout;
    FilterAdapter filterAdapter;
    AdjustAdapter adjustAdapter;
    SplashView splashView;
    StickerView stickerView;
    TextAdapter textAdapter;
    ViewPager viewPager;
    int click = 0;
    TabLayout tabLayout;
    Bitmap mOperationalBitmap = null;
    private ImageView resultIv, imgDone, imgClose;

    public static ArrayList<Integer> fetchTextStickerColor() {
        ArrayList<Integer> list = new ArrayList<>();


        list.add(Color.parseColor("#b71c1c"));
        list.add(Color.parseColor("#ff8a80"));
        list.add(Color.parseColor("#ff5252"));
        list.add(Color.parseColor("#ff1744"));
        list.add(Color.parseColor("#d50000"));
        list.add(Color.parseColor("#880e4f"));
        list.add(Color.parseColor("#ff80ab"));
        list.add(Color.parseColor("#ff4081"));
        list.add(Color.parseColor("#f50057"));
        list.add(Color.parseColor("#c51162"));
        list.add(Color.parseColor("#4a148c"));
        list.add(Color.parseColor("#ea80fc"));
        list.add(Color.parseColor("#e040fb"));
        list.add(Color.parseColor("#d500f9"));
        list.add(Color.parseColor("#aa00ff"));
        list.add(Color.parseColor("#311b92"));
        list.add(Color.parseColor("#b388ff"));
        list.add(Color.parseColor("#7c4dff"));
        list.add(Color.parseColor("#651fff"));
        list.add(Color.parseColor("#6200ea"));
        list.add(Color.parseColor("#1a237e"));
        list.add(Color.parseColor("#8c9eff"));
        list.add(Color.parseColor("#536dfe"));
        list.add(Color.parseColor("#3d5afe"));
        list.add(Color.parseColor("#304ffe"));
        list.add(Color.parseColor("#0d47a1"));
        list.add(Color.parseColor("#82b1ff"));
        list.add(Color.parseColor("#448aff"));
        list.add(Color.parseColor("#2979ff"));
        list.add(Color.parseColor("#2962ff"));
        list.add(Color.parseColor("#01579b"));
        list.add(Color.parseColor("#80d8ff"));
        list.add(Color.parseColor("#40c4ff"));
        list.add(Color.parseColor("#00b0ff"));
        list.add(Color.parseColor("#0091ea"));
        list.add(Color.parseColor("#006064"));
        list.add(Color.parseColor("#84ffff"));
        list.add(Color.parseColor("#18ffff"));
        list.add(Color.parseColor("#00e5ff"));
        list.add(Color.parseColor("#00b8d4"));
        list.add(Color.parseColor("#004d40"));
        list.add(Color.parseColor("#a7ffeb"));
        list.add(Color.parseColor("#64ffda"));
        list.add(Color.parseColor("#1de9b6"));
        list.add(Color.parseColor("#00bfa5"));
        list.add(Color.parseColor("#1b5e20"));
        list.add(Color.parseColor("#b9f6ca"));
        list.add(Color.parseColor("#69f0ae"));
        list.add(Color.parseColor("#00e676"));
        list.add(Color.parseColor("#00c853"));
        list.add(Color.parseColor("#33691e"));
        list.add(Color.parseColor("#ccff90"));
        list.add(Color.parseColor("#b2ff59"));
        list.add(Color.parseColor("#76ff03"));
        list.add(Color.parseColor("#64dd17"));
        list.add(Color.parseColor("#827717"));
        list.add(Color.parseColor("#f4ff81"));
        list.add(Color.parseColor("#eeff41"));
        list.add(Color.parseColor("#c6ff00"));
        list.add(Color.parseColor("#aeea00"));
        list.add(Color.parseColor("#f57f17"));
        list.add(Color.parseColor("#ffff8d"));
        list.add(Color.parseColor("#ffff00"));
        list.add(Color.parseColor("#ffea00"));
        list.add(Color.parseColor("#ffd600"));
        list.add(Color.parseColor("#ff6f00"));
        list.add(Color.parseColor("#ffe57f"));
        list.add(Color.parseColor("#ffd740"));
        list.add(Color.parseColor("#ffc400"));
        list.add(Color.parseColor("#ffab00"));
        list.add(Color.parseColor("#e65100"));
        list.add(Color.parseColor("#ffd180"));
        list.add(Color.parseColor("#ffab40"));
        list.add(Color.parseColor("#ff9100"));
        list.add(Color.parseColor("#ff6d00"));
        list.add(Color.parseColor("#bf360c"));
        list.add(Color.parseColor("#ff9e80"));
        list.add(Color.parseColor("#ff6e40"));
        list.add(Color.parseColor("#ff3d00"));
        list.add(Color.parseColor("#dd2c00"));
        list.add(Color.parseColor("#795548"));
        list.add(Color.parseColor("#6d4c41"));
        list.add(Color.parseColor("#5d4037"));
        list.add(Color.parseColor("#4e342e"));
        list.add(Color.parseColor("#3e2723"));
        list.add(Color.parseColor("#9e9e9e"));
        list.add(Color.parseColor("#757575"));
        list.add(Color.parseColor("#616161"));
        list.add(Color.parseColor("#424242"));
        list.add(Color.parseColor("#212121"));
        list.add(Color.parseColor("#607d8b"));
        list.add(Color.parseColor("#546e7a"));
        list.add(Color.parseColor("#455a64"));
        list.add(Color.parseColor("#37474f"));
        list.add(Color.parseColor("#263238"));
        return list;
    }

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            mOriginalBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(getIntent().getStringExtra("data")));
            mOperationalBitmap = mOriginalBitmap;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        DataBinder.listAssetFiles(this, "Sticker");
        tabLayout = findViewById(R.id.tabLayout);
        resultIv = findViewById(R.id.resultIv);
        viewPager = findViewById(R.id.viewPager);
        stickerView = findViewById(R.id.stickerView);
        splashView = findViewById(R.id.splashView);
        imgDone = findViewById(R.id.imgDone);
        imgClose = findViewById(R.id.imgClose);

        filter = findViewById(R.id.filter);
        adjust = findViewById(R.id.adjust);
        sticker = findViewById(R.id.sticker);
        text = findViewById(R.id.Text);

        stickerLayout = findViewById(R.id.stickerLayout);

        subRecyclerView = findViewById(R.id.subRecyclerView);
        colorRecyclerView = findViewById(R.id.colorRecyclerView);
        fontRecyclerView = findViewById(R.id.fontRecyclerView);
        resultIv.setImageBitmap(mOriginalBitmap);

        sbBrightness = findViewById(R.id.sbBrightness);
        sbContrast = findViewById(R.id.sbContrast);
        sbSaturation = findViewById(R.id.sbSaturation);
        sbTemp = findViewById(R.id.sbTemp);
        sbSharp = findViewById(R.id.sbSharp);

        ArrayList<ColorFilter> data = new ArrayList<>();
        data.add(null);
        data.add(new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY));
        data.add(new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY));
        data.add(new PorterDuffColorFilter(Color.DKGRAY, PorterDuff.Mode.MULTIPLY));
        data.add(new PorterDuffColorFilter(Color.MAGENTA, PorterDuff.Mode.MULTIPLY));
        data.add(new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY));
        data.add(new PorterDuffColorFilter(Color.YELLOW, PorterDuff.Mode.MULTIPLY));
        data.add(new PorterDuffColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY));
        data.add(new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY));
        data.add(new PorterDuffColorFilter(Color.CYAN, PorterDuff.Mode.MULTIPLY));
        StickerFragment.setClickSticker(this);

        BitmapStickerIcon deleteIcon = new BitmapStickerIcon(ContextCompat.getDrawable(this, R.drawable.sticker_ic_close_white_18dp), BitmapStickerIcon.LEFT_TOP);
        deleteIcon.setIconEvent(new DeleteIconEvent());
        BitmapStickerIcon zoomIcon = new BitmapStickerIcon(ContextCompat.getDrawable(this, R.drawable.sticker_ic_scale_white_18dp), BitmapStickerIcon.RIGHT_BOTOM);
        zoomIcon.setIconEvent(new ZoomIconEvent());
        BitmapStickerIcon flipIcon = new BitmapStickerIcon(ContextCompat.getDrawable(this, R.drawable.sticker_ic_flip_white_18dp), BitmapStickerIcon.RIGHT_TOP);
        flipIcon.setIconEvent(new FlipHorizontallyEvent());
        stickerView.setIcons(Arrays.asList(deleteIcon, zoomIcon, flipIcon));
        stickerView.setConstrained(true);

        ArrayList<Tool> toolsList = new ArrayList<>();
        toolsList.add(new Tool("Filter", R.drawable.filter));
        toolsList.add(new Tool("adjust", R.drawable.adjust));
        toolsList.add(new Tool("Sticker", R.drawable.sticker));
        toolsList.add(new Tool("Text", R.drawable.text));

        ArrayList<AdjustModel> adjustList = new ArrayList<>();
        adjustList.add(new AdjustModel("Brightness", R.drawable.brightness));
        adjustList.add(new AdjustModel("Contrast", R.drawable.contrast));
        adjustList.add(new AdjustModel("Saturation", R.drawable.saturation));
        adjustList.add(new AdjustModel("Temp", R.drawable.temp));
        adjustList.add(new AdjustModel("Sharp", R.drawable.sharp));
        adjustList.add(new AdjustModel("Focus", R.drawable.focus));

        ArrayList<TextTool> textTool = new ArrayList<>();
        textTool.add(new TextTool("color", R.drawable.fontcolor));
        textTool.add(new TextTool("Bg Color", R.drawable.bgcolor));
        textTool.add(new TextTool("Font", R.drawable.font));
        textTool.add(new TextTool("Align", R.drawable.center));
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                visible(subRecyclerView);
                gone(colorRecyclerView, fontRecyclerView, splashView, stickerLayout);
                stickerUnLock();
                subRecyclerView.setAdapter(filterAdapter = new FilterAdapter(MainActivity.this, data, new Filter() {
                    @Override
                    public void onClickItem(ColorFilter filter) {
                        resultIv.setColorFilter(filter);
                    }
                }));
                filterAdapter.setBitmap(mOriginalBitmap);
            }
        });
        adjust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stickerUnLock();
                visible(subRecyclerView);
                gone(colorRecyclerView, fontRecyclerView, splashView, stickerLayout);
                subRecyclerView.setAdapter(adjustAdapter = new AdjustAdapter(MainActivity.this, adjustList, new Adjust() {
                    @Override
                    public void onClickAdjust(int position) {
                        if (position == 0) {
                            stickerUnLock();
                            visible(sbBrightness);
                            gone(sbContrast, colorRecyclerView, sbSaturation, sbTemp, sbSharp, splashView, stickerLayout);
                        } else if (position == 1) {
                            stickerUnLock();
                            visible(sbContrast);
                            gone(sbBrightness, colorRecyclerView, sbSaturation, sbTemp, sbSharp, splashView, stickerLayout);
                        } else if (position == 2) {
                            stickerUnLock();
                            visible(sbSaturation);
                            gone(sbBrightness, colorRecyclerView, sbContrast, sbTemp, sbSharp, splashView, stickerLayout);
                        } else if (position == 3) {
                            stickerUnLock();
                            visible(sbTemp);
                            gone(sbContrast, colorRecyclerView, sbSaturation, sbBrightness, sbSharp, splashView, stickerLayout);
                        } else if (position == 4) {
                            stickerUnLock();
                            visible(sbSharp);
                            gone(sbContrast, colorRecyclerView, sbSaturation, sbBrightness, sbTemp, splashView, stickerLayout);
                        } else if (position == 5) {
                            visible(splashView);
                            gone(sbContrast, colorRecyclerView, sbSaturation, sbBrightness, sbSharp, sbTemp, stickerLayout);
                            splashView.setCurrentSplashMode(SplashView.SHAPE);
                            splashView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                            splashView.setImageBitmap(NativeStackBlur.process(mOperationalBitmap, 150));
                            SplashSticker splashSticker = new SplashSticker(((BitmapDrawable) getDrawable(R.drawable.blur_1_mask)).getBitmap(), ((BitmapDrawable) getDrawable(R.drawable.blur_1_shadow)).getBitmap());
                            splashView.addSticker(splashSticker);
                            splashView.removeTouchIcon();
                            stickerView.setLocked(true);
                            splashView.setLock(false);
                        }
                        gone(tabLayout);
                    }
                }));
            }
        });
        sticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                visible(stickerLayout);
                visible(viewPager);
                visible(tabLayout);
                stickerUnLock();
                gone(subRecyclerView, splashView);
                viewPager.setAdapter(new pagerAdapter(getSupportFragmentManager(), MainActivity.this));
                tabLayout.setupWithViewPager(viewPager);
            }
        });
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFullScreenEditTextFragment();
                visible(subRecyclerView);
                gone(fontRecyclerView, sbBrightness, sbContrast, colorRecyclerView, sbSaturation, sbTemp, sbSharp, stickerLayout);
                txtAdapter();
            }
        });
        imgDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gone(colorRecyclerView, fontRecyclerView);
                visible(subRecyclerView);
                txtAdapter();
            }
        });
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitDialog();
            }
        });

        sbBrightness.setOnSeekBarChangeListener(this);
        sbContrast.setOnSeekBarChangeListener(this);
        sbSaturation.setOnSeekBarChangeListener(this);
        sbTemp.setOnSeekBarChangeListener(this);
        sbSharp.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        exitDialog();
    }

    private Bitmap applyColorMatrix(Bitmap source, ColorMatrix colorMatrix) {
        Bitmap result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), source.getConfig());
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(source, 0, 0, paint);
        return result;
    }

    private Bitmap applyBrightness(Bitmap source, float brightness) {
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.set(new float[]{1, 0, 0, 0, brightness * 255, 0, 1, 0, 0, brightness * 255, 0, 0, 1, 0, brightness * 255, 0, 0, 0, 1, 0});
        return applyColorMatrix(source, colorMatrix);
    }

    private Bitmap applyContrast(Bitmap source, float contrast) {
        ColorMatrix colorMatrix = new ColorMatrix();
        float scale = contrast + 1f;
        float translate = -(0.5f * scale - 0.5f) * 255f;
        colorMatrix.set(new float[]{scale, 0, 0, 0, translate, 0, scale, 0, 0, translate, 0, 0, scale, 0, translate, 0, 0, 0, 1, 0});
        return applyColorMatrix(source, colorMatrix);
    }

    private Bitmap applySaturation(Bitmap source, float saturation) {
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(saturation);
        return applyColorMatrix(source, colorMatrix);
    }

    private Bitmap applyTemperature(Bitmap source, float temperature) {
        float rT = temperature > 0 ? temperature : 0;
        float bT = temperature < 0 ? -temperature : 0;
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.set(new float[]{1, 0, 0, 0, rT * 255, 0, 1, 0, 0, 0, 0, 0, 1, 0, bT * 255, 0, 0, 0, 1, 0});
        return applyColorMatrix(source, colorMatrix);
    }

    private Bitmap applySharpness(Bitmap source, float sharpness) {
        float[] sharpnessMatrix = {-1, -1, -1, -1, 9 + sharpness, -1, -1, -1, -1};
        return applyConvolutionFilter(source, sharpnessMatrix);
    }

    private Bitmap applyConvolutionFilter(Bitmap source, float[] kernel) {
        Bitmap result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), source.getConfig());
        android.renderscript.RenderScript rs = android.renderscript.RenderScript.create(this);
        android.renderscript.Allocation input = android.renderscript.Allocation.createFromBitmap(rs, source);
        android.renderscript.Allocation output = android.renderscript.Allocation.createTyped(rs, input.getType());
        android.renderscript.ScriptIntrinsicConvolve3x3 convolution = android.renderscript.ScriptIntrinsicConvolve3x3.create(rs, android.renderscript.Element.U8_4(rs));
        convolution.setInput(input);
        convolution.setCoefficients(kernel);
        convolution.forEach(output);
        output.copyTo(result);
        input.destroy();
        output.destroy();
        convolution.destroy();
        rs.destroy();
        return result;
    }

    private void showFullScreenEditTextFragment() {
        FullScreenEditTextFragment fullScreenEditTextFragment = new FullScreenEditTextFragment();
        ConstraintLayout rlAddTextSticker;
        FrameLayout frameLayout;

        frameLayout = findViewById(R.id.changefragment);
        rlAddTextSticker = findViewById(R.id.rlAddTextSticker);

        visible(rlAddTextSticker);
        fullScreenEditTextFragment.setOnCloseListener(new FullScreenEditTextFragment.OnCloseListener() {
            @Override
            public void onClose(String strSticker) {
                if (!strSticker.isEmpty()) {
                    TextSticker textSticker = new TextSticker(MainActivity.this);
                    textSticker.setText(strSticker);
                    stickerView.addSticker(textSticker);
                    textSticker.resizeText();
                    stickerUnLock();
                    gone(splashView);
                    frameLayout.setVisibility(View.GONE);
                }
            }
        });
        fullScreenEditTextFragment.show(getSupportFragmentManager(), "FullScreenEditTextFragment");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        frameLayout.setVisibility(View.VISIBLE);
        transaction.replace(R.id.changefragment, fullScreenEditTextFragment);
        transaction.commit();
    }

    public void stickerUnLock() {
        stickerView.setLocked(false);
    }

    @Override
    public void onClickSticker(String path) {
        AssetManager assetManager = getAssets();
        InputStream inputStream;
        Bitmap bitmap = null;
        try {
            inputStream = assetManager.open(path);
            bitmap = BitmapFactory.decodeStream(inputStream);
            gone(stickerLayout);
        } catch (IOException e) {
            e.printStackTrace();
        }
        stickerView.addSticker(new DrawableSticker(new BitmapDrawable(getResources(), bitmap)));
        gone(tabLayout,viewPager);
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if (b) {
            if (seekBar == sbBrightness) {
                float brightness = (float) (sbBrightness.getProgress() - 100) / 100f;
                resultIv.setImageBitmap(applyBrightness(mOperationalBitmap, brightness));
            } else if (seekBar == sbContrast) {
                float contrast = (float) sbContrast.getProgress() / 100f;
                resultIv.setImageBitmap(applyContrast(mOperationalBitmap, contrast));
            } else if (seekBar == sbSaturation) {
                float saturation = (float) sbSaturation.getProgress() / 100f;
                resultIv.setImageBitmap(applySaturation(mOperationalBitmap, saturation));
            } else if (seekBar == sbSharp) {
                float sharpness = (float) sbSharp.getProgress() / 100f;
                resultIv.setImageBitmap(applySharpness(mOperationalBitmap, sharpness));
            } else if (seekBar == sbTemp) {
                float temperature = (float) (sbTemp.getProgress() - 100) / 100f;
                resultIv.setImageBitmap(applyTemperature(mOperationalBitmap, temperature));
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public void visible(View view) {
        view.setVisibility(View.VISIBLE);
    }

    public void gone(View view, View view1, View view2, View view3, View view4, View view5, View view6, View view7) {
        view.setVisibility(View.GONE);
        view1.setVisibility(View.GONE);
        view2.setVisibility(View.GONE);
        view3.setVisibility(View.GONE);
        view4.setVisibility(View.GONE);
        view5.setVisibility(View.GONE);
        view6.setVisibility(View.GONE);
        view7.setVisibility(View.GONE);
    }

    public void gone(View view, View view1, View view2, View view3, View view4, View view5, View view6) {
        view.setVisibility(View.GONE);
        view1.setVisibility(View.GONE);
        view2.setVisibility(View.GONE);
        view3.setVisibility(View.GONE);
        view4.setVisibility(View.GONE);
        view5.setVisibility(View.GONE);
        view6.setVisibility(View.GONE);
    }

    public void gone(View view, View view1, View view2, View view3) {
        view.setVisibility(View.GONE);
        view1.setVisibility(View.GONE);
        view2.setVisibility(View.GONE);
        view3.setVisibility(View.GONE);
    }

    public void gone(View view, View view1, View view2, View view3, View view4, View view5) {
        view.setVisibility(View.GONE);
        view1.setVisibility(View.GONE);
        view2.setVisibility(View.GONE);
        view3.setVisibility(View.GONE);
        view4.setVisibility(View.GONE);
        view5.setVisibility(View.GONE);
    }

    public void gone(View view, View view1, View view2, View view3, View view4) {
        view.setVisibility(View.GONE);
        view1.setVisibility(View.GONE);
        view2.setVisibility(View.GONE);
        view3.setVisibility(View.GONE);
        view4.setVisibility(View.GONE);
    }

    public void gone(View view, View view1, View view2) {
        view.setVisibility(View.GONE);
        view1.setVisibility(View.GONE);
        view2.setVisibility(View.GONE);

    }

    public void gone(View view, View view1) {
        view.setVisibility(View.GONE);
        view1.setVisibility(View.GONE);

    }

    public void gone(View view) {
        view.setVisibility(View.GONE);
    }

    public void txtAdapter() {
        ArrayList<TextTool> textTool = new ArrayList<>();
        textTool.add(new TextTool("color", R.drawable.fontcolor));
        textTool.add(new TextTool("Bg Color", R.drawable.bgcolor));
        textTool.add(new TextTool("Font", R.drawable.font));
        textTool.add(new TextTool("Align", R.drawable.center));
        subRecyclerView.setAdapter(textAdapter = new TextAdapter(textTool, MainActivity.this, new TextInter() {
            @Override
            public void onClickText(int position) {
                switch (position) {
                    case 0:
                        stickerUnLock();
                        visible(colorRecyclerView);
                        gone(fontRecyclerView);
                        colorRecyclerView.setAdapter(new ColorAdapter(MainActivity.this, fetchTextStickerColor(), new ColorInter() {
                            @Override
                            public void onclickColor(int position) {
                                if (stickerView.getCurrentSticker() instanceof TextSticker) {
                                    TextSticker operational = (TextSticker) stickerView.getCurrentSticker();
                                    operational.setTextColor(fetchTextStickerColor().get(position));
                                    stickerView.replace(operational);
                                    stickerView.invalidate();
                                }
                            }
                        }));
                        break;
                    case 1:
                        stickerUnLock();
                        visible(colorRecyclerView);
                        gone(fontRecyclerView);
                        colorRecyclerView.setAdapter(new ColorAdapter(MainActivity.this, fetchTextStickerColor(), new ColorInter() {
                            @Override
                            public void onclickColor(int position) {
                                if (stickerView.getCurrentSticker() instanceof TextSticker) {
                                    TextSticker operational = (TextSticker) stickerView.getCurrentSticker();
                                    operational.setTextBackgroundColor(fetchTextStickerColor().get(position));
                                    stickerView.replace(operational);
                                    stickerView.invalidate();
                                }
                            }
                        }));
                        break;
                    case 2:
                        stickerUnLock();
                        visible(fontRecyclerView);
                        gone(colorRecyclerView);
                        fontRecyclerView.setAdapter(new FontAdapter(MainActivity.this, new FontClick() {
                            @Override
                            public void onFontClick(Typeface typeface) {
                                if (stickerView.getCurrentSticker() instanceof TextSticker) {
                                    TextSticker operational = (TextSticker) stickerView.getCurrentSticker();
                                    operational.setTypeface(typeface);
                                    stickerView.replace(operational);
                                    stickerView.invalidate();
                                }
                            }
                        }));
                        break;
                    case 3:
                        stickerUnLock();
                        textTool.clear();
                        textTool.add(new TextTool("color", R.drawable.fontcolor));
                        textTool.add(new TextTool("Bg Color", R.drawable.bgcolor));
                        textTool.add(new TextTool("Font", R.drawable.font));
                        if (stickerView.getCurrentSticker() instanceof TextSticker) {
                            TextSticker operational = (TextSticker) stickerView.getCurrentSticker();
                            if (click == 0) {
                                textTool.add(new TextTool("Left", R.drawable.left));
                                operational.setTextAlign(Layout.Alignment.ALIGN_NORMAL);
                                click++;
                            } else if (click == 1) {
                                textTool.add(new TextTool("Center", R.drawable.center));
                                operational.setTextAlign(Layout.Alignment.ALIGN_CENTER);
                                click++;
                            } else if (click == 2) {
                                textTool.add(new TextTool("Right", R.drawable.right));
                                operational.setTextAlign(Layout.Alignment.ALIGN_OPPOSITE);
                                click = 0;
                            }
                            stickerView.replace(operational);
                            stickerView.invalidate();
                        }
                        textAdapter.notifyDataSetChanged();
                        break;
                }
            }
        }));

    }
    public String saveToAlbum(Bitmap bitmap1) {
        File file = null;
        String fileName = System.currentTimeMillis() + ".png";
        File root = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "CropImage/");
        if (!root.exists()){
            root.mkdir();
        }
        if (root.mkdirs() || root.isDirectory()) {
            file = new File(root, fileName);
        }
        FileOutputStream os = null;
        try {
            if (resultIv.equals("")) {
                Toast.makeText(this, "00", Toast.LENGTH_SHORT).show();
            }
            os = new FileOutputStream(file);
            bitmap1.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.flush();
        } catch (FileNotFoundException e) {
            Log.e("TAG", e.getMessage());
        } catch (IOException e) {
            Log.e("TAG", e.getMessage());
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                Log.e("TAG", e.getMessage());
            }
        }
        if (file == null) {
            return "";
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            String path = null;
            try {
                path = file.getCanonicalPath();
            } catch (IOException e) {
                Log.e("TAG", e.getMessage());
            }
            MediaScannerConnection.scanFile(MainActivity.this, new String[]{path}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String path, Uri uri) {
                            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                            mediaScanIntent.setData(uri);
                            sendBroadcast(mediaScanIntent);
                        }
                    });
        } else {
            String relationDir = file.getParent();
            File file1 = new File(relationDir);
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.fromFile(file1.getAbsoluteFile())));
        }
        return root + "/" + fileName;
    }

    public void exitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setMessage("are you sure!!");

        builder.setTitle("Exit !");

        builder.setCancelable(false);

        builder.setPositiveButton("Discard changes", (DialogInterface.OnClickListener) (dialog, which) -> {
            finish();
        });
        builder.setNegativeButton("Keep Editing", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}