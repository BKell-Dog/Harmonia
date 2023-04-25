package com.example.harmonialauncher.lock;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.harmonialauncher.Utils.Util;
import com.example.harmonialauncher.appgrid.AppObject;
import com.example.harmonialauncher.database.AppEntity;
import com.example.harmonialauncher.database.AppRepository;
import com.example.harmonialauncher.database.LockEntity;
import com.example.harmonialauncher.database.LockRepository;

import java.util.ArrayList;
import java.util.List;

public class LockActivityViewModel extends AndroidViewModel {
    private static final String TAG = LockActivityViewModel.class.getSimpleName();
    private AppRepository appRepository;
    private LockRepository lockRepository;
    private LiveData<List<AppEntity>> appEntityList;
    private LiveData<List<LockEntity>> lockedList;
    private ArrayList<LockEntity> lockedArray = new ArrayList<>();
    private Observer<List<LockEntity>> obs;

    public LockActivityViewModel(@NonNull Application application) {
        super(application);

        appRepository = new AppRepository(application);
        appEntityList = appRepository.getAllApps();
        if (appEntityList.getValue() == null)
            insertAppValues(appRepository);

        lockRepository = new LockRepository(application);
        lockedList = lockRepository.getLockedList();

        obs = new Observer<List<LockEntity>>() {
            @Override
            public void onChanged(List<LockEntity> lockEntities) {
                lockedArray.clear();
                lockedArray.addAll(lockEntities);
            }
        };
        lockedList.observeForever(obs);
    }

    private void insertAppValues(AppRepository repo) {
        ArrayList<AppObject> apps = Util.loadAllApps(getApplication());
        for (AppObject app : apps) {
            AppEntity entity = AppEntity.Factory.toAppEntity(getApplication(), app);
            repo.upsert(entity);
        }
        appEntityList = appRepository.getAllApps();
    }

    public LiveData<List<AppEntity>> getAllApps()
    {
        return appEntityList;
    }

    public LiveData<List<LockEntity>> getLockedList() {
        return lockedList;
    }

    public void lockApp(AppObject a, long lockedUntil)
    {
        lockApp(a.getPackageName(), lockedUntil);
    }

    public void lockApp(String packageName, long lockedUntil)
    {
        LockEntity toLock = new LockEntity(packageName, "", lockedUntil);
        lockRepository.upsert(toLock);
    }

    public void lockApps(List<AppObject> apps, long lockedUntil)
    {
        for (AppObject a : apps)
            lockApp(a.getPackageName(), lockedUntil);
    }

    public void onCleared()
    {
        super.onCleared();
        lockedList.removeObserver(obs);
    }

    public boolean update()
    {
        if (lockedArray == null || lockedArray.size() < 1)
            return false;

        ArrayList<LockEntity> remove = new ArrayList<>();

        //Iterate through each lock entity and check for expired times.
        for (LockEntity l : lockedArray)
            if (l.lockedUntil < System.currentTimeMillis())
                remove.add(l);

        //For each expired lock, remove from the repo.
        for (LockEntity l : remove)
            lockRepository.remove(l);

        Log.d(TAG, "update: " + remove);

        return remove.size() > 0;
    }

}
