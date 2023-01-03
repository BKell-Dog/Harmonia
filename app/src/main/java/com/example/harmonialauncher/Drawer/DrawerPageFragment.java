package com.example.harmonialauncher.Drawer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.core.view.GestureDetectorCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.harmonialauncher.R;
import com.example.harmonialauncher.AppObject;
import com.example.harmonialauncher.Config.ConfigManager;
import com.example.harmonialauncher.Util;
import com.example.harmonialauncher.lockManager.HarmoniaFragment;

import java.util.ArrayList;

// Purpose of this class: retrieve app data for all installed apps, and display app name as well as
// app icon (type Drawable) on the screen in a grid. Grid will not exceed 4 columns and 5 rows, and
// all app elements (icon + text) will be scaled into fixed dimensions as if the grid was full; apps
// will not resize as more space on the screen appears. When an app element is tapped, the app opens.
// When an app element is held down, options appear and the app may move to where the finger decides.
public class DrawerPageFragment extends HarmoniaFragment {
    private static final String TAG = "Drawer Page Fragment";
    private Context CONTEXT;
    private int pageNum;


    public DrawerPageFragment(int pageNum) {
        super(R.layout.drawer_page);
        this.pageNum = pageNum;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CONTEXT = getActivity().getApplicationContext();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.drawer_page, container, false);

        GridView gv = v.findViewById(R.id.drawer_page_grid);
        ArrayList<AppObject> appList = new ArrayList<AppObject>();
        ArrayList<AppObject> allApps = Util.loadAllApps(this);
        Log.d(TAG, "DRAWER PAGE " + pageNum);
        for (int k = pageNum * 20; k < (pageNum * 20) + 20; k++)
            try {
                appList.add(allApps.get(k));
            } catch (IndexOutOfBoundsException e) {break;} catch (Exception e) {e.printStackTrace();}
        gv.setAdapter(new DrawerGridAdapter(getContext(), R.layout.app, allApps));
        gv.setNumColumns(4);

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AppObject app = (AppObject) adapterView.getItemAtPosition(i);
                Log.d(TAG, app.toString() + " CLICKED");
                String pkg = app.getPackageName();
                if (pkg != null) {
                    Util.openApp(CONTEXT, pkg);
                } else if (app.getName().equalsIgnoreCase("Harmonia")) {
                    //Use Fragment Transaction to open settings fragment in ViewPager
                    //ViewPager2 vp = getActivity().findViewById(R.id.ViewPager);
                    //vp.setCurrentItem(1, true);
                }
            }
        });

        return v;
    }


    //Methods for determining window size
    private void setElementDimens(DrawerGridAdapter g, int numCols, View parentView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Rect bounds = ((Activity) getContext()).getWindowManager().getCurrentWindowMetrics().getBounds();
            int windowHeight = bounds.height();
            int windowWidth = bounds.width();
            int adjustedHeight = windowHeight - Util.getNavigationBarSize(CONTEXT).y;

            g.setElementDimen(adjustedHeight, windowWidth);
        }
    }

    public String toString()
    {return "Drawer Page Fragment #" + pageNum;}
}
