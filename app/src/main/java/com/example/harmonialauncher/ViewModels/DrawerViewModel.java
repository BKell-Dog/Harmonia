package com.example.harmonialauncher.ViewModels;

import androidx.lifecycle.ViewModel;

public class DrawerViewModel extends ViewModel {
    private static final String TAG = "Drawer Page View Model";
    private int currentPage = -1;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}