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
    public final int HOMESCREENAPPNUM = 15;
    private AppObject[] apps = new AppObject[HOMESCREENAPPNUM];
    private Context CONTEXT;
    private int layout_id;

    //TODO: Alter the home screen system to reference a specified set of home screen apps, not the first 20 aps out of all apps.
    public HomeScreenGridAdapter(@NonNull Context context, int resource, ArrayList<AppObject> appList) {
        super(context, resource, appList);
        CONTEXT = context;
        layout_id = resource;
        for (int i = 0; i < HOMESCREENAPPNUM && i < appList.size(); i++) {
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

    /**
     * Method removes the AppObject at the specified position, replaces it with the provided AppObject,
     * and returns the removed AppObject.
     * @param app
     * @param position
     * @return app which was removed from list
     */
    public AppObject replace(AppObject app, int position) {
        if (position >= 0 && position < apps.length) {
            AppObject a = apps[position];
            apps[position] = app;
            return a;
        }
        else
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

    public AppObject[] getAppList()
    {return apps;}
}
