package com.mmjang.ankillusion.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mmjang.ankillusion.R;
import com.mmjang.ankillusion.anki.AnkiDroidHelper;
import com.mmjang.ankillusion.data.Constant;
import com.mmjang.ankillusion.utils.Utils;

import cn.hzw.doodle.DoodleActivity;
import cn.hzw.doodle.DoodleParams;

public class LauncherActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ALL = 1;
    private AnkiDroidHelper mAnkidroid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        ensurePermissions();
    }

    private void ensurePermissions() {
        if(!Utils.hasPermissions(this, Constant.PERMISSION_FOR_EXPORT)){
            ActivityCompat.requestPermissions(this, Constant.PERMISSION_FOR_EXPORT, REQUEST_CODE_ALL);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(grantResults.length == 0){
            return ;
        }

        String message = "";

        if(requestCode == REQUEST_CODE_ALL){
            for(int i = 0; i < grantResults.length; i ++){
                String perm = permissions[i];
                int res = grantResults[i];
                if(perm.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE) && res == PackageManager.PERMISSION_DENIED){
                    message += getResources().getString(R.string.msg_no_storage_permission);
                }
                if(perm.equals(com.ichi2.anki.api.AddContentApi.READ_WRITE_PERMISSION) && res == PackageManager.PERMISSION_DENIED){
                    message += getResources().getString(R.string.msg_no_anki_api);
                }
            }

            if(!message.isEmpty()){
                Utils.showMessage(this, message);
            }
        }
    }
}
