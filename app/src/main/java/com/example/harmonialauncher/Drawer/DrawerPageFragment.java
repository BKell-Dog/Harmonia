package com.example.harmonialauncher.Drawer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GestureDetectorCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.harmonialauncher.AppGridAdapter;
import com.example.harmonialauncher.AppGridPage;
import com.example.harmonialauncher.GestureDetection.HarmoniaGestureDetector;
import com.example.harmonialauncher.LockActivity.LockStatusChangeListener;
import com.example.harmonialauncher.R;
import com.example.harmonialauncher.AppObject;
import com.example.harmonialauncher.Config.ConfigManager;
import com.example.harmonialauncher.Util;
import com.example.harmonialauncher.lockManager.HarmoniaFragment;
import com.example.harmonialauncher.lockManager.LockManager;

import java.util.ArrayList;

// Purpose of this class: retrieve app data for all installed apps, and display app name as well as
// app icon (type Drawable) on the screen in a grid. Grid will not exceed 4 columns and 5 rows, and
// all app elements (icon + text) will be scaled into fixed dimensions as if the grid was full; apps
// will not resize as more space on the screen appears. When an app element is tapped, the app opens.
// When an app element is held down, options appear and the app may move to where the finger decides.
public class DrawerPageFragment extends AppGridPage implements LockStatusChangeListener.LockStatusListener {
    private static final String TAG = "Drawer Page Fragment";
    private int pageNum;

    public DrawerPageFragment(int pageNum) {
        super(R.layout.app_grid_page);
        this.pageNum = pageNum;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new DrawerGridAdapter(CONTEXT, R.layout.app, getAppList());
    }

    private ArrayList<AppObject> getAppList()
    {
        ArrayList<AppObject> appList = new ArrayList<AppObject>();
        ArrayList<AppObject> allApps = Util.loadAllApps(this);
        for (int k = pageNum * 20; k < (pageNum * 20) + 20; k++)
            try {
                appList.add(allApps.get(k));
            } catch (IndexOutOfBoundsException e) {
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        return appList;
    }

    public String toString() {
        return "Drawer Page Fragment #" + pageNum;
    }
}
