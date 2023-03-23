package com.example.harmonialauncher.database;

import android.content.Context;

import com.example.harmonialauncher.appgrid.AppGridFragment;
import com.example.harmonialauncher.appgrid.AppGridViewModel;
import com.example.harmonialauncher.appgrid.AppObject;

import java.util.ArrayList;
import java.util.List;

public class DBHelper {

    public static ArrayList<AppObject> getHomeScreenApps(Context c, List<AppEntity> appEntities) {
        ArrayList<AppObject> homeApps = AppObject.Factory.toAppObjects(c, appEntities);
        int size = homeApps.size();
        if (size > AppGridViewModel.NUMOFAPPSONPAGE) {
            homeApps.subList(0, AppGridViewModel.NUMOFAPPSONPAGE - 1).clear();
        }
        return homeApps;
    }
}
