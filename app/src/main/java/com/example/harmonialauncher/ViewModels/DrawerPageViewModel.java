package com.example.harmonialauncher.ViewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.harmonialauncher.Helpers.AppObject;
import com.example.harmonialauncher.Utils.ConfigManager;
import com.example.harmonialauncher.Utils.Util;

import java.util.ArrayList;

public class DrawerPageViewModel extends AppGridViewModel {
    private static final String TAG = DrawerPageViewModel.class.getSimpleName();
    private int pageNum = -1;

    public DrawerPageViewModel(@NonNull Application application) {
        super(application);
    }

    public int getPageNum() {
        Log.d(TAG, "getPageNum: " + pageNum);return pageNum;
    }

    public ArrayList<AppObject> getAppList() {
        Log.d(TAG, "getAppList: " + pageNum);return appList;
    }

    public void setDrawerPageApps(int pageNum) {
        ArrayList<AppObject> allApps = Util.loadAllApps(application.getApplicationContext()); //TODO: change this to read from ConfigManager.
        appList.clear();
        Log.d(TAG, "setDrawerPageApps: " + System.identityHashCode(appList));
        for (int k = pageNum * NUMOFAPPSONPAGE; k < (pageNum * NUMOFAPPSONPAGE) + NUMOFAPPSONPAGE && k < allApps.size(); k++)
            appList.add(allApps.get(k));
    }

    public void setPageNum(int num) {
        pageNum = num;
    }

    public void writeAppsToFile() {
        ConfigManager.writeDrawerAppsToFile(application.getApplicationContext(), appList);
    }
}
