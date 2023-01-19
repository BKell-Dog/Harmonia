package com.example.harmonialauncher.Adapters;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.harmonialauncher.Fragments.DrawerFragment;
import com.example.harmonialauncher.Fragments.HomeScreenFragment;

public class HomePageAdapter extends PageAdapter {
    private static final String TAG = "Drawer Page Adapter";

    public HomePageAdapter(@NonNull FragmentActivity fragmentActivity, int numOfPages) {
        super(fragmentActivity);
    }

    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new HomeScreenFragment();
            case 1:
                return new DrawerFragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount()
    {return 2;}
}