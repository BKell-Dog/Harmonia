package com.example.harmonialauncher.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.harmonialauncher.Fragments.DrawerFragment;
import com.example.harmonialauncher.Fragments.HomeScreenFragment;

public class HomePageAdapter extends PageAdapter {
    private static final String TAG = HomePageAdapter.class.getSimpleName();

    public HomePageAdapter(@NonNull FragmentActivity fragmentActivity, int numOfPages) {
        super(fragmentActivity, 2);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new DrawerFragment();
        }
        return new HomeScreenFragment();
    }
}