package com.example.harmonialauncher.HomeScreen;

import static com.example.harmonialauncher.MainActivity.THRESHOLD;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.harmonialauncher.AppGridPage;
import com.example.harmonialauncher.Drawer.DrawerGridAdapter;
import com.example.harmonialauncher.GestureDetection.HarmoniaGestureDetector;
import com.example.harmonialauncher.LockActivity.LockStatusChangeListener;
import com.example.harmonialauncher.MainActivity;
import com.example.harmonialauncher.R;
import com.example.harmonialauncher.AppObject;
import com.example.harmonialauncher.Config.ConfigManager;
import com.example.harmonialauncher.AppGridAdapter;
import com.example.harmonialauncher.PackageLoader;
import com.example.harmonialauncher.Settings.WhitelistManager;
import com.example.harmonialauncher.Util;
import com.example.harmonialauncher.lockManager.HarmoniaFragment;
import com.example.harmonialauncher.lockManager.LockManager;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;

/*
This class will manage the GridView which displays the apps, its construction and popualtion, and will
generate the apps to display as well. It will manage the default and preset packages, as well as the
pressing of buttons and opening of apps. This screen is the home screen and launcher.
 */

public class HomeScreenFragment extends AppGridPage implements LockStatusChangeListener.LockStatusListener {
    private final static String TAG = "Home Screen Fragment";

    public HomeScreenFragment() {
        super(R.layout.app_grid_page);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new HomeScreenGridAdapter(CONTEXT, R.layout.app, Util.loadAllApps(CONTEXT));
    }
        /*gv.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                switch (dragEvent.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        view.setVisibility(View.INVISIBLE);
                        break;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        break;
                    case DragEvent.ACTION_DRAG_LOCATION:
                        Point p = new Point((int) dragEvent.getX(), (int) dragEvent.getY());
                        for (int i = 0; i < gv.getChildCount(); i++) {
                            if (gv.getChildAt(i) != null) {
                                Point viewLoc = Util.getLocationOnScreen(gv.getChildAt(i));
                                Point viewBound = new Point(viewLoc.x + gv.getChildAt(i).getWidth(), viewLoc.y + gv.getChildAt(i).getHeight());
                                Rect bounds = new Rect(viewLoc.x, viewLoc.y, viewBound.x, viewBound.y);
                                if (bounds.contains(p.x, p.y)) {
                                    ((AppGridAdapter) gv.getAdapter()).swap();
                                }
                            }
                        }
                        break;
                }
                return true;
            }
        });*/

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY)
    {
        float e1y = event1.getY(), e2y = event2.getY();
        float e1x = event1.getX(), e2x = event2.getX();
        float xTranslation = e2x - e1x, yTranslation = e2y - e1y;

        if (Math.abs(yTranslation) > Math.abs(xTranslation)) //Fling more vertical than horizontal
            //Vertical flings will move between home screen and app drawer, sent to the viewpager in MainActivity.
            if (yTranslation < -THRESHOLD) //Upward fling
                ((MainActivity)getActivity()).setPage(1);
        return true;
    }
}