package com.example.harmonialauncher.AppGrid;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.harmonialauncher.AppGrid.Views.AppView;
import com.example.harmonialauncher.Helpers.SingleTapDetector;
import com.example.harmonialauncher.R;
import com.example.harmonialauncher.Utils.Util;
import com.example.harmonialauncher.Utils.LockManager;

import java.util.ArrayList;
import java.util.Collections;

public class AppGridAdapter extends ArrayAdapter<AppObject> implements AppHolder {

    private final static String TAG = AppGridAdapter.class.getSimpleName();
    public final int INVISIBLE = 0, GREYSCALE = 1; //Variables for drawing locked apps
    private int lockMode = GREYSCALE; //Change this variable to change disappearance mode
    public static final int COLS = 4, ROWS = 5;
    protected ArrayList<AppObject> apps;
    protected Context CONTEXT;
    protected int layout_id;

    //CHANGE THESE TO CHANGE ICON SIZE
    protected int horizontalBuffer = 75, verticalBuffer = 75;
    protected final int pageHorizontalBuffer = 0, pageVerticalBuffer = 120;
    protected int elementHeight = -1, elementWidth = -1;
    protected int gridWidth = 0, gridHeight = 0;
    private int dragInvisibleIndex = -1;
    private SingleTapDetector std;

    public AppGridAdapter(@NonNull Context context, int resource, ArrayList<AppObject> appList) {
        super(context, resource, appList);
        CONTEXT = context;
        apps = appList;
        layout_id = resource;
        std = new SingleTapDetector(context);
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

    public AppObject getItem(int position) {
        if (position >= 0 && position < apps.size())
            return apps.get(position);
        else
            return null;
    }

    public ArrayList<AppObject> getAppList() {
        return apps;
    }

    public int getItemCount() {
        return apps.size();
    }

    public Point getElementDimens() {
        return new Point(elementWidth, elementHeight);
    }


    public int getLockMode() {
        return lockMode;
    }

    @SuppressLint("ClickableViewAccessibility")
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

        AppView appView = gridItemView.findViewById(R.id.app_layout);

        appView.setText(app.getName());
        Drawable image = app.getImage();
        if (image == null) {
            if (Build.VERSION.SDK_INT > 21)
                image = CONTEXT.getResources().getDrawable(app.getImageId(), null);
            else
                image = CONTEXT.getResources().getDrawable(app.getImageId());
        }
        appView.setImageDrawable(image);

        //Resize icon to fit within the GridView area
        appView.setIconLayoutParams(new LinearLayout.LayoutParams(elementWidth - horizontalBuffer, elementHeight - verticalBuffer));

        //Resize the Grid Item to the app's previously defined height and width, which are set below
        appView.setLayoutParams(new LinearLayout.LayoutParams(elementWidth, elementHeight));

        //Make app invisible or greyscale if it is meant to be locked
        if (position == dragInvisibleIndex || app.isLocked() || LockManager.isLocked(app.getPackageName())) {
            if (lockMode == INVISIBLE || dragInvisibleIndex != -1)
                appView.setVisibility(View.INVISIBLE);
            else if (lockMode == GREYSCALE)
                appView.setImageDrawable(Util.convertToGreyscale(app.getImage()));
        }

        appView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (std.onTouch(null, event)) {
                    // Isolate app package name
                    String appPackageName = Util.findAppByName(appView.getText(), CONTEXT).getPackageName();

                    //Start app
                    Util.openApp(CONTEXT, appPackageName);
                    return true;
                }
                return false;
            }
        });

        appView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (view != null) {
                    ClipData data = ClipData.newPlainText("", "");
                    Log.d(TAG, "onLongPress: START DRAG EVENT NOW NIUGGAH");
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                        view.startDragAndDrop(data, shadowBuilder, view, 0);
                    else
                        view.startDrag(data, shadowBuilder, view, 0);
                    view.setVisibility(View.INVISIBLE);
                }
                return true;
            }
        });

        return appView;
    }


    public void setDimensions(int gridWidth, int gridHeight)
    {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        setElementDimens();
    }

    public void setDragInvisible(int index)
    {
        dragInvisibleIndex = index;
    }

    /**
     * This method resizes each GridView element size to fit the screen, and therefore, the gridView
     * won't scroll. This method must be called before Adapter.getView() to be effective.
     */
    public void setElementDimens() {
        elementHeight = (gridHeight - pageVerticalBuffer) / ROWS;
        elementWidth = (gridWidth - pageHorizontalBuffer) / COLS;
    }

    public void setLockMode(int lockMode) {
        if (lockMode == INVISIBLE || lockMode == GREYSCALE)
            this.lockMode = lockMode;
        else
            Log.d(TAG, "Mode out of bounds!");
    }

    public void swap(int a, int b) {
        if (a >= 0 && b >= 0 && a < apps.size() && b < apps.size()) {
            Collections.swap(apps, a, b);
        }
    }

    @NonNull
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (AppObject a : apps)
            s.append(a.toString()).append("\n");
        return s.toString();
    }
}
