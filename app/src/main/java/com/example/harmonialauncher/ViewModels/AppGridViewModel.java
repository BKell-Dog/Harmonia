package com.example.harmonialauncher.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.harmonialauncher.Helpers.AppObject;
import com.example.harmonialauncher.Utils.LockManager;

import java.util.ArrayList;

public class AppGridViewModel extends AndroidViewModel {
    private static final String TAG = "App Grid View Model";
    protected final Application application;
    protected ArrayList<AppObject> appList = new ArrayList<>();
    protected boolean firstBoot = true, onScreen = false;
    public final int NUMOFAPPSONPAGE = 20, NUM_COLS = 4;

    public AppGridViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
    }

    public ArrayList<AppObject> getAppList() {
        return appList;
    }
    public boolean isFirstBoot() {
        return firstBoot;
    }

    public boolean isLocked(AppObject app)
    {return LockManager.isLocked(app.getPackageName());}

    public boolean isOnScreen()
    {return onScreen;}

    public void setAppList(ArrayList<AppObject> apps) {
        appList = apps;
    }
    public void setFirstBoot(boolean firstBoot) {
        this.firstBoot = firstBoot;
    }

    public void setOnScreen(boolean onScreen)
    {this.onScreen = onScreen;}

    public void swap(int a, int b)
    {
        if (a >= 0 && a < appList.size() && b >= 0 && b < appList.size()) {
            AppObject app = appList.get(a);
            appList.set(a, appList.get(b));
            appList.set(b, app);
        }
    }
}
