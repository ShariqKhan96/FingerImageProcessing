package com.webxert.fingerimageprocessing;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;

public class ThumbResultActivity extends AppCompatActivity {

    String filePath = null;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thumb_result);
        imageView = findViewById(R.id.imageView);
        findViewById(R.id.process).setOnClickListener(v ->
        {
            Toast.makeText(this, "TODO LATER", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(this, ShowProcessedImageActivity.class);
//            intent.putExtra("file_path", filePath);
//            startActivity(intent);
//            finish();
        });
        filePath = getIntent().getStringExtra("file_path");
        if (filePath != null) {
            //Picasso.get().load(new File(filePath).getAbsolutePath()).into(imageView);
            Log.e("filepath", filePath);
            File file = new File(filePath);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Uri uri = Uri.fromFile(file);
                    imageView.setImageURI(uri);
                }
            }, 0);


        }

    }
}
