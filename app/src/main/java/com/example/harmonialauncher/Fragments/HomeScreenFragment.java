package com.example.harmonialauncher.Fragments;

import static com.example.harmonialauncher.MainActivity.THRESHOLD;

import android.content.ClipData;
import android.graphics.Color;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.harmonialauncher.Adapters.AppGridAdapter;
import com.example.harmonialauncher.Helpers.AppObject;
import com.example.harmonialauncher.Utils.ConfigManager;
import com.example.harmonialauncher.Adapters.HomeScreenGridAdapter;
import com.example.harmonialauncher.Utils.HarmoniaGestureDetector;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        gv.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                int action = event.getAction();
                switch (action) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        // handle drag started
                        break;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        // get the view that was entered
                        View enteredView = (View) event.getLocalState();
                        // change the background color of the entered view
                        v.setBackgroundColor(Color.GREEN);
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        // get the view that was exited
                        View exitedView = (View) event.getLocalState();
                        // change the background color of the exited view back to its original color
                        v.setBackgroundColor(Color.RED);
                        break;
                    case DragEvent.ACTION_DROP:
                        // update app grid
                        // get the view that was dropped
                        View droppedView = (View) event.getLocalState();
                        // get the container view that the view was dropped on
                        ViewGroup container = (ViewGroup) v;
                        // get the index of the dropped view within the container
                        int index = container.indexOfChild(droppedView);
                        // get the index of the view that was previously in the dropped view's position
                        int oldIndex = gv.indexOfChild(droppedView);
                        // remove the dropped view from its original position
                        gv.removeView(droppedView);
                        // add the dropped view to its new position
                        gv.addView(droppedView, index);
                        // update app grid array
                        adapter.swap(oldIndex, index);
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                        // handle drag ended
                        break;
                    default:
                }
                return true;
            }
        });

        return v;
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

    @Override
    public void onLongPress(MotionEvent event)
    {
        View tappedView = Util.findChildAt(gv, (int) event.getX(), (int) event.getY());
        if (tappedView != null) {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(tappedView);
            tappedView.startDrag(data, shadowBuilder, gv, 0);
            tappedView.setVisibility(View.INVISIBLE);
        }
    }
}