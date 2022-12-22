package com.example.harmonialauncher;

import androidx.annotation.RequiresApi;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

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

    //Get package names set to default in the OS
    private static String dialerPackage = null;
    private static String SMSPackage = null;
    private static String cameraPackage = null;
    private static String galleryPackage = null;
    private static String emailPackage = null;
    private static String contactsPackage = null;
    private static String settingsPackage = null;

    public ViewPager2 vp;

    //TODO: Find a way around this API requirement while still using Telephony for SMS package
    @SuppressLint("ResourceType")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vp = (ViewPager2) findViewById(R.id.ViewPager);

        PageAdapter pa = new PageAdapter(this);
        vp.setAdapter(pa);
        vp.setUserInputEnabled(false);
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