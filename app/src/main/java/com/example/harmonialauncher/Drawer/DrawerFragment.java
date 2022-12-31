package com.example.harmonialauncher.Drawer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.harmonialauncher.R;
import com.example.harmonialauncher.PageAdapter;
import com.example.harmonialauncher.Util;
import com.example.harmonialauncher.lockManager.HarmoniaFragment;

public class DrawerFragment extends HarmoniaFragment {

    private static final String TAG = "Drawer Fragment";
    private int numOfPages;

    public DrawerFragment() {
        super(R.layout.drawer_fragment);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Load all apps into Arraylist, then find how many drawer pages are needed.
        //Each page in the drawer will hold twenty apps at most, a 5x4 grid (rows x cols)
        numOfPages = (Util.loadAllApps(this).size() / 20) + 1;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.drawer_fragment, container, false);
        if (v == null)
            return null;

        //Initialize view pager to scroll horizontally
        ViewPager2 vp = (ViewPager2) v.findViewById(R.id.drawer_view_pager);
        vp.setAdapter(new DrawerPageAdapter(this.getActivity(), numOfPages));
        vp.setUserInputEnabled(true);
        vp.canScrollHorizontally(1);
        vp.setCurrentItem(0);
        vp.setVisibility(View.VISIBLE);

        return v;
    }

    public class DrawerPageAdapter extends PageAdapter {
        private static final String TAG = "Drawer Page Adapter";

        public DrawerPageAdapter(@NonNull FragmentActivity fragmentActivity, int numOfPages) {
            super(fragmentActivity);

            //i < nOP - 1, this is to create one less drawer page than currently
            for (int i = 0; i < numOfPages - 1; i++) {
                super.fragments.add(new DrawerPageFragment(i));
                super.nameIndex.add("Drawer Page " + i);
            }
        }
    }
}
