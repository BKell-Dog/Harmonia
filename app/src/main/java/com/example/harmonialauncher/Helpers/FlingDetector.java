package com.example.harmonialauncher.Helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;

import androidx.annotation.Nullable;

public class FlingDetector implements View.OnTouchListener {
    private static final String TAG = "Vertical Fling Detector";
    private static final float CUSTOM_SLOP_MULTIPLIER = 2.2f;
    private static final int SEC_IN_MILLIS = 1000;
    public static final int HORIZONTAL = 0, VERTICAL = 1;

    private VelocityTracker mVelocityTracker;
    private float mMinimumFlingVelocity;
    private float mMaximumFlingVelocity;
    private float mDownX, mDownY;
    private int mode = VERTICAL;
    private boolean mShouldCheckVerticalFling, mShouldCheckHorizontalFling;
    private double mCustomTouchSlop;

    public FlingDetector(Context context) {
        ViewConfiguration vc = ViewConfiguration.get(context);
        mMinimumFlingVelocity = vc.getScaledMinimumFlingVelocity();
        mMaximumFlingVelocity = vc.getScaledMaximumFlingVelocity();
        mCustomTouchSlop = CUSTOM_SLOP_MULTIPLIER * vc.getScaledTouchSlop();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(@Nullable View view, MotionEvent ev) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getX();
                mDownY = ev.getY();
                mShouldCheckVerticalFling = false;
                mShouldCheckHorizontalFling = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mShouldCheckVerticalFling || mShouldCheckHorizontalFling) {
                    break;
                }
                if (mode == VERTICAL && Math.abs(ev.getY() - mDownY) > mCustomTouchSlop &&
                        Math.abs(ev.getY() - mDownY) > Math.abs(ev.getX() - mDownX)) {
                    mShouldCheckVerticalFling = true;
                }
                if (mode == HORIZONTAL && Math.abs(ev.getX() - mDownX) > mCustomTouchSlop &&
                        Math.abs(ev.getY() - mDownY) < Math.abs(ev.getX() - mDownX)) {
                    mShouldCheckHorizontalFling = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mode == VERTICAL && mShouldCheckVerticalFling) {
                    mVelocityTracker.computeCurrentVelocity(SEC_IN_MILLIS, mMaximumFlingVelocity);
                    if (Math.abs(mVelocityTracker.getYVelocity()) > mMinimumFlingVelocity) {
                        cleanUp();
                        return true;
                    }
                }
                if (mode == HORIZONTAL && mShouldCheckHorizontalFling) {
                    mVelocityTracker.computeCurrentVelocity(SEC_IN_MILLIS, mMaximumFlingVelocity);
                    if (Math.abs(mVelocityTracker.getXVelocity()) > mMinimumFlingVelocity) {
                        cleanUp();
                        return true;
                    }
                }
                cleanUp();
            case MotionEvent.ACTION_CANCEL:
                cleanUp();
        }
        return false;
    }

    public void setMode(int mode)
    {
        if (mode == HORIZONTAL || mode == VERTICAL)
            this.mode = mode;
    }

    public int getMode()
    {return mode;}

    private void cleanUp() {
        if (mVelocityTracker == null) {
            return;
        }
        mVelocityTracker.recycle();
        mVelocityTracker = null;
    }
}
