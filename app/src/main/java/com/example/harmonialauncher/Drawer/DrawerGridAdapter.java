package com.example.harmonialauncher.Drawer;

import android.content.Context;

import androidx.annotation.NonNull;
import com.example.harmonialauncher.AppGridAdapter;
import com.example.harmonialauncher.AppObject;

import java.util.ArrayList;

public class DrawerGridAdapter extends AppGridAdapter {

    private final static String TAG = "Drawer Grid Adapter";

    public DrawerGridAdapter(@NonNull Context context, int resource, ArrayList<AppObject> appList) {
        super(context, resource, appList);
        CONTEXT = context;
        apps = appList;
        layout_id = resource;
        super.pageVerticalBuffer = 120;
    }
}