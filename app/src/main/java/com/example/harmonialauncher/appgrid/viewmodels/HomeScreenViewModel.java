package com.example.harmonialauncher.appgrid.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.harmonialauncher.database.AppRepository;
import com.example.harmonialauncher.database.HomeScreenAppEntity;
import com.example.harmonialauncher.database.HomeScreenRepository;

import java.util.ArrayList;
import java.util.List;

public class HomeScreenViewModel extends AppGridViewModel {
    private static final String TAG = HomeScreenViewModel.class.getSimpleName();

    private HomeScreenRepository hsRepository;
    private LiveData<List<HomeScreenAppEntity>> homeScreenAppList;

    public HomeScreenViewModel(@NonNull Application application) {
        super(application);

        hsRepository = new HomeScreenRepository(application);
        homeScreenAppList = hsRepository.getAllApps();
    }

    public LiveData<List<HomeScreenAppEntity>> getHomeScreenApps() {
        return homeScreenAppList;
    }

    public void upsert(HomeScreenAppEntity appEntity) {
        hsRepository.upsert(appEntity);
    }

    public void overwriteValues(List<HomeScreenAppEntity> allHSApps) {
        hsRepository.overwrite(allHSApps);
    }
}
