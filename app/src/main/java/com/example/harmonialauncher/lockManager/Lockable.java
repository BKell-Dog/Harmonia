package com.example.harmonialauncher.lockManager;

import java.util.HashMap;

public interface Lockable {
    void lock();

    void unlock();

    boolean isLocked();
}
