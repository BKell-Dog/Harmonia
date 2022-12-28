package com.example.harmonialauncher.HomeScreen;

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

public class HomeScreenGridAdapter extends AppGridAdapter {

    private final static String TAG = "Grid Adapter";
    private int homeScreenAppNum = 20;
    private AppObject[] apps = new AppObject[homeScreenAppNum];
    private Context CONTEXT;
    private int layout_id;

    //TODO: Alter the home screen system to reference a specified set of home screen apps, not the first 20 aps out of all apps.
    public HomeScreenGridAdapter(@NonNull Context context, int resource, ArrayList<AppObject> appList) {
        super(context, resource, appList);
        CONTEXT = context;
        layout_id = resource;
        for (int i = 0; i < homeScreenAppNum && i < appList.size(); i++) {
            apps[i] = appList.get(i);
        }
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
        AppObject app = apps[position];

        //If the current index of the array holds a null value, return an empty view.
        if (app == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            return inflater.inflate(R.layout.empty, null);
        }

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

    public int getCount() {
        return apps.length;
    }

    public AppObject replace(AppObject app, int position) {
        AppObject a = apps[position];
        apps[position] = app;
        return a;
    }

    public AppObject remove(int position) {
        AppObject a = apps[position];
        apps[position] = null;
        return a;
    }

    public AppObject get(int position) {
        if (position > 0 && position < apps.length)
            return apps[position];
        else
            return null;
    }
}
