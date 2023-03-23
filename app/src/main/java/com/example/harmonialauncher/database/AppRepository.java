package com.example.harmonialauncher.database;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import androidx.lifecycle.LiveData;

import com.example.harmonialauncher.appgrid.AppObject;

import java.util.ArrayList;
import java.util.List;

public class AppRepository {
    private static final String TAG = AppRepository.class.getSimpleName();

    private AppDao mAppDao;
    private LiveData<List<AppEntity>> mAllApps;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public AppRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mAppDao = db.appDao();
        mAllApps = mAppDao.getAppList();

        // Insert values if needed
        AppDatabase.databaseWriteExecutor.execute(() -> { // Take this activity off the UI thread

            if (mAllApps == null || mAllApps.getValue() == null || mAllApps.getValue().size() == 0) {
                ArrayList<AppObject> apps = new ArrayList<>();

                PackageManager pm = application.getPackageManager();
                List<PackageInfo> packages = pm.getInstalledPackages(0); //TODO: Update this package fetching method
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
                    apps.add(new AppObject(packageName, label, icon));
                }

                //Remove all apps that are not launchable
                ArrayList<AppObject> removeApps = new ArrayList<AppObject>();
                for (AppObject app : apps)
                    if (pm.getLaunchIntentForPackage(app.getPackageName()) == null
                            || app.getPackageName() == null || app.getImageId() == 0)
                        removeApps.add(app);
                apps.removeAll(removeApps);

                ArrayList<AppEntity> appEntities = AppEntity.Factory.toAppEntities(application, apps);
                for (AppEntity e : appEntities)
                    if (e.appName != null && e.packageName != null && e.imageId != null) {
                        mAppDao.upsert(e);
                    }
            }
        });
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<AppEntity>> getAllApps() {
        return mAllApps;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void upsert(AppEntity appEntity) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mAppDao.upsert(appEntity);
        });
    }

    public void update(AppEntity appEntity) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mAppDao.update(appEntity);
        });
    }

    public String toString() {
        return "App Repository";
    }
}