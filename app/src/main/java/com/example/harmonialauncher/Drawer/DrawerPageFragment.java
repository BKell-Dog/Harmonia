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
public class DrawerPageFragment extends HarmoniaFragment implements LockStatusChangeListener.LockStatusListener {
    private static final String TAG = "Drawer Page Fragment";
    private Context CONTEXT;
    private int pageNum;
    private int numCols = 4;
    private GridView gv = null;

    public DrawerPageFragment(int pageNum) {
        super(R.layout.app_grid_page);
        this.pageNum = pageNum;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CONTEXT = getActivity();

        HarmoniaGestureDetector.add(this);
        LockStatusChangeListener.add(this);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.app_grid_page, container, false);

        gv = v.findViewById(R.id.app_page_grid);
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
        gv.setAdapter(new DrawerGridAdapter(getContext(), R.layout.app, appList));
        gv.setNumColumns(numCols);

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AppObject app = (AppObject) adapterView.getItemAtPosition(i);
                Log.d(TAG, app.toString() + " CLICKED");
                String pkg = app.getPackageName();
                if (pkg != null) {
                    Util.openApp(CONTEXT, pkg);
                }
            }
        });

        return v;
    }

    public void onStatusChanged() {
        gv.setAdapter(new DrawerGridAdapter(getContext(), R.layout.app, getAppList()));
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        //Check if view is created
        if (gv == null || !onScreen)
            return false;

        for (int i = 0; i < ((AppGridAdapter) gv.getAdapter()).getCount(); i++) {
            View v = gv.getChildAt(i);
            AppObject app = ((AppGridAdapter) gv.getAdapter()).getItem(i);
            Point coords = Util.getLocationOnScreen(v);
            Rect bounds = new Rect(coords.x, coords.y, coords.x + v.getWidth(), coords.y + v.getHeight());
            if (bounds.contains((int) e.getX(), (int) e.getY())) {           //Check that tap coords were on top of app view
                if (!LockManager.isLocked(app.getPackageName())) {         //Check that app is not locked
                    Util.openApp(this.CONTEXT, app.getPackageName());
                    return true;
                }
            }
        }
        return false;
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

    public void setOnScreen()
    {onScreen = true;}
    public void setOffScreen()
    {onScreen = false;}

    public String toString() {
        return "Drawer Page Fragment #" + pageNum;
    }
}
