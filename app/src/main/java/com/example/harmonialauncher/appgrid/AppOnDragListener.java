package com.example.harmonialauncher.appgrid;

import android.graphics.Rect;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.harmonialauncher.appgrid.Views.AppView;

public class AppOnDragListener implements View.OnDragListener {
    private static final String TAG = AppOnDragListener.class.getSimpleName();
    private AppHolder callback;
    private boolean considerEvent = false;
    private int boxToleranceY = 100, boxToleranceX = 100;
    private Rect leftArea, rightArea;

    public AppOnDragListener(AppHolder callback)
    {
        this.callback = callback;
    }

    @Override
    public boolean onDrag(View view, DragEvent dragEvent) {
        int action = dragEvent.getAction();

        //originalView is the original app object that was long pressed.
        //view is the view which is now underneath the drag shadow.
        final AppView originalView = (AppView) dragEvent.getLocalState();
        AppView overView = null;
        try{
            overView = (AppView) view;
        } catch (Exception e) {e.printStackTrace(); return false;}

        //We must now extract the app names from the TextViews within the app.xml layout.
        //If any changes are made to this file they must be reflected here.
        //Currently the hierarchy goes:
        //LinearLayout -> LinearLayout (index 1) -> TextView (index 0)
        if (!((String)originalView.getTag()).equalsIgnoreCase("app_layout") || !((String)view.getTag()).equalsIgnoreCase("app_layout"))
            return false;

        switch (action) {
            case DragEvent.ACTION_DRAG_STARTED:
                // handle drag started
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                Log.d(TAG, "ON ACTION DRAG ENTERED");
                considerEvent = true;

                // Define region where the shadow must enter in order to initiate a swap.
                float l = overView.getX(), t = overView.getY(), r = l + overView.getMeasuredWidth(), b = t + overView.getMeasuredHeight();
                float centerY = (b + t) / 2;
                leftArea = new Rect((int) l, (int) centerY - boxToleranceY, (int) l + boxToleranceX, (int) centerY + boxToleranceY);
                rightArea = new Rect((int) r - boxToleranceX, (int) centerY - boxToleranceY, (int) r, (int) centerY + boxToleranceY);
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                considerEvent = false;
                leftArea = null;
                rightArea = null;
                break;
            case DragEvent.ACTION_DROP:
                Log.d(TAG, "ACTION DROP");
                break;
            case DragEvent.ACTION_DRAG_LOCATION:
                Rect shadowArea = new Rect((int) dragEvent.getX(), (int) dragEvent.getY(), (int) dragEvent.getX() + originalView.getMeasuredWidth(), (int) dragEvent.getY() + originalView.getMeasuredHeight());
                ViewGroup parent = (ViewGroup) overView.getParent();
                Log.d(TAG, "onDrag: Right Rect: " + rightArea + " Left Rect: " + leftArea + " ShadowRect: " + shadowArea);
                Log.d(TAG, "onDrag: " + rightArea.intersect(shadowArea));
                if (callback != null && rightArea.intersect(shadowArea))
                {
                    int index = parent.indexOfChild(overView);
                    if (index < parent.getChildCount() - 1)
                    {
                        int originalIndex = parent.indexOfChild(originalView);
                        callback.swap(index, originalIndex);
                    }
                }
                else if (leftArea.intersect(shadowArea))
                {
                    int index = parent.indexOfChild(overView);
                    if (index < 0)
                    {
                        int originalIndex = parent.indexOfChild(originalView);
                        callback.swap(index, originalIndex);
                    }
                }
                Log.d(TAG, "ON DRAG LOCATION");
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                originalView.setVisibility(View.VISIBLE);
                considerEvent = false;
                break;
        }
        return true;
    }
}
