package com.example.harmonialauncher.HomeScreen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.harmonialauncher.R;
import com.example.harmonialauncher.AppObject;
import com.example.harmonialauncher.Config.ConfigManager;
import com.example.harmonialauncher.AppGridAdapter;
import com.example.harmonialauncher.PackageLoader;
import com.example.harmonialauncher.Settings.WhitelistManager;
import com.example.harmonialauncher.Util;
import com.example.harmonialauncher.lockManager.LockManager;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;

/*
This class will manage the GridView which displays the apps, its construction and popualtion, and will
generate the apps to display as well. It will manage the default and preset packages, as well as the
pressing of buttons and opening of apps. This screen is the home screen and launcher.
 */

public class HomeScreenFragment extends Fragment {
    private final static String TAG = "Home Screen Fragment";
    private Context CONTEXT;

    GridView gv;
    private int numCols = 3;

    //App Name Constants
    private static final String DIALER = "Dialer";
    private static final String MESSENGER = "Messenger";

    public HomeScreenFragment() {
        super(R.layout.home_screen);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CONTEXT = getActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout for this fragment
        View v = inflater.inflate(R.layout.home_screen, container, false);

        //Populate Grid Layout in home_screen.xml with instances of app.xml
        //Get grid view
        gv = (GridView) v.findViewById(R.id.home_screen_grid);

        //Set adapter, set element dimensions for proper scaling, and add a specific app for Harmonia Settings
        HomeScreenGridAdapter ga = new HomeScreenGridAdapter(CONTEXT, R.layout.app, Util.loadAllApps(this));
        AppObject HarmoniaSettings = new AppObject(null, "Harmonia", R.drawable.harmonia_icon, false);
        ga.add(HarmoniaSettings);
        setElementDimens(ga, numCols, v);

        gv.setAdapter(ga);
        gv.setNumColumns(numCols);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppObject app = (AppObject) parent.getItemAtPosition(position);
                Log.d(TAG, app.toString() + " CLICKED");
                String pkg = app.getPackageName();
                if (pkg != null) {
                    if (ConfigManager.getMode().equalsIgnoreCase(ConfigManager.ELDERLY) && app.getName().equalsIgnoreCase(DIALER)) {
                        Intent i = new Intent(Intent.ACTION_DIAL);
                        startActivity(i);
                    } else
                        Util.openApp(CONTEXT, pkg);
                } else if (app.getName().equalsIgnoreCase("Harmonia")) {
                    //Use Fragment Transaction to open settings fragment in ViewPager
                    ViewPager2 vp = getActivity().findViewById(R.id.ViewPager);
                    vp.setCurrentItem(1, true);
                }
            }
        });
        return v;
    }

    //Methods for determining window size -----------------------------------------------------
    private void setElementDimens(AppGridAdapter g, int numCols, View parentView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Rect bounds = ((Activity) getContext()).getWindowManager().getCurrentWindowMetrics().getBounds();
            int windowHeight = bounds.height();
            int windowWidth = bounds.width();
            int adjustedHeight = windowHeight - Util.getNavigationBarSize(CONTEXT).y;

            g.setElementDimen(adjustedHeight, windowWidth);
        }
    }
}