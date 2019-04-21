package com.mmjang.ankillusion.ui;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mmjang.ankillusion.R;
import com.mmjang.ankillusion.data.Settings;

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
    Button btnDelete;
    private DoodleOnTouchGestureListener touchGestureListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        settings = Settings.getInstance(this);
        setUpDoodleView();
        //set button
        btnDelete = findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(doodleView != null){
                            IDoodleSelectableItem item = touchGestureListener.getSelectedItem();
                            if(item != null) {
                                doodleView.removeItem(item);
                            }
                        }
                    }
                }
        );
    }

    private void setUpDoodleView() {
        imageContainer = findViewById(R.id.image_container);
        Bitmap bitmap = ImageUtils.createBitmapFromPath(Environment.getExternalStorageDirectory() + "/Download/heart.jpg", this);
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
        imageContainer.addView(doodleView);
    }
}
