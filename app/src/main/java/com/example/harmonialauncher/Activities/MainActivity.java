package com.example.harmonialauncher.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.harmonialauncher.Adapters.PageAdapter;
import com.example.harmonialauncher.AppGrid.HomeScreenFragment;
import com.example.harmonialauncher.Blur.WallpaperView;
import com.example.harmonialauncher.Fragments.DrawerFragment;
import com.example.harmonialauncher.Helpers.FlingDetector;
import com.example.harmonialauncher.Helpers.WallpaperManager;
import com.example.harmonialauncher.Interfaces.PageHolder;
import com.example.harmonialauncher.R;
import com.example.harmonialauncher.Utils.Util;
import com.example.harmonialauncher.ViewModels.MainActivityViewModel;
import com.example.harmonialauncher.Views.FlingCatcher;


/*
This class will manage the fragment viewer which switches between the HomeScreenFragment and the SettingsFragment.
It will switch from home to settings once the Harmonia settings button is pressed, and switch from settings to
home once the back button is pressed in the settings menu.
 */
public class MainActivity extends HarmoniaActivity implements PageHolder {
    private static final String TAG = MainActivity.class.getSimpleName();
    private MainActivityViewModel vm;
    public ViewPager2 vp;
    private FlingCatcher fc;

    //Variables relating to the blur effect placed over the wallpaper when the viewpager switches between pages.
    private WallpaperView wallpaper;
    private float blurRadius = 50;

    @SuppressLint({"MissingInflatedId"})
    protected void onCreate(Bundle savedInstanceState) {
        //Initialize splash screen to show before activity begins calculations, and to disappear once
        // activity completes pre-loading.
        SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        //Set window to show behind status bar (on top) and navigation bar (on bottom w/ three buttons)
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        WindowInsets insets = null;
        int navigationBarHeight = 0, statusBarHeight = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            insets = getWindowManager().getCurrentWindowMetrics().getWindowInsets();
            statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top; //in pixels
            navigationBarHeight = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom; //in pixels
        }
        else {
            navigationBarHeight = 150;
            statusBarHeight = 120;
        }


        vm = new ViewModelProvider(this).get(MainActivityViewModel.class);
        vm.setCurrentPage(0);

        vp = findViewById(R.id.ViewPager);
        vp.setPadding(0, statusBarHeight, 0, navigationBarHeight);
        fc = findViewById(R.id.fling_detector);
        fc.setCallback(this);
        fc.setMode(FlingDetector.VERTICAL);

        wallpaper = (WallpaperView) findViewById(R.id.wallpaper);
        wallpaper.setImageDrawable(WallpaperManager.getWallpaper(this));

        vp.setAdapter(new MainPageAdapter(this));

        //Set page adapter to scroll vertically between home screen and drawer
        vp.setUserInputEnabled(false);
        vp.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        vp.setCurrentItem(0);
        vp.setCurrentItem(1);
        vp.setCurrentItem(vm.getCurrentPage());
        vp.setVisibility(View.VISIBLE);

        update();
    }

    public void update() {
        vp.setCurrentItem(vm.getCurrentPage());
        vp.invalidate();
    }

    public void incrementPage() {
        if (vp.getCurrentItem() == 0) {
            vm.setCurrentPage(1);
            wallpaper.setBlurRadius(blurRadius);
            update();
        }
    }

    public void decrementPage() {
        if (vp.getCurrentItem() == 1) {
            vm.setCurrentPage(0);
            wallpaper.setBlurRadius(0);
            update();
        }
    }

    public static class MainPageAdapter extends PageAdapter {
        public MainPageAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity, 2);
        }

        @NonNull
        public Fragment createFragment(int position) {
            if (position == 0)
                return new HomeScreenFragment();
            else if (position == 1)
                return new DrawerFragment();
            else
                throw new IndexOutOfBoundsException();
        }
    }
}