package com.example.harmonialauncher.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.harmonialauncher.AppGrid.AppObject;

import java.util.ArrayList;
import java.util.List;

public abstract class AppListAdapter extends BaseAdapter {

    private static final String TAG = "App List Adapter";
    protected Context CONTEXT;
    protected int resource;
    protected ArrayList<AppObject> apps = new ArrayList<AppObject>();

    public AppListAdapter(Context context, int resource) {
        super();
        CONTEXT = context;
        this.resource = resource;
    }

    public static ArrayList<AppObject> sortListAlphabetically(ArrayList<AppObject> appList) {
        ArrayList<String> names = new ArrayList<String>();
        for (AppObject a : appList)
            names.add(a.getName());
        for (int i = 0; i < appList.size() - 1; i++) {
            if (appList.get(i).getName().compareTo(appList.get(i + 1).getName()) < 0) {
                AppObject app1 = appList.get(i);
                appList.set(i, appList.get(i + 1));
                appList.set(i + 1, app1);
                i = 0;
            }
        }
        return appList;
    }

    public abstract View getView(int position, View convertView, ViewGroup parent);

    public AppObject getApp(int position) {
        if (position >= 0 && position < apps.size())
            return apps.get(position);
        return null;
    }

    public AppObject get(int position) {
        return getApp(position);
    }

    public AppObject getItem(int position) {
        return getApp(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public int getCount() {
        return apps.size();
    }

    public int getIndexByName(String name) {
        for (AppObject a : apps) {
            if (a.getName().equalsIgnoreCase(name)) {
                return apps.indexOf(a);
            }
        }
        return -1;
    }

    public void add(AppObject a) {
        apps.add(a);
    }

    public void addAll(List<AppObject> list) {
        apps.addAll(list);
    }

    public void clear() {
        apps.clear();
    }

    public ArrayList<AppObject> getAppList() {
        return apps;
    }

    public AppObject findAppByName(String name) {
        ArrayList<String> names = new ArrayList<String>();
        for (AppObject a : apps)
            names.add(a.getName());
        for (String s : names) {
            if (s.equalsIgnoreCase(name))
                return apps.get(names.indexOf(s));
        }
        return null;
    }

    public AppObject findAppByPackage(String packageName) {
        ArrayList<String> packages = new ArrayList<String>();
        for (AppObject a : apps)
            packages.add(a.getPackageName());
        for (String s : packages) {
            if (s.equalsIgnoreCase(packageName))
                return apps.get(packages.indexOf(s));
        }
        return null;
    }

}
