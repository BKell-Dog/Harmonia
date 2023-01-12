package com.example.harmonialauncher;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.harmonialauncher.Drawer.DrawerFragment;
import com.example.harmonialauncher.Drawer.DrawerPageFragment;
import com.example.harmonialauncher.GestureDetection.HarmoniaGestureDetector;
import com.example.harmonialauncher.HomeScreen.HomeScreenFragment;
import com.example.harmonialauncher.R;
import com.example.harmonialauncher.lockManager.HarmoniaActivity;
import com.example.harmonialauncher.lockManager.HarmoniaFragment;

import java.util.ArrayList;
import java.util.HashMap;


/*
This class will manage the fragment viewer which switches between the HomeScreenFragment and the SettingsFragment.
It will switch from home to settings once the Harmonia settings button is pressed, and switch from settings to
home once the back button is pressed in the settings menu.
 */
public class MainActivity extends HarmoniaActivity {
    private static final String TAG = "Main Activity";
    public final Context CONTEXT = MainActivity.this;
    public static Application instance;
    public ViewPager2 vp;

    //Gesture Detection
    private GestureDetectorCompat gd;
    public final int THRESHOLD = 100;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HarmoniaGestureDetector gest = new HarmoniaGestureDetector();
        gd = new GestureDetectorCompat(this, gest);
        gest.add(this);

        vp = findViewById(R.id.ViewPager);

        MainPageAdapter pa = new MainPageAdapter(this);
        vp.setAdapter(pa);

        //Set page adapter to scroll vertically between home screen and drawer
        vp.setUserInputEnabled(false);
        vp.setOrientation(ViewPager2.ORIENTATION_VERTICAL);

        vp.setCurrentItem(0);
        vp.setVisibility(View.VISIBLE);

        instance = this.getApplication();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gd.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        return onTouchEvent(e);
    }


    public static Context getContext() {
        return instance.getApplicationContext();
    }


    @Override
    public boolean onDown(MotionEvent event) {
        return true;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
        float e1y = event1.getY(), e2y = event2.getY();
        float e1x = event1.getX(), e2x = event2.getX();
        float xTranslation = e2x - e1x, yTranslation = e2y - e1y;
        float greaterTranslation = Math.abs(yTranslation - xTranslation);

        if (greaterTranslation > 0) //Fling more vertical than horizontal
        {
            //Vertical flings will move between home screen and app drawer, sent to the viewpager in MainActivity.
            if (yTranslation < -THRESHOLD) //Upward fling
                vp.setCurrentItem(vp.getAdapter().getItemCount() - 1);
            else if (yTranslation > THRESHOLD) //Downward fling
                vp.setCurrentItem(vp.getCurrentItem() - 1);
        }

        Log.d(TAG, "onFling: " + event1.toString() + event2.toString());

        return true;
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

        public Fragment createFragment(int position) {
            if (position == 1 || position == 0)
                return fragments.get(position);
            else
                throw new IndexOutOfBoundsException();
        }
    }
}