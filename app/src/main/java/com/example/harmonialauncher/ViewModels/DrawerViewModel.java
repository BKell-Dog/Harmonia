package com.example.harmonialauncher.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class DrawerViewModel extends AndroidViewModel {
    private static final String TAG = DrawerViewModel.class.getSimpleName();
    private int currentPage = -1;
    private final int numOfPages;

    public DrawerViewModel(@NonNull Application application, int numOfPages)
    {
        super(application);
        this.numOfPages = numOfPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = (currentPage >= 0 && currentPage < numOfPages) ? currentPage : this.currentPage;
    }

    public int getNumOfPages()
    {return numOfPages;}

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
            return (T) new DrawerViewModel(application, numOfPages);
        }
    }
}