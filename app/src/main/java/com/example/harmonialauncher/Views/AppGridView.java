package com.example.harmonialauncher.Views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.ListAdapter;

import com.example.harmonialauncher.Adapters.AppGridAdapter;
import com.example.harmonialauncher.Helpers.TimeHelper;
import com.example.harmonialauncher.Interfaces.AppHolder;

public class AppGridView extends GridView implements AppHolder {
    public static final String TAG = AppGridView.class.getSimpleName();

    public final int BOX_TOLERANCE_X = 100, BOX_TOLERANCE_Y = 300;
    public static final long SWAP_TIMEOUT = 500;
    private long nextSwap = 0;

    public AppGridView(Context context) {
        super(context);
    }

    public AppGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AppGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
                                int originalIndex = getChildByView(originalView);
                                swap(originalIndex, i);
                                nextSwap = TimeHelper.now() + SWAP_TIMEOUT;
                                Log.d(TAG, "onDragEvent: " + originalView.getText());
                                child.setVisibility(View.INVISIBLE);
                                originalView.setVisibility(View.VISIBLE);
                                Log.d(TAG, "onDragEvent: SWAP LEFT " + originalIndex + " -- " + i);
                                return true;
                            } else if (rightBound.contains(loc.x, loc.y)) {
                                //Swap Right
                                int originalIndex = getChildByView(originalView);
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
                AppGridAdapter adapter = (AppGridAdapter)getAdapter();
                adapter.setDragInvisible(-1);
                setAdapter(getAdapter());

                //Set drag shadow animation

                return true;
            default:
                return false;
        }
    }

    public int getChildByView(AppView view) {
        for (int i = 0; i < getChildCount(); i++) {
            AppView view2 = (AppView) getChildAt(i);
            if (view2.getText().equalsIgnoreCase(view.getText()))
                return i;
        }
        return -1;
    }

    public void setAdapter(ListAdapter adapter) {
        if (adapter instanceof AppGridAdapter)
            super.setAdapter(adapter);
    }

    @Override
    public void swap(int a, int b) {
        AppGridAdapter adapter = (AppGridAdapter) getAdapter();
        adapter.swap(a, b);
        adapter.setDragInvisible(b);
        setAdapter(adapter);
        invalidateViews();
    }
}
