package com.example.harmonialauncher.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.core.view.GestureDetectorCompat;

import com.example.harmonialauncher.Adapters.AppGridAdapter;
import com.example.harmonialauncher.Helpers.AppObject;
import com.example.harmonialauncher.Utils.ConfigManager;
import com.example.harmonialauncher.Utils.HarmoniaGestureDetector;
import com.example.harmonialauncher.Utils.LockStatusChangeListener;
import com.example.harmonialauncher.R;
import com.example.harmonialauncher.Utils.Util;
import com.example.harmonialauncher.Utils.LockManager;

public class AppGridPage extends HarmoniaFragment implements LockStatusChangeListener.LockStatusListener {

    public static final int NUM_COLS = 4;
    private static final String TAG = "App Grid Page";
    protected Context CONTEXT;
    //Drawing Grid
    protected GridView gv;
    protected AppGridAdapter adapter;
    //Gesture Detection
    protected GestureDetectorCompat gd;

    public AppGridPage(int resource) {
        super(resource);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CONTEXT = getActivity();

        //All subclasses should override this method if only to initialize their adapter in their own way.

        gd = new GestureDetectorCompat(this.getActivity(), new HarmoniaGestureDetector());
        HarmoniaGestureDetector.add(this);
        LockStatusChangeListener.add(this);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate layout for this fragment
        View v = inflater.inflate(R.layout.app_grid_page, container, false);

        //Populate Grid Layout in home_screen.xml with instances of app.xml
        //Get grid view
        gv = v.findViewById(R.id.app_page_grid);

        if (adapter == null && getActivity() != null) {
            adapter = new AppGridAdapter(CONTEXT, R.layout.app, Util.loadFirstTwentyApps(this.getActivity()));
        }

        gv.setAdapter(adapter);
        gv.setNumColumns(NUM_COLS);
        setElementDimens(adapter, NUM_COLS, v);


        gv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gd.onTouchEvent(motionEvent);
            }
        });

        return v;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            for (int i = 0; i < gv.getChildCount(); i++) {
                if (gv.getChildAt(i) != null) {
                    Rect bounds = Util.getViewBounds(gv.getChildAt(i));
                    if (bounds.contains((int) e.getX(), (int) e.getY())) {
                        ClipData data = ClipData.newPlainText("", "");
                        View.DragShadowBuilder shadow = new View.DragShadowBuilder(gv.getChildAt(i));
                        gv.startDragAndDrop(data, shadow, gv.getChildAt(i), 0);
                        gv.getChildAt(i).setVisibility(View.INVISIBLE);
                    }
                }
            }
        }
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        //Check if view is created
        if (gv == null || !onScreen)
            return false;

        for (int i = 0; i < ((AppGridAdapter) gv.getAdapter()).getCount(); i++) {
            View v = gv.getChildAt(i);
            if (v != null) {
                Point coords = Util.getLocationOnScreen(v);
                Rect bounds = new Rect(coords.x, coords.y, coords.x + v.getWidth(), coords.y + v.getHeight());
                if (bounds.contains((int) e.getX(), (int) e.getY())) {
                    AppGridAdapter a = (AppGridAdapter) gv.getAdapter();
                    AppObject app = a.get(i);
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

    //Methods for determining window size -----------------------------------------------------
    protected void setElementDimens(AppGridAdapter g, int numCols, View parentView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Rect bounds = ((Activity) getContext()).getWindowManager().getCurrentWindowMetrics().getBounds();
            int windowHeight = bounds.height();
            int windowWidth = bounds.width();
            int adjustedHeight = windowHeight - Util.getNavigationBarSize(CONTEXT).y;

            g.setElementDimen(adjustedHeight, windowWidth);
        }
    }
}
