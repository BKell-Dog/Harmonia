package com.example.harmonialauncher.LockManager;

import android.content.Intent;

import com.example.harmonialauncher.lock.Lockable;

public abstract class HarmoniaIntent extends Intent implements Lockable {

    protected boolean locked = false;

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
}
