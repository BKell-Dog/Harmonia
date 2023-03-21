package com.example.harmonialauncher.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.harmonialauncher.lock.LockActivity;
import com.example.harmonialauncher.appgrid.AppObject;
import com.example.harmonialauncher.R;
import com.example.harmonialauncher.Settings.SettingsActivity;
import com.example.harmonialauncher.lock.LockManager;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Util {

    public static final String LOCK_PACKAGE_NAME = "Harmonia Lock App",
            LOCK_APP_NAME = "App Locker",
            EXIT_PACKAGE_NAME = "Harmonia Exit App",
            EXIT_APP_NAME = "Exit Harmonia",
            LAUNCHER_SETTINGS_PACKAGE_NAME = "Settings Launcher Page",
            LAUNCHER_SETTINGS_APP_NAME = "Select Launcher",
            SETTINGS_PACKAGE_NAME = "Settings Page",
            SETTINGS_APP_NAME = "Harmonia Settings";
    private static final String TAG = "Util";
    private static ArrayList<AppObject> apps = new ArrayList<AppObject>();

    public static View findChildAt(@NonNull ViewGroup v, int x, int y)
    {
        int children = v.getChildCount();
        if (children == 0)
            return null;

        for (int i = 0; i < v.getChildCount(); i++) {
            if (v.getChildAt(i) != null) {
                Rect bounds = getViewBounds(v.getChildAt(i));
                if (bounds.contains(x, y)) {
                    return v.getChildAt(i);
                }
            }
        }
        return null;
    }

    public static ArrayList<AppObject> loadAllApps(Context c) {

        apps.clear();

        PackageManager pm = c.getPackageManager();
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

        apps.addAll(loadHarmoniaApps(c));

        return apps;
    }

    public static ArrayList<AppObject> loadAllApps(Fragment f) {
        return loadAllApps(f.getActivity());
    }

    public static ArrayList<AppObject> loadFirstFifteenApps(@NonNull Context c) {
        ArrayList<AppObject> allApps = loadAllApps(c);
        ArrayList<AppObject> apps = new ArrayList<>();
        for (int i = 0; i < 15 && i < allApps.size(); i++)
            apps.add(allApps.get(i));
        return apps;
    }

    /**
     * Method to load AppObjects which are specific to the functionality of
     * Harmonia. For example, Harmonia settings or Lock Manager.
     *
     * @return ArrayList<AppObject>
     */
    private static ArrayList<AppObject> loadHarmoniaApps(@NonNull Context c) {
        ArrayList<AppObject> hApps = new ArrayList<AppObject>();

        //Lock Activity
        hApps.add(new AppObject(LOCK_PACKAGE_NAME, LOCK_APP_NAME, R.drawable.lock_icon, ResourcesCompat.getDrawable(c.getResources(), R.drawable.lock_icon, null), false));

        //App icon which opens the settings page for changing default launcher
        hApps.add(new AppObject(LAUNCHER_SETTINGS_PACKAGE_NAME, LAUNCHER_SETTINGS_APP_NAME, R.drawable.launcher_icon, ResourcesCompat.getDrawable(c.getResources(), R.drawable.launcher_icon, null), false));

        //Exit Intent, for Testing on Real Phones
        hApps.add(new AppObject(EXIT_PACKAGE_NAME, EXIT_APP_NAME, R.drawable.exit_icon, ResourcesCompat.getDrawable(c.getResources(), R.drawable.exit_icon, null), false));

        hApps.add(new AppObject(SETTINGS_PACKAGE_NAME, SETTINGS_APP_NAME, R.drawable.settings_icon, ResourcesCompat.getDrawable(c.getResources(), R.drawable.settings_icon, null), false));

        return hApps;
    }

    public static boolean openApp(Context context, String appPackageName) {
        if (context == null)
            return false;

        if (appPackageName.equalsIgnoreCase(LOCK_PACKAGE_NAME)) {
            Intent intent = new Intent(context, LockActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return true;
        } else if (appPackageName.equalsIgnoreCase(EXIT_PACKAGE_NAME)) {
            ((Activity) context).finish();
            return true;
        } else if(appPackageName.equalsIgnoreCase(SETTINGS_PACKAGE_NAME)) {
            Intent intent = new Intent(context, SettingsActivity.class);
            context.startActivity(intent);
        } else if (appPackageName.equalsIgnoreCase(LAUNCHER_SETTINGS_PACKAGE_NAME)) {
            final Intent intent;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                intent = new Intent(Settings.ACTION_HOME_SETTINGS);
            } else {
                intent = new Intent(Settings.ACTION_SETTINGS);
            }
            if (!LockManager.isLocked(intent)) {
                context.startActivity(intent);
                return true;
            }
            return false;
        }

        if (LockManager.isLocked(appPackageName))
            return false;

        //Form intent from package name
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(appPackageName);

        if (intent != null) {
            context.startActivity(intent);
            Log.d(TAG, "Started app, package name: '" + appPackageName + "'");
            return true;
        } else {
            Log.e(TAG, "Cannot start app. AppPackageName:'" + appPackageName + "'");
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

    public static ArrayList<String> getAllPackages(Fragment f) {
        return getAllPackages(f.getActivity());
    }

    public static ArrayList<String> getAppPackages(Activity act) {
        ArrayList<AppObject> apps = loadAllApps(act);
        ArrayList<String> packs = new ArrayList<String>();
        for (AppObject a : apps)
            packs.add(a.getPackageName());
        return packs;
    }

    public static ArrayList<String> getAppPackages(Fragment f) {
        return getAppPackages(f.getActivity());
    }

    public static AppObject findAppByPackageName(String packageName, Context c) {
        for (AppObject a : apps)
            if (a.getPackageName().equalsIgnoreCase(packageName))
                return a;
        return null;
    }

    public static AppObject findAppByPackageName(String packageName, Fragment f) {
        return findAppByPackageName(packageName, f.getActivity());
    }

    public static AppObject findAppByPackageName(String packageName, Activity a) {
        return findAppByPackageName(packageName, a);
    }

    public static AppObject findAppByName(String name, Context c) {
        for (AppObject a : apps)
            if (a.getName().equalsIgnoreCase(name))
                return a;
        return null;
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
            } catch (IllegalAccessException e) { e.printStackTrace();
            } catch (InvocationTargetException e) { e.printStackTrace();
            } catch (NoSuchMethodException e) { e.printStackTrace();
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

    public static boolean isPackage(String name, Fragment f) {
        return isPackage(name, f.getActivity());
    }

    public static Point getLocationOnScreen(@NonNull View v) {
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        return new Point(location[0], location[1]);
    }

    public static Rect getViewBounds(View v) {
        Point viewLoc = Util.getLocationOnScreen(v);
        Point viewBound = new Point(viewLoc.x + v.getWidth(), viewLoc.y + v.getHeight());
        return new Rect(viewLoc.x, viewLoc.y, viewBound.x, viewBound.y);
    }

    public static Drawable convertToGreyscale(@NonNull Drawable d) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);

        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);

        d.setColorFilter(filter);

        return d;
    }

    public static Drawable convertToColor(@NonNull Drawable d)
    {
        d.setColorFilter(null);
        return d;
    }

    public static Drawable getDrawableByResource(Context context, int resourceId)
    {
        return ResourcesCompat.getDrawable(context.getResources(), resourceId, null);
    }

    public static ArrayList<AppObject> getLockedApps(Context c) {
        ArrayList<AppObject> locked = new ArrayList<>();
        if (apps == null || apps.size() == 0)
            apps = loadAllApps(c);
        for (AppObject a : apps) {
            if (LockManager.isLocked(a.getPackageName()))
                locked.add(a);
        }
        return locked;
    }


    public static void exitHarmonia(Context context) {
        openApp(context, EXIT_PACKAGE_NAME);
    }
}
