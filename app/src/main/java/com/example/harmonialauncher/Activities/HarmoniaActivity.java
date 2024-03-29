package com.example.harmonialauncher.Activities;

import android.view.MotionEvent;

import androidx.appcompat.app.AppCompatActivity;

import com.example.harmonialauncher.Interfaces.Gesturable;
import com.example.harmonialauncher.Interfaces.Lockable;

public abstract class HarmoniaActivity extends AppCompatActivity implements Lockable, Gesturable {

    protected boolean locked = false;
    protected boolean onScreen = false;

    public void lock() {
        locked = true;
    }

    public void lock(long millisLocked) {
        locked = true;
    }

    public void unlock() {
        locked = false;
    }

    public boolean isLocked() {
        return locked;
    }

    public long getTimeRemaining() {
        return 0;
    }

    public long getEndTime() {
        return 0;
    }

    //---------------------------------<Methods for Capturing Gestures>----------------------------
    public boolean onDown(MotionEvent event) {
        return false;
    }

    public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
        return false;
    }

    public void onLongPress(MotionEvent event) {
    }

    public boolean onSingleTapConfirmed(MotionEvent event) {
        return false;
    }

    public void onStart() {
        onScreen = true;
        super.onStart();
    }

    public void onResume() {
        onScreen = true;
        super.onResume();
    }

    public void onDestroy() {
        onScreen = false;
        super.onDestroy();
    }

    public void onPause() {
        onScreen = false;
        super.onPause();
    }

    public void onStop() {
        onScreen = false;
        super.onStop();
    }

    public boolean isOnScreen() {
        return onScreen;
    }
}
