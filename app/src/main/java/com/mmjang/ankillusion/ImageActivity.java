package com.mmjang.ankillusion;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

import cn.forward.androids.utils.ImageUtils;
import cn.hzw.doodle.DoodleBitmap;
import cn.hzw.doodle.DoodleColor;
import cn.hzw.doodle.DoodleOnTouchGestureListener;
import cn.hzw.doodle.DoodlePen;
import cn.hzw.doodle.DoodleShape;
import cn.hzw.doodle.DoodleTouchDetector;
import cn.hzw.doodle.DoodleView;
import cn.hzw.doodle.IDoodleListener;
import cn.hzw.doodle.core.IDoodle;
import cn.hzw.doodle.core.IDoodleItem;
import cn.hzw.doodle.core.IDoodleSelectableItem;

public class ImageActivity extends AppCompatActivity {
    DoodleView doodleView;
    LinearLayout imageContainer;
    Button btnDelete;
    private DoodleOnTouchGestureListener touchGestureListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        final DoodleOnTouchGestureListener touchGestureListener = new MyItemOnTouchGestureListener(doodleView, null);
        DoodleTouchDetector touchDetector = new DoodleTouchDetector(this, touchGestureListener);
        doodleView.setDefaultTouchDetector(touchDetector);
        doodleView.setPen(DoodlePen.BRUSH);
        doodleView.setShape(DoodleShape.FILL_RECT);
        doodleView.setColor(new DoodleColor(Color.RED));
        imageContainer.addView(doodleView);

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


    private static class MyItemOnTouchGestureListener extends DoodleOnTouchGestureListener {

        public MyItemOnTouchGestureListener(DoodleView doodle, ISelectionListener listener) {
            super(doodle, listener);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            mLastTouchX = mTouchX;
            mLastTouchY = mTouchY;
            mTouchX = e.getX();
            mTouchY = e.getY();

            boolean found = false;
            IDoodleSelectableItem item;
            List<IDoodleItem> items = mDoodle.getAllItem();
            for (int i = items.size() - 1; i >= 0; i--) {
                IDoodleItem elem = items.get(i);
                if (!elem.isDoodleEditable()) {
                    continue;
                }

                if (elem instanceof DoodleBitmap){
                    continue;
                }

                if (!(elem instanceof IDoodleSelectableItem)) {
                    continue;
                }

                item = (IDoodleSelectableItem) elem;

                if (item.contains(mDoodle.toX(mTouchX), mDoodle.toY(mTouchY))) {
                    found = true;
                    setSelectedItem(item);
                    mDoodle.setEditMode(true);
                    PointF xy = item.getLocation();
                    mStartX = xy.x;
                    mStartY = xy.y;
                    break;
                }
            }
            if (!found) { // not found
                if (mSelectedItem != null) { // 取消选定
                    IDoodleSelectableItem old = mSelectedItem;
                    setSelectedItem(null);
                    mDoodle.setEditMode(false);
                    if (mSelectionListener != null) {
                        mSelectionListener.onSelectedItem(mDoodle, old, false);
                    }
                }
            }
            return true;
        }
    }
}
