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
        super.pageVerticalBuffer = 150;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (position < 0 || position > apps.length)
        {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            return inflater.inflate(R.layout.empty, null);
        }
        else
            return super.getView(position, convertView, parent);
    }

    @Override
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
