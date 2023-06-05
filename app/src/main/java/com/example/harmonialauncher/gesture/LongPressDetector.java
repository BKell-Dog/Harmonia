package com.example.harmonialauncher.gesture;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.example.harmonialauncher.Helpers.TimeHelper;

public class LongPressDetector extends GestureDetector {
    private static final String TAG = LongPressDetector.class.getSimpleName();

    private float mSingleTapTimeout;
    private double mCustomTouchSlop;

    public boolean isLongPressed = false;
    private LongPressCallback callback = null;
    private MotionEvent motionEvent = null;
    private final Handler handler = new Handler();
    private final Runnable mLongPressed = new Runnable() {
        @Override
        public void run() {
            isLongPressed = true;
            if (callback != null)
                callback.onLongPress(motionEvent);
        }
    };

    public LongPressDetector(Context context, LongPressCallback callback) {
        ViewConfiguration vc = ViewConfiguration.get(context);
        mCustomTouchSlop = CUSTOM_SLOP_MULTIPLIER * vc.getScaledTouchSlop();
        mSingleTapTimeout = ViewConfiguration.getLongPressTimeout();
        this.callback = callback;
        isLongPressed = false;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        motionEvent = event;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mInitX = event.getX();
                mInitY = event.getY();
                handler.postDelayed(mLongPressed, ViewConfiguration.getLongPressTimeout() * 2L);
                break;
            case MotionEvent.ACTION_MOVE:
                mCurrentX = event.getX();
                mCurrentY = event.getY();
                if (Math.abs(mCurrentX - mInitX) > mCustomTouchSlop ||
                        Math.abs(mCurrentY - mInitY) > mCustomTouchSlop) {
                    handler.removeCallbacks(mLongPressed);
                }
                break;
            default:
                cleanUp();
                break;
        }
        return isLongPressed;
    }

    @Override
    public void cleanUp()
    {
        super.cleanUp();
        isLongPressed = false;
        handler.removeCallbacks(mLongPressed);
    }

    public interface LongPressCallback
    {
        boolean onLongPress(MotionEvent event);
    }
}
