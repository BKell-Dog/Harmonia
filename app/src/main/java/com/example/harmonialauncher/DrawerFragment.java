package com.example.harmonialauncher;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.harmonia.R;
import com.example.harmonialauncher.lockManager.HarmoniaFragment;

import java.util.ArrayList;

public class DrawerFragment extends HarmoniaFragment {

    private static final String TAG = "Drawer Fragment";

    public DrawerFragment()
    {
        super(R.layout.drawer_fragment);

        //Load all apps into Arraylist, then find how many drawer pages are needed.
        //Each page in the drawer will hold twenty apps at most, a 5x4 grid (rows x cols)
        int numOfPages = (Util.loadAllApps().size() / 20) + 1;

        //Initialize view pager to scroll horizontally
        ViewPager2 vp = (ViewPager2) getView().findViewById(R.id.drawer_view_pager);
        vp.setAdapter(new DrawerPageAdapter(this.getActivity(), numOfPages));
        vp.setUserInputEnabled(true);
        vp.canScrollHorizontally(1);
        vp.setCurrentItem(0);
        vp.setVisibility(View.VISIBLE);
    }

    public class DrawerPageAdapter extends PageAdapter {
        private static final String TAG = "Drawer Page Adapter";

        public DrawerPageAdapter(@NonNull FragmentActivity fragmentActivity, int numOfPages) {
            super(fragmentActivity);

            for (int i = 0; i < numOfPages; i++)
            {
                super.fragments.add(new DrawerPageFragment());
                super.nameIndex.add("Drawer " + i);
            }
        }
    }
}
