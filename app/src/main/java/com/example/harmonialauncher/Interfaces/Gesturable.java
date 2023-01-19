package com.example.harmonialauncher.Interfaces;

import android.view.MotionEvent;

/**
 * All classes implementing this interface must have a variable called onScreen which is true if the
 * View has been STARTED or RESUMED, and false if it had been STOPPED, PAUSED, or DESTROYED.
 */
public interface Gesturable {

    public boolean onDown(MotionEvent event);

    public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY);

    public void onLongPress(MotionEvent event);

    public boolean onSingleTapConfirmed(MotionEvent event);

    public boolean isOnScreen();
}
