package com.example.harmonialauncher;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Util {

    public static ArrayList<AppObject> loadAllApps() {
        ArrayList<AppObject> apps = new ArrayList<AppObject>();
        PackageManager pm = getGenericContext().getPackageManager();
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
            if ((app.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
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
            String packageName = (String) packages.get(installedApps.indexOf(app)).packageName;
            String label = (String) pm.getApplicationLabel(app);
            Drawable icon = pm.getApplicationIcon(app);
            apps.add(new AppObject(packageName, label, icon, false));
        }

        return apps;
    }

    public static ArrayList<String> getAllPackages()
    {
        PackageManager pm = getGenericContext().getPackageManager();
        List<PackageInfo> packages = pm.getInstalledPackages(0);
        ArrayList<String> packs = new ArrayList<String>();
        for (PackageInfo p : packages)
            packs.add(p.packageName);
        return packs;
    }

    public static ArrayList<String> getAppPackages()
    {
        ArrayList<AppObject> apps = loadAllApps();
        ArrayList<String> packs = new ArrayList<String>();
        for (AppObject a : apps)
            packs.add(a.getPackageName());
        return packs;
    }

    public static AppObject findAppByPackageName(String packageName) {
        ArrayList<AppObject> apps = loadAllApps();
        for (AppObject a : apps)
            if (a.getPackageName().equalsIgnoreCase(packageName))
                return a;
        return null;
    }

    public static Context getGenericContext() {
        Context c = MainActivity.getContext();
        return c;
    }

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
}
