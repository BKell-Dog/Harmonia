package com.example.harmonialauncher.Drawer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.harmonialauncher.R;
import com.example.harmonialauncher.PageAdapter;
import com.example.harmonialauncher.Util;
import com.example.harmonialauncher.lockManager.HarmoniaFragment;

public class DrawerFragment extends HarmoniaFragment {

    private static final String TAG = "Drawer Fragment";
    private int numOfPages;
    public ViewPager2 vp = null;

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
        vp = v.findViewById(R.id.drawer_view_pager);
        vp.setAdapter(new DrawerPageAdapter(this.getActivity(), numOfPages));
        vp.setUserInputEnabled(false);
        vp.canScrollHorizontally(1);
        vp.setCurrentItem(0);
        vp.setVisibility(View.VISIBLE);

        return v;
    }

    public boolean setPage(int index)
    {
        if (vp != null) {
            try {
                vp.setCurrentItem(index);
                return true;
            } catch (IndexOutOfBoundsException e) {Log.d(TAG, "Index Out of Bounds in Drawer Fragment."); e.printStackTrace();}
            catch (Exception e) {e.printStackTrace();}
        }
        return false;
    }

    public int getCurrentPage()
    {return vp != null ? vp.getCurrentItem() : -1;}

    public int getLastPage()
    {return vp != null ? vp.getAdapter().getItemCount() - 1 : -1;}

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

    public String toString()
    {
        if (vp == null)
            return "";
        String s = "";
        for (int i = 0; i < vp.getAdapter().getItemCount(); i++)
        {
            s += ((DrawerPageAdapter)this.vp.getAdapter()).getFragment(i).toString() + "\n";
        }
        return s;
    }
}
