package com.example.harmonialauncher.appgrid;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.harmonialauncher.database.AppEntity;
import com.example.harmonialauncher.appgrid.Views.AppGridView;
import com.example.harmonialauncher.Fragments.HarmoniaFragment;
import com.example.harmonialauncher.Utils.HarmoniaGestureDetector;
import com.example.harmonialauncher.lock.LockStatusChangeListener;
import com.example.harmonialauncher.R;
import com.example.harmonialauncher.Utils.Util;
import com.example.harmonialauncher.lock.LockManager;

import java.util.ArrayList;
import java.util.List;

public class AppGridFragment extends HarmoniaFragment implements LockStatusChangeListener.LockStatusListener {

    private static final String TAG = AppGridFragment.class.getSimpleName();
    protected Context CONTEXT;
    protected AppGridViewModel vm;
    //Drawing Grid
    protected AppGridView gv;
    protected AppGridAdapter adapter;
    public static final int NUM_COLS = 4;
    //Gesture Detection
    protected GestureDetectorCompat gd;
    protected ArrayList<AppObject> appList = new ArrayList<>();

    public AppGridFragment(ArrayList<AppObject> apps) {
        super(R.id.app_page_grid);
        appList = apps;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        vm = new ViewModelProvider(requireActivity()).get(AppGridViewModel.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CONTEXT = getActivity();

        //All subclasses should override this method if only to initialize their adapter in their own way.

        gd = new GestureDetectorCompat(getActivity(), new HarmoniaGestureDetector());
        HarmoniaGestureDetector.add(this);
        LockStatusChangeListener.add(this);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.app_grid_page, container, false);

        if (adapter == null)
            adapter = new AppGridAdapter(CONTEXT, R.id.app_page_grid, appList);

        // Populate Grid Layout in home_screen.xml with instances of app.xml
        gv = v.findViewById(R.id.app_page_grid);
        gv.setAdapter(adapter);
        gv.setNumColumns(AppGridViewModel.NUM_COLS);
        gv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        return v;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        //Check if view is created
        if (gv == null || !onScreen)
            return false;

        for (int i = 0; i < gv.getAdapter().getCount(); i++) {
            View v = gv.getChildAt(i);
            if (v != null) {
                Point coords = Util.getLocationOnScreen(v);
                Rect bounds = new Rect(coords.x, coords.y, coords.x + v.getWidth(), coords.y + v.getHeight());
                if (bounds.contains((int) e.getX(), (int) e.getY())) {
                    AppGridAdapter a = gv.getAdapter();
                    AppObject app = a.getItem(i);
                    Log.d(TAG, "Child at: " + i);
                    Log.d(TAG, "View Rect: " + bounds);
                    Log.d(TAG, "Tap Coords: (" + e.getX() + ", " + e.getY() + ")");
                    Log.d(TAG, "TAP INTERSECTS " + app);
                    if (app != null && !LockManager.isLocked(app.getPackageName())) {         //Check that app is not locked
                        Util.openApp(this.CONTEXT, app.getPackageName());
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void onStatusChanged() {
        if (adapter != null)
            gv.setAdapter(adapter);//.copy());
    }
}
