package com.example.harmonialauncher.lockManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.harmonialauncher.lockManager.Lockable;

public class HarmoniaActivity extends AppCompatActivity implements Lockable {

    protected boolean locked = false;

    public void lock() {
        locked = true;
    }

    public void lock(long millisLocked){locked = true;}

    public void unlock() {
        locked = false;
    }

    public boolean isLocked() {
        return locked;
    }

    public long getTimeRemaining() {return 0;}
    public long getEndTime() {return 0;}
}
