package com.example.harmonialauncher.AppGrid;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.harmonialauncher.Database.AppEntity;
import com.example.harmonialauncher.Database.AppRepository;
import com.example.harmonialauncher.Utils.LockManager;
import com.example.harmonialauncher.Utils.Util;

import java.util.ArrayList;

public class AppGridViewModel extends AndroidViewModel {
    private static final String TAG = AppGridViewModel.class.getSimpleName();
    protected final Application application;
    private AppRepository repository;
    protected ArrayList<AppObject> homeScreenApps, drawerScreenApps;
    protected ArrayList<AppObject> appList = new ArrayList<>();
    protected ArrayList<AppEntity> appEntityList = new ArrayList<>();
    protected boolean firstBoot = true, onScreen = false;
    public final int NUMOFAPPSONPAGE = 20, NUM_COLS = 4;
    public final static String TYPE_HOME = "HOME",
                                TYPE_DRAWER = "DRAWER";

    public AppGridViewModel(@NonNull Application application) {
        super(application);
        this.application = application;

        homeScreenApps = Util.loadFirstFifteenApps(application);
        drawerScreenApps = Util.loadAllApps(application);

        //repository = new AppRepository(application); //TODO: Uncomment these lines when work on the repository and database continue
        //appEntityList = repository.getAllApps().getValue();
        //appList = AppObjectFactory.toAppObjects(application, appEntityList);
    }

    public ArrayList<AppObject> getAppList(String type, int pageNum) {
        if (type.equalsIgnoreCase(TYPE_HOME))
        {
            return homeScreenApps;
        }
        else if (type.equalsIgnoreCase(TYPE_DRAWER))
        {
            ArrayList<AppObject> drawerApps = new ArrayList<>();
            for (int i = pageNum * NUMOFAPPSONPAGE; i < drawerScreenApps.size() && i < (pageNum + 1) * NUMOFAPPSONPAGE; i++)
            {
                drawerApps.add(drawerScreenApps.get(i));
            }
            return drawerApps;
        }
        else
            return null;
    }

    public int getNumOfPages()
    {
        return (int) Math.ceil((double) drawerScreenApps.size() / (double) NUMOFAPPSONPAGE);
    }

    public boolean isFirstBoot() {
        return firstBoot;
    }

    public boolean isLocked(AppObject app)
    {return LockManager.isLocked(app.getPackageName());}

    public boolean isOnScreen()
    {return onScreen;}
    public void setFirstBoot(boolean firstBoot) {
        this.firstBoot = firstBoot;
    }

    public void setOnScreen(boolean onScreen)
    {this.onScreen = onScreen;}

    //TODO: rework this entire method
    public void swap(String type, int pageNum, int a, int b)
    {
        if (type.equalsIgnoreCase(TYPE_HOME)) {
            if (a >= 0 && a < homeScreenApps.size() && b >= 0 && b < homeScreenApps.size()) {
                AppObject app = homeScreenApps.get(a);
                homeScreenApps.set(a, homeScreenApps.get(b));
                homeScreenApps.set(b, app);
            }
        }
        else if (type.equalsIgnoreCase(TYPE_DRAWER))
        {
            /*int index1 = (pageNum * NUMOFAPPSONPAGE) + a,
                    index2 = (pageNum * NUMOFAPPSONPAGE) + b;
            if (a >= 0 && a < drawerScreenApps.size() && b >= 0 && b < drawerScreenApps.size()) {
                AppObject app = drawerScreenApps.get((pageNum * NUMOFAPPSONPAGE) + a);
                drawerScreenApps.set(a, drawerScreenApps.get((pageNum * NUMOFAPPSONPAGE) + b));
                homeScreenApps.set(b, app);
            }*/
        }
    }

    public static class AppObjectFactory {
        public static ArrayList<AppObject> toAppObjects(Context context, ArrayList<AppEntity> entities) {
            ArrayList<AppObject> apps = new ArrayList<>();
            for (AppEntity entity : entities)
                apps.add(toAppObject(context, entity));
            return apps;
        }

        public static AppObject toAppObject(Context context, AppEntity entity)
        {
            AppObject app = new AppObject(entity.packageName, entity.appName, entity.imageId, false);
            app.setImage(Util.getDrawableById(context, entity.imageId));
            return app;
        }
    }
}
