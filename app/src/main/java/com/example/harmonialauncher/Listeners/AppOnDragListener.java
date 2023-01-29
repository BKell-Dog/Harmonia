package com.example.harmonialauncher.Listeners;

import android.graphics.Color;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AppOnDragListener implements View.OnDragListener {
    private static final String TAG = "AppOnDragListener";

    @Override
    public boolean onDrag(View view, DragEvent dragEvent) {
        int action = dragEvent.getAction();

        //originalView is the original app object that was long pressed.
        //view is the view which is now underneath the drag shadow.
        final ViewGroup originalView = (ViewGroup) dragEvent.getLocalState();
        ViewGroup view2 = (ViewGroup) view;

        //We must now extract the app names from the TextViews within the app.xml layout.
        //If any changes are made to this file they must be reflected here.
        //Currently the hierarchy goes:
        //LinearLayout -> LinearLayout (index 1) -> TextView (index 0)
        Log.d(TAG, originalView.toString() + " --- " + view.toString());
        if (!((String)originalView.getTag()).equalsIgnoreCase("app_layout") || !((String)view.getTag()).equalsIgnoreCase("app_layout"))
            return false;

        String originalAppName = ((TextView)((ViewGroup)originalView.getChildAt(1)).getChildAt(0)).getText().toString();
        String newAppName = ((TextView)((ViewGroup)view2.getChildAt(1)).getChildAt(0)).getText().toString();
/*
        int index1 = -1, index2 = -1;
        for (int i = 0; i < getCount(); i++) {
            if (apps[i].getName().equalsIgnoreCase(newAppName))
                index1 = i;
            else if (apps[i].getName().equalsIgnoreCase(originalAppName))
                index2 = i;
        }
        Log.d(TAG, "ON DRAG " + index1 + ", " + index2);


        switch (action) {
            case DragEvent.ACTION_DRAG_STARTED:
                // handle drag started
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                Log.d(TAG, "ON ACTION DRAG ENTERED");

                if (Math.abs(index1 - index2) == 1)
                {
                    swap(index1, index2);
                    view.invalidate();
                    //gv.setAdapter(new HomeScreenGridAdapter(CONTEXT, R.layout.app, getAppList()));
                }
                else if (index2 < index1)
                {
                    for (int i = index1; i > index2; i--)
                    {
                        swap(i, i - 1);
                    }
                }
                else if (index2 > index1)
                {
                    for (int i = index1; i < index2; i++)
                    {
                        swap(i, i + 1);
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
                Log.d(TAG, "ACTION DROP");
                break;
            case DragEvent.ACTION_DRAG_LOCATION:
                Log.d(TAG, "ON DRAG LOCATION");
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                originalView.post(new Runnable(){
                    @Override
                    public void run() {
                        originalView.setVisibility(View.VISIBLE);
                    }
                });
                break;
        }*/
        return true;
    }
}
