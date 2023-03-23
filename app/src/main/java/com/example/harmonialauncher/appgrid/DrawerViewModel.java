package com.example.harmonialauncher.appgrid;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.harmonialauncher.database.AppEntity;
import com.example.harmonialauncher.database.AppRepository;

import java.util.List;

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
        Log.d(TAG, "setCurrentPage: Num of Pages " + getNumOfPages());
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