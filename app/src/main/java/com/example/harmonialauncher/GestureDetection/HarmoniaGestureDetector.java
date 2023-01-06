package com.example.harmonialauncher.GestureDetection;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import java.util.ArrayList;

public class HarmoniaGestureDetector extends GestureDetector.SimpleOnGestureListener {
    private static final String TAG = "Harmonia Gesture Detector";
    private static ArrayList<Gesturable> classes = new ArrayList<>();

    @Override
    public boolean onDown(MotionEvent event) {
        for (Gesturable g : classes)
            if (g.isOnScreen())
                g.onDown(event);
        return true;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
        for (Gesturable g : classes)
            if (g.isOnScreen())
                g.onFling(event1, event2, velocityX, velocityY);
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        for (Gesturable g : classes)
            if (g.isOnScreen())
                g.onSingleTapConfirmed(event);
        return true;
    }

    public static void add(Gesturable g)
    {
        if (!classes.contains(g))
            classes.add(g);
    }

    public static void remove(Gesturable g)
    {
        if (classes.contains(g))
            classes.remove(g);
    }

    public static boolean contains(Gesturable g)
    {return classes.contains(g);}
}

