package com.example.harmonialauncher;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.GridView;

import com.example.harmonia.R;
import com.example.harmonialauncher.lockManager.HarmoniaFragment;

import java.util.ArrayList;

// Purpose of this class: retrieve app data for all installed apps, and display app name as well as
// app icon (type Drawable) on the screen in a grid. Grid will not exceed 4 columns and 5 rows, and
// all app elements (icon + text) will be scaled into fixed dimensions as if the grid was full; apps
// will not resize as more space on the screen appears. When an app element is tapped, the app opens.
// When an app element is held down, options appear and the app may move to where the finger decides.
public class DrawerPageFragment extends HarmoniaFragment {
    private Context CONTEXT;
    public DrawerPageFragment()
    {
        super(R.layout.drawer_page);
        GridView gv = (GridView) getActivity().findViewById(R.id.drawer_page_grid);
        gv.setAdapter(new DrawerGridAdapter(getContext(), R.layout.app, Util.loadAllApps()));
        gv.setNumColumns(4);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CONTEXT = getActivity().getApplicationContext();
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
}
