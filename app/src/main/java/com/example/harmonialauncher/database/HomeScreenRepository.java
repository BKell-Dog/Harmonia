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

    public void deleteAll()
    {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mHSDao.deleteAll();
        });
    }

    public void upsert(HomeScreenAppEntity appEntity) {
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
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mHSDao.update(appEntity);
        });
    }

    public void remove(HomeScreenAppEntity appEntity) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mHSDao.delete(appEntity);
        });
    }

    public void overwrite(List<HomeScreenAppEntity> newEntities)
    {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mHSDao.deleteAll();
            for (HomeScreenAppEntity hsae : newEntities)
                mHSDao.insert(hsae);
        });
    }

}
