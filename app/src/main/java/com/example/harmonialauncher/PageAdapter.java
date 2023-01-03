package com.example.harmonialauncher;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.harmonialauncher.Drawer.DrawerFragment;
import com.example.harmonialauncher.HomeScreen.HomeScreenFragment;

import java.util.ArrayList;

public abstract class PageAdapter extends FragmentStateAdapter {

    private static final String TAG = "Page Adapter";
    protected ArrayList<Fragment> fragments = new ArrayList<Fragment>();

    protected ArrayList<String> nameIndex = new ArrayList<String>();

    public PageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    public abstract Fragment createFragment(int position);

    @NonNull
    public Fragment getFragment(int position) {
        try {
            return fragments.get(position);
        }
        catch (IndexOutOfBoundsException e) {Log.d(TAG, "Out of Bounds Exception: Index out of bounds in Page Adapter list.");return null;}
        catch (Exception e) {e.printStackTrace();return null;}
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

    public Fragment remove(int position)
    {
        try {
            return fragments.remove(position);
        }
        catch (IndexOutOfBoundsException e) {Log.d(TAG, "Index out of bounds, cannot remove element."); e.printStackTrace();return null;}
        catch (Exception e) {e.printStackTrace();return null;}
    }
}
