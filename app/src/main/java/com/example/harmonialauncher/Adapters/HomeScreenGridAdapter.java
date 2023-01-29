package com.example.harmonialauncher.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.harmonialauncher.Helpers.AppObject;
import com.example.harmonialauncher.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class HomeScreenGridAdapter extends AppGridAdapter {

    public static final int HOMESCREENAPPNUM = 15;
    private final static String TAG = "Grid Adapter";
    private final AppObject[] apps = new AppObject[HOMESCREENAPPNUM];

    //TODO: Alter the home screen system to reference a specified set of home screen apps, not the first 20 aps out of all apps.
    public HomeScreenGridAdapter(@NonNull Context context, int resource, ArrayList<AppObject> appList) {
        super(context, resource, appList);
        for (int i = 0; i < HOMESCREENAPPNUM; i++) {
            if (i < appList.size())
                apps[i] = appList.get(i);
            else
                apps[i] = null;
        }
        super.pageVerticalBuffer = 150;
    }

    public AppObject get(int position) {
        if (position >= 0 && position < apps.length)
            return apps[position];
        else
            return null;
    }

    public AppObject[] getAppArray() {
        return apps;
    }

    public ArrayList<AppObject> getAppList() {
        return new ArrayList<>(Arrays.asList(apps));
    }

    @Override
    public int getCount() {
        return HOMESCREENAPPNUM;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v;
        //Apps[position] is either null or an AppObject. If null, inflate empty view to fill app space.
        if (apps[position] == null || position > apps.length) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.empty, null);


            Point dimens = setElementDimen(parent.getHeight(), parent.getWidth());
            v.setLayoutParams(new LinearLayout.LayoutParams(dimens.x, dimens.y));

            return v;
        } else {
            v = super.getView(position, convertView, parent);
            v.setOnDragListener(new View.OnDragListener() {
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
                    }
                    return true;
                }
            });
            return v;
        }
    }

    public AppObject remove(int position) {
        AppObject a = apps[position];
        apps[position] = null;
        return a;
    }

    /**
     * Method removes the AppObject at the specified position, replaces it with the provided AppObject,
     * and returns the removed AppObject.
     *
     * @param app
     * @param position
     * @return app which was removed from list
     */
    public AppObject replace(AppObject app, int position) {
        if (position >= 0 && position < apps.length) {
            AppObject a = apps[position];
            apps[position] = app;
            return a;
        } else
            return null;
    }

    public void swap(int a, int b) {
        super.swap(a, b);
        if (a >= 0 && b >= 0 && a < apps.length && b < apps.length) {
            AppObject app1 = apps[a];
            apps[a] = apps[b];
            apps[b] = app1;
        }
    }
}
