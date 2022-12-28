package com.example.harmonialauncher.Drawer;

import android.content.Context;
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

import com.example.harmonialauncher.R;
import com.example.harmonialauncher.AppGridAdapter;
import com.example.harmonialauncher.AppObject;

import java.util.ArrayList;

public class DrawerGridAdapter extends AppGridAdapter {

    private final static String TAG = "Drawer Grid Adapter";
    private ArrayList<AppObject> apps;
    private Context CONTEXT;
    private int layout_id;


    public DrawerGridAdapter(@NonNull Context context, int resource, ArrayList<AppObject> appList) {
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
        if (position > apps.size())
            return null;
        AppObject app = apps.get(position);

        //Get Icon and Label and set their values to the specific app
        TextView label = (TextView) gridItemView.findViewById(R.id.label);
        ImageView icon = (ImageView) gridItemView.findViewById(R.id.image);
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
        //TODO: place limits on the scaling of the grid to it doesn't exceed 3x3
        gridItemView.setLayoutParams(new ViewGroup.LayoutParams(app.getWidth(), app.getHeight()));
        return gridItemView;
    }

    //This method resizes each GridView element size to fit the screen, and therefore, the gridView
    //won't scroll. This method must be called before Adapter.getView() to be effective.
    public void setElementDimen(int screenHeight, int screenWidth) {
        if (screenHeight <= 0 || screenWidth <= 0)
            return;

        int elementHeight = screenHeight / ROWS;
        int elementWidth = screenWidth / COLS;
        for (AppObject app : apps) {
            app.setWidth(elementWidth);
            app.setHeight(elementHeight);
        }
        this.horizontalBuffer = (int) (elementWidth * 0.2);
        this.verticalBuffer = (int) (elementHeight * 0.3);
    }
}