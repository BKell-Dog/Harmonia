package com.example.harmonialauncher.appgrid.views;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.ListAdapter;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import com.example.harmonialauncher.Utils.Util;
import com.example.harmonialauncher.appgrid.AppGridAdapter;
import com.example.harmonialauncher.appgrid.AppHolder;
import com.example.harmonialauncher.Helpers.TimeHelper;
import com.example.harmonialauncher.R;
import com.example.harmonialauncher.gesture.LongPressDetector;
import com.example.harmonialauncher.lock.LockStatusChangeListener;

import java.util.ArrayList;

public class AppGridView extends GridView implements AppHolder,
        SharedPreferences.OnSharedPreferenceChangeListener,
        LongPressDetector.LongPressCallback {
    public static final String TAG = AppGridView.class.getSimpleName();

    private LongPressDetector lpd;

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
        initialize();
    }

    public AppGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        PreferenceManager.getDefaultSharedPreferences(context).registerOnSharedPreferenceChangeListener(this);
        initialize();
    }

    public AppGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        PreferenceManager.getDefaultSharedPreferences(context).registerOnSharedPreferenceChangeListener(this);
        initialize();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AppGridView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        PreferenceManager.getDefaultSharedPreferences(context).registerOnSharedPreferenceChangeListener(this);
        initialize();
    }

    private void initialize()
    {
        lpd = new LongPressDetector(getContext(), this);
    }

    @Override
    public boolean onDragEvent(DragEvent event) {
        int action = event.getAction();
        AppView originalView = (AppView) event.getLocalState();
        switch (action) {
            case DragEvent.ACTION_DRAG_LOCATION:
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
            case DragEvent.ACTION_DROP:
                //Set the drag shadow to land at the drop point, not fly back to the initial point.
                float x = event.getX(), y = event.getY();
                originalView.setX(x - (originalView.getWidth() / 2f));
                originalView.setY(y - (originalView.getHeight() / 2f));
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                nextSwap = 0;
                //Set original view to be visible
                originalView.setVisibility(View.VISIBLE);
                AppGridAdapter adapter = getAdapter();
                adapter.setDragInvisible(-1);
                setAdapter(getAdapter());
                break;
        }
        return true;
    }

    public boolean onInterceptTouchEvent(MotionEvent event)
    {
        onTouchEvent(event);
        return super.onInterceptTouchEvent(event);
    }
    
    public boolean onLongPress(MotionEvent event)
    {
        Log.d(TAG, "BEGIN DRAG NOW");
        int position = pointToPosition((int)event.getX(), (int)event.getY());
        View view = getChildAt(position);
        if (view != null) {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                view.startDragAndDrop(data, shadowBuilder, view, 0);
            else
                view.startDrag(data, shadowBuilder, view, 0);
            view.setVisibility(View.INVISIBLE);
        }
        return true;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        boolean longPress = lpd.onTouch(null, event);
        if (longPress)
            lpd.cleanUp();
        return true;
    }

    public int getChildIndexByView(AppView view) {
        for (int i = 0; i < getChildCount(); i++) {
            AppView view2 = (AppView) getChildAt(i);
            if (view2.getText().equalsIgnoreCase(view.getText()))
                return i;
        }
        return -1;
    }
    
    public View getChildByLocation(int x, int y)
    {
        for (int i = 0; i < getChildCount(); i++)
        {
            View child = getChildAt(i);
            Rect bound = new Rect((int)child.getX(), (int)child.getY(), (int)child.getX() + child.getWidth(), (int)child.getY() + child.getHeight());
            if (bound.contains(x, y))
                return child;
        }
        return null;
    }

    public AppView getChildByName(@NonNull String name)
    {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child instanceof AppView)
            {
                AppView av = (AppView) child;
                if (av.getAppName().equalsIgnoreCase(name))
                    return av;
            }
        }
        return null;
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

    public void reload()
    {
        setAdapter(getAdapter());
        invalidate();
    }

    public void setAdapter(ListAdapter adapter) {
        if (adapter instanceof AppGridAdapter) {
            super.setAdapter(adapter);
        }
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
