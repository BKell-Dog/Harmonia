package com.example.harmonialauncher.appgrid;

import static com.example.harmonialauncher.preferences.PreferenceData.LOCK_MODE_GREYSCALE;
import static com.example.harmonialauncher.preferences.PreferenceData.LOCK_MODE_INVISIBLE;
import static com.example.harmonialauncher.preferences.PreferenceData.STYLE_GREYSCALE;
import static com.example.harmonialauncher.preferences.PreferenceData.STYLE_NORMAL;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.example.harmonialauncher.Helpers.TimeHelper;
import com.example.harmonialauncher.appgrid.viewmodels.AppGridViewModel;
import com.example.harmonialauncher.appgrid.views.AppGridView;
import com.example.harmonialauncher.Fragments.HarmoniaFragment;
import com.example.harmonialauncher.Utils.HarmoniaGestureDetector;
import com.example.harmonialauncher.database.AppEntity;
import com.example.harmonialauncher.database.LockEntity;
import com.example.harmonialauncher.lock.LockStatusChangeListener;
import com.example.harmonialauncher.R;
import com.example.harmonialauncher.Utils.Util;

import java.util.ArrayList;
import java.util.List;

public class AppGridFragment extends HarmoniaFragment implements LockStatusChangeListener.LockStatusListener,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = AppGridFragment.class.getSimpleName();
    protected Context CONTEXT;

    // View Models and Database
    protected AppGridViewModel vm;

    //Drawing Grid
    protected AppGridView gv;
    protected AppGridAdapter adapter;
    public static final int NUM_COLS = 4;

    //Gesture Detection
    protected ArrayList<AppObject> appList = new ArrayList<>();
    protected ArrayList<LockEntity> lockedList = new ArrayList<>();
    protected ArrayList<String> lockedPacks = new ArrayList<>();

    //Updating lock list
    private Handler handler;
    private Runnable run;
    private long nextUpdateTime = Long.MAX_VALUE;

    public AppGridFragment() {
        super(R.id.app_page_grid);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        vm = new ViewModelProvider(requireActivity()).get(AppGridViewModel.class);

        vm.getAppList().observe(this, new Observer<List<AppEntity>>() {
            @Override
            public void onChanged(List<AppEntity> appEntities) {
                appList = AppObject.Factory.toAppObjects(requireContext(), appEntities);
            }
        });

        vm.getLockedList().observe(this, new Observer<List<LockEntity>>() {
            @Override
            public void onChanged(List<LockEntity> lockEntities) {
                lockedList = new ArrayList<>(lockEntities);

                lockedPacks.clear();
                for (LockEntity l : lockedList) {
                    lockedPacks.add(l.appPackageName);
                    if (l.lockedUntil < nextUpdateTime)
                        nextUpdateTime = l.lockedUntil;
                }

                if (gv != null) {
                    if (adapter == null)
                        adapter = new AppGridAdapter(CONTEXT, R.id.app_page_grid, appList);
                    adapter.setLockedPackages(lockedPacks);
                    gv.setAdapter(adapter);
                }

                Log.d(TAG, "onChanged: " + lockedPacks);
            }
        });

        // Set a recurring task to update the locked_table database.
        handler = new Handler();
        run = new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: " + TimeHelper.now() + " --- " + nextUpdateTime);
                if (nextUpdateTime < TimeHelper.now()) {
                    Log.d(TAG, "run: LOCK LIST UPDATE");
                    updateLockedList();
                    LockStatusChangeListener.onLockStatusChanged();
                }
                handler.postDelayed(this, 20000); // Repeat every 30 seconds (30,000 ms).
            }
        };
        handler.post(run);
    }

    //All subclasses should override this method if only to initialize their adapter in their own way.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null)
            vm.initializeApps();

        CONTEXT = requireContext();

        HarmoniaGestureDetector.add(this);
        LockStatusChangeListener.add(this);
        PreferenceManager.getDefaultSharedPreferences(CONTEXT).registerOnSharedPreferenceChangeListener(this);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.app_grid_page, container, false);

        if (adapter == null)
            adapter = new AppGridAdapter(CONTEXT, R.id.app_page_grid, appList);
        adapter.setLockedPackages(lockedPacks);

        // Populate Grid Layout in home_screen.xml with instances of app.xml
        gv = v.findViewById(R.id.app_page_grid);
        gv.setAdapter(adapter);
        gv.setNumColumns(AppGridViewModel.NUM_COLS);
        gv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        updateLockedList();
    }

    @Override
    public void onStatusChanged() {
        if (adapter != null) {
            adapter.setLockedPackages(lockedPacks);
            gv.setAdapter(adapter);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        if (key.equalsIgnoreCase(CONTEXT.getResources().getString(R.string.set_locked_app_style_key))) {
            int style = Integer.parseInt(prefs.getString(getString(R.string.set_app_screen_style_key), STYLE_NORMAL + ""));
            if (style == STYLE_NORMAL)
                adapter.setLockMode(Integer.parseInt(prefs.getString(key, LOCK_MODE_GREYSCALE + "")));
            else if (style == STYLE_GREYSCALE) {
                prefs.edit().putInt(key, LOCK_MODE_INVISIBLE).commit(); // Do not change to .apply()
                adapter.setLockMode(Integer.parseInt(prefs.getString(key, LOCK_MODE_INVISIBLE + "")));
            }

            gv.setAdapter(adapter);
        }
        else if (key.equalsIgnoreCase(CONTEXT.getResources().getString(R.string.set_app_screen_style_key)))
        {
            int style = Integer.parseInt(prefs.getString(key, STYLE_NORMAL + ""));
            adapter.setStyle(style);
            gv.setAdapter(adapter);

            //When the total app theme is greyscale, we must not allow locked apps to be drawn in greyscale.
            //Therefore, we set lock mode to INVISIBLE in preferences.
            if (style == STYLE_GREYSCALE)
                prefs.edit().putInt(getString(R.string.set_locked_app_style_key), LOCK_MODE_INVISIBLE).apply();
        }
    }

    private void updateLockedList()
    {
        ArrayList<LockEntity> remove = new ArrayList<>();
        for (LockEntity le : lockedList)
            if (le.lockedUntil < TimeHelper.now())
                remove.add(le);
        lockedList.removeAll(remove);
        vm.overwriteLockList(lockedList);
    }
}
