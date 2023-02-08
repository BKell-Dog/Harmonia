package com.example.harmonialauncher;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.harmonialauncher.Adapters.PageAdapter;
import com.example.harmonialauncher.Fragments.DrawerFragment;
import com.example.harmonialauncher.Helpers.FlingDetector;
import com.example.harmonialauncher.Interfaces.PageHolder;
import com.example.harmonialauncher.Utils.HarmoniaGestureDetector;
import com.example.harmonialauncher.Fragments.HomeScreenFragment;
import com.example.harmonialauncher.Activities.HarmoniaActivity;
import com.example.harmonialauncher.Fragments.HarmoniaFragment;
import com.example.harmonialauncher.ViewModels.MainActivityViewModel;
import com.example.harmonialauncher.Views.FlingCatcher;


/*
This class will manage the fragment viewer which switches between the HomeScreenFragment and the SettingsFragment.
It will switch from home to settings once the Harmonia settings button is pressed, and switch from settings to
home once the back button is pressed in the settings menu.
 */
public class MainActivity extends HarmoniaActivity implements PageHolder {
    public static final int THRESHOLD = HarmoniaGestureDetector.THRESHOLD;
    private static final String TAG = "Main Activity";
    private MainActivityViewModel vm;
    public ViewPager2 vp;
    private FlingCatcher fc;
    public FragmentContainerView fcv;
    private FragmentManager manager;
    //Gesture Detection
    private GestureDetectorCompat gd;

    @SuppressLint({"ClickableViewAccessibility", "MissingInflatedId"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vm = new ViewModelProvider(this).get(MainActivityViewModel.class);
        vm.setCurrentPage(0);

        vp = findViewById(R.id.ViewPager);
        fc = findViewById(R.id.fling_detector);
        fc.setCallback(this);
        fc.setMode(FlingDetector.VERTICAL);

        final MainPageAdapter pa = new MainPageAdapter(this);
        vp.setAdapter(pa);

        //Set page adapter to scroll vertically between home screen and drawer
        vp.setUserInputEnabled(false);
        vp.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        vp.setCurrentItem(vm.getCurrentPage());
        vp.setVisibility(View.VISIBLE);

        vp.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                for (int i = 0; i < pa.getItemCount(); i++)
                    if (i == position)
                        ((HarmoniaFragment) pa.getFragment(i)).setOnScreen();
                    else
                        ((HarmoniaFragment) pa.getFragment(i)).setOffScreen();
            }
        });
        update();
    }

    public void update()
    {
        vp.setCurrentItem(vm.getCurrentPage());
        vp.invalidate();
    }

    public void incrementPage()
    {
       if (vp.getCurrentItem() == 0) {
           vm.setCurrentPage(1);
           update();
       }
    }

    public void decrementPage()
    {
        if (vp.getCurrentItem() == 1) {
            vm.setCurrentPage(0);
            update();
        }
    }

    public class MainPageAdapter extends PageAdapter {
        public final String HOMESCREEN = "Home Screen", DRAWER = "Drawer";

        public MainPageAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);

            fragments.add(new HomeScreenFragment());
            nameIndex.add(HOMESCREEN);
            fragments.add(new DrawerFragment());
            nameIndex.add(DRAWER);
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