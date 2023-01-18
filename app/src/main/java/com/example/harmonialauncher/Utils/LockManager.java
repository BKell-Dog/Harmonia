package com.example.harmonialauncher.Utils;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.harmonialauncher.Helpers.AppObject;
import com.example.harmonialauncher.Interfaces.Lockable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LockManager {

    public static final String EXIT = "EXIT";
    private static final String TAG = "Lock Manager";
    //Constants
    private static final long INDEFINITELY = -1;
    //These hash maps hold the locked classes or package names as key, and as value hold the end time of the lock period.
    private static HashMap<Class<?>, Long> locked = new HashMap<Class<?>, Long>();
    private static HashMap<String, Long> lockedPacks = new HashMap<String, Long>();
    //Variables for efficiency
    private static long nearestTime = -1;

    public static boolean isLocked(Lockable obj) {
        update();

        //Check if object is internally locked. If so, add it to our locked map, return true.
        if (obj.isLocked())
            return true;

        //Check for AppObject and corresponding package name
        if (obj instanceof AppObject)
            if (inMap(((AppObject) obj).getPackageName()))
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

        if (nearestTime > System.currentTimeMillis() + millisLocked)
            nearestTime = System.currentTimeMillis() + millisLocked;

        if (obj instanceof AppObject)
            lockedPacks.put(((AppObject) obj).getPackageName(), millisLocked);
    }

    public static void lock(String packageName) {
        lock(packageName, -1);
    }

    public static void lock(String packageName, int millisLocked) {
        lock(packageName, (long) millisLocked);
    }

    //TODO: locked packages are not synced with corresponding Lockable App Objects. Must fix.
    public static void lock(String packageName, long millisLocked) {
        lockedPacks.put(packageName, System.currentTimeMillis() + millisLocked);
        if (nearestTime > System.currentTimeMillis() + millisLocked)
            nearestTime = System.currentTimeMillis() + millisLocked;
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

    //TODO: with long type time variables, we experience overflows and time ends up negative.
    public static long getTimeRemaining(@NonNull Lockable obj) {
        if (obj instanceof AppObject) {
            AppObject a = (AppObject) obj;
            if (inMap(a.getPackageName()))
                return lockedPacks.get(a.getPackageName()) - System.currentTimeMillis();
            else
                return 0;
        } else if (inMap(obj)) {
            return locked.get(obj.getClass()) - System.currentTimeMillis();
        } else
            return 0;
    }

    public static long getEndTime(@NonNull Lockable obj) {
        if (inMap(obj))
            return locked.get(obj.getClass());
        else
            return 0;
    }

    public static void add(Lockable obj, long endTime) {
        locked.put(obj.getClass(), endTime);
    }

    /**
     * Since locked apps are stored in an ArrayList without reference to the time period for which
     * they are locked, this method, which will be called before all invocations of isLocked(), will
     * check that each app in the list has not expired its lock period. If current time > start time
     * + lock period, then we remove the app and package from the lists.
     */
    public static void update() {

        if (nearestTime == -1 || nearestTime < System.currentTimeMillis()) {
            Long smallestValue = Long.MAX_VALUE;
            for (HashMap.Entry<String, Long> entry : lockedPacks.entrySet()) {
                if (entry.getValue() < smallestValue) {
                    smallestValue = entry.getValue();
                }
            }
            nearestTime = smallestValue;
        }

        //If hashmaps are empty, there is nothing to update. If no apps have expired yet, no need to update.
        if ((locked.size() <= 0 && lockedPacks.size() <= 0) || nearestTime > System.currentTimeMillis())
            return;

        Log.d(TAG, "NearestTime: " + nearestTime);

        HashMap<Class<?>, Long> newLocked = (HashMap<Class<?>, Long>) locked.clone();
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

        HashMap<String, Long> newPacks = (HashMap<String, Long>) lockedPacks.clone();
        ArrayList<String> removePacks = new ArrayList<String>();
        for (Map.Entry<String, Long> pack : lockedPacks.entrySet()) {
            if (pack.getValue() > 0 && pack.getValue() < System.currentTimeMillis()) {
                removePacks.add(pack.getKey());
            }
        }
        Log.d(TAG, "151515 Current Time " + System.currentTimeMillis() + " --- Remove Packs: " + removePacks);

        for (String key : removePacks)
            newPacks.remove(key);

        lockedPacks = newPacks;
    }

    public static boolean exitLocked() {
        return inMap(EXIT);
    }
}
