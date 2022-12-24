package com.example.harmonialauncher;

import androidx.annotation.RequiresApi;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.harmonia.R;
import com.example.harmonialauncher.lockManager.HarmoniaActivity;


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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vp = (ViewPager2) findViewById(R.id.ViewPager);

        PageAdapter pa = new PageAdapter(this);
        vp.setAdapter(pa);

        //Set page adapter to scroll vertically between home screen and drawer
        vp.setUserInputEnabled(true);
        vp.canScrollVertically(1);

        vp.setCurrentItem(0);
        vp.setVisibility(View.VISIBLE);

        instance = this.getApplication();

        /*final WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        try{
            wallpaperManager.setResource(R.drawable.gradient_background);
            Log.d(TAG, "WALLPAPER RUNNING");
        }catch(IOException ioe){Log.d(TAG, "EXCEPTION");ioe.printStackTrace();}*/
    }

    public static Context getContext()
    {
        return instance.getApplicationContext();
    }

}