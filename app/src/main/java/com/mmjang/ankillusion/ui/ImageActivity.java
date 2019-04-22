package com.mmjang.ankillusion.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mmjang.ankillusion.R;
import com.mmjang.ankillusion.data.Constant;
import com.mmjang.ankillusion.data.Settings;
import com.theartofdev.edmodo.cropper.CropImageView;

import cn.forward.androids.utils.ImageUtils;
import cn.hzw.doodle.DoodleColor;
import cn.hzw.doodle.DoodleOnTouchGestureListener;
import cn.hzw.doodle.DoodlePen;
import cn.hzw.doodle.DoodleShape;
import cn.hzw.doodle.DoodleTouchDetector;
import cn.hzw.doodle.DoodleView;
import cn.hzw.doodle.IDoodleListener;
import cn.hzw.doodle.MyDoodleOnTouchGestureListener;
import cn.hzw.doodle.core.IDoodle;
import cn.hzw.doodle.core.IDoodleSelectableItem;

public class ImageActivity extends AppCompatActivity {
    Settings settings;
    DoodleView doodleView;
    LinearLayout imageContainer;
    CropImageView cropImageView;
    LinearLayout imageCropButtons;
    ImageButton btnRotate;
    ImageButton btnCrop;
    ImageButton btnClose;
    private DoodleOnTouchGestureListener touchGestureListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        settings = Settings.getInstance(this);
        assginViews();
        hideDoodleView(true);
        //Bitmap bitmap = ImageUtils.createBitmapFromPath(Environment.getExternalStorageDirectory() + "/Download/heart.jpg", this);
        //cropImageView.setImageBitmap(bitmap);
        handleIntent();
        setUpListener();
    }

    private void handleIntent() {
        Intent intent = getIntent();
        if(intent != null && intent.getAction().equals(Intent.ACTION_SEND) && intent.getType().startsWith("image/")){
            try {
                Uri uri = (Uri) intent.getExtras().get(Intent.EXTRA_STREAM);
                cropImageView.setImageUriAsync(uri);
            }
            catch(Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setUpListener() {
        btnRotate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(cropImageView != null){
                            cropImageView.rotateImage(90);
                        }
                    }
                }
        );

        btnCrop.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(cropImageView != null){
                            //if(cropImageView.)
                            Rect rect = cropImageView.getCropRect();
                            double w = 0, h = 0;
                            if(rect.width() > Constant.MAX_IMAGE_WIDTH){
                                w = Constant.MAX_IMAGE_WIDTH;
                                h = w * ((double) rect.height()/ (double) rect.width());
                            }else{
                                w = rect.width();
                                h = rect.height();
                            }

                            cropImageView.getCroppedImageAsync((int) w, (int) h);
                        }
                    }
                }
        );

        cropImageView.setOnSetImageUriCompleteListener(
                new CropImageView.OnSetImageUriCompleteListener() {
                    @Override
                    public void onSetImageUriComplete(CropImageView view, Uri uri, Exception error) {
                        //by default, get the whole image;
                        Rect rect = cropImageView.getWholeImageRect();
                        //cropImageView.getCropWindowRect()
                        Toast.makeText(ImageActivity.this,
                                "width: " + rect.width() + "height: " + rect.height()
                                , Toast.LENGTH_SHORT).show();
                        //cropImageView.setCropRect(rect);
                    }
                }
        );

        cropImageView.setOnCropImageCompleteListener(
                new CropImageView.OnCropImageCompleteListener() {
                    @Override
                    public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {
                        setUpDoodleView(result.getBitmap());
                    }
                }
        );
    }

    private void assginViews() {
        //btnDelete = findViewById(R.id.btn_close);
        imageContainer = findViewById(R.id.image_container);
        cropImageView = findViewById(R.id.crop_image_view);
        imageCropButtons = findViewById(R.id.image_crop_buttons);
        btnRotate = findViewById(R.id.btn_rotate);
        btnCrop = findViewById(R.id.btn_crop);
        btnClose = findViewById(R.id.btn_close);
    }

    private void setUpDoodleView(Bitmap bitmap) {
        Toast.makeText(this, "width: " + bitmap.getWidth() + "height: " + bitmap.getHeight(), Toast.LENGTH_SHORT).show();

        doodleView = new DoodleView(this, bitmap, new IDoodleListener() {
            @Override
            public void onSaved(IDoodle doodle, Bitmap doodleBitmap, Runnable callback) {
                Toast.makeText(ImageActivity.this, "onSaved", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onReady(IDoodle doodle) {
                doodle.setSize(30 * doodle.getUnitSize());
            }
        });

        touchGestureListener = new MyDoodleOnTouchGestureListener(doodleView, null);
        DoodleTouchDetector touchDetector = new DoodleTouchDetector(this, touchGestureListener);
        doodleView.setDefaultTouchDetector(touchDetector);
        doodleView.setPen(DoodlePen.BRUSH);
        doodleView.setShape(DoodleShape.FILL_RECT);
        doodleView.setColor(new DoodleColor(Color.parseColor(settings.getOcclusionColor())));

        hideCropView();
        imageContainer.setVisibility(View.VISIBLE);
        imageContainer.addView(doodleView);
    }

    private void showDoodleView(){

    }

    private void clearDoodleView(){
        if(doodleView != null){
            doodleView.clear();
        }
    }

    private void hideCropView(){
        cropImageView.clearImage();
        cropImageView.setVisibility(View.GONE);
        imageCropButtons.setVisibility(View.GONE);
    }

    private void hideDoodleView(boolean clear){
        clearDoodleView();
        if(imageContainer != null){
            imageContainer.setVisibility(View.GONE);
        }
    }
}
