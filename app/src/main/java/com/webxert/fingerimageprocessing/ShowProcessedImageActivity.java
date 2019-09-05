package com.webxert.fingerimageprocessing;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class ShowProcessedImageActivity extends AppCompatActivity {

    String filePath;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_processed_image);
        imageView = findViewById(R.id.imageView);
        filePath = getIntent().getStringExtra("file_path");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Uri uri = Uri.fromFile(new File(filePath));
                Bitmap bitmap;
                try {

                    //bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    //Bitmap grayScale = BitmapFactory.getGrayScaleBitmap(bitmap);
                    //imageView.setImageBitmap(BitmapFactory.drawBitmap(bitmap, grayScale));
                } catch (Exception e) {
                    Log.e("bitmapException", e.getMessage());
                }

            }
        }, 0);
        findViewById(R.id.save).setOnClickListener(v ->
        {
            //Toast.makeText(this, "TODO Later", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        findViewById(R.id.cancel).setOnClickListener(v ->
        {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }
}
