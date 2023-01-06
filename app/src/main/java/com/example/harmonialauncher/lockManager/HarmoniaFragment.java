package com.example.harmonialauncher.lockManager;

import android.view.MotionEvent;

import androidx.fragment.app.Fragment;

import com.example.harmonialauncher.GestureDetection.Gesturable;
import com.example.harmonialauncher.lockManager.Lockable;

import java.util.HashMap;

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

    public void onStart()
    {
        onScreen = true;
        super.onStart();
    }
    public void onResume()
    {
        onScreen = true;
        super.onResume();
    }

    public void onDestroy()
    {
        onScreen = false;
        super.onDestroy();
    }
    public void onPause()
    {
        onScreen = false;
        super.onPause();
    }
    public void onStop()
    {
        onScreen = false;
        super.onStop();
    }
    public boolean isOnScreen()
    {return onScreen;}
}
