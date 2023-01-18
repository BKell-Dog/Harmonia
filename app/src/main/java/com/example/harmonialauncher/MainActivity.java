package com.example.harmonialauncher;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.harmonialauncher.Adapters.PageAdapter;
import com.example.harmonialauncher.Fragments.DrawerFragment;
import com.example.harmonialauncher.Utils.HarmoniaGestureDetector;
import com.example.harmonialauncher.Fragments.HomeScreenFragment;
import com.example.harmonialauncher.Activities.HarmoniaActivity;
import com.example.harmonialauncher.Fragments.HarmoniaFragment;


/*
This class will manage the fragment viewer which switches between the HomeScreenFragment and the SettingsFragment.
It will switch from home to settings once the Harmonia settings button is pressed, and switch from settings to
home once the back button is pressed in the settings menu.
 */
public class MainActivity extends HarmoniaActivity {
    public static final int THRESHOLD = HarmoniaGestureDetector.THRESHOLD;
    private static final String TAG = "Main Activity";
    public static Application instance;
    public final Context CONTEXT = MainActivity.this;
    public ViewPager2 vp;
    //Gesture Detection
    private GestureDetectorCompat gd;

    public static Context getContext() {
        return instance.getApplicationContext();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vp = findViewById(R.id.ViewPager);

        final MainPageAdapter pa = new MainPageAdapter(this);
        vp.setAdapter(pa);

        //Set page adapter to scroll vertically between home screen and drawer
        vp.setUserInputEnabled(false);
        vp.setOrientation(ViewPager2.ORIENTATION_VERTICAL);

        vp.setCurrentItem(0);
        vp.setVisibility(View.VISIBLE);

        instance = this.getApplication();

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
    }

    public void setPage(int position) {
        if (position >= 0 && position < vp.getAdapter().getItemCount()) {
            vp.setCurrentItem(position);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //gd.onTouchEvent(event);
        return super.onTouchEvent(event);
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
            if (position == 1 || position == 0)
                return fragments.get(position);
            else
                throw new IndexOutOfBoundsException();
        }
    }
}