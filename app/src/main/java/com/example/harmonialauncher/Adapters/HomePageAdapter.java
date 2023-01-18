package com.example.harmonialauncher.Adapters;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public class HomePageAdapter extends PageAdapter {
    private static final String TAG = "Drawer Page Adapter";

    public HomePageAdapter(@NonNull FragmentActivity fragmentActivity, int numOfPages) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        try {
            return super.fragments.get(position);
        } catch (IndexOutOfBoundsException e) {
            Log.d(TAG, "Out of Bounds Exception: Index out of bounds in Drawer Page list.");
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}