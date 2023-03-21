package com.example.harmonialauncher.applist;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.harmonialauncher.Utils.Util;
import com.example.harmonialauncher.appgrid.AppObject;
import com.example.harmonialauncher.error.ErrorMessageDialog;

import java.util.ArrayList;
import java.util.Collections;

public class AppListViewModel extends AndroidViewModel {
    private static final String TAG = AppListViewModel.class.getSimpleName();
    private ArrayList<AppObject> apps = new ArrayList<>();
    public final static int SORT_AZ = 0,
                                SORT_ZA = 1,
                                SORT_OLDNEW = 2,
                                SORT_NEWOLD = 3;

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
