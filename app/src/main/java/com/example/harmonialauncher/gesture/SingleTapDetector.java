package com.example.harmonialauncher.gesture;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

public class SingleTapDetector extends GestureDetector {
    private static final String TAG = SingleTapDetector.class.getSimpleName();

    private float mSingleTapTimeout;
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
                cleanUp();
                break;
             case MotionEvent.ACTION_CANCEL:
                 cleanUp();
        }
        return false;
    }
}
