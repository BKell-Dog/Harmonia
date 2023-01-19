package com.example.harmonialauncher.Adapters;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.harmonialauncher.Fragments.DrawerPageFragment;

public class DrawerPageAdapter extends PageAdapter {
    private static final String TAG = "Drawer Page Adapter";

    public DrawerPageAdapter(@NonNull FragmentActivity fragmentActivity, int numOfPages) {
        super(fragmentActivity);

        //i < nOP - 1, this is to create one less drawer page than currently
        for (int i = 0; i <= numOfPages - 1; i++) {
            //Log.d(TAG, "Create Drawer Page " + i);
            super.fragments.add(new DrawerPageFragment(i));
            super.nameIndex.add("Drawer Page " + i);
        }
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        DrawerPageFragment dpf = new DrawerPageFragment(position);
        fragments.set(position, dpf);
        return dpf;
    }

    public void setPageOnScreen(int index) {
        for (int i = 0; i < fragments.size(); i++)
            if (i == index)
                ((DrawerPageFragment) fragments.get(i)).setOnScreen();
            else
                ((DrawerPageFragment) fragments.get(i)).setOffScreen();
    }
}
