package com.example.harmonialauncher.lock;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowInsets;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.harmonialauncher.Activities.HarmoniaActivity;
import com.example.harmonialauncher.appgrid.AppObject;
import com.example.harmonialauncher.Helpers.TimeHelper;
import com.example.harmonialauncher.R;
import com.example.harmonialauncher.Utils.Util;
import com.example.harmonialauncher.database.AppEntity;

import java.util.ArrayList;
import java.util.List;



//TODO: implement the above schema

public class LockActivity extends HarmoniaActivity {

    private static final String TAG = LockActivity.class.getSimpleName();
    private Context CONTEXT;
    private LockActivityViewModel vm;
    private ArrayList<AppObject> appList = new ArrayList<>();
    private final String[] fragmentTags = new String[] {"Lock Media Picker", "Lock App Picker", "Lock Website Picker"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lock_activity);
        CONTEXT = this;

        getSupportFragmentManager().beginTransaction()
                .add(R.id.lock_activity_frame_layout, LockMediaPicker.class, null, fragmentTags[0])
                .commit();

        LockMediaPicker lmp = (LockMediaPicker) getSupportFragmentManager().findFragmentByTag(fragmentTags[0]);

        vm = new ViewModelProvider(this).get(LockActivityViewModel.class);
        vm.getAllApps().observe(this, new Observer<List<AppEntity>>() {
            @Override
            public void onChanged(List<AppEntity> appEntities) {
                appList.clear();
                for (AppEntity a : appEntities)
                    appList.add(AppObject.Factory.toAppObject(CONTEXT, a));
                LockAppPicker lap = (LockAppPicker) getSupportFragmentManager().findFragmentByTag(fragmentTags[1]);
                if (lap != null)
                    lap.setAppList(appList);
            }
        });
    }

    public void moveToAppPicker()
    {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.lock_activity_frame_layout, LockAppPicker.class, null, fragmentTags[1])
                .setReorderingAllowed(true)
                .addToBackStack(fragmentTags[1])
                .commit();
        LockAppPicker lap = (LockAppPicker) getSupportFragmentManager().findFragmentByTag(fragmentTags[1]);
        Log.d(TAG, "moveToAppPicker: lap == null: " + (lap == null) + " AppList == null: " + (appList == null));
        if (lap != null)
            lap.setAppList(appList);
    }

    public void onDestroy() {
        super.onDestroy();
        LockStatusChangeListener.onStatusChanged();
    }
}
