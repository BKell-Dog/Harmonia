package com.example.harmonialauncher.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.harmonialauncher.Helpers.AppObject;
import com.example.harmonialauncher.R;

import java.util.ArrayList;
import java.util.Arrays;

public class HomeScreenGridAdapter extends AppGridAdapter {

    public static final int HOMESCREENAPPNUM = 15;
    private final static String TAG = "Grid Adapter";
    private AppObject[] apps = new AppObject[HOMESCREENAPPNUM];

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

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (position < 0 || position > apps.length) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            return inflater.inflate(R.layout.empty, null);
        } else
            return super.getView(position, convertView, parent);
    }

    @Override
    public int getCount() {
        return apps.length;
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

    public AppObject remove(int position) {
        AppObject a = apps[position];
        apps[position] = null;
        return a;
    }

    public AppObject get(int position) {
        if (position >= 0 && position < apps.length)
            return apps[position];
        else
            return null;
    }

    public ArrayList<AppObject> getAppList() {
        return new ArrayList<>(Arrays.asList(apps));
    }

    public AppObject[] getAppArray() {
        return apps;
    }
}
