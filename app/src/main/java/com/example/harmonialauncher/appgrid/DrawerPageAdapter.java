package com.example.harmonialauncher.appgrid;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.harmonialauncher.Adapters.PageAdapter;
import com.example.harmonialauncher.appgrid.viewmodels.AppGridViewModel;

import java.util.ArrayList;

public class DrawerPageAdapter extends PageAdapter {
    private static final String TAG = DrawerPageAdapter.class.getSimpleName();

    public DrawerPageAdapter(@NonNull FragmentActivity fragmentActivity, int pageCount) {
        super(fragmentActivity, pageCount);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Log.d(TAG, "Return new drawer page fragment: " + position);
        return new DrawerPageFragment(position);
    }
}
