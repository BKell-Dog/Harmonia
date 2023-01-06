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
import android.widget.TextView;

import com.example.harmonialauncher.Drawer.DrawerFragment;
import com.example.harmonialauncher.Drawer.DrawerPageFragment;
import com.example.harmonialauncher.HomeScreen.HomeScreenFragment;
import com.example.harmonialauncher.R;
import com.example.harmonialauncher.lockManager.HarmoniaActivity;
import com.example.harmonialauncher.lockManager.HarmoniaFragment;

import java.util.ArrayList;
import java.util.HashMap;

//TODO: ON 1/4/2023, bring back LockManager, create a Harmonia Lock App which blacklists selected apps for a period of time,
// Say, 1 hour. Those apps will appear grey, or invisible, and cannot be opened when tapped.


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
    private final int OFFSCREENPAGELIMIT = 1;

    //Gesture Detection
    private GestureDetectorCompat gd;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gd = new GestureDetectorCompat(this, new ScreenGestureDetector());

        vp = findViewById(R.id.ViewPager);

        MainPageAdapter pa = new MainPageAdapter(this);
        vp.setAdapter(pa);

        //Set page adapter to scroll vertically between home screen and drawer
        vp.setUserInputEnabled(false);
        vp.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        //vp.setPageTransformer(new MainPageTransformer());
        //vp.setOffscreenPageLimit(vp.getAdapter().getItemCount());

        vp.setCurrentItem(0);
        vp.setVisibility(View.VISIBLE);

        instance = this.getApplication();

        /*final WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        try{
            wallpaperManager.setResource(R.drawable.gradient_background);
            Log.d(TAG, "WALLPAPER RUNNING");
        }catch(IOException ioe){Log.d(TAG, "EXCEPTION");ioe.printStackTrace();}*/
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.gd.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e)
    {
        return onTouchEvent(e);
    }


    public static Context getContext() {
        return instance.getApplicationContext();
    }


    public class ScreenGestureDetector extends GestureDetector.SimpleOnGestureListener
    {
        private static final String TAG = "DRAWER PAGE GEST DETECT";
        private static final int THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent event) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
            //Vertical flings will move between home screen and app drawer, sent to the viewpager in MainActivity.
            if (event1.getY() - event2.getY() > THRESHOLD) //Upward fling
                vp.setCurrentItem(vp.getAdapter().getItemCount() - 1);
            else if (event1.getY() - event2.getY() < -THRESHOLD) //Downward fling
                vp.setCurrentItem(vp.getCurrentItem() - 1);

            //Horizontal flings will move between pages of the drawer, sent to the viewpager in DrawerFragment.
            else if (event1.getX() - event2.getX() < -THRESHOLD && vp.getCurrentItem() == 1) //Rightward fling
            {
                MainPageAdapter pa = (MainPageAdapter)vp.getAdapter();
                int position = pa.getIndexByName(pa.DRAWER);
                DrawerFragment df = (DrawerFragment) pa.getFragment(position);
                int currentPage = df.getCurrentPageIndex();
                if (currentPage > 0)
                    df.setPage(currentPage - 1);
            }
            else if (event1.getX() - event2.getX() > THRESHOLD && vp.getCurrentItem() == 1) //Leftward fling
            {
                MainPageAdapter pa = (MainPageAdapter)vp.getAdapter();
                int position = pa.getIndexByName(pa.DRAWER);
                DrawerFragment df = (DrawerFragment) pa.getFragment(position);
                int currentPage = df.getCurrentPageIndex(), lastPage = df.getLastPageIndex();
                if (currentPage < lastPage) {
                    df.setPage(currentPage + 1);
                }
            }

            Log.d(TAG, "onFling: " + event1.toString() + event2.toString());

            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e)
        {
            Log.d(TAG, "TAPPED TAPPED TAPPED");
            HarmoniaFragment f = (HarmoniaFragment)((PageAdapter)vp.getAdapter()).getFragment(vp.getCurrentItem());
            if (f instanceof HomeScreenFragment)
            {
                ((HomeScreenFragment) f).onTap(e);
            }
            else if (f instanceof DrawerFragment)
            {
                f = ((DrawerFragment) f).getCurrentPage();
                if (f instanceof DrawerPageFragment)
                    ((DrawerPageFragment)f).onTap(e);
            }
            return true;
        }

    }
    public class MainPageAdapter extends PageAdapter
    {
        public final String HOMESCREEN = "Home Screen", DRAWER = "Drawer";

        public MainPageAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);

            fragments.add(new HomeScreenFragment());
            nameIndex.add(HOMESCREEN);
            fragments.add(new DrawerFragment());
            nameIndex.add(DRAWER);
        }

        public Fragment createFragment(int position)
        {
            if (position == 1 || position == 0)
                return fragments.get(position);
            else
                throw new IndexOutOfBoundsException();
        }
    }

    /*private class MainPageTransformer implements ViewPager2.PageTransformer
    {
        private static final String TAG = "Main Page Transformer";
        public void transformPage(@NonNull View page, float position)
        {
            Log.d(TAG, "REACHED TRANSFORM PAGE");
            if (position == 1f)
            {
                page.setTranslationY(page.getHeight());
            }
            else if (position == -1f)
            {
                page.setTranslationY(-1 * page.getHeight());
            }
        }
    }*/
}