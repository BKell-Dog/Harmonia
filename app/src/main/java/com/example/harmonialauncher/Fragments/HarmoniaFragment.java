package com.example.harmonialauncher.Fragments;

import android.view.MotionEvent;

import androidx.fragment.app.Fragment;

import com.example.harmonialauncher.gesture.Gesturable;
import com.example.harmonialauncher.lock.Lockable;

public abstract class HarmoniaFragment extends Fragment implements Lockable, Gesturable {

    protected boolean locked = false;
    protected boolean onScreen = false;

    public HarmoniaFragment(int resource) {
        super(resource);
    }

    public void lock() {
        locked = true;
    }

    public void unlock() {
        locked = false;
    }

    public boolean isLocked() {
        return locked;
    }

    //---------------------------------<Methods for Capturing Gestures>----------------------------
    public boolean onDown(MotionEvent e) {
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

    public void setOnScreen() {
        onScreen = true;
    }

    public void setOffScreen() {
        onScreen = false;
    }

    public boolean isOnScreen() {
        return onScreen;
    }
}
