package com.example.harmonialauncher;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class PageAdapter extends FragmentStateAdapter {

    private static final String TAG = "Page Adapter";
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();

    public ArrayList<String> nameIndex = new ArrayList<String>();
    public final String HOMESCREEN = "Home Screen",
                        SETTINGS = "Settings",
                        WHITELIST = "Whitelist";

    public PageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        fragments.add(new HomeScreenFragment());
        nameIndex.add(HOMESCREEN);
        fragments.add(new SettingsFragment());
        nameIndex.add(SETTINGS);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0)
            return new HomeScreenFragment();
        else if (position == 1)
            return new SettingsFragment();
        return null;
    }

    @NonNull
    public Fragment getFragment(int position)
    {
        if (position >= 0 && position < fragments.size())
            return fragments.get(position);
        return null;
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }

    public int getIndexByName(String name)
    {
        for (String s : nameIndex)
            if (s.equalsIgnoreCase(name)) {
                return nameIndex.indexOf(s);
            }
        return -1;
    }
}
