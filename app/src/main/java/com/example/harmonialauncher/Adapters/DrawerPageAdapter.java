package com.example.harmonialauncher.Adapters;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.harmonialauncher.Fragments.DrawerPageFragment;

public class DrawerPageAdapter extends PageAdapter {
    private static final String TAG = DrawerPageAdapter.class.getSimpleName();

    public DrawerPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);

        //i < nOP - 1, this is to create one less drawer page than currently
        for (int i = 0; i <= 5 - 1; i++) { //TODO: URGENT. Find a way to include data of how many pages we have from a config file, through a repository, and into the DrawerViewModel class.
            super.fragments.add(new DrawerPageFragment(i));
            super.nameIndex.add("Drawer Page " + i);
        }
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        DrawerPageFragment dpf = new DrawerPageFragment(position);
        fragments.set(position, dpf);
        Log.d(TAG, "createFragment: ");
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
