package com.example.harmonialauncher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.harmonialauncher.Config.ConfigManager;
import com.example.harmonialauncher.Settings.WhitelistManager;
import com.example.harmonialauncher.lockManager.LockManager;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;

/*
This class will manage the GridView which displays the apps, its construction and popualtion, and will
generate the apps to display as well. It will manage the default and preset packages, as well as the
pressing of buttons and opening of apps. This screen is the home screen and launcher.
 */

public class HomeScreenFragment extends Fragment {
    private final static String TAG = "Home Screen Fragment";
    private Context CONTEXT;

    GridView gv;
    private int numCols = 3;

    //App Name Constants
    private static final String DIALER = "Dialer";
    private static final String MESSENGER = "Messenger";

    public HomeScreenFragment() {
        super(R.layout.home_screen);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CONTEXT = getActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout for this fragment
        View v = inflater.inflate(R.layout.home_screen, container, false);

        //Populate Grid Layout in home_screen.xml with instances of app.xml
        //Get grid view
        gv = (GridView) v.findViewById(R.id.home_screen_grid);

        //Set adapter, set element dimensions for proper scaling, and add a specific app for Harmonia Settings
        GridAdapter ga = new GridAdapter(CONTEXT, R.layout.app, getApps());
        AppObject HarmoniaSettings = new AppObject(null, "Harmonia", R.drawable.harmonia_icon, false);
        ga.add(HarmoniaSettings);
        setElementDimens(ga, numCols, v);

        gv.setAdapter(ga);
        gv.setNumColumns(numCols);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppObject app = (AppObject) parent.getItemAtPosition(position);
                Log.d(TAG, app.toString() + " CLICKED");
                String pkg = app.getPackageName();
                if (pkg != null) {
                    if (ConfigManager.getMode().equalsIgnoreCase(ConfigManager.ELDERLY) && app.getName().equalsIgnoreCase(DIALER)) {
                        Intent i = new Intent(Intent.ACTION_DIAL);
                        startActivity(i);
                    } else
                        openApp(CONTEXT, pkg);
                } else if (app.getName().equalsIgnoreCase("Harmonia")) {
                    //Use Fragment Transaction to open settings fragment in ViewPager
                    ViewPager2 vp = getActivity().findViewById(R.id.ViewPager);
                    vp.setCurrentItem(1, true);
                }
            }
        });

        return v;
    }

    public ArrayList<AppObject> getApps() {
        ArrayList<AppObject> apps = new ArrayList<>();
        PackageLoader pl = new PackageLoader(CONTEXT);
        ArrayList<String> installedPacks = pl.getInstalledPacks();

        //Dialer
        ArrayList<String> dialerPackages = new ArrayList<String>();
        AppObject dialer = null;
        dialerPackages.add(pl.getDefaultDialerPackage());
        dialerPackages.addAll(pl.getPresetDialerPackage());
        dialerPackages.retainAll(installedPacks);
        for (String pack : dialerPackages) {
            dialer = new AppObject(pack, DIALER, R.drawable.phone_icon, getResources().getDrawable(R.drawable.phone_icon), false);
            if (dialer != null) {
                apps.add(dialer);
                break;
            }
        }

        //SMS
        ArrayList<String> SMSPackages = new ArrayList<String>();
        AppObject SMS = null;
        SMSPackages.add(pl.getDefaultSMSPackage());
        SMSPackages.addAll(pl.getPresetSMSPackage());
        SMSPackages.retainAll(installedPacks);
        for (String pack : SMSPackages) {
            SMS = new AppObject(pack, "Messenger", R.drawable.text_icon, getResources().getDrawable(R.drawable.text_icon), false);
            if (SMS != null) {
                apps.add(SMS);
                break;
            }
        }

        //Camera
        ArrayList<String> cameraPackages = new ArrayList<String>();
        AppObject cam = null;
        cameraPackages.add(pl.getDefaultCameraPackage());
        cameraPackages.addAll(pl.getPresetCameraPackage());
        cameraPackages.retainAll(installedPacks);
        for (String pack : cameraPackages) {
            cam = new AppObject(pack, "Camera", R.drawable.camera_icon, getResources().getDrawable(R.drawable.camera_icon), false);
            if (cam != null) {
                apps.add(cam);
                break;
            }
        }

        //Gallery
        ArrayList<String> galleryPackages = new ArrayList<String>();
        AppObject gallery = null;
        galleryPackages.add(pl.getDefaultGalleryPackage());
        galleryPackages.addAll(pl.getPresetGalleryPackage());
        galleryPackages.retainAll(installedPacks);
        for (String pack : galleryPackages) {
            gallery = new AppObject(pack, "Gallery", R.drawable.gallery_icon, getResources().getDrawable(R.drawable.gallery_icon), false);
            if (gallery != null) {
                apps.add(gallery);
                break;
            }
        }

        //Email
        ArrayList<String> emailPackages = new ArrayList<String>();
        AppObject email = null;
        emailPackages.add(pl.getDefaultEmailPackage());
        emailPackages.addAll(pl.getPresetEmailPackage());
        emailPackages.retainAll(installedPacks);
        for (String pack : emailPackages) {
            email = new AppObject(pack, "Email", R.drawable.email_icon, getResources().getDrawable(R.drawable.email_icon), false);
            if (email != null) {
                apps.add(email);
                break;
            }
        }

        //Contacts
        ArrayList<String> contactsPackages = new ArrayList<String>();
        AppObject contacts = null;
        contactsPackages.add(pl.getDefaultContactsPackage());
        contactsPackages.addAll(pl.getPresetContactsPackage());
        contactsPackages.retainAll(installedPacks);
        for (String pack : contactsPackages) {
            contacts = new AppObject(pack, "Contacts", R.drawable.contacts_icon, getResources().getDrawable(R.drawable.contacts_icon), false);
            if (contacts != null) {
                apps.add(contacts);
                break;
            }
        }

        //File System
        ArrayList<String> filesPackages = new ArrayList<String>();
        AppObject files = null;
        filesPackages.add(pl.getDefaultFilesPackage());
        filesPackages.addAll(pl.getPresetFilesPackage());
        filesPackages.retainAll(installedPacks);
        for (String pack : filesPackages) {
            files = new AppObject(pack, "Files", R.drawable.files_icon, getResources().getDrawable(R.drawable.files_icon), false);
            if (files != null) {
                apps.add(files);
                break;
            }
        }

        //Calendar
        ArrayList<String> calendarPackages = new ArrayList<String>();
        AppObject calendar = null;
        calendarPackages.add(pl.getDefaultCalendarPackage());
        calendarPackages.addAll(pl.getPresetCalendarPackage());
        calendarPackages.retainAll(installedPacks);
        for (String pack : calendarPackages) {
            calendar = new AppObject(pack, "Calendar", R.drawable.calendar_icon, getResources().getDrawable(R.drawable.calendar_icon), false);
            if (calendar != null) {
                apps.add(calendar);
                break;
            }
        }

        ArrayList<AppObject> whitelistApps = new ArrayList<AppObject>();
        ArrayList<String> packages = WhitelistManager.getWhitelist();
        for (String p : packages) {
            AppObject a = Util.findAppByPackageName(p);
            whitelistApps.add(a);
            Log.d(TAG, a.toString());
        }

        apps.addAll(whitelistApps);

        return apps;
    }

    private static boolean openApp(Context context, String appPackageName) {
        if (context == null) {
            context = Util.getGenericContext();
        }

        //if (LockManager.isLocked(Util.findAppByPackageName(appPackageName)))

        //Form intent from package name
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(appPackageName);

        if (intent != null) {
            context.startActivity(intent);
            Log.d(TAG, "Started app, package name: '" + appPackageName + "'");
            return true;
        } else {
            Log.e(TAG, "Cannot start app, appPackageName:'" + appPackageName + "'");
            return false;
        }
    }

    //Methods for determining window size -----------------------------------------------------
    private void setElementDimens(GridAdapter g, int numCols, View parentView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int windowHeight = displayMetrics.heightPixels;
            int windowWidth = displayMetrics.widthPixels;
            int adjustedHeight = windowHeight - Util.getNavigationBarSize(CONTEXT).y;

            g.setElementDimen(numCols, adjustedHeight, windowWidth);
        }
    }


}