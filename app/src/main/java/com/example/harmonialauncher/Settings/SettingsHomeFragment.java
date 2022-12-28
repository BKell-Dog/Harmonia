package com.example.harmonialauncher.Settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.harmonialauncher.R;
import com.example.harmonialauncher.*;
import com.example.harmonialauncher.lockManager.LockManager;
import com.example.harmonialauncher.lockManager.LockScreen;

import java.util.ArrayList;

public class SettingsHomeFragment extends Fragment {

    private final static String TAG = "Settings Home Fragment";
    private Context CONTEXT;

    private final String setLauncher = "Set Launcher",
            setWhitelist = "Set Whitelist",
            exitHarmonia = "Exit Harmonia";

    public SettingsHomeFragment() {
        super(R.layout.settings_screen);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CONTEXT = getActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.settings_home_screen, container, false);

        LinearLayout ll = (LinearLayout) v.findViewById(R.id.settings_list);

        //Programmatically fill the linear layout with the settings, read from a list
        ArrayList<String> settingNames = new ArrayList<String>();
        settingNames.add(setLauncher);
        settingNames.add(setWhitelist);
        settingNames.add(exitHarmonia);
        //TODO: add settings to enable or disable app showing up on lock screen before unlocking the phone (I put this in for old people but not everyone wil want it)

        //TODO: implement the settings button list from R.menu.settings_menu data
        for (final String name : settingNames) {
            View card = LayoutInflater.from(CONTEXT).inflate(R.layout.cardview_button, null);
            ((TextView) card.findViewById(R.id.button_text)).setText(name);
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Set button functionality
                    switch (name) {
                        case setLauncher:
                            //TODO: configure the lockmanager to lock this following function
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                final Intent intent = new Intent(Settings.ACTION_HOME_SETTINGS);
                                if (!LockManager.isLocked(intent))
                                    startActivity(intent);
                            } else {
                                final Intent intent = new Intent(Settings.ACTION_SETTINGS);
                                if (!LockManager.isLocked(intent))
                                    startActivity(intent);
                            }
                            break;
                        case setWhitelist:
                            if (!LockManager.isLocked(new WhitelistFragment())) {
                                ViewPager2 vp = (ViewPager2) getActivity().findViewById(R.id.settings_view_pager);
                                vp.setCurrentItem(((SettingsPageAdapter) vp.getAdapter()).getIndexByName(((SettingsPageAdapter) vp.getAdapter()).WHITELIST));
                            } else {
                                LockScreen ls = new LockScreen();
                                ls.showLockScreenWindow(v, new WhitelistFragment());
                            }
                            break;
                        case exitHarmonia:
                            if (!LockManager.exitLocked()) {
                                getActivity().finish();
                                System.exit(0);
                            }
                            break;
                    }
                }
            });
            ll.addView(card);
        }

        //Set back button to return to the home screen.
        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    backToHome();
                    return true;
                }
                return false;
            }
        });

        ImageButton b = (ImageButton) v.findViewById(R.id.settings_back_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToHome();
            }
        });

        return v;
    }


    private void backToHome() {
        ((ViewPager2) getActivity().findViewById(R.id.ViewPager)).setCurrentItem(0);
    }
}
