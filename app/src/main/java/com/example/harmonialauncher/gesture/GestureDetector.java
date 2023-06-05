package com.example.harmonialauncher.gesture;

import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

public abstract class GestureDetector implements View.OnTouchListener{

    /**
     * Variables tracking motion across time
     */
    protected static final int SEC_IN_MILLIS = 1000;
    protected long mInitTime, mCurrentTime, mFinalTime;

    /**
     * Variables tracking motion across space
     */
    protected float mInitX, mInitY, mCurrentX, mCurrentY, mFinalX, mFinalY;
    protected boolean outOfSlop = false;
    protected static final float CUSTOM_SLOP_MULTIPLIER = 2.2f;
    protected VelocityTracker mVelocityTracker;


    @Override
    public abstract boolean onTouch(View view, MotionEvent event);

    protected void cleanUp() {
        if (mVelocityTracker == null) {
            return;
        }
        mVelocityTracker.recycle();
        mVelocityTracker = null;

        mInitTime = 0;
        mCurrentTime = 0;
        mFinalTime = 0;
        mInitX = 0;
        mInitY = 0;
        mCurrentX = 0;
        mCurrentY = 0;
        mFinalX = 0;
        mFinalY = 0;
        outOfSlop = false;
    }
}
