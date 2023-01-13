package com.example.harmonialauncher.lockManager;

import android.view.MotionEvent;

import androidx.fragment.app.Fragment;

import com.example.harmonialauncher.GestureDetection.Gesturable;

public class HarmoniaFragment extends Fragment implements Lockable, Gesturable {

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
    public boolean onDown(MotionEvent e) {return false;}

    public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {return false;}

    public boolean onLongPress(MotionEvent event) {return false;}

    public boolean onSingleTapConfirmed(MotionEvent event) {return false;}

    public void setOnScreen()
    {onScreen = true;}

    public void setOffScreen()
    {onScreen = false;}

    public boolean isOnScreen()
    {return onScreen;}
}
