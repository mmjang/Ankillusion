package cn.hzw.doodle;

import android.animation.ValueAnimator;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.view.MotionEvent;

import java.util.List;

import cn.forward.androids.ScaleGestureDetectorApi27;
import cn.forward.androids.TouchGestureDetector;
import cn.hzw.doodle.core.IDoodle;
import cn.hzw.doodle.core.IDoodleItem;
import cn.hzw.doodle.core.IDoodlePen;
import cn.hzw.doodle.core.IDoodleSelectableItem;

import static cn.hzw.doodle.util.DrawUtil.computeAngle;

/**
 * DoodleView的涂鸦手势监听
 * Created on 30/06/2018.
 */

public class MyDoodleOnTouchGestureListener extends DoodleOnTouchGestureListener {
    public MyDoodleOnTouchGestureListener(DoodleView doodle, ISelectionListener listener) {
        super(doodle, listener);
    }

    public void setSelectedItem(IDoodleSelectableItem selectedItem) {
        IDoodleSelectableItem old = mSelectedItem;
        mSelectedItem = selectedItem;

        if (old != null) { // 取消选定
            old.setSelected(false);
            if (mSelectionListener != null) {
                mSelectionListener.onSelectedItem(mDoodle, old, false);
            }
            mDoodle.notifyItemFinishedDrawing(old);
        }
        if (mSelectedItem != null) {
            mSelectedItem.setSelected(true);
            if (mSelectionListener != null) {
                mSelectionListener.onSelectedItem(mDoodle, mSelectedItem, true);
            }
            mDoodle.markItemToOptimizeDrawing(mSelectedItem);
        }

    }

    @Override
    public void onScrollBegin(MotionEvent event) {
        mLastTouchX = mTouchX = event.getX();
        mLastTouchY = mTouchY = event.getY();
        mDoodle.setScrollingDoodle(true);

        boolean found = false;
        IDoodleSelectableItem item;
        List<IDoodleItem> items = mDoodle.getAllItem();

        for (int i = items.size() - 1; i >= 0; i--) {
            IDoodleItem elem = items.get(i);
            if (!elem.isDoodleEditable()) {
                continue;
            }

            if (elem instanceof DoodleBitmap) {
                continue;
            }

            if (!(elem instanceof IDoodleSelectableItem)) {
                continue;
            }

            item = (IDoodleSelectableItem) elem;

            if (item.contains(mDoodle.toX(mTouchX), mDoodle.toY(mTouchY))) {
                setSelectedItem(item);
                mDoodle.setEditMode(true);
                found = true;
                break;
            }
        }

        //滑动起始位置不在矩形范围内，且不在旋转控制杆范围内，取消选择，新建矩形
        if (!found && !(mSelectedItem instanceof DoodleRotatableItemBase
                && (((DoodleRotatableItemBase) mSelectedItem).canRotate(mDoodle.toX(mTouchX), mDoodle.toY(mTouchY))))) {
                if (mSelectedItem != null) { // 取消选定
                    IDoodleSelectableItem old = mSelectedItem;
                    setSelectedItem(null);
                    mDoodle.setEditMode(false);
                    if (mSelectionListener != null) {
                        mSelectionListener.onSelectedItem(mDoodle, old, false);
                    }
                }
        }

        if (mDoodle.isEditMode() || isPenEditable(mDoodle.getPen())) {
            if (mSelectedItem != null) {
                PointF xy = mSelectedItem.getLocation();
                mStartX = xy.x;
                mStartY = xy.y;
                if (mSelectedItem instanceof DoodleRotatableItemBase
                        && (((DoodleRotatableItemBase) mSelectedItem).canRotate(mDoodle.toX(mTouchX), mDoodle.toY(mTouchY)))) {
                    ((DoodleRotatableItemBase) mSelectedItem).setIsRotating(true);
                    mRotateDiff = mSelectedItem.getItemRotate() -
                            computeAngle(mSelectedItem.getPivotX(), mSelectedItem.getPivotY(), mDoodle.toX(mTouchX), mDoodle.toY(mTouchY));
                }
            } else {
                if (mDoodle.isEditMode()) {
                    mStartX = mDoodle.getDoodleTranslationX();
                    mStartY = mDoodle.getDoodleTranslationY();
                }
            }
        } else {
            // 点击copy
            if (mDoodle.getPen() == DoodlePen.COPY && mCopyLocation.contains(mDoodle.toX(mTouchX), mDoodle.toY(mTouchY), mDoodle.getSize())) {
                mCopyLocation.setRelocating(true);
                mCopyLocation.setCopying(false);
            } else {
                if (mDoodle.getPen() == DoodlePen.COPY) {
                    mCopyLocation.setRelocating(false);
                    if (!mCopyLocation.isCopying()) {
                        mCopyLocation.setCopying(true);
                        mCopyLocation.setStartPosition(mDoodle.toX(mTouchX), mDoodle.toY(mTouchY));
                    }
                }

                // 初始化绘制
                mCurrPath = new Path();
                mCurrPath.moveTo(mDoodle.toX(mTouchX), mDoodle.toY(mTouchY));
                if (mDoodle.getShape() == DoodleShape.HAND_WRITE) { // 手写
                    mCurrDoodlePath = DoodlePath.toPath(mDoodle, mCurrPath);
                } else {  // 画图形
                    mCurrDoodlePath = DoodlePath.toShape(mDoodle,
                            mDoodle.toX(mTouchDownX), mDoodle.toY(mTouchDownY), mDoodle.toX(mTouchX), mDoodle.toY(mTouchY));
                }
                if (mDoodle.isOptimizeDrawing()) {
                    mDoodle.markItemToOptimizeDrawing(mCurrDoodlePath);
                } else {
                    mDoodle.addItem(mCurrDoodlePath);
                }
            }
        }
        mDoodle.refresh();
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
        mDoodle.refresh();
        return true;
    }
}
