package com.example.harmonialauncher.lockManager;

import java.util.HashMap;

public interface Lockable {
    public void lock();
    public void unlock();
    public boolean isLocked();
}
