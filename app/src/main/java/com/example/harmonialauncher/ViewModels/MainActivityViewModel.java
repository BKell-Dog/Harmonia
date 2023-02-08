package com.example.harmonialauncher.ViewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.example.harmonialauncher.Helpers.AppObject;
import com.example.harmonialauncher.Utils.ConfigManager;
import com.example.harmonialauncher.Utils.Util;

import java.util.ArrayList;

public class MainActivityViewModel extends ViewModel {

    private static final String TAG = MainActivityViewModel.class.getSimpleName();
    private int currentPage = 0;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}
