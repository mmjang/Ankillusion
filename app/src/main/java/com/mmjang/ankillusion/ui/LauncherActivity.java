package com.mmjang.ankillusion.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.mmjang.ankillusion.R;
import com.mmjang.ankillusion.anki.AnkiDroidHelper;
import com.mmjang.ankillusion.data.Constant;
import com.mmjang.ankillusion.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.hzw.doodle.DoodleActivity;
import cn.hzw.doodle.DoodleParams;

public class LauncherActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ALL = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private static final int REQUEST_IMAGE_GALLERY = 3;
    private AnkiDroidHelper mAnkidroid;
    ImageButton btnOpenCamera;
    ImageButton btnOpenGallery;
    ImageButton btnSettings;
    ImageButton btnHelp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        ensurePermissions();

        btnOpenCamera = findViewById(R.id.btn_open_camera);
        btnOpenGallery = findViewById(R.id.btn_open_gallery);
        btnSettings = findViewById(R.id.btn_settings);
        btnHelp = findViewById(R.id.btn_help);

        btnOpenCamera.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openCameraIntent();
                    }
                }
        );

        btnOpenGallery.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openGalleryIntent();
                    }
                }
        );

        btnSettings.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(LauncherActivity.this, SettingsActivity.class);
                        startActivity(intent);
                    }
                }
        );

        btnHelp.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = getString(R.string.documentation_url);
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                }
        );
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

    String imageFilePath;
    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalCacheDir();
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();
        return image;
    }

    private void openCameraIntent() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if(pictureIntent.resolveActivity(getPackageManager()) != null){
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(this, "error!", Toast.LENGTH_SHORT).show();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.mmjang.ankillusion.provider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        photoURI);
                startActivityForResult(pictureIntent,
                        REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            try {
                Uri uri = Uri.fromFile(new File(imageFilePath));
                Intent shareIntent = new Intent(this, ImageActivity.class);
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setType("image/jpeg");
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(shareIntent);
            }catch (Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }

        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK) {
            try {
                Uri uri = data.getData();
                Intent shareIntent = new Intent(this, ImageActivity.class);
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setType("image/jpeg");
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(shareIntent);
            }catch (Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void openGalleryIntent(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_IMAGE_GALLERY);
    }


}
