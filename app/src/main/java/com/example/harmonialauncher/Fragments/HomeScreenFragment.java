package com.example.harmonialauncher.Fragments;

import android.content.ClipData;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.example.harmonialauncher.Adapters.HomeScreenGridAdapter;
import com.example.harmonialauncher.Listeners.LockStatusChangeListener;
import com.example.harmonialauncher.R;
import com.example.harmonialauncher.Utils.Util;
import com.example.harmonialauncher.ViewModels.HomeScreenViewModel;

/*
This class will manage the GridView which displays the apps, its construction and popualtion, and will
generate the apps to display as well. It will manage the default and preset packages, as well as the
pressing of buttons and opening of apps. This screen is the home screen and launcher.
 */

public class HomeScreenFragment extends AppGridPage implements LockStatusChangeListener.LockStatusListener {
    private final static String TAG = "Home Screen Fragment";
    private HomeScreenViewModel vm;

    public HomeScreenFragment() {
        super(R.layout.app_grid_page);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        vm = new ViewModelProvider(requireActivity()).get(HomeScreenViewModel.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new HomeScreenGridAdapter(CONTEXT, R.layout.app, vm.getAppList());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.d(TAG, "onTouch: REGISTERED");
                return false;
            }
        });

        Log.d(TAG, "onCreateView: ");

        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        vm.writeAppsToFile();
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
    public void onLongPress(MotionEvent event) {
        View tappedView = Util.findChildAt(gv, (int) event.getX(), (int) event.getY());
        if (tappedView != null) {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(tappedView);
            tappedView.startDrag(data, shadowBuilder, gv, 0);
            tappedView.setVisibility(View.INVISIBLE);
        }
    }
}