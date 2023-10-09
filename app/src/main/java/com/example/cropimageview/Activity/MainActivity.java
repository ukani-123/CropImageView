package com.example.cropimageview.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.commit451.nativestackblur.NativeStackBlur;
import com.example.cropimageview.Adapter.AdjustAdapter;
import com.example.cropimageview.Adapter.FilterAdapter;
import com.example.cropimageview.Adapter.ToolsAdapter;
import com.example.cropimageview.Adapter.pagerAdapter;
import com.example.cropimageview.Fragment.StickerFragment;
import com.example.cropimageview.Helper.BitmapStickerIcon;
import com.example.cropimageview.Helper.DataBinder;
import com.example.cropimageview.Helper.DeleteIconEvent;
import com.example.cropimageview.Helper.DrawableSticker;
import com.example.cropimageview.Helper.FlipHorizontallyEvent;
import com.example.cropimageview.Helper.SplashSticker;
import com.example.cropimageview.Helper.SplashView;
import com.example.cropimageview.Helper.StickerView;
import com.example.cropimageview.Helper.ZoomIconEvent;
import com.example.cropimageview.Interface.Adjust;
import com.example.cropimageview.Interface.ClickSticker;
import com.example.cropimageview.Interface.Filter;
import com.example.cropimageview.Interface.Tools;
import com.example.cropimageview.R;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, ClickSticker {
    private ImageView resultIv, close, reset, done;
    RecyclerView subRecyclerView, mainRecyclerView, adjustmentRcv;
    Bitmap mOriginalBitmap;
    SeekBar sbBrightness, sbContrast, sbSaturation, sbSharp, sbTemp;
    FilterAdapter filterAdapter;
    ToolsAdapter toolsAdapter;
    AdjustAdapter adjustAdapter;
    TextView progresses;
    SplashView splashView;
    StickerView stickerView;
    ViewPager viewPager;
    TabLayout tabLayout;
    Bitmap mOperationalBitmap = null;

    @SuppressLint("MissingInflatedId")
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
        done = (ImageView) findViewById(R.id.Done);
        stickerView = findViewById(R.id.stickerView);
        splashView = findViewById(R.id.splashView);
        reset = (ImageView) findViewById(R.id.reset);
        progresses = findViewById(R.id.txtSeekbar);
        close = (ImageView) findViewById(R.id.close);
        subRecyclerView = (RecyclerView) findViewById(R.id.subRecyclerView);
        mainRecyclerView = (RecyclerView) findViewById(R.id.mainRecyclerView);
        adjustmentRcv = (RecyclerView) findViewById(R.id.adjustmentRcv);
        sbBrightness = findViewById(R.id.sbBrightness);
        sbContrast = findViewById(R.id.sbContrast);
        sbSharp = findViewById(R.id.sbSharp);
        sbTemp = findViewById(R.id.sbTemp);
        sbSaturation = findViewById(R.id.sbSaturation);

        progresses.setVisibility(View.GONE);
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

        BitmapStickerIcon deleteIcon = new BitmapStickerIcon(ContextCompat.getDrawable(this,
                com.example.cropimageview.R.drawable.sticker_ic_close_white_18dp),
                BitmapStickerIcon.LEFT_TOP);
        deleteIcon.setIconEvent(new DeleteIconEvent());
        BitmapStickerIcon zoomIcon = new BitmapStickerIcon(ContextCompat.getDrawable(this,
                com.example.cropimageview.R.drawable.sticker_ic_scale_white_18dp),
                BitmapStickerIcon.RIGHT_BOTOM);
        zoomIcon.setIconEvent(new ZoomIconEvent());
        BitmapStickerIcon flipIcon = new BitmapStickerIcon(ContextCompat.getDrawable(this,
                com.example.cropimageview.R.drawable.sticker_ic_flip_white_18dp),
                BitmapStickerIcon.RIGHT_TOP);
        flipIcon.setIconEvent(new FlipHorizontallyEvent());
        stickerView.setIcons(Arrays.asList(deleteIcon, zoomIcon, flipIcon));
        stickerView.setConstrained(true);
        subRecyclerView.setAdapter(filterAdapter = new FilterAdapter(MainActivity.this, data, new Filter() {
            @Override
            public void onClickItem(ColorFilter filter) {
                resultIv.setColorFilter(filter);
            }
        }));
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImageToGallery(MainActivity.this, splashView.getBitmap(mOperationalBitmap));
                resultIv.setDrawingCacheEnabled(false);
                Toast.makeText(MainActivity.this, "Image Save Successfully", Toast.LENGTH_SHORT).show();
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
                mOperationalBitmap = mOriginalBitmap;
            }
        });
        filterAdapter.setBitmap(mOriginalBitmap);

        ArrayList<Integer> toolsList = new ArrayList<>();
        toolsList.add(R.drawable.filter);
        toolsList.add(R.drawable.adjust);
        mainRecyclerView.setAdapter(toolsAdapter = new ToolsAdapter(this, toolsList, new Tools() {
            @Override
            public void onClickTool(int tool) {
                if (tool == 0) {
                    mainRecyclerView.setVisibility(View.GONE);
                    subRecyclerView.setVisibility(View.VISIBLE);
                } else if (tool == 1) {
                    adjustmentRcv.setVisibility(View.VISIBLE);
                    mainRecyclerView.setVisibility(View.GONE);
                    subRecyclerView.setVisibility(View.GONE);
                }
            }
        }));

        ArrayList<Integer> adjustList = new ArrayList<>();
        adjustList.add(R.drawable.brightness);
        adjustList.add(R.drawable.contrast);
        adjustList.add(R.drawable.saturation);
        adjustList.add(R.drawable.temp);
        adjustList.add(R.drawable.sharp);
        adjustList.add(R.drawable.focus);
        adjustList.add(R.drawable.sticker);

        adjustmentRcv.setAdapter(adjustAdapter = new AdjustAdapter(this, adjustList, new Adjust() {
            @Override
            public void onClickAdjust(int position) {
                if (position == 0) {
                    sbBrightness.setVisibility(View.VISIBLE);
                    sbContrast.setVisibility(View.GONE);
                    stickerView.setLocked(false);
                    sbSaturation.setVisibility(View.GONE);
                    sbSharp.setVisibility(View.GONE);
                    sbTemp.setVisibility(View.GONE);
                    splashView.setVisibility(View.GONE);
                } else if (position == 1) {
                    sbContrast.setVisibility(View.VISIBLE);
                    sbBrightness.setVisibility(View.GONE);
                    sbTemp.setVisibility(View.GONE);
                    sbSharp.setVisibility(View.GONE);
                    stickerView.setLocked(false);
                    sbSaturation.setVisibility(View.GONE);
                    splashView.setVisibility(View.GONE);
                } else if (position == 2) {
                    sbSaturation.setVisibility(View.VISIBLE);
                    stickerView.setLocked(false);
                    sbBrightness.setVisibility(View.GONE);
                    sbTemp.setVisibility(View.GONE);
                    sbSharp.setVisibility(View.GONE);
                    sbContrast.setVisibility(View.GONE);
                    splashView.setVisibility(View.GONE);
                } else if (position == 3) {
                    sbTemp.setVisibility(View.VISIBLE);
                    stickerView.setLocked(false);
                    sbContrast.setVisibility(View.GONE);
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
                    sbBrightness.setVisibility(View.GONE);
                    splashView.setVisibility(View.GONE);
                } else if (position == 5) {
                    splashView.setVisibility(View.VISIBLE);
                    splashView.setCurrentSplashMode(SplashView.SHAPE);
                    splashView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                    splashView.setImageBitmap(NativeStackBlur.process(mOperationalBitmap, 100));
                    SplashSticker splashSticker = new SplashSticker(((BitmapDrawable) getDrawable(R.drawable.blur_1_mask)).getBitmap(), ((BitmapDrawable) getDrawable(R.drawable.blur_1_shadow)).getBitmap());
                    splashView.addSticker(splashSticker);

                    stickerView.setLocked(true);
                    sbSharp.setVisibility(View.GONE);
                    sbTemp.setVisibility(View.GONE);
                    sbContrast.setVisibility(View.GONE);
                    sbSaturation.setVisibility(View.GONE);
                    sbBrightness.setVisibility(View.GONE);
                } else if (position == 6) {
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
                }
            }
        }));
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
                sbSaturation.setVisibility(View.GONE);
                sbBrightness.setVisibility(View.GONE);
                sbSharp.setVisibility(View.GONE);
                sbTemp.setVisibility(View.GONE);
                progresses.setVisibility(View.GONE);
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
    public void onStartTrackingTouch(SeekBar seekBar) {}
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