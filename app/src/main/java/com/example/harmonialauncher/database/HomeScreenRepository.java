package com.example.harmonialauncher.database;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.harmonialauncher.appgrid.AppObject;

import java.util.ArrayList;
import java.util.List;

public class HomeScreenRepository {
    private static final String TAG = HomeScreenRepository.class.getSimpleName();

    private HomeScreenDao mHSDao;
    private LiveData<List<HomeScreenAppEntity>> mAppList;

    public HomeScreenRepository(Application application)
    {
        HomeScreenDatabase db = HomeScreenDatabase.getDatabase(application);
        mHSDao = db.hsDao();
        mAppList = mHSDao.getAppList();
    }

    public LiveData<List<HomeScreenAppEntity>> getAllApps() {
        return mAppList;
    }

    public void deleteAll() {
        Log.d(TAG, "Delete All from Home Screen DB");
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mHSDao.deleteAll();
        });
    }

    public void upsert(HomeScreenAppEntity appEntity) {
        Log.d(TAG, "Upsert Home Screen DB");
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mHSDao.upsert(appEntity);
        });
    }

    public void upsert(List<HomeScreenAppEntity> apps)
    {
        for (HomeScreenAppEntity a : apps)
            upsert(a);
    }

    public void update(HomeScreenAppEntity appEntity) {
        Log.d(TAG, "Update Home Screen DB");
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mHSDao.update(appEntity);
        });
    }

    public void remove(HomeScreenAppEntity appEntity) {
        Log.d(TAG, "Remove from Home Screen DB");
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mHSDao.delete(appEntity);
        });
    }

    public void overwrite(List<HomeScreenAppEntity> newEntities) {
        Log.d(TAG, "Overwrite Home Screen DB");
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mHSDao.deleteAll();
            for (HomeScreenAppEntity hsae : newEntities)
                mHSDao.insert(hsae);
        });
    }

}
