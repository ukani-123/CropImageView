package com.example.cropimageview.Activity;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.cropimageview.R;

public class ImageSelectionActivity extends AppCompatActivity {

    Button btnChooseImage;
    ActivityResultLauncher<String> getImage;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_selection);

        btnChooseImage = findViewById(R.id.btnChooseImage);

        getImage = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                Intent intent = new Intent(ImageSelectionActivity.this,CropImageActivity.class);
                intent.putExtra("DATA",result.toString());
                startActivityForResult(intent,100);
            }
        });

        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    getImage.launch("image/*");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK&&requestCode==100){
            startActivity(new Intent(ImageSelectionActivity.this, MainActivity.class).putExtra("data",data.getStringExtra("RESULT")));
        }
    }
}