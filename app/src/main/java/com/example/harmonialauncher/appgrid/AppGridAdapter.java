package com.example.harmonialauncher.appgrid;

import static com.example.harmonialauncher.preferences.PreferenceData.LOCK_MODE_GREYSCALE;
import static com.example.harmonialauncher.preferences.PreferenceData.LOCK_MODE_INVISIBLE;
import static com.example.harmonialauncher.preferences.PreferenceData.STYLE_GREYSCALE;
import static com.example.harmonialauncher.preferences.PreferenceData.STYLE_NORMAL;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import androidx.core.content.res.ResourcesCompat;
import androidx.preference.PreferenceManager;

import com.example.harmonialauncher.appgrid.views.AppView;
import com.example.harmonialauncher.preferences.PreferenceData;
import com.example.harmonialauncher.gesture.SingleTapDetector;
import com.example.harmonialauncher.R;
import com.example.harmonialauncher.Utils.Util;
import com.example.harmonialauncher.lock.LockManager;

import java.util.ArrayList;
import java.util.Collections;

import javax.security.auth.login.LoginException;

public class AppGridAdapter extends ArrayAdapter<AppObject> implements AppHolder {

    private final static String TAG = AppGridAdapter.class.getSimpleName();
    private int lockMode = LOCK_MODE_GREYSCALE; //Change this variable to change disappearance mode
    public static final int COLS = 4, ROWS = 5;
    protected ArrayList<AppObject> apps;
    protected ArrayList<String> lockedPacks = new ArrayList<>();
    protected Context CONTEXT;
    protected int layout_id;

    //CHANGE THESE TO CHANGE ICON SIZE
    protected int horizontalBuffer = 75, verticalBuffer = 75;
    protected final int pageHorizontalBuffer = 0, pageVerticalBuffer = 120;
    protected int elementHeight = -1, elementWidth = -1;
    protected int gridWidth = 0, gridHeight = 0;
    private int dragInvisibleIndex = -1;
    private int style;
    private SingleTapDetector std;

    public AppGridAdapter(@NonNull Context context, int resource, @NonNull ArrayList<AppObject> appList) {
        super(context, resource, appList);
        CONTEXT = context;
        apps = appList;
        layout_id = resource;

        std = new SingleTapDetector(context);

        initializePreferences();
        setElementDimens();
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

    public int getCount() {
        if (apps != null)
            return apps.size();
        else
            return 0;
    }

    public Point getElementDimens() {
        return new Point(elementWidth, elementHeight);
    }


    public int getLockMode() {
        return lockMode;
    }

    public int getStyle()
    {return style;}

    @SuppressLint("ClickableViewAccessibility")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (gridHeight == 0 || gridWidth == 0)
            setDimensions(parent.getWidth(), parent.getHeight());

        LayoutInflater inflater = (LayoutInflater) CONTEXT.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            convertView = inflater.inflate(R.layout.app, null);
        }

        AppObject app = apps.get(position);
        if (app == null)
            return inflater.inflate(R.layout.app, null);
        AppView appView = convertView.findViewById(R.id.app_layout);

        appView.setText(app.getName());
        Drawable image = app.getImage();
        if (image == null) {
            if (app.getImageId() == 0)
            {
                try {
                    image = CONTEXT.getPackageManager().getApplicationIcon(app.getPackageName());
                } catch (PackageManager.NameNotFoundException e) {
                    Log.e(TAG, "Invalid Package Name");
                    e.printStackTrace();
                }
            }
            else {
                if (Build.VERSION.SDK_INT > 21) {
                    image = ResourcesCompat.getDrawable(CONTEXT.getResources(), app.getImageId(), null);
                }
                else
                    image = ResourcesCompat.getDrawable(CONTEXT.getResources(), R.drawable.error_icon, null);
            }
        }
        appView.setPackageName(app.getPackageName());
        if (app.isLocked())
            appView.lock();

        //Resize icon to fit within the GridView area
        appView.setIconLayoutParams(new LinearLayout.LayoutParams(elementWidth - horizontalBuffer, elementHeight - verticalBuffer));

        //Resize the Grid Item to the app's previously defined height and width, which are set below
        appView.setLayoutParams(new LinearLayout.LayoutParams(elementWidth, elementHeight));

        if (style == STYLE_NORMAL)
        {
            appView.setImageDrawable(image);

            //Make app invisible or greyscale if it is meant to be locked
            if (position == dragInvisibleIndex || app.isLocked() || lockedPacks.contains(app.getPackageName())) {
                if (lockMode == LOCK_MODE_INVISIBLE || dragInvisibleIndex != -1)
                    appView.setVisibility(View.INVISIBLE);
                else if (lockMode == LOCK_MODE_GREYSCALE)
                    appView.setImageDrawable(Util.convertToGreyscale(image));
                else
                    appView.setImageDrawable(Util.convertToGreyscale(image));
            }
        }
        else if (style == STYLE_GREYSCALE)
        {
            lockMode = LOCK_MODE_INVISIBLE;
            appView.setImageDrawable(Util.convertToGreyscale(image));

            //Make app invisible if it is meant to be locked, not greyscale (since we are already in greyscale)
            if (position == dragInvisibleIndex || app.isLocked() || lockedPacks.contains(app.getPackageName())) {
                appView.setImageDrawable(ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.lock_icon, null));
            }
        }

        appView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.d(TAG, "onLongClick: ");

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
        if (lockMode == PreferenceData.LOCK_MODE_INVISIBLE || lockMode == LOCK_MODE_GREYSCALE)
            this.lockMode = lockMode;
        else
            Log.e(TAG, "Mode out of bounds!");
    }

    public void setLockedPackages(ArrayList<String> lockedPackages)
    {
        this.lockedPacks = lockedPackages;
        if (apps != null)
            for (AppObject app : apps)
                if (lockedPacks.contains(app.getPackageName()))
                    app.lock();
    }

    public ArrayList<String> getLockedPacks()
    {
        return lockedPacks;
    }

    public void setStyle(int newStyle)
    {
        this.style = newStyle;
    }

    public void swap(int a, int b) {
        if (a >= 0 && b >= 0 && a < apps.size() && b < apps.size()) {
            Collections.swap(apps, a, b);
        }
    }

    /**
     * This method fetched data from stored preferences upon first instantiation of this class,
     * which assures the data used at all times is in line with previous preferences.
     * ATTENTION: Whenever a new and relevant preference is created, it must be added below like the
     * others.
     */
    public void initializePreferences()
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(CONTEXT);
        //onSharedPreferenceChanged(prefs, CONTEXT.getResources().getString(R.string.set_app_screen_style_key));
        //onSharedPreferenceChanged(prefs, CONTEXT.getResources().getString(R.string.set_locked_app_style_key));
    }

    @NonNull
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (AppObject a : apps)
            s.append(a.toString()).append("\n");
        return s.toString();
    }
}
