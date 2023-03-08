package com.example.harmonialauncher.applist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.example.harmonialauncher.Activities.HarmoniaActivity;
import com.example.harmonialauncher.Activities.MainActivity;
import com.example.harmonialauncher.Helpers.SingleTapDetector;
import com.example.harmonialauncher.Helpers.WallpaperManager;
import com.example.harmonialauncher.R;
import com.example.harmonialauncher.Utils.Util;
import com.example.harmonialauncher.ViewModels.MainActivityViewModel;
import com.example.harmonialauncher.appgrid.AppGridViewModel;
import com.example.harmonialauncher.appgrid.AppObject;
import com.example.harmonialauncher.blur.WallpaperView;
import com.example.harmonialauncher.preferences.PreferenceData;

import java.util.ArrayList;

public class AppListActivity extends HarmoniaActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = AppListActivity.class.getSimpleName();

    private Context CONTEXT;
    private WallpaperView wallpaper;
    private AppGridViewModel vm;
    private SingleTapDetector std;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_list_view);

        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);

        CONTEXT = this;
        std = new SingleTapDetector(this);
        vm = new ViewModelProvider(this).get(AppGridViewModel.class);

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

        LinearLayout ll = (LinearLayout) findViewById(R.id.app_list_linear_layout);
        ll.setPadding(0, statusBarHeight, 0, navigationBarHeight);
        ll.requestDisallowInterceptTouchEvent(true);

        ArrayList<AppObject> apps = vm.getAppList();
        for (AppObject app : apps)
        {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View listItem = inflater.inflate(R.layout.app_list_item, null, false);
            TextView text = (TextView) listItem.findViewById(R.id.app_list_text_view);
            text.setText(app.getName());

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

            ll.addView(listItem);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d(TAG, "onSharedPreferenceChanged: HIT");
        if (key.equalsIgnoreCase(getResources().getString(R.string.set_screen_layout_key)))
        {
            int layout = Integer.parseInt(sharedPreferences.getString(key, PreferenceData.LAYOUT_GRID + ""));
            if (layout == PreferenceData.LAYOUT_GRID)
            {
                Intent intent =  new Intent(this, MainActivity.class);
                startActivity(intent);
                this.finish();
            }
        }
    }
}
