package com.example.harmonialauncher.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.example.harmonialauncher.Utils.Util;

public class DrawerViewModel extends AndroidViewModel {
    private static final String TAG = DrawerViewModel.class.getSimpleName();
    private int currentPage = -1;
    private final int numOfPages;

    public DrawerViewModel(@NonNull Application application)
    {
        super(application);
        this.numOfPages = Util.loadAllApps(application).size();
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}