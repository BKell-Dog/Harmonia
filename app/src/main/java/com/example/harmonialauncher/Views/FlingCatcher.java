package com.example.harmonialauncher.Views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.harmonialauncher.Helpers.FlingDetector;
import com.example.harmonialauncher.Interfaces.PageHolder;

public class FlingCatcher extends ConstraintLayout {
    private static final String TAG = FlingCatcher.class.getSimpleName();
    private Context context;
    private FlingDetector fd;
    private PageHolder callback;
    private float firstX = -1, firstY = -1;

    public FlingCatcher(Context context) {
        super(context);
        this.context = context;
        fd = new FlingDetector(context);
    }

    public FlingCatcher(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        fd = new FlingDetector(context);
        Log.d(TAG, "FlingCatcher: " + context);
    }

    public FlingCatcher(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        fd = new FlingDetector(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FlingCatcher(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        fd = new FlingDetector(context);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        boolean fling = fd.onTouch(null, event);
        if (fling) {
            if (callback != null) {
                if (fd.getMode() == FlingDetector.VERTICAL) {
                    if (firstY > event.getY())
                        callback.incrementPage(); //Upward fling, return to HomeScreen
                    else
                        callback.decrementPage(); //Downward fling, go to DrawerScreen
                    Log.d(TAG, "dispatchTouchEvent: INNER VERT " + firstY + " --- " + event.getY());
                }
                else if (fd.getMode() == FlingDetector.HORIZONTAL)
                {
                    if (firstX < event.getX())
                        callback.decrementPage();
                    else
                        callback.incrementPage();
                    Log.d(TAG, "dispatchTouchEvent: INNER HORIZ");
                }
            } else
                Log.e(TAG, "Callback Runnable was not instantiated.");
            reset();
        } else {
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                firstX = event.getX();
                firstY = event.getY();
            }
        }
        return super.dispatchTouchEvent(event);
    }

    public void setCallback(PageHolder ma) {
        callback = ma;
    }

    public void setMode(int mode)
    {fd.setMode(mode);}

    public int getMode()
    {return fd.getMode();}

    private void reset() {
        firstX = -1f;
        firstY = -1f;
    }
}
