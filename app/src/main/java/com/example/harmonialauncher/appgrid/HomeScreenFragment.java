package com.example.harmonialauncher.appgrid;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.harmonialauncher.Utils.Util;
import com.example.harmonialauncher.appgrid.viewmodels.AppGridViewModel;
import com.example.harmonialauncher.appgrid.viewmodels.HomeScreenViewModel;
import com.example.harmonialauncher.database.AppEntity;
import com.example.harmonialauncher.database.HomeScreenAppEntity;
import com.example.harmonialauncher.lock.LockStatusChangeListener;
import com.example.harmonialauncher.R;

import java.util.ArrayList;
import java.util.List;

/*
This class will manage the GridView which displays the apps, its construction and popualtion, and will
generate the apps to display as well. It will manage the default and preset packages, as well as the
pressing of buttons and opening of apps. This screen is the home screen and launcher.
 */

public class HomeScreenFragment extends AppGridFragment implements LockStatusChangeListener.LockStatusListener {
    private final static String TAG = HomeScreenFragment.class.getSimpleName();
    private HomeScreenViewModel vm;
    private LiveData<List<HomeScreenAppEntity>> mHSApps;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        vm = new ViewModelProvider(requireActivity()).get(HomeScreenViewModel.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        if (savedInstanceState == null) {
            ArrayList<HomeScreenAppEntity> hsEntities = HomeScreenAppEntity.toHSAE(Util.loadFirstFifteenApps(requireContext()));
            vm.overwriteValues(hsEntities);
        }

        final Fragment frag = this;
        mHSApps = vm.getHomeScreenApps();
        mHSApps.observe(getViewLifecycleOwner(), new Observer<List<HomeScreenAppEntity>>() {
            @Override
            public void onChanged(List<HomeScreenAppEntity> homeScreenAppEntities) {
                appList.clear();
                ArrayList<AppObject> allApps = Util.loadAllApps(frag);
                for (HomeScreenAppEntity hsae : homeScreenAppEntities) {
                    appList.add(Util.findAppByPackageName(allApps, hsae.packageName));
                }
                adapter = new AppGridAdapter(frag.requireContext(), R.layout.app, appList);
                adapter.setLockedPackages(lockedPacks);
                gv.setAdapter(adapter);
                gv.invalidate();
            }
        });

        return v;
    }

    public void onDestroy()
    {
        super.onDestroy();
        appList = adapter.getAppList();
        if (appList.size() > AppGridViewModel.NUMOFAPPSONPAGE)
            appList = new ArrayList<>(appList.subList(0, AppGridViewModel.NUMOFAPPSONPAGE));
        vm.overwriteValues(HomeScreenAppEntity.toHSAE(appList));
    }
}