package com.example.harmonialauncher.AppGrid.Views;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;

import androidx.preference.PreferenceManager;

import com.example.harmonialauncher.Adapters.AppListAdapter;
import com.example.harmonialauncher.AppGrid.AppGridAdapter;
import com.example.harmonialauncher.AppGrid.AppHolder;
import com.example.harmonialauncher.Helpers.TimeHelper;
import com.example.harmonialauncher.R;
import com.example.harmonialauncher.Utils.Util;

public class AppGridView extends GridView implements AppHolder, SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String TAG = AppGridView.class.getSimpleName();

    /**
     * These variables designate the tolerances for the areas which the drag event must enter in
     * order to perform a swap of cells. Once a drag shadow enters a cell, these boxes are calculated
     * on the left and right of the cell, centered along Y and on the far left and far right.
     */
    public final int BOX_TOLERANCE_X = 100, BOX_TOLERANCE_Y = 300;
    public static final long SWAP_TIMEOUT = 500;
    private long nextSwap = 0;

    public AppGridView(Context context) {
        super(context);
        PreferenceManager.getDefaultSharedPreferences(context).registerOnSharedPreferenceChangeListener(this);
    }

    public AppGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        PreferenceManager.getDefaultSharedPreferences(context).registerOnSharedPreferenceChangeListener(this);
    }

    public AppGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        PreferenceManager.getDefaultSharedPreferences(context).registerOnSharedPreferenceChangeListener(this);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AppGridView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onDragEvent(DragEvent event) {
        int action = event.getAction();
        AppView originalView = (AppView) event.getLocalState();
        switch (action) {
            case DragEvent.ACTION_DRAG_STARTED:
                originalView.setVisibility(View.INVISIBLE);
                return true;
            case DragEvent.ACTION_DRAG_LOCATION:
                originalView.setVisibility(View.INVISIBLE);
                Point loc = new Point((int) event.getX(), (int) event.getY());
                if (TimeHelper.now() > nextSwap) {
                    //Iterate through all grid children
                    for (int i = 0; i < getChildCount(); i++) {
                        AppView child = (AppView) getChildAt(i);
                        Rect bounds = new Rect((int) child.getX(), (int) child.getY(), (int) child.getX() + child.getWidth(), (int) child.getY() + child.getHeight());
                        if (child.getVisibility() == View.VISIBLE && bounds.contains(loc.x, loc.y)) {

                            //Child view contains touch point
                            int l = (int) child.getX(), t = (int) child.getY(), r = l + child.getWidth(), b = t + child.getHeight();
                            int midY = t + ((b - t) / 2);

                            //Define special boundaries at edge of app view.
                            Rect leftBound = new Rect(l, midY - BOX_TOLERANCE_Y, l + BOX_TOLERANCE_X, midY + BOX_TOLERANCE_Y);
                            Rect rightBound = new Rect(r - BOX_TOLERANCE_X, midY - BOX_TOLERANCE_Y, r, midY + BOX_TOLERANCE_Y);

                            if (leftBound.contains(loc.x, loc.y)) {
                                //Swap Left
                                int originalIndex = getChildIndexByView(originalView);
                                swap(originalIndex, i);
                                nextSwap = TimeHelper.now() + SWAP_TIMEOUT;
                                Log.d(TAG, "onDragEvent: " + originalView.getText());
                                child.setVisibility(View.INVISIBLE);
                                originalView.setVisibility(View.VISIBLE);
                                Log.d(TAG, "onDragEvent: SWAP LEFT " + originalIndex + " -- " + i);
                                return true;
                            } else if (rightBound.contains(loc.x, loc.y)) {
                                //Swap Right
                                int originalIndex = getChildIndexByView(originalView);
                                swap(originalIndex, i);
                                nextSwap = TimeHelper.now() + SWAP_TIMEOUT;
                                child.setVisibility(View.INVISIBLE);
                                originalView.setVisibility(View.VISIBLE);
                                Log.d(TAG, "onDragEvent: SWAP RIGHT " + originalIndex + " -- " + i);
                                return true;
                            }
                        }
                    }
                }
                return false;
            case DragEvent.ACTION_DRAG_ENDED:
                nextSwap = 0;
                //Set original view to be visible
                originalView.setVisibility(View.VISIBLE);
                AppGridAdapter adapter = getAdapter();
                adapter.setDragInvisible(-1);
                setAdapter(getAdapter());

                //Set drag shadow animation

                return true;
            default:
                return false;
        }
    }

    public int getChildIndexByView(AppView view) {
        for (int i = 0; i < getChildCount(); i++) {
            AppView view2 = (AppView) getChildAt(i);
            if (view2.getText().equalsIgnoreCase(view.getText()))
                return i;
        }
        return -1;
    }

    public AppGridAdapter getAdapter() {
        return (AppGridAdapter) super.getAdapter();
    }

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH)
    {
        super.onSizeChanged(w, h, oldW, oldH);
        getAdapter().setDimensions(w, h);
    }

    public void setAdapter(ListAdapter adapter) {
        if (adapter instanceof AppGridAdapter)
            super.setAdapter(adapter);
    }

    @Override
    public void swap(int a, int b) {
        AppGridAdapter adapter = getAdapter();
        if (Math.abs(a - b) < 2) {
            adapter.swap(a, b);
            adapter.setDragInvisible(b);
        } else {
            if (a > b) {
                for (int i = a; i > b; i--)
                    adapter.swap(i, i - 1);
                adapter.setDragInvisible(b);
            } else {
                for (int i = a; i < b; i++)
                    adapter.swap(i, i + 1);
                adapter.setDragInvisible(b);
            }
        }
        setAdapter(adapter);
        invalidateViews();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equalsIgnoreCase(getContext().getResources().getString(R.string.set_app_screen_style_key)))
        {
            for (int i = 0; i < getChildCount(); i++)
                getChildAt(i).invalidate();
        }
    }
}
