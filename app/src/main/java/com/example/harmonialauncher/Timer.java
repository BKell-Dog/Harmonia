package com.example.harmonialauncher;

import android.util.Log;

public class Timer extends Thread {

    private static final String TAG = "Timer";
    private Long startTime, endTime;

    public void run() {
        if (startTime != null && endTime != null) {
            while (System.nanoTime() < endTime) {
            }
            startTime = null;
            endTime = null;
        }
    }

    public void setTimer(int seconds) {
        startTime = System.nanoTime();
        endTime = System.nanoTime() + (seconds * 1000000000);
        Log.d(TAG, "StartTime: " + startTime + "\nEndTime: " + endTime);
    }

    public boolean addTime(int seconds) {
        if (endTime != null) {
            endTime += (seconds * 1000000000);
            return true;
        } else
            return false;
    }

    public boolean isDone() {
        return (startTime == null && endTime == null);
    }

    public long getElapsedTime() {
        if (startTime != null)
            return ((System.nanoTime() - startTime) / 1000000000);
        else
            return 0;
    }

    public long getRemainingTime() {
        if (endTime != null)
            return ((endTime - System.nanoTime()) / 1000000000);
        else
            return 0;
    }
}
