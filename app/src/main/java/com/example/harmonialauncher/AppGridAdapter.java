package com.example.harmonialauncher;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.harmonialauncher.R;
import com.example.harmonialauncher.lockManager.LockManager;

import java.util.ArrayList;

public class AppGridAdapter extends ArrayAdapter<AppObject> {

    private final static String TAG = "Grid Adapter";
    protected ArrayList<AppObject> apps;
    protected Context CONTEXT;
    protected int layout_id;

    protected final int COLS = 4, ROWS = 5;
    protected int horizontalBuffer = 200, verticalBuffer = 200;
    protected int pageHorizontalBuffer = 0, pageVerticalBuffer = 120;
    protected int elementHeight = -1, elementWidth = -1;

    public AppGridAdapter(@NonNull Context context, int resource, ArrayList<AppObject> appList) {
        super(context, resource, appList);
        CONTEXT = context;
        apps = appList;
        layout_id = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View gridItemView = convertView;
        if (gridItemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridItemView = inflater.inflate(R.layout.app, null);
        }
        AppObject app = apps.get(position);

        //Set element dimens is called so that in case the app list had been refreshed and all dimensions
        // have been lost, we can reset them here. But it is only called if the apps dimensions are each
        // equal to -1.
        Point p = getElementDimens();
        if (p.y == -1 || p.x == -1)
            setElementDimen(parent.getHeight(), parent.getWidth());

        //Make app invisible if it is meant to be locked
        if (app.isLocked() || LockManager.isLocked(app))
            gridItemView.setVisibility(View.INVISIBLE);

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

        gridItemView.setBackgroundColor(Color.RED);
        //Resize the Grid Item to the app's previously defined height and width, which are set below
        //TODO: place limits on the scaling of the grid to it doesn't exceed 3x3
        gridItemView.setLayoutParams(new LinearLayout.LayoutParams(app.getWidth(), app.getHeight()));
        return gridItemView;
    }

    public int getCount() {
        return apps.size();
    }

    public void add(AppObject app) {
        apps.add(app);
    }

    public AppObject get(int position) {
        Log.d(TAG, "FIRST POINT REACHED. LIST SIZE: " + apps.size() + " Position: " + position);
        if (position >= 0 && position < apps.size()) {
            Log.d(TAG, "POINT REACHED");
            return apps.get(position);
        }
        else
            return null;
    }

    //This method resizes each GridView element size to fit the screen, and therefore, the gridView
    //won't scroll. This method must be called before Adapter.getView() to be effective.
    public void setElementDimen(int screenHeight, int screenWidth) {
        if (screenHeight <= 0 || screenWidth <= 0)
            return;

        elementHeight = (screenHeight - pageVerticalBuffer) / ROWS;
        elementWidth = (screenWidth - pageHorizontalBuffer) / COLS;
        for (AppObject app : apps) {
            app.setWidth(elementWidth);
            app.setHeight(elementHeight);
        }
        this.horizontalBuffer = (int) (elementWidth * 0.2);
        this.verticalBuffer = (int) (elementHeight * 0.1);
    }

    public Point getElementDimens()
    {
        return new Point(elementWidth, elementHeight);
    }

    public String toString()
    {
        String s = "";
        for (AppObject a : apps)
            s += a.toString() + "\n";
        return s;
    }
}
