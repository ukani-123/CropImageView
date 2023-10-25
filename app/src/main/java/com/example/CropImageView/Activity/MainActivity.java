package com.example.CropImageView.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
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
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Layout;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import com.example.CropImageView.Interface.TextStickerListener;
import com.example.CropImageView.Interface.Tools;
import com.example.cropimageview.R;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, ClickSticker, TextStickerListener {
    RecyclerView subRecyclerView, mainRecyclerView, adjustmentRcv, textRecyclerView, colorRecyclerView, fontRecyclerView;
    Bitmap mOriginalBitmap;
    LinearLayout mainBottom, txtBottom;
    SeekBar sbBrightness, sbContrast, sbSaturation, sbSharp, sbTemp;
    ToolsAdapter toolsAdapter;
    TextView btnText;
    FilterAdapter filterAdapter;
    AdjustAdapter adjustAdapter;
    EditText editText;
    TextSticker textSticker;
    SplashView splashView;
    StickerView stickerView;
    TextAdapter textAdapter;
    ViewPager viewPager;
    int click = 0;
    TabLayout tabLayout;
    Bitmap mOperationalBitmap = null;
    private ImageView resultIv, close, reset, done, txtClose, txtDone;

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
        /*FontBinder.listAssetFiles(this,"Fonts");
        Log.d("list", "onCreate: "+FontBinder.getFontPathList());*/

        textSticker = new TextSticker(MainActivity.this);

        txtBottom = findViewById(R.id.txtBottom);
        mainBottom = findViewById(R.id.mainBottom);
        tabLayout = findViewById(R.id.tabLayout);
        txtClose = findViewById(R.id.closetxt);
        txtDone = findViewById(R.id.Donetxt);
        btnText = findViewById(R.id.btnDone);
        resultIv = findViewById(R.id.resultIv);
        viewPager = findViewById(R.id.viewPager);
        done = findViewById(R.id.Done);
        stickerView = findViewById(R.id.stickerView);
        splashView = findViewById(R.id.splashView);
        editText = findViewById(R.id.edtText);
        reset = findViewById(R.id.reset);
        close = findViewById(R.id.close);
        subRecyclerView = findViewById(R.id.subRecyclerView);
        textRecyclerView = findViewById(R.id.textRecyclerView);
        colorRecyclerView = findViewById(R.id.colorRecyclerView);
        mainRecyclerView = findViewById(R.id.mainRecyclerView);
        adjustmentRcv = findViewById(R.id.adjustmentRcv);
        fontRecyclerView = findViewById(R.id.fontRecyclerView);
        sbBrightness = findViewById(R.id.sbBrightness);
        sbContrast = findViewById(R.id.sbContrast);
        sbSharp = findViewById(R.id.sbSharp);
        sbTemp = findViewById(R.id.sbTemp);
        sbSaturation = findViewById(R.id.sbSaturation);

        resultIv.setImageBitmap(mOriginalBitmap);

        ArrayList<ColorFilter> data = new ArrayList<>();
        data.add(new PorterDuffColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY));
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

        ArrayList<String> fontList = new ArrayList<>();
        fontList.add("");

        BitmapStickerIcon deleteIcon = new BitmapStickerIcon(ContextCompat.getDrawable(this,
                R.drawable.sticker_ic_close_white_18dp),
                BitmapStickerIcon.LEFT_TOP);
        deleteIcon.setIconEvent(new DeleteIconEvent());
        BitmapStickerIcon zoomIcon = new BitmapStickerIcon(ContextCompat.getDrawable(this,
                R.drawable.sticker_ic_scale_white_18dp),
                BitmapStickerIcon.RIGHT_BOTOM);
        zoomIcon.setIconEvent(new ZoomIconEvent());
        BitmapStickerIcon flipIcon = new BitmapStickerIcon(ContextCompat.getDrawable(this,
                R.drawable.sticker_ic_flip_white_18dp),
                BitmapStickerIcon.RIGHT_TOP);
        flipIcon.setIconEvent(new FlipHorizontallyEvent());
        stickerView.setIcons(Arrays.asList(deleteIcon, zoomIcon, flipIcon));
        stickerView.setConstrained(true);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
                } else {
                    saveImageToGallery(MainActivity.this, splashView.getBitmap(mOperationalBitmap));
                    resultIv.setDrawingCacheEnabled(false);
                    Toast.makeText(MainActivity.this, "Image Save Successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultIv.setColorFilter(null);
                resultIv.setImageBitmap(mOriginalBitmap);
                sbBrightness.setProgress(0);
                sbContrast.setProgress(0);
                sbSaturation.setProgress(0);
                sbTemp.setProgress(0);
                sbSharp.setProgress(0);
                splashView.setVisibility(View.GONE);
                stickerView.setVisibility(View.GONE);
                viewPager.setVisibility(View.GONE);
                tabLayout.setVisibility(View.GONE);
                stickerView.remove(textSticker);

                mOperationalBitmap = mOriginalBitmap;
            }
        });
        ArrayList<Integer> toolsList = new ArrayList<>();
        toolsList.add(R.drawable.filter);
        toolsList.add(R.drawable.adjust);
        toolsList.add(R.drawable.sticker);
        toolsList.add(R.drawable.text);

        ArrayList<Integer> adjustList = new ArrayList<>();
        adjustList.add(R.drawable.brightness);
        adjustList.add(R.drawable.contrast);
        adjustList.add(R.drawable.saturation);
        adjustList.add(R.drawable.temp);
        adjustList.add(R.drawable.sharp);
        adjustList.add(R.drawable.focus);

        ArrayList<Integer> textTool = new ArrayList<>();
        textTool.add(R.drawable.color);
        textTool.add(R.drawable.bgcolor);
        textTool.add(R.drawable.font);
        textTool.add(R.drawable.center);
        mainRecyclerView.setAdapter(toolsAdapter = new ToolsAdapter(this, toolsList, new Tools() {
            @Override
            public void onClickTool(int tool) {
                if (tool == 0) {
                    tabLayout.setVisibility(View.GONE);
                    editText.setVisibility(View.GONE);
                    subRecyclerView.setVisibility(View.VISIBLE);
                    btnText.setVisibility(View.GONE);
                    subRecyclerView.setAdapter(filterAdapter = new FilterAdapter(MainActivity.this, data, new Filter() {
                        @Override
                        public void onClickItem(ColorFilter filter) {
                            resultIv.setColorFilter(filter);
                        }
                    }));
                    filterAdapter.setBitmap(mOriginalBitmap);
                } else if (tool == 1) {
                    tabLayout.setVisibility(View.GONE);
                    editText.setVisibility(View.GONE);
                    btnText.setVisibility(View.GONE);
                    viewPager.setVisibility(View.GONE);
                    tabLayout.setVisibility(View.GONE);
                    subRecyclerView.setVisibility(View.VISIBLE);
                    subRecyclerView.setAdapter(adjustAdapter = new AdjustAdapter(MainActivity.this, adjustList, new Adjust() {
                        @Override
                        public void onClickAdjust(int position) {
                            if (position == 0) {
                                sbBrightness.setVisibility(View.VISIBLE);
                                sbContrast.setVisibility(View.GONE);
                                stickerView.setLocked(false);
                                viewPager.setVisibility(View.GONE);
                                tabLayout.setVisibility(View.GONE);
                                sbSaturation.setVisibility(View.GONE);
                                sbSharp.setVisibility(View.GONE);
                                sbTemp.setVisibility(View.GONE);
                                splashView.setVisibility(View.GONE);
                            } else if (position == 1) {
                                sbContrast.setVisibility(View.VISIBLE);
                                sbBrightness.setVisibility(View.GONE);
                                sbTemp.setVisibility(View.GONE);
                                viewPager.setVisibility(View.GONE);
                                tabLayout.setVisibility(View.GONE);
                                sbSharp.setVisibility(View.GONE);
                                stickerView.setLocked(false);
                                sbSaturation.setVisibility(View.GONE);
                                splashView.setVisibility(View.GONE);
                            } else if (position == 2) {
                                sbSaturation.setVisibility(View.VISIBLE);
                                stickerView.setLocked(false);
                                sbBrightness.setVisibility(View.GONE);
                                viewPager.setVisibility(View.GONE);
                                tabLayout.setVisibility(View.GONE);
                                sbTemp.setVisibility(View.GONE);
                                sbSharp.setVisibility(View.GONE);
                                sbContrast.setVisibility(View.GONE);
                                splashView.setVisibility(View.GONE);
                            } else if (position == 3) {
                                sbTemp.setVisibility(View.VISIBLE);
                                stickerView.setLocked(false);
                                sbContrast.setVisibility(View.GONE);
                                viewPager.setVisibility(View.GONE);
                                tabLayout.setVisibility(View.GONE);
                                sbSharp.setVisibility(View.GONE);
                                sbSaturation.setVisibility(View.GONE);
                                sbBrightness.setVisibility(View.GONE);
                                splashView.setVisibility(View.GONE);
                            } else if (position == 4) {
                                sbSharp.setVisibility(View.VISIBLE);
                                sbTemp.setVisibility(View.GONE);
                                stickerView.setLocked(false);
                                sbContrast.setVisibility(View.GONE);
                                sbSaturation.setVisibility(View.GONE);
                                viewPager.setVisibility(View.GONE);
                                tabLayout.setVisibility(View.GONE);
                                sbBrightness.setVisibility(View.GONE);
                                splashView.setVisibility(View.GONE);
                            } else if (position == 5) {
                                splashView.setVisibility(View.VISIBLE);
                                splashView.setCurrentSplashMode(SplashView.SHAPE);
                                splashView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                                splashView.setImageBitmap(NativeStackBlur.process(mOperationalBitmap, 150));
                                SplashSticker splashSticker = new SplashSticker(((BitmapDrawable) getDrawable(R.drawable.blur_1_mask)).getBitmap(), ((BitmapDrawable) getDrawable(R.drawable.blur_1_shadow)).getBitmap());
                                splashView.addSticker(splashSticker);
                                stickerView.setLocked(true);

                                viewPager.setVisibility(View.GONE);
                                tabLayout.setVisibility(View.GONE);
                                sbSharp.setVisibility(View.GONE);
                                sbTemp.setVisibility(View.GONE);
                                sbContrast.setVisibility(View.GONE);
                                sbSaturation.setVisibility(View.GONE);
                                sbBrightness.setVisibility(View.GONE);
                            }
                            /*else if (position == 6) {
                                stickerView.setVisibility(View.VISIBLE);
                                viewPager.setVisibility(View.VISIBLE);
                                tabLayout.setVisibility(View.VISIBLE);
                                viewPager.setAdapter(new pagerAdapter(getSupportFragmentManager(), MainActivity.this));
                                tabLayout.setupWithViewPager(viewPager);
                                stickerView.setLocked(false);

                                splashView.setVisibility(View.GONE);
                                sbSharp.setVisibility(View.GONE);
                                sbTemp.setVisibility(View.GONE);
                                sbContrast.setVisibility(View.GONE);
                                sbSaturation.setVisibility(View.GONE);
                                sbBrightness.setVisibility(View.GONE);
                            }*/
//                    adjustmentRcv.setVisibility(View.GONE);
                            tabLayout.setVisibility(View.GONE);

                        }
                    }));

                } else if (tool == 2) {
                    stickerView.setVisibility(View.VISIBLE);
                    viewPager.setVisibility(View.VISIBLE);
                    tabLayout.setVisibility(View.VISIBLE);
                    viewPager.setAdapter(new pagerAdapter(getSupportFragmentManager(), MainActivity.this));
                    subRecyclerView.setVisibility(View.GONE);
                    tabLayout.setupWithViewPager(viewPager);
                    editText.setVisibility(View.GONE);
                    stickerView.setLocked(false);
                    btnText.setVisibility(View.GONE);
                    splashView.setVisibility(View.GONE);
                    sbSharp.setVisibility(View.GONE);
                    sbTemp.setVisibility(View.GONE);
                    sbContrast.setVisibility(View.GONE);
                    sbSaturation.setVisibility(View.GONE);
                    sbBrightness.setVisibility(View.GONE);
                } else if (tool == 3) {
                    subRecyclerView.setVisibility(View.VISIBLE);
                    editText.setVisibility(View.VISIBLE);
                    mainBottom.setVisibility(View.GONE);
                    txtBottom.setVisibility(View.VISIBLE);
                    viewPager.setVisibility(View.GONE);
                    tabLayout.setVisibility(View.GONE);
                    viewPager.setVisibility(View.GONE);
                    textRecyclerView.setVisibility(View.VISIBLE);
                    btnText.setVisibility(View.VISIBLE);
                    subRecyclerView.setAdapter(textAdapter = new TextAdapter(textTool, MainActivity.this, new TextInter() {

                        @Override
                        public void onClickText(int position) {
                            fontRecyclerView.setVisibility(View.GONE);
                            textRecyclerView.setVisibility(View.GONE);
                            mainRecyclerView.setVisibility(View.GONE);
                            subRecyclerView.setVisibility(View.VISIBLE);
                            sbBrightness.setVisibility(View.GONE);
                            sbContrast.setVisibility(View.GONE);
                            sbSaturation.setVisibility(View.GONE);
                            sbSharp.setVisibility(View.GONE);
                            sbTemp.setVisibility(View.GONE);
                            adjustmentRcv.setVisibility(View.GONE);
                            if (position == 0) {
                                tabLayout.setVisibility(View.GONE);
                                viewPager.setVisibility(View.GONE);
                                textRecyclerView.setVisibility(View.GONE);
                                btnText.setVisibility(View.GONE);
                                mainRecyclerView.setVisibility(View.VISIBLE);
                                editText.setVisibility(View.GONE);
                                subRecyclerView.setVisibility(View.GONE);
                                colorRecyclerView.setVisibility(View.VISIBLE);
                                stickerView.setVisibility(View.VISIBLE);
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
                            } else if (position == 1) {
                                tabLayout.setVisibility(View.GONE);
                                viewPager.setVisibility(View.GONE);
                                textRecyclerView.setVisibility(View.GONE);
                                subRecyclerView.setVisibility(View.GONE);
                                mainRecyclerView.setVisibility(View.VISIBLE);
                                editText.setVisibility(View.GONE);
                                btnText.setVisibility(View.GONE);
                                colorRecyclerView.setVisibility(View.VISIBLE);
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
                            } else if (position == 2) {
                                editText.setVisibility(View.GONE);
                                fontRecyclerView.setVisibility(View.VISIBLE);
                                textRecyclerView.setVisibility(View.GONE);
                                subRecyclerView.setVisibility(View.GONE);
                                sbBrightness.setVisibility(View.GONE);
                                mainRecyclerView.setVisibility(View.VISIBLE);
                                btnText.setVisibility(View.GONE);
                                sbContrast.setVisibility(View.GONE);
                                sbSaturation.setVisibility(View.GONE);
                                sbSharp.setVisibility(View.GONE);
                                sbTemp.setVisibility(View.GONE);
                                adjustmentRcv.setVisibility(View.GONE);
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
                            } else if (position == 3) {
                                tabLayout.setVisibility(View.GONE);
                                viewPager.setVisibility(View.GONE);
                                editText.setVisibility(View.GONE);
                                btnText.setVisibility(View.GONE);
                                mainRecyclerView.setVisibility(View.VISIBLE);
                                textTool.clear();
                                textTool.add(R.drawable.color);
                                textTool.add(R.drawable.bgcolor);
                                textTool.add(R.drawable.font);
                                if (stickerView.getCurrentSticker() instanceof TextSticker) {
                                    TextSticker operational = (TextSticker) stickerView.getCurrentSticker();
                                    if (click == 0) {
                                        textTool.add(R.drawable.left);
                                        operational.setTextAlign(Layout.Alignment.ALIGN_OPPOSITE);
                                        click++;
                                    } else if (click == 1) {
                                        textTool.add(R.drawable.center);
                                        operational.setTextAlign(Layout.Alignment.ALIGN_NORMAL);
                                        click++;
                                    } else if (click == 2) {
                                        textTool.add(R.drawable.right);
                                        operational.setTextAlign(Layout.Alignment.ALIGN_CENTER);
                                        click = 0;
                                    }
                                    stickerView.replace(operational);
                                    stickerView.invalidate();
                                }
                                textAdapter.notifyDataSetChanged();
                            }
                        }
                    }));

                    btnText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (editText.getText().toString().equals("")){
                                editText.setVisibility(View.GONE);
                                btnText.setVisibility(View.GONE);
                            }else {
                                String text = editText.getText().toString();
                                textSticker.setText(text);
                                textSticker.resizeText();
                                stickerView.addSticker(textSticker);
                                editText.setText(" ");
                                editText.setVisibility(View.GONE);
                                btnText.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }
        }));
        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainBottom.setVisibility(View.VISIBLE);
                mainRecyclerView.setVisibility(View.VISIBLE);
                btnText.setVisibility(View.GONE);
                subRecyclerView.setVisibility(View.GONE);
                txtBottom.setVisibility(View.GONE);
                editText.setVisibility(View.GONE);
                textRecyclerView.setVisibility(View.GONE);
                colorRecyclerView.setVisibility(View.GONE);
                fontRecyclerView.setVisibility(View.GONE);
            }
        });
        txtDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setVisibility(View.GONE);
                textRecyclerView.setVisibility(View.GONE);
                fontRecyclerView.setVisibility(View.GONE);
                colorRecyclerView.setVisibility(View.GONE);
                mainRecyclerView.setVisibility(View.VISIBLE);
                subRecyclerView.setVisibility(View.VISIBLE);
            }
        });

        sbBrightness.setOnSeekBarChangeListener(this);
        sbContrast.setOnSeekBarChangeListener(this);
        sbSaturation.setOnSeekBarChangeListener(this);
        sbTemp.setOnSeekBarChangeListener(this);
        sbSharp.setOnSeekBarChangeListener(this);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subRecyclerView.setVisibility(View.GONE);
                mainRecyclerView.setVisibility(View.VISIBLE);
                adjustmentRcv.setVisibility(View.GONE);
                sbContrast.setVisibility(View.GONE);
                viewPager.setVisibility(View.GONE);
                sbSaturation.setVisibility(View.GONE);
                tabLayout.setVisibility(View.GONE);
                sbBrightness.setVisibility(View.GONE);
                sbSharp.setVisibility(View.GONE);
                sbTemp.setVisibility(View.GONE);
            }
        });
    }

    private void saveImageToGallery(Context context, Bitmap bitmap) {
        String fileName = "Image_" + System.currentTimeMillis() + ".jpg";
        File imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), fileName);
        try {
            OutputStream outputStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            MediaScannerConnection.scanFile(context, new String[]{imageFile.getAbsolutePath()}, null, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void onTextStickerChanged(TextSticker textSticker) {
        textSticker.notifyDataSetChanged();
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

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
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
        mOperationalBitmap = ((BitmapDrawable) resultIv.getDrawable()).getBitmap();
    }

    @Override
    public void onClickSticker(String path) {
        AssetManager assetManager = getAssets();
        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open(path);
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        stickerView.addSticker(new DrawableSticker(new BitmapDrawable(getResources(), bitmap)));
        tabLayout.setVisibility(View.GONE);
        viewPager.setVisibility(View.GONE);
    }

}