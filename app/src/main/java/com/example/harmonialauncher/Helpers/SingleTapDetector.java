package com.example.harmonialauncher.Helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

public class SingleTapDetector implements View.OnTouchListener {
    private static final String TAG = SingleTapDetector.class.getSimpleName();
    private static final float CUSTOM_SLOP_MULTIPLIER = 2.2f;
    private static final int SEC_IN_MILLIS = 1000;
    private float mInitX, mInitY, mFinalX, mFinalY;
    private float mSingleTapTimeout, mInitTime, mFinalTime;
    private double mCustomTouchSlop;

    public SingleTapDetector(Context context) {
        ViewConfiguration vc = ViewConfiguration.get(context);
        mCustomTouchSlop = CUSTOM_SLOP_MULTIPLIER * vc.getScaledTouchSlop();
        mSingleTapTimeout = ViewConfiguration.getTapTimeout();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mInitX = event.getX();
                mInitY = event.getY();
                mInitTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_UP:
                mFinalX = event.getX();
                mFinalY = event.getY();
                mFinalTime = System.currentTimeMillis();
                if (Math.abs(mFinalX - mInitX) < mCustomTouchSlop &&
                        Math.abs(mFinalY - mInitY) < mCustomTouchSlop &&
                        mFinalTime - mInitTime < mSingleTapTimeout) {
                    return true;
                }
                break;
             case MotionEvent.ACTION_CANCEL:
        }
        return false;
    }
}
