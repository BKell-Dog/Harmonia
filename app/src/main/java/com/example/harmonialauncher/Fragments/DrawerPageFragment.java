package com.example.harmonialauncher.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.harmonialauncher.Helpers.AppObject;
import com.example.harmonialauncher.Adapters.DrawerGridAdapter;
import com.example.harmonialauncher.Utils.ConfigManager;
import com.example.harmonialauncher.Utils.LockStatusChangeListener;
import com.example.harmonialauncher.R;
import com.example.harmonialauncher.Utils.Util;
import com.example.harmonialauncher.ViewModels.DrawerPageViewModel;

import java.util.ArrayList;

// Purpose of this class: retrieve app data for all installed apps, and display app name as well as
// app icon (type Drawable) on the screen in a grid. Grid will not exceed 4 columns and 5 rows, and
// all app elements (icon + text) will be scaled into fixed dimensions as if the grid was full; apps
// will not resize as more space on the screen appears. When an app element is tapped, the app opens.
// When an app element is held down, options appear and the app may move to where the finger decides.
public class DrawerPageFragment extends AppGridPage implements LockStatusChangeListener.LockStatusListener {
    private static final String TAG = "Drawer Page Fragment";
    private DrawerPageViewModel vm;
    private int pageNum;

    public DrawerPageFragment(int pageNum) {
        super(R.layout.app_grid_page);
        this.pageNum = pageNum;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        vm = new ViewModelProvider(requireActivity()).get(DrawerPageViewModel.class);
        vm.setPageNum(pageNum);
        vm.setDrawerPageApps(pageNum);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new DrawerGridAdapter(CONTEXT, R.layout.app, vm.getAppList());
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        vm.writeAppsToFile();
    }

    @NonNull
    public String toString() {
        return "Drawer Page Fragment #" + vm.getPageNum();
    }
}
