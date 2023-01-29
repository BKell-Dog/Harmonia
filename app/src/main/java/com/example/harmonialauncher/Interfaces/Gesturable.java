package com.example.harmonialauncher.Interfaces;

import android.view.MotionEvent;

/**
 * All classes implementing this interface must have a variable called onScreen which is true if the
 * View has been STARTED or RESUMED, and false if it had been STOPPED, PAUSED, or DESTROYED.
 */
public interface Gesturable {

    boolean onDown(MotionEvent event);

    boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY);

    void onLongPress(MotionEvent event);

    boolean onSingleTapConfirmed(MotionEvent event);

    boolean isOnScreen();
}
