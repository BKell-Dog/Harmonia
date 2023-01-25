package com.example.harmonialauncher.Fragments;

import static com.example.harmonialauncher.MainActivity.THRESHOLD;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.example.harmonialauncher.Adapters.AppGridAdapter;
import com.example.harmonialauncher.Helpers.AppObject;
import com.example.harmonialauncher.Utils.ConfigManager;
import com.example.harmonialauncher.Adapters.HomeScreenGridAdapter;
import com.example.harmonialauncher.Utils.HarmoniaGestureDetector;
import com.example.harmonialauncher.Utils.LockStatusChangeListener;
import com.example.harmonialauncher.MainActivity;
import com.example.harmonialauncher.R;
import com.example.harmonialauncher.Utils.Util;
import com.example.harmonialauncher.ViewModels.HomeScreenViewModel;

import java.util.ArrayList;

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

        /*v.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                int action = dragEvent.getAction();


                //originalView is the original app object that was long tapped.
                //view is the view which is not underneath the drag shadow.
                View originalView = (View) dragEvent.getLocalState();
                int index1 = -1, index2 = -1;
                for (int i = 0; i < gv.getChildCount(); i++)
                    if (gv.getChildAt(i).equals(view))
                        index1 = i;
                    else if (gv.getChildAt(i).equals(originalView))
                        index2 = i;
                Log.d(TAG, "ON DRAG " + index1 + ", " + index2);


                switch (action) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        // handle drag started
                        break;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        Log.d(TAG, "ON ACTION DRAG ENTERED");

                        if (Math.abs(index1 - index2) == 1)
                        {
                            adapter.swap(index1, index2);
                            gv.setAdapter(new HomeScreenGridAdapter(CONTEXT, R.layout.app, adapter.getAppList()));
                        }
                        else if (index2 < index1)
                        {
                            for (int i = index1; i > index2; i--)
                            {
                                adapter.swap(i, i - 1);
                            }
                        }
                        else if (index2 > index1)
                        {
                            for (int i = index1; i < index2; i++)
                            {
                                adapter.swap(i, i + 1);
                            }
                        }
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        // get the view that was exited
                        View exitedView = (View) dragEvent.getLocalState();
                        // change the background color of the exited view back to its original color
                        view.setBackgroundColor(Color.RED);
                        break;
                    case DragEvent.ACTION_DROP:

                        break;
                    case DragEvent.ACTION_DRAG_LOCATION:
                        Log.d(TAG, "ON DRAG LOCATION");

                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                        originalView.setVisibility(View.VISIBLE);
                        break;
                    default:
                }
                return true;
            }
        });*/

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
    public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
        if (event1 == null || event2 == null)
            return false;

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