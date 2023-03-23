package com.example.harmonialauncher.applist;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.harmonialauncher.Utils.Util;
import com.example.harmonialauncher.appgrid.AppObject;
import com.example.harmonialauncher.database.AppEntity;
import com.example.harmonialauncher.database.AppRepository;
import com.example.harmonialauncher.error.ErrorMessageDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AppListViewModel extends AndroidViewModel {
    private static final String TAG = AppListViewModel.class.getSimpleName();
    private Application APPLICATION;
    private ArrayList<AppObject> apps = new ArrayList<>();

    private AppRepository repository;
    protected LiveData<List<AppEntity>> appEntityList;
    public final static int SORT_AZ = 0,
                                SORT_ZA = 1,
                                SORT_OLDNEW = 2,
                                SORT_NEWOLD = 3;

    public AppListViewModel(@NonNull Application application) {
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
    }

    public LiveData<List<AppEntity>> getAppList()
    {
        return appEntityList;
    }

    public void sortList(int sortType)
    {
        switch(sortType)
        {
            case SORT_AZ:
                Collections.sort(apps, (a, b) -> a.getName().compareToIgnoreCase(b.getName()));
                break;
            case SORT_ZA:
                Collections.sort(apps, (a, b) -> - a.getName().compareToIgnoreCase(b.getName()));
                break;
            //TODO: Create sorting method for Old-New and New-Old
        }
    }
}
