package com.example.harmonialauncher.applist;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.harmonialauncher.Utils.Util;
import com.example.harmonialauncher.appgrid.AppObject;
import com.example.harmonialauncher.error.ErrorMessageDialog;

import java.util.ArrayList;

public class AppListViewModel extends AndroidViewModel {
    private static final String TAG = AppListViewModel.class.getSimpleName();
    private ArrayList<AppObject> apps = new ArrayList<>();

    public AppListViewModel(@NonNull Application application) {
        super(application);

        try {
            apps = Util.loadAllApps(application);
        }
        catch (Exception e)
        {
            ErrorMessageDialog.showDialog(application, e);
        }
    }

    public ArrayList<AppObject> getAppList()
    {
        return apps;
    }
}
