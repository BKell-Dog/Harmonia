package com.example.harmonialauncher.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.example.harmonialauncher.Adapters.AppGridAdapter;
import com.example.harmonialauncher.Listeners.LockStatusChangeListener;
import com.example.harmonialauncher.R;
import com.example.harmonialauncher.ViewModels.AppGridViewModel;

// Purpose of this class: retrieve app data for all installed apps, and display app name as well as
// app icon (type Drawable) on the screen in a grid. Grid will not exceed 4 columns and 5 rows, and
// all app elements (icon + text) will be scaled into fixed dimensions as if the grid was full; apps
// will not resize as more space on the screen appears. When an app element is tapped, the app opens.
// When an app element is held down, options appear and the app may move to where the finger decides.
public class DrawerPageFragment extends AppGridFragment implements LockStatusChangeListener.LockStatusListener {
    private static final String TAG = DrawerPageFragment.class.getSimpleName();
    private AppGridViewModel vm;
    private int pageNum;

    public DrawerPageFragment(int pageNum) {
        super(AppGridViewModel.TYPE_DRAWER, R.layout.app_grid_page);
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

        adapter = new AppGridAdapter(CONTEXT, R.layout.app, vm.getAppList(AppGridViewModel.TYPE_DRAWER, pageNum));
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void onResume()
    {
        super.onResume();
        Log.d(TAG, "onResume: " + pageNum + "---" + vm.getAppList(AppGridViewModel.TYPE_DRAWER, pageNum));
    }

    @NonNull
    public String toString() {
        return "Drawer Page Fragment #" + pageNum;
    }
}
