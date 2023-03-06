package com.example.harmonialauncher.Activities;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.harmonialauncher.Adapters.PageAdapter;
import com.example.harmonialauncher.appgrid.HomeScreenFragment;
import com.example.harmonialauncher.blur.WallpaperView;
import com.example.harmonialauncher.Fragments.DrawerFragment;
import com.example.harmonialauncher.Helpers.FlingDetector;
import com.example.harmonialauncher.Helpers.WallpaperManager;
import com.example.harmonialauncher.Interfaces.PageHolder;
import com.example.harmonialauncher.R;
import com.example.harmonialauncher.ViewModels.MainActivityViewModel;
import com.example.harmonialauncher.Views.FlingCatcher;


/*
This class will manage the fragment viewer which switches between the HomeScreenFragment and the SettingsFragment.
It will switch from home to settings once the Harmonia settings button is pressed, and switch from settings to
home once the back button is pressed in the settings menu.
 */
public class MainActivity extends HarmoniaActivity implements PageHolder, SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private MainActivityViewModel vm;
    public ViewPager2 vp;
    private FlingCatcher fc;

    //Variables relating to the blur effect placed over the wallpaper when the viewpager switches between pages.
    private WallpaperView wallpaper;
    private float blurRadius = 70;

    /*@ReportsCrashes(formKey = "", // will not be used
            mailTo = "bkelldog59@gmail.com",
            mode = ReportingInteractionMode.SILENT,
            resToastText = R.string.crash_toast_text)
*/

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
        } else {
            navigationBarHeight = 150;
            statusBarHeight = 120;
        }

        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);

        vm = new ViewModelProvider(this).get(MainActivityViewModel.class);
        vm.setCurrentPage(0);

        vp = findViewById(R.id.ViewPager);
        vp.setPadding(0, statusBarHeight, 0, navigationBarHeight);
        fc = findViewById(R.id.fling_detector);
        fc.setCallback(this);
        fc.setMode(FlingDetector.VERTICAL);

        wallpaper = (WallpaperView) findViewById(R.id.wallpaper);
        wallpaper.setImageDrawable(WallpaperManager.getWallpaper(this));
        wallpaper.setBlurRadius(0);
        wallpaper.setDimmed(false);

        vp.setAdapter(new MainPageAdapter(this));

        //Set page adapter to scroll vertically between home screen and drawer
        vp.setUserInputEnabled(false);
        vp.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        vp.setCurrentItem(0);
        incrementPage();
        decrementPage();
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
            wallpaper.setDimmed(true);
            update();
        }
    }

    public void decrementPage() {
        if (vp.getCurrentItem() == 1) {
            vm.setCurrentPage(0);
            wallpaper.setBlurRadius(0);
            wallpaper.setDimmed(false);
            update();
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equalsIgnoreCase(getResources().getString(R.string.set_app_screen_style_key))) {
            Toast.makeText(this, "PREFERENCE CHANGED", Toast.LENGTH_SHORT).show();
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