package com.example.harmonialauncher.appgrid;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.harmonialauncher.appgrid.viewmodels.AppGridViewModel;
import com.example.harmonialauncher.database.AppEntity;
import com.example.harmonialauncher.lock.LockStatusChangeListener;
import com.example.harmonialauncher.R;

import java.util.ArrayList;
import java.util.List;

// Purpose of this class: retrieve app data for all installed apps, and display app name as well as
// app icon (type Drawable) on the screen in a grid. Grid will not exceed 4 columns and 5 rows, and
// all app elements (icon + text) will be scaled into fixed dimensions as if the grid was full; apps
// will not resize as more space on the screen appears. When an app element is tapped, the app opens.
// When an app element is held down, options appear and the app may move to where the finger decides.
public class DrawerPageFragment extends AppGridFragment implements LockStatusChangeListener.LockStatusListener {
    private static final String TAG = DrawerPageFragment.class.getSimpleName();
    private AppGridViewModel vm;
    private int pageNum;

    public DrawerPageFragment(int pageNum, ArrayList<AppObject> appList) {
        super(appList);
        this.pageNum = pageNum;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        vm = new ViewModelProvider(requireActivity()).get(AppGridViewModel.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        vm.getAppList().observe(this, new Observer<List<AppEntity>>() {
            @Override
            public void onChanged(List<AppEntity> appEntities) {
                appList = vm.getDrawerScreenApps(pageNum);
                Log.d(TAG, "onChanged: DRAWER CHANGED");

                adapter = new AppGridAdapter(CONTEXT, R.id.app_page_grid, appList);
                gv.setAdapter(adapter);
            }
        });

        Log.d(TAG, "onCreate: DRAWER SET 1");
        appList = vm.getDrawerScreenApps(pageNum);
        adapter = new AppGridAdapter(CONTEXT, R.id.app_page_grid, appList);
        gv.setAdapter(adapter);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        vm.getAppList().observe(getViewLifecycleOwner(), new Observer<List<AppEntity>>() {
            @Override
            public void onChanged(List<AppEntity> appEntities) {
                Log.d(TAG, "onChanged: DRAWER OBSERVED 2");
                appList = vm.getDrawerScreenApps(pageNum);
                adapter = new AppGridAdapter(CONTEXT, R.layout.app, appList);
                gv.setAdapter(adapter);
            }
        });
        Log.d(TAG, "onCreateView: DRAWER SET 2");
        appList = vm.getDrawerScreenApps(pageNum);
        adapter = new AppGridAdapter(CONTEXT, R.layout.app, appList);
        gv.setAdapter(adapter);
        return v;
    }

    @NonNull
    public String toString() {
        return "Drawer Page Fragment #" + pageNum;
    }
}
