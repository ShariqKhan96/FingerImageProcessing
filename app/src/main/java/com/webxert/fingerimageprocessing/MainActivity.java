package com.webxert.fingerimageprocessing;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Uri mCaptureImage;
    String mediaPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.thumb).setOnClickListener(v -> requestPermission());
        findViewById(R.id.gallery).setOnClickListener(v -> openGallery());

    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, Constants.FILE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.FILE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    Uri uri = data.getData();
                    File file = null;
                    try {
                        file = FileUtils.from(this, uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(this, ThumbResultActivity.class);
                    intent.putExtra("file_path", file.getAbsolutePath());
                    startActivity(intent);
                }
            } else {
                Toast.makeText(this, "Image not picked or something went wrong", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == Constants.REQUEST_CODE_CAM) {
            if (resultCode == RESULT_OK) {
                String[] projection = {MediaStore.Images.Media.DATA};
                Cursor cursor = managedQuery(mCaptureImage, projection, null,
                        null, null);
                int column_index_data = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();

                String capturedImageFilePath = cursor.getString(column_index_data);
                mediaPath = capturedImageFilePath;
                Intent intent = new Intent(this, ThumbResultActivity.class);
                intent.putExtra("file_path", mediaPath);
                startActivity(intent);
                Log.e("data", mediaPath);
            }
        }


    }

    public void requestPermission() {
        Dexter.withActivity(this).withPermissions(Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted or not
                        if (report.areAllPermissionsGranted()) {
                            try {
                                String fileName = System.currentTimeMillis() + ".jpg";
                                ContentValues values = new ContentValues();
                                values.put(MediaStore.Images.Media.TITLE, fileName);
                                mCaptureImage = getContentResolver()
                                        .insert(
                                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                                values);
                                Intent intent = new Intent(
                                        MediaStore.ACTION_IMAGE_CAPTURE);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                        mCaptureImage);
                                startActivityForResult(intent, Constants.REQUEST_CODE_CAM);
                            } catch (Exception e) {
                                Log.e("", "", e);
                            }

//                            if (mTextureView.isAvailable()) {
//                                openCamera(mTextureView.getWidth(), mTextureView.getHeight());
//                            } else {
//                                mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
//                            }
                        }
                        // check for permanent denial of any permission show alert dialog
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // open Settings activity
                            showSettingsDialog();
                        }


                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).withErrorListener(error -> Toast.makeText(MainActivity.this, "Error occurred! ", Toast.LENGTH_SHORT).show())
                .onSameThread()
                .check();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Need Permissions");
        builder.setMessage("Enable Camera And Storage permissions to use this app");
        builder.setPositiveButton("Go", (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.show();

    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }


}
