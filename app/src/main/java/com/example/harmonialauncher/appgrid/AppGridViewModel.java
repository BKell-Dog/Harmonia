package com.example.harmonialauncher.appgrid;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.harmonialauncher.database.AppEntity;
import com.example.harmonialauncher.database.AppRepository;
import com.example.harmonialauncher.lock.LockManager;
import com.example.harmonialauncher.Utils.Util;

import java.util.ArrayList;
import java.util.List;

public class AppGridViewModel extends AndroidViewModel {
    private static final String TAG = AppGridViewModel.class.getSimpleName();
    private AppRepository repository;
    protected ArrayList<AppObject> homeScreenApps = new ArrayList<>(), drawerScreenApps = new ArrayList<>();
    protected LiveData<List<AppEntity>> appEntityList;
    protected boolean onScreen = false;
    public static final int NUMOFAPPSONPAGE = 20, NUM_COLS = 4;
    public final static String TYPE_HOME = "HOME",
            TYPE_DRAWER = "DRAWER";

    public AppGridViewModel(@NonNull Application application) {
        super(application);

        repository = new AppRepository(application);
        appEntityList = repository.getAllApps();
        if (appEntityList.getValue() == null)
            insertAppValues(repository);

        appEntityList.observeForever(new Observer<List<AppEntity>>() {
            @Override
            public void onChanged(List<AppEntity> appEntities) {
                homeScreenApps.clear();
                drawerScreenApps.clear();
                for (int i = 0; i < NUMOFAPPSONPAGE && i < appEntities.size(); i++)
                    homeScreenApps.add(AppObject.Factory.toAppObject(application, appEntities.get(i)));
                for (AppEntity a : appEntities)
                    drawerScreenApps.add(AppObject.Factory.toAppObject(application, a));
            }
        });
    }

    private void insertAppValues(AppRepository repo) {
        ArrayList<AppObject> apps = Util.loadAllApps(getApplication());
        for (AppObject app : apps) {
            AppEntity entity = AppEntity.Factory.toAppEntity(getApplication(), app);
            repo.upsert(entity);
        }
        appEntityList = repository.getAllApps();
    }

    public LiveData<List<AppEntity>> getAppList() {
        return appEntityList;
    }

    public ArrayList<AppObject> getHomeScreenApps()
    {return homeScreenApps;}

    public ArrayList<AppObject> getDrawerScreenApps()
    {return drawerScreenApps;}

    public ArrayList<AppObject> getDrawerScreenApps(int pageNum)
    {
        return new ArrayList<>(drawerScreenApps.subList(NUMOFAPPSONPAGE * pageNum, NUMOFAPPSONPAGE * (pageNum + 1)));
    }


    public boolean isLocked(AppObject app) {
        return LockManager.isLocked(app.getPackageName());
    }

    public boolean isOnScreen() {
        return onScreen;
    }

    public void setOnScreen(boolean onScreen) {
        this.onScreen = onScreen;
    }

    //TODO: rework this entire method
    public void swap(String type, int pageNum, int a, int b) {
        if (type.equalsIgnoreCase(TYPE_HOME)) {
            if (a >= 0 && a < homeScreenApps.size() && b >= 0 && b < homeScreenApps.size()) {
                AppObject app = homeScreenApps.get(a);
                homeScreenApps.set(a, homeScreenApps.get(b));
                homeScreenApps.set(b, app);
            }
        } else if (type.equalsIgnoreCase(TYPE_DRAWER)) {
            /*int index1 = (pageNum * NUMOFAPPSONPAGE) + a,
                    index2 = (pageNum * NUMOFAPPSONPAGE) + b;
            if (a >= 0 && a < drawerScreenApps.size() && b >= 0 && b < drawerScreenApps.size()) {
                AppObject app = drawerScreenApps.get((pageNum * NUMOFAPPSONPAGE) + a);
                drawerScreenApps.set(a, drawerScreenApps.get((pageNum * NUMOFAPPSONPAGE) + b));
                homeScreenApps.set(b, app);
            }*/
        }
    }
}
