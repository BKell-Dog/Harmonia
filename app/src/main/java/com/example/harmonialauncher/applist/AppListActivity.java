package com.example.harmonialauncher.applist;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowInsets;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.example.harmonialauncher.Activities.HarmoniaActivity;
import com.example.harmonialauncher.appgrid.AppGridActivity;
import com.example.harmonialauncher.gesture.FlingDetector;
import com.example.harmonialauncher.gesture.FlingListener;
import com.example.harmonialauncher.gesture.SingleTapDetector;
import com.example.harmonialauncher.Helpers.TimeHelper;
import com.example.harmonialauncher.R;
import com.example.harmonialauncher.Utils.Util;
import com.example.harmonialauncher.gesture.FlingCatcher;
import com.example.harmonialauncher.appgrid.AppObject;
import com.example.harmonialauncher.blur.WallpaperView;
import com.example.harmonialauncher.lock.LockManager;
import com.example.harmonialauncher.lock.LockStatusChangeListener;
import com.example.harmonialauncher.preferences.PreferenceData;

import java.util.ArrayList;

public class AppListActivity extends HarmoniaActivity implements FlingListener,
                                                                LockStatusChangeListener.LockStatusListener,
                                                                SharedPreferences.OnSharedPreferenceChangeListener,
                                                                PopupMenu.OnMenuItemClickListener {
    private static final String TAG = AppListActivity.class.getSimpleName();

    private AppListActivity CONTEXT;
    private WallpaperView wallpaper;
    private AppListViewModel vm;
    private SingleTapDetector std;
    private FlingCatcher fc;
    private ImageView filterButton;
    private int scrollY = 0;

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_list_view);

        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        LockStatusChangeListener.add(this);

        CONTEXT = this;
        std = new SingleTapDetector(this);
        vm = new ViewModelProvider(this).get(AppListViewModel.class);
        if (vm == null)
            Log.e(TAG, "onCreate: VIEW MODEL IS NULL");

        //Set window to show behind status bar (on top) and navigation bar (on bottom w/ three buttons)
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        WindowInsets insets = null;
        int navigationBarHeight = 0, statusBarHeight = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            insets = getWindowManager().getCurrentWindowMetrics().getWindowInsets();
            statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top; //in pixels
            navigationBarHeight = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom; //in pixels
        } else {
            navigationBarHeight = 150;
            statusBarHeight = 120;
        }

        wallpaper = (WallpaperView) findViewById(R.id.wallpaper);
        wallpaper.setBackgroundColor(Color.BLACK);
        wallpaper.setBlurRadius(0);
        wallpaper.setDimmed(false);

        fc = findViewById(R.id.app_list_fling_catcher);
        fc.setCallback(this);
        fc.setMode(FlingDetector.VERTICAL);

        LinearLayout ll = (LinearLayout) findViewById(R.id.app_list_linear_layout);
        ll.requestDisallowInterceptTouchEvent(true);

        ScrollView scroll = (ScrollView) findViewById(R.id.app_list_scroll_view);
        scroll.setPadding(0, statusBarHeight, 0, navigationBarHeight);
        scroll.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                // Update vertical scroll position
                scrollY = scroll.getScrollY();
            }
        });

        inflateListItems(ll);

        // Instantiate filter menu
        filterButton = (ImageView) findViewById(R.id.filter_button);
        //flingUp();
        filterButton.setY(-300f);

        // Since the background will be black, we must invert the colors of the filter_icon drawable.
        Drawable d = filterButton.getDrawable();
        final float[] NEGATIVE = new float[]{-1.0f, 0, 0, 0, 255,
                0, -1.0f, 0, 0, 255,
                0, 0, -1.0f, 0, 255,
                0, 0, 0, 1.0f, 0};
        d.setColorFilter(new ColorMatrixColorFilter(new ColorMatrix(NEGATIVE)));

        // Set button to open a context menu when long tapped.
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(CONTEXT, filterButton);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.filter_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(CONTEXT);
                popup.show();
            }
        });
    }

    private void inflateListItems(LinearLayout ll) {
        // Clear linear layout
        ll.removeAllViews();

        // Inflate list items
        ArrayList<AppObject> apps = vm.getAppList();
        for (AppObject app : apps) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View listItem = inflater.inflate(R.layout.app_list_item, null, false);
            TextView text = (TextView) listItem.findViewById(R.id.app_list_text_view);
            TextView timeLeft = (TextView) listItem.findViewById(R.id.time_left_timer);
            boolean addToView = true;

            //If app name is too long, shorten it and add on an ellipse.
            String name = app.getName();
            if (name.length() > 20)
                name = name.substring(0, 20).trim() + "...";

            if (text != null)
                text.setText(name);
            else
                addToView = false;

            if (timeLeft != null && LockManager.isLocked(app.getPackageName())) {
                TimeHelper remaining = LockManager.getTimeRemaining(app.getPackageName());
                timeLeft.setText(remaining.getTimeFormatted(TimeHelper.HHMM));
            } else if (timeLeft == null) {
                addToView = false;
                Log.e(TAG, "onCreate: TextView time_left_timer is null!");
            } else
                timeLeft.setText("");

            listItem.setOnTouchListener(new View.OnTouchListener() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (std.onTouch(null, motionEvent)) {
                        String appName = ((TextView) ((ViewGroup) view).getChildAt(0)).getText().toString();
                        AppObject app = Util.findAppByName(appName, CONTEXT);
                        String appPackageName = app.getPackageName();

                        Util.openApp(CONTEXT, appPackageName);
                    }
                    return true;
                }
            });

            if (addToView)
                ll.addView(listItem);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equalsIgnoreCase(getResources().getString(R.string.set_screen_layout_key))) {
            Log.e(TAG, "onSharedPreferenceChanged: " + key);
            int layout = Integer.parseInt(sharedPreferences.getString(key, PreferenceData.LAYOUT_GRID + ""));
            if (layout == PreferenceData.LAYOUT_GRID) {
                Intent intent = new Intent(this, AppGridActivity.class);
                startActivity(intent);
                this.finish();
            }
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.option2: // Sort list A - Z
                vm.sortList(AppListViewModel.SORT_AZ);
                inflateListItems(findViewById(R.id.app_list_linear_layout));
                return true;
            case R.id.option3: // Sort list Z - A
                vm.sortList(AppListViewModel.SORT_ZA);
                inflateListItems(findViewById(R.id.app_list_linear_layout));
                return true;
            case R.id.option4:
                // Sort list New - Old
                Toast.makeText(this, "SORT New-Old", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.option5:
                // Sort list Old - New
                Toast.makeText(this, "SORT Old-New", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onStatusChanged() {
        Log.d(TAG, "onStatusChanged: LOCK STATUS CHANGED");
        recreate();
    }

    @Override
    public void flingUp() {
        // Hide filter button above screen.
        ObjectAnimator animation = ObjectAnimator.ofFloat(filterButton, "translationY", -300f);
        animation.setDuration(100);
        animation.start();
    }

    @Override
    public void flingDown() {
        // If scroll view has been pulled to the top, then show filter button.
        if (scrollY < 2) {
            ObjectAnimator animation = ObjectAnimator.ofFloat(filterButton, "translationY", 100f);
            animation.setDuration(150);
            animation.start();
        }
    }

    @Override
    public void flingRight() {}

    @Override
    public void flingLeft() {}
}
