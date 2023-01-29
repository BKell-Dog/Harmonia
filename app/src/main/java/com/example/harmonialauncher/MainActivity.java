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
import com.example.harmonialauncher.Views.HarmoniaConstraintLayout;
import com.example.harmonialauncher.Views.ViewPagerWrapper;


/*
This class will manage the fragment viewer which switches between the HomeScreenFragment and the SettingsFragment.
It will switch from home to settings once the Harmonia settings button is pressed, and switch from settings to
home once the back button is pressed in the settings menu.
 */
public class MainActivity extends HarmoniaActivity implements PageHolder {
    public static final int THRESHOLD = HarmoniaGestureDetector.THRESHOLD;
    private static final String TAG = "Main Activity";
    private MainActivityViewModel vm;
    public static Application instance;
    public final Context CONTEXT = MainActivity.this;
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
/*
        fcv = findViewById(R.id.MainActivityFragment);


        manager = getSupportFragmentManager();
        manager.beginTransaction()
                .add(R.id.MainActivityFragment, new HomeScreenFragment(), HomeScreenFragment.class.getSimpleName())
                .add(R.id.MainActivityFragment, new DrawerFragment(), DrawerFragment.class.getSimpleName())
                .commit();

        update();*/
    }

    public void incrementPage()
    {
       if (vp.getCurrentItem() == 0) {
           vm.setCurrentPage(1);
           vp.setCurrentItem(1);
       }
    }

    public void decrementPage()
    {
        if (vp.getCurrentItem() == 1) {
            vm.setCurrentPage(0);
            vp.setCurrentItem(0);
        }
    }

    public class MainPageGestureDetector extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        /**
         * This method will intercept fling events which are more vertical than they are horizontal,
         * and based on the direction of the fling will adjust the viewpager accordingly between
         * Home Screen Fragment and Drawer Fragment. If the fling is more horizontal than it is
         * vertical, the event is passed to children (i.e. Drawer Fragment) where it is processed.
         *
         * @return true is vertical, false if horizontal.
         */
        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, float velocity1, float velocity2) {
            Log.d(TAG, "MAIN ACTIVITY ON FLING");
            if (event1 == null || event2 == null)
                return false;

            float e1y = event1.getY(), e2y = event2.getY();
            float e1x = event1.getX(), e2x = event2.getX();
            float xTranslation = e2x - e1x, yTranslation = e2y - e1y;

            if (Math.abs(yTranslation) > Math.abs(xTranslation)) //Fling more vertical than horizontal
                if (e2y - e1y < -THRESHOLD) //Upward fling
                {
                    vm.setCurrentPage(1);
                    vp.invalidate();
                    return true;
                } else {
                    vm.setCurrentPage(0);
                    vp.invalidate();
                    return true;
                }
            else
                return false;
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