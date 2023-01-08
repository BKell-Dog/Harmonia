package com.example.harmonialauncher.lockManager;

import android.content.Intent;
import android.util.Log;

import com.example.harmonialauncher.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LockManager {

    private static final String TAG = "Lock Manager";

    //These hash maps hold the locked classes or package names as key, and as value hold the end time of the lock period.
    private static HashMap<Class<?>, Long> locked = new HashMap<Class<?>, Long>();
    private static HashMap<String, Long> lockedPacks = new HashMap<String, Long>();

    //Constants
    private static final long INDEFINITELY = -1;
    public static final String EXIT = "EXIT";

    public static boolean isLocked(Lockable obj) {
        update();

        //Check if object is internally locked. If so, add it to our locked map, return true.
        if (obj.isLocked())
            return true;

        //If object is in our hash map, then it has not been unlocked: return true.
        return inMap(obj);
    }

    public static boolean isLocked(String packageName) {
        update();

        return inMap(packageName) && lockedPacks.get(packageName) != 0;
    }

    public static boolean isLocked(Intent i) {
        update();

        if (i.getPackage() != null) {
            return inMap(i.getPackage()) && lockedPacks.get(i.getPackage()) != 0;
        } else if (i.getAction() != null) {
            return inMap(i.getAction()) && lockedPacks.get(i.getAction()) != 0;
        }
        return false;
    }

    public static void lock(Lockable obj) {
        obj.lock();
        if (!inMap(obj))
            locked.put(obj.getClass(), INDEFINITELY);
    }

    public static void lock(Lockable obj, int millisLocked) {
        lock(obj, (long) millisLocked);
    }

    public static void lock(Lockable obj, long millisLocked) {
        obj.lock();
        locked.put(obj.getClass(), System.currentTimeMillis() + millisLocked);
    }

    public static void lock(String packageName) {
        lock(packageName, -1);
    }

    public static void lock(String packageName, int millisLocked) {
        lock(packageName, (long) millisLocked);
    }

    public static void lock(String packageName, long millisLocked) {
        lockedPacks.put(packageName, System.currentTimeMillis() + millisLocked);
        Log.d(TAG, "LOCKED APP " + packageName);
    }

    public static void lock(Intent i) {
        if (i.getPackage() != null)
            lock(i.getPackage());
        else if (i.getAction() != null)
            lock(i.getAction());
    }

    public static void lock(Intent i, int millisLocked) {
        if (i.getPackage() != null)
            lock(i.getPackage(), millisLocked);
        else if (i.getAction() != null)
            lock(i.getAction(), millisLocked);
    }

    public static void lock(Intent i, long millisLocked) {
        if (i.getPackage() != null)
            lock(i.getPackage(), millisLocked);
        else if (i.getAction() != null)
            lock(i.getAction(), millisLocked);
    }

    public static void unlock(Lockable obj) {
        obj.unlock();
        locked.remove(obj.getClass());
    }

    public static void unlock(String packageName) {
        if (inMap(packageName))
            lockedPacks.remove(packageName);
    }

    public static void unlock(Intent i) {
        if (i.getPackage() != null)
            unlock(i.getPackage());
        else if (i.getAction() != null)
            unlock(i.getAction());
    }

    private static boolean inMap(Lockable obj) {
        return locked.containsKey(obj.getClass()) && locked.get(obj.getClass()).intValue() != 0;
    }

    private static boolean inMap(String packageName) {
        return lockedPacks.containsKey(packageName);
    }

    public static long getTimeRemaining(Lockable obj) {
        if (inMap(obj))
            return locked.get(obj.getClass()) - System.currentTimeMillis();
        else
            return 0;
    }

    public static long getEndTime(Lockable obj) {
        if (inMap(obj))
            return locked.get(obj.getClass());
        else
            return 0;
    }

    public static void add(Lockable obj, long endTime) {
        locked.put(obj.getClass(), endTime);
    }

    public static void update() {
        HashMap<Class<?>, Long> newLocked = new HashMap<Class<?>, Long>();
        ArrayList<Class<?>> remove = new ArrayList<Class<?>>();
        for (Map.Entry<Class<?>, Long> entry : locked.entrySet()) {
            //Remove all key-value pair for which the end time has passed.
            if (entry.getValue() > 0 && entry.getValue() < System.currentTimeMillis())
                //Remove entry from map
                remove.add(entry.getKey());
        }

        //Remove expired entries
        for (Class<?> c : remove)
            newLocked.remove(c);

        locked = newLocked;

        HashMap<String, Long> newPacks = new HashMap<String, Long>();
        ArrayList<String> removePacks = new ArrayList<String>();
        for (Map.Entry<String, Long> pack : lockedPacks.entrySet()) {
            if (pack.getValue() > 0 && pack.getValue() < System.currentTimeMillis())
                removePacks.add(pack.getKey());
        }

        for (String key : removePacks)
            newPacks.remove(key);

        lockedPacks = newPacks;
    }

    public static boolean exitLocked() {
        return inMap(EXIT);
    }
}
