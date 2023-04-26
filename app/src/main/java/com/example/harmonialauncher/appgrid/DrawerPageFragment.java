package com.example.harmonialauncher.appgrid;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.harmonialauncher.appgrid.viewmodels.AppGridViewModel;
import com.example.harmonialauncher.appgrid.viewmodels.DrawerViewModel;
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
    private DrawerViewModel vm;
    private int pageNum;

    public DrawerPageFragment(int pageNum) {
        this.pageNum = pageNum;
        Log.d(TAG, "DrawerPageFragment: " + pageNum);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        vm = new ViewModelProvider(requireActivity()).get(DrawerViewModel.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        vm.getAppList().observe(getViewLifecycleOwner(), new Observer<List<AppEntity>>() {
            @Override
            public void onChanged(List<AppEntity> appEntities) {
                appList = AppObject.Factory.toAppObjects(CONTEXT, appEntities);
                appList = DrawerViewModel.getDrawerScreenApps(appList, pageNum);
                adapter = new AppGridAdapter(CONTEXT, R.layout.app, appList);
                gv.setAdapter(adapter);
            }
        });

        appList = DrawerViewModel.getDrawerScreenApps(appList, pageNum);
        adapter = new AppGridAdapter(CONTEXT, R.layout.app, appList);
        gv.setAdapter(adapter);
        return v;
    }

    @NonNull
    public String toString() {
        return "Drawer Page Fragment #" + pageNum;
    }
}
