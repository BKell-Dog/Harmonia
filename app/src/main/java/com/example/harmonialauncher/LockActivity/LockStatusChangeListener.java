package com.example.harmonialauncher.LockActivity;

import java.util.ArrayList;

public class LockStatusChangeListener {

    private static ArrayList<LockStatusListener> listeners = new ArrayList<LockStatusListener>();

    public static void add(LockStatusListener l)
    {listeners.add(l);}

    public static void remove(LockStatusListener l)
    {listeners.remove(l);}

    public static void onStatusChanged()
    {
        for (LockStatusListener l : listeners)
            l.onStatusChanged();
    }

    public interface LockStatusListener
    {
        public void onStatusChanged();
    }
}
