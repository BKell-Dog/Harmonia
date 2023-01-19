package com.example.harmonialauncher.Settings;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

/*
This class handles the fragments within the settings screen itself. The master Settings XML file contains a
ViewPager2 which switches between individual settings, handled here.
 */

public class SettingsPageAdapter extends FragmentStateAdapter {

    private static final String TAG = "Settings Page Adapter";
    public final String SETTINGSHOME = "Settings Home",
            WHITELIST = "Whitelist";
    public ArrayList<String> nameIndex = new ArrayList<String>();
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();

    public SettingsPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        fragments.add(new SettingsHomeFragment());
        nameIndex.add(SETTINGSHOME);
        fragments.add(new WhitelistFragment());
        nameIndex.add(WHITELIST);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0)
            return new SettingsHomeFragment();
        else if (position == 1)
            return new WhitelistFragment();
        return null;
    }

    @NonNull
    public Fragment getFragment(int position) {
        if (position >= 0 && position < fragments.size())
            return fragments.get(position);
        return null;
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }

    public int getIndexByName(String name) {
        for (String s : nameIndex)
            if (s.equalsIgnoreCase(name)) {
                return nameIndex.indexOf(s);
            }
        return -1;
    }

}
