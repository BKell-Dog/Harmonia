package com.example.harmonialauncher.appgrid.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.harmonialauncher.appgrid.AppObject;
import com.example.harmonialauncher.database.AppEntity;
import com.example.harmonialauncher.database.AppRepository;

import java.util.ArrayList;
import java.util.List;

public class DrawerViewModel extends AppGridViewModel {
    private static final String TAG = DrawerViewModel.class.getSimpleName();
    private int currentPage = 0, numOfPages = 0;


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

    public static ArrayList<AppObject> getDrawerScreenApps(@NonNull ArrayList<AppObject> list, int pageNum)
    {
        if (pageNum < 0 || pageNum > list.size() / NUMOFAPPSONPAGE || list.size() == 0)
            return null;

        int start = NUMOFAPPSONPAGE * pageNum, end = NUMOFAPPSONPAGE * (pageNum + 1);
        if (end > list.size())
            end = list.size() - 1;

        return new ArrayList<>(list.subList(start, end));
    }

    public int getNumOfPages() {
        return numOfPages;
    }

    public void setNumOfPages(int num) {
        numOfPages = num;
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