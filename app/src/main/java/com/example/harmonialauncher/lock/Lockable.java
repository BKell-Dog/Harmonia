package com.example.harmonialauncher.lock;

public interface Lockable {
    void lock();

    void unlock();

    boolean isLocked();
}
