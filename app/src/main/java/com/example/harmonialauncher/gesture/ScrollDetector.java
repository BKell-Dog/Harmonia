package com.example.harmonialauncher.gesture;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * A scroll is differentiated from a tap, a drag, a swipe, and a fling by the following:
 * Tap: has on-screen target, does not move beyond touch slop
 * Drag: has on-screen target, moves beyond slop, slow motion, controlled and fine movement
 * Fling: no on-screen target, moves beyond slop, fast motion, uncontrolled, possibly ends off screen
 * Swipe: no on-screen target, moves beyond slop, slow or fast motion, controlled
 * Scroll: no on-screen target, moves beyond slop, slow motion, controlled
 */
public class ScrollDetector implements View.OnTouchListener {
    private static final String TAG = ScrollDetector.class.getSimpleName();
    private static final float CUSTOM_SLOP_MULTIPLIER = 2.2f;
    private static final int SEC_IN_MILLIS = 1000;
    private float mInitX, mInitY;
    private final float mSingleTapTimeout;
    private float mInitTime;
    private final double mCustomTouchSlop;

    public ScrollDetector(Context context) {
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
            case MotionEvent.ACTION_MOVE:
                //Check that touch event has moved outside the touch slop or singleTapTimeout has run out
                if (Math.abs(event.getY() - mInitY) > mCustomTouchSlop ||
                        Math.abs(event.getX() - mInitX) > mCustomTouchSlop ||
                        System.currentTimeMillis() > mInitTime + mSingleTapTimeout)
                {
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                mInitX = 0;
                mInitY = 0;
                mInitTime = 0;
        }
        return false;
    }
}
