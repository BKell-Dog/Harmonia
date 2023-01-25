package com.example.harmonialauncher.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.harmonialauncher.Helpers.AppObject;
import com.example.harmonialauncher.Utils.ConfigManager;
import com.example.harmonialauncher.Utils.LockManager;
import com.example.harmonialauncher.Utils.Util;

import java.util.ArrayList;

public class HomeScreenViewModel extends AppGridViewModel {
    private static final String TAG = "Home Screen View Model";
    private final Application application;

    public HomeScreenViewModel(@NonNull Application application) {
        super(application);
        this.application = application;

        ArrayList<AppObject> receivedApps = ConfigManager.readHomeAppOrderFromFile(application.getApplicationContext());
        if (receivedApps != null)
            setAppList(ConfigManager.readHomeAppOrderFromFile(application.getApplicationContext()));
        else
            setAppList(Util.loadFirstTwentyApps(application.getApplicationContext()));
    }


    public void writeAppsToFile()
    {
        ConfigManager.writeHomeAppsToFile(application.getApplicationContext(), appList);
    }
}
