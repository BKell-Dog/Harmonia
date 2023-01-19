package com.example.harmonialauncher.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.harmonialauncher.Helpers.AppObject;
import com.example.harmonialauncher.R;
import com.example.harmonialauncher.Utils.Util;
import com.example.harmonialauncher.Utils.LockManager;

import java.util.ArrayList;
import java.util.Collections;

public class AppGridAdapter extends ArrayAdapter<AppObject> {

    private final static String TAG = "Grid Adapter";
    public final int INVISIBLE = 0, GREYSCALE = 1; //Variables for drawing locked apps
    private int mode = GREYSCALE; //Change this variable to change disappearance mode
    protected final int COLS = 4, ROWS = 5;
    protected ArrayList<AppObject> apps;
    protected Context CONTEXT;
    protected int layout_id;
    protected int horizontalBuffer = 300, verticalBuffer = 300;
    protected int pageHorizontalBuffer = 0, pageVerticalBuffer = 120;
    protected int elementHeight = -1, elementWidth = -1;
    private ArrayList<String> lockedPacks = new ArrayList<String>();

    public AppGridAdapter(@NonNull Context context, int resource, ArrayList<AppObject> appList) {
        super(context, resource, appList);
        CONTEXT = context;
        apps = appList;
        layout_id = resource;
    }

    public void add(AppObject app) {
        apps.add(app);
    }

    public AppGridAdapter copy() {
        try {
            return (AppGridAdapter) this.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public AppObject get(int position) {
        if (position >= 0 && position < apps.size())
            return apps.get(position);
        else
            return null;
    }

    public ArrayList<AppObject> getAppList() {
        return apps;
    }

    public int getCount() {
        return apps.size();
    }

    public Point getElementDimens() {
        return new Point(elementWidth, elementHeight);
    }

    public ArrayList<String> getLockedPacks() {
        return lockedPacks;
    }

    public int getMode() {
        return mode;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View gridItemView = convertView;
        if (gridItemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            LayoutInflater inflater = (LayoutInflater) CONTEXT.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridItemView = inflater.inflate(R.layout.app, null);
        }
        AppObject app = apps.get(position);

        //Set element dimens is called so that in case the app list had been refreshed and all dimensions
        // have been lost, we can reset them here. But it is only called if the apps dimensions are each
        // equal to -1.
        //Point p = getElementDimens();
        //if (p.y == -1 || p.x == -1) //I commented these out because for some reason HScreen icons kept shrinking
        setElementDimen(parent.getHeight(), parent.getWidth());

        //Get Icon and Label and set their values to the specific app
        TextView label = gridItemView.findViewById(R.id.label);
        ImageView icon = gridItemView.findViewById(R.id.image);
        label.setText(app.getName());
        Drawable image = app.getImage();
        if (image == null) {
            if (Build.VERSION.SDK_INT > 21)
                image = CONTEXT.getResources().getDrawable(app.getImageId(), null);
            else
                image = CONTEXT.getResources().getDrawable(app.getImageId());
        }
        icon.setImageDrawable(image);

        //Resize icon to fit within the GridView area
        icon.setLayoutParams(new LinearLayout.LayoutParams(app.getWidth() - horizontalBuffer, app.getHeight() - verticalBuffer));

        //Resize the Grid Item to the app's previously defined height and width, which are set below
        gridItemView.setLayoutParams(new LinearLayout.LayoutParams(app.getWidth(), app.getHeight()));

        //Make app invisible or greyscale if it is meant to be locked
        if (app.isLocked() || LockManager.isLocked(app.getPackageName())) {
            if (inLockedList(app.getPackageName()))
                lockedPacks.add(app.getPackageName());

            if (mode == INVISIBLE)
                gridItemView.setVisibility(View.INVISIBLE);
            else if (mode == GREYSCALE)
                icon.setImageDrawable(Util.convertToGreyscale(app.getImage()));
        }

        //Setting child views to not respond to touch events
        gridItemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
        icon.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
        label.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });

        gridItemView.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                int action = dragEvent.getAction();
                switch (action) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        // handle drag started
                        break;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        // get the view that was entered
                        View enteredView = (View) dragEvent.getLocalState();
                        Log.d(TAG, enteredView.toString());
                        Log.d(TAG, view.toString());
                        // change the background color of the entered view
                        view.setBackgroundColor(Color.GREEN);
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        // get the view that was exited
                        View exitedView = (View) dragEvent.getLocalState();
                        // change the background color of the exited view back to its original color
                        view.setBackgroundColor(Color.RED);
                        break;
                    case DragEvent.ACTION_DROP:

                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                        // handle drag ended
                        break;
                    default:
                }
                return true;
            }
        });

        gridItemView.setBackgroundColor(Color.BLACK);

        return gridItemView;
    }

    public boolean inLockedList(String packageName) {
        for (String pack : lockedPacks)
            if (pack.equalsIgnoreCase(packageName))
                return true;
        return false;
    }

    /**
     * This method resizes each GridView element size to fit the screen, and therefore, the gridView
     * won't scroll. This method must be called before Adapter.getView() to be effective.
     */
    public Point setElementDimen(int screenHeight, int screenWidth) {
        if (screenHeight <= 0 || screenWidth <= 0)
            return new Point(0, 0);

        elementHeight = (screenHeight - pageVerticalBuffer) / ROWS;
        elementWidth = (screenWidth - pageHorizontalBuffer) / COLS;
        for (AppObject app : apps) {
            if (app != null) {
                app.setWidth(elementWidth);
                app.setHeight(elementHeight);
            }
        }
        this.horizontalBuffer = (int) (elementWidth * 0.2);
        this.verticalBuffer = (int) (elementHeight * 0.1);

        return new Point(elementWidth, elementHeight);
    }

    public void setMode(int mode) {
        if (mode == 0 || mode == 1)
            this.mode = mode;
        else
            Log.d(TAG, "Mode out of bounds. Possibly update method setMode to accommodate new modes.");
    }

    public void swap(int a, int b) {
        if (a >= 0 && b >= 0 && a < apps.size() && b < apps.size()) {
            Collections.swap(apps, a, b);
        }
    }

    @NonNull
    public String toString() {
        String s = "";
        for (AppObject a : apps)
            s += a.toString() + "\n";
        return s;
    }
}
