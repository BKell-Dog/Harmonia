package com.example.harmonialauncher.appgrid.viewmodels;

import androidx.lifecycle.ViewModel;

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
