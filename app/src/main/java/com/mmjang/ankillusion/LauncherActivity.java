package com.mmjang.ankillusion;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.InputStream;

import cn.hzw.doodle.DoodleActivity;
import cn.hzw.doodle.DoodleParams;

public class LauncherActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        initStoragePermission();
    }

    private void initStoragePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            int result = ContextCompat.checkSelfPermission(LauncherActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (result != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(LauncherActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                } else {
                    ActivityCompat.requestPermissions(LauncherActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE);
                }
            } else {
                openImage();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_STORAGE) {
            if (requestCode == REQUEST_CODE_STORAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImage();
            } else {
            }
        }
    }

    private void openImage() {
        DoodleParams params = new DoodleParams(); // 涂鸦参数
        params.mImagePath = Environment.getExternalStorageDirectory() + "/Download/heart.jpg";
        DoodleActivity.startActivityForResult(LauncherActivity.this, params, 0);
    }
}
