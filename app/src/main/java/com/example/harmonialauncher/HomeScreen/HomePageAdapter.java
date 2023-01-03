package com.example.harmonialauncher.HomeScreen;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.harmonialauncher.PageAdapter;

import java.util.ArrayList;

public class HomePageAdapter extends PageAdapter {
    private static final String TAG = "Drawer Page Adapter";

    public HomePageAdapter(@NonNull FragmentActivity fragmentActivity, int numOfPages) {
        super(fragmentActivity);
    }

    @Override
    public Fragment createFragment(int position)
    {
        try {
            return super.fragments.get(position);
        }
        catch (IndexOutOfBoundsException e) {Log.d(TAG, "Out of Bounds Exception: Index out of bounds in Drawer Page list.");return null;}
        catch (Exception e) {e.printStackTrace();return null;}

    }
}