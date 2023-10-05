package com.example.cropimageview.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.example.cropimageview.R;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.UUID;

public class CropImageActivity extends AppCompatActivity {
    String result;
    Uri imgUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);

        passIntent();

        UCrop.Options options = new UCrop.Options();

        String uri = new StringBuilder(UUID.randomUUID().toString()).append(".jpg").toString();
        UCrop.of(imgUri,Uri.fromFile(new File(getCacheDir(),uri)))
                .withOptions(options)
                .withAspectRatio(0,0)
                .useSourceImageAspectRatio()
                .withMaxResultSize(2000,2000)
                .start(CropImageActivity.this);
    }
    private void passIntent() {
        Intent intent = getIntent();
        if (intent.getExtras()!= null){
            result = getIntent().getStringExtra("DATA");
            imgUri = Uri.parse(result);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP){
            final  Uri resultUri = UCrop.getOutput(data);
            Intent intent = new Intent();
            intent.putExtra("RESULT",resultUri+"");
            setResult(-1,intent);
            finish();
        }else if (resultCode ==UCrop.RESULT_ERROR){
            final Throwable error = UCrop.getError(data);
            Toast.makeText(this, "not done", Toast.LENGTH_SHORT).show();
        }
    }
}