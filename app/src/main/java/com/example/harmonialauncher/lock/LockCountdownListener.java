package com.example.harmonialauncher.lock;

//TODO: Implement a scheme to allow the app countdown timers to update in real time as time progresses.
public interface LockCountdownListener {

    void onTickMillis();

    void onTickSeconds();
}
