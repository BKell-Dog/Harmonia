package com.example.harmonialauncher.Fragments;

import static com.example.harmonialauncher.MainActivity.THRESHOLD;

import android.os.Bundle;
import android.view.MotionEvent;

import com.example.harmonialauncher.Helpers.AppObject;
import com.example.harmonialauncher.Utils.ConfigManager;
import com.example.harmonialauncher.Adapters.HomeScreenGridAdapter;
import com.example.harmonialauncher.Utils.LockStatusChangeListener;
import com.example.harmonialauncher.MainActivity;
import com.example.harmonialauncher.R;
import com.example.harmonialauncher.Utils.Util;

import java.util.ArrayList;

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

        ArrayList<AppObject> receivedApps = ConfigManager.readHomeAppOrderFromFile(this.getActivity());
        if (receivedApps != null)
            adapter = new HomeScreenGridAdapter(CONTEXT, R.layout.app, receivedApps);
        else
            adapter = new HomeScreenGridAdapter(CONTEXT, R.layout.app, Util.loadFirstTwentyApps(this.getActivity()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        ConfigManager.writeHomeAppsToFile(this.getActivity(), adapter.getAppList());
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
    public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
        float e1y = event1.getY(), e2y = event2.getY();
        float e1x = event1.getX(), e2x = event2.getX();
        float xTranslation = e2x - e1x, yTranslation = e2y - e1y;

        if (Math.abs(yTranslation) > Math.abs(xTranslation)) //Fling more vertical than horizontal
            //Vertical flings will move between home screen and app drawer, sent to the viewpager in MainActivity.
            if (yTranslation < -THRESHOLD) //Upward fling
                ((MainActivity) getActivity()).setPage(1);
        return true;
    }
}