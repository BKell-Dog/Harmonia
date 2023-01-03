package com.example.harmonialauncher;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import androidx.fragment.app.Fragment;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Util {

    private static final String TAG = "Util";

    public static ArrayList<AppObject> loadAllApps(Activity a) {
        ArrayList<AppObject> apps = new ArrayList<AppObject>();
        PackageManager pm = a.getApplicationContext().getPackageManager();
        List<PackageInfo> packages = pm.getInstalledPackages(0);
        List<ApplicationInfo> applications = new ArrayList<ApplicationInfo>();

        for (PackageInfo p : packages)
            try {
                applications.add(pm.getApplicationInfo(p.packageName, 0));
            } catch (Exception e) {
                e.printStackTrace();
            }

        //Take all uninstalled apps out of the list
        List<ApplicationInfo> installedApps = new ArrayList<ApplicationInfo>();
        for (ApplicationInfo app : applications) {
            //checks for flags; if flagged, check if updated system app
            if ((app.flags & ApplicationInfo.FLAG_INSTALLED) != 0) { //FLAG_UPDATED_SYSTEM_APP) != 0) {
                installedApps.add(app);
                //it's a system app, not interested
            } else if ((app.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                //Discard this one
                //in this case, it should be a user-installed app
            } else {
                installedApps.add(app);
            }
        }

        for (ApplicationInfo app : installedApps) {
            String packageName = packages.get(installedApps.indexOf(app)).packageName;
            String label = (String) pm.getApplicationLabel(app);
            Drawable icon = pm.getApplicationIcon(app);
            apps.add(new AppObject(packageName, label, icon, false));
        }

        //Remove all apps that are not launchable
        ArrayList<AppObject> removeApps = new ArrayList<AppObject>();
        for (AppObject app : apps)
            if (pm.getLaunchIntentForPackage(app.getPackageName()) == null)
                removeApps.add(app);
        apps.removeAll(removeApps);

        return apps;
    }

    public static ArrayList<AppObject> loadAllApps(Fragment f)
    {return loadAllApps(f.getActivity());}

    public static boolean openApp(Context context, String appPackageName) {
        if (context == null)
            return false;

        //if (LockManager.isLocked(Util.findAppByPackageName(appPackageName)))

        //Form intent from package name
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(appPackageName);

        if (intent != null) {
            context.startActivity(intent);
            Log.d(TAG, "Started app, package name: '" + appPackageName + "'");
            return true;
        } else {
            Log.e(TAG, "Cannot start app, appPackageName:'" + appPackageName + "'");
            return false;
        }
    }

    public static ArrayList<String> getAllPackages(Activity a) {
        PackageManager pm = a.getApplicationContext().getPackageManager();
        List<PackageInfo> packages = pm.getInstalledPackages(0);
        ArrayList<String> packs = new ArrayList<String>();
        for (PackageInfo p : packages)
            packs.add(p.packageName);
        return packs;
    }

    public static ArrayList<String> getAllPackages(Fragment f)
    {return getAllPackages(f.getActivity());}

    public static ArrayList<String> getAppPackages(Activity act) {
        ArrayList<AppObject> apps = loadAllApps(act);
        ArrayList<String> packs = new ArrayList<String>();
        for (AppObject a : apps)
            packs.add(a.getPackageName());
        return packs;
    }

    public static ArrayList<String> getAppPackages(Fragment f)
    {return getAppPackages(f.getActivity());}

    public static AppObject findAppByPackageName(String packageName, Activity act) {
        ArrayList<AppObject> apps = loadAllApps(act);
        for (AppObject a : apps)
            if (a.getPackageName().equalsIgnoreCase(packageName))
                return a;
        return null;
    }
    public static AppObject findAppByPackageName(String packageName, Fragment f)
    {return findAppByPackageName(packageName, f.getActivity());}

    public static Point getNavigationBarSize(Context context) {
        Point appUsableSize = getAppUsableScreenSize(context);
        Point realScreenSize = getRealScreenSize(context);

        // navigation bar on the right
        if (appUsableSize.x < realScreenSize.x) {
            return new Point(realScreenSize.x - appUsableSize.x, appUsableSize.y);
        }

        // navigation bar at the bottom
        if (appUsableSize.y < realScreenSize.y) {
            return new Point(appUsableSize.x, realScreenSize.y - appUsableSize.y);
        }

        // navigation bar is not present
        return new Point();
    }

    public static Point getAppUsableScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public static Point getRealScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();

        if (Build.VERSION.SDK_INT >= 17) {
            display.getRealSize(size);
        } else if (Build.VERSION.SDK_INT >= 14) {
            try {
                size.x = (Integer) Display.class.getMethod("getRawWidth").invoke(display);
                size.y = (Integer) Display.class.getMethod("getRawHeight").invoke(display);
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            } catch (NoSuchMethodException e) {
            }
        }

        return size;
    }

    public static boolean isPackage(String name, Activity a) {
        ArrayList<String> packs = Util.getAllPackages(a);
        for (String p : packs)
            if (p.equalsIgnoreCase(name))
                return true;
        return false;
    }

    public static boolean isPackage(String name, Fragment f)
    {return isPackage(name, f.getActivity());}
}
