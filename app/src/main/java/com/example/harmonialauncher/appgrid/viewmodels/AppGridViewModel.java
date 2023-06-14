package com.example.harmonialauncher.appgrid.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.harmonialauncher.appgrid.AppObject;
import com.example.harmonialauncher.database.AppEntity;
import com.example.harmonialauncher.database.AppRepository;
import com.example.harmonialauncher.database.HomeScreenAppEntity;
import com.example.harmonialauncher.database.HomeScreenRepository;
import com.example.harmonialauncher.database.LockEntity;
import com.example.harmonialauncher.database.LockRepository;
import com.example.harmonialauncher.Utils.Util;

import java.util.ArrayList;
import java.util.List;

public class AppGridViewModel extends AndroidViewModel {
    private static final String TAG = AppGridViewModel.class.getSimpleName();
    protected AppRepository appRepository;
    protected LockRepository lockRepository;
    protected LiveData<List<AppEntity>> appEntityList;
    protected LiveData<List<LockEntity>> lockedList;
    protected boolean onScreen = false;
    public static final int NUMOFAPPSONPAGE = 20, NUM_COLS = 4;
    public final static String TYPE_HOME = "HOME",
            TYPE_DRAWER = "DRAWER";

    public AppGridViewModel(@NonNull Application application) {
        super(application);

        appRepository = new AppRepository(application);
        appEntityList = appRepository.getAllApps();

        lockRepository = new LockRepository(application);
        lockedList = lockRepository.getLockedList();
    }

    private void writeAppValues(AppRepository repo) {
        ArrayList<AppObject> apps = Util.loadAllApps(getApplication());
        for (AppObject app : apps) {
            AppEntity entity = AppEntity.Factory.toAppEntity(app);
            repo.upsert(entity);
        }
        appEntityList = appRepository.getAllApps();
    }

    public void initializeApps()
    {
        //writeAppValues(appRepository);
    }

    public void overwriteLockList(ArrayList<LockEntity> lockList)
    {
        lockRepository.overwrite(lockList);
    }

    public LiveData<List<AppEntity>> getAppList() {
        return appEntityList;
    }

    public LiveData<List<LockEntity>> getLockedList() {
        return lockedList;
    }

    public boolean isOnScreen() {
        return onScreen;
    }

    public void setOnScreen(boolean onScreen) {
        this.onScreen = onScreen;
    }

    //TODO: rework this entire method
    public void swap(String type, int pageNum, int a, int b) {
        /*if (type.equalsIgnoreCase(TYPE_HOME)) {
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
            }
        }
        writeAppValues(appRepository);*/
    }
}
