package com.example.harmonialauncher.lock;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.harmonialauncher.appgrid.AppObject;
import com.example.harmonialauncher.Helpers.TimeHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LockManager {

    private static final String TAG = LockManager.class.getSimpleName();
    public static final String EXIT = "EXIT";
    //Constants
    public static final long INDEFINITELY = Long.MAX_VALUE;
    //These hash maps hold the locked classes or package names as key, and as value hold the end time of the lock period.
    private static HashMap<Class<?>, Long> locked = new HashMap<>();

    /**
     * Hash Map lockedPacks takes as a key the package name of the app, which is always unique. It
     * takes as value the time until which that package is to be locked, referenced to the Java Epoch
     * of January 1st, 1970 at 00:00:00 GMT. The Long is the number of milliseconds which have passed
     * since that date and time.
     */
    private static HashMap<String, Long> lockedPacks = new HashMap<>();
    //Variables for efficiency
    private static Long nearestTime = null;

    public static boolean isLocked(Lockable obj) {
        update();

        //Check if object is internally locked. If so, add it to our locked map, return true.
        if (obj.isLocked()) {
            return true;
        }

        //Check for AppObject and corresponding package name
        if (obj instanceof AppObject)
            if (inMap(((AppObject) obj).getPackageName()))
                return true;

        //If object is in our hash map, then it has not been unlocked: return true.
        return inMap(obj);
    }

    public static boolean isLocked(String packageName) {
        update();

        return inMap(packageName) && lockedPacks.get(packageName) > TimeHelper.now();
    }

    public static boolean isLocked(Intent i) {
        update();

        if (i.getPackage() != null) {
            return inMap(i.getPackage()) && lockedPacks.get(i.getPackage()) > TimeHelper.now();
        } else if (i.getAction() != null) {
            return inMap(i.getAction()) && lockedPacks.get(i.getAction()) > TimeHelper.now();
        }
        return false;
    }

    public static void lock(Lockable obj, TimeHelper time)
    {
        if (obj instanceof AppObject)
            lock(((AppObject) obj).getPackageName(), time);
        else
            if (inMap(obj))
                locked.put(obj.getClass(), time.getAbsoluteTime());
        LockStatusChangeListener.onLockStatusChanged();
    }
    public static void lock(String packageName, TimeHelper time)
    {
        if (!inMap(packageName))
        {
            lockedPacks.put(packageName, time.getAbsoluteTime());

            if (nearestTime > time.getAbsoluteTime())
                nearestTime = time.getAbsoluteTime();

            LockStatusChangeListener.onLockStatusChanged();
        }
    }
    //TODO: locked packages are not synced with corresponding Lockable App Objects. Must fix.

    public static void lock(Intent i) {
        if (i.getPackage() != null)
            lock(i.getPackage(), new TimeHelper(Long.MAX_VALUE, TimeHelper.INPUT_ABSOLUTE));
        else if (i.getAction() != null)
            lock(i.getAction(), new TimeHelper(Long.MAX_VALUE, TimeHelper.INPUT_ABSOLUTE));
    }

    public static void unlock(Lockable obj) {
        obj.unlock();
        locked.remove(obj.getClass());

        LockStatusChangeListener.onLockStatusChanged();
    }

    public static void unlock(String packageName) {
        if (inMap(packageName))
            lockedPacks.remove(packageName);

        LockStatusChangeListener.onLockStatusChanged();
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
    public static TimeHelper getTimeRemaining(@NonNull Lockable obj) {
        if (obj instanceof AppObject) {
            AppObject a = (AppObject) obj;
            return getTimeRemaining(a.getPackageName());
        } else if (inMap(obj)) {
            return new TimeHelper(locked.get(obj.getClass()), TimeHelper.INPUT_ABSOLUTE);
        } else
            return null;
    }

    public static TimeHelper getTimeRemaining(@NonNull String packageName)
    {
        if (inMap(packageName))
            return new TimeHelper(lockedPacks.get(packageName), TimeHelper.INPUT_ABSOLUTE);
        else
            return null;
    }

    public static TimeHelper getEndTime(@NonNull Lockable obj) {
        if (inMap(obj))
            return new TimeHelper(locked.get(obj.getClass()), TimeHelper.INPUT_ABSOLUTE);
        else
            return new TimeHelper(TimeHelper.now(), TimeHelper.INPUT_ABSOLUTE);
    }


    /**
     * Since locked apps are stored in an ArrayList without reference to the time period for which
     * they are locked, this method, which will be called before all invocations of isLocked(), will
     * check that each app in the list has not expired its lock period. If current time > start time
     * + lock period, then we remove the app and package from the lists.
     */
    public static void update() {

        //Update Nearest Time variable
        if (nearestTime == null || nearestTime < TimeHelper.now()) {
            Long smallestTime = Long.MAX_VALUE;
            for (HashMap.Entry<String, Long> entry : lockedPacks.entrySet()) {
                if (entry.getValue() < smallestTime) {
                    smallestTime = entry.getValue();
                }
            }
            nearestTime = smallestTime;
        }

        //If hashmaps are empty, there is nothing to update. If no apps have expired yet, no need to update.
        if ((locked.size() <= 0 && lockedPacks.size() <= 0) || nearestTime > TimeHelper.now())
            return;

        Log.d(TAG, "NearestTime: " + nearestTime);

        //Update generic Lockable types
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
            if (pack.getValue() < TimeHelper.now()) {
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
