package com.example.harmonialauncher.lock;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.harmonialauncher.Utils.Util;
import com.example.harmonialauncher.appgrid.AppObject;
import com.example.harmonialauncher.database.AppEntity;
import com.example.harmonialauncher.database.AppRepository;

import java.util.ArrayList;
import java.util.List;

public class LockActivityViewModel extends AndroidViewModel {
    private static final String TAG = LockActivityViewModel.class.getSimpleName();
    private AppRepository repository;
    private LiveData<List<AppEntity>> appEntityList;

    public LockActivityViewModel(@NonNull Application application) {
        super(application);

        repository = new AppRepository(application);
        appEntityList = repository.getAllApps();
        if (appEntityList.getValue() == null)
            insertAppValues(repository);

    }

    private void insertAppValues(AppRepository repo) {
        ArrayList<AppObject> apps = Util.loadAllApps(getApplication());
        for (AppObject app : apps) {
            AppEntity entity = AppEntity.Factory.toAppEntity(getApplication(), app);
            repo.upsert(entity);
        }
        appEntityList = repository.getAllApps();
    }

    public LiveData<List<AppEntity>> getAllApps()
    {
        return appEntityList;
    }

}
