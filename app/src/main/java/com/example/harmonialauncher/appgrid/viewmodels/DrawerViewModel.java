package com.example.harmonialauncher.appgrid.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class DrawerViewModel extends AppGridViewModel {
    private static final String TAG = DrawerViewModel.class.getSimpleName();
    private int currentPage = -1;

    public DrawerViewModel(@NonNull Application application)
    {
        super(application);
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = (currentPage >= 0 && currentPage < getNumOfPages()) ? currentPage : this.currentPage;
    }

    public int getNumOfPages()
    {
        return (int) Math.ceil(((double)drawerScreenApps.size()) / ((double)NUMOFAPPSONPAGE));
    }

    public static class DrawerViewModelFactory implements ViewModelProvider.Factory
    {
        private final Application application;
        private final int numOfPages;
        public DrawerViewModelFactory(Application application, int numOfPages)
        {
            this.application = application;
            this.numOfPages = numOfPages;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass)
        {
            return (T) new DrawerViewModel(application);
        }
    }
}