package com.example.harmonialauncher.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.harmonialauncher.Utils.HarmoniaGestureDetector;
import com.example.harmonialauncher.MainActivity;
import com.example.harmonialauncher.Adapters.PageAdapter;
import com.example.harmonialauncher.R;
import com.example.harmonialauncher.Utils.Util;

public class DrawerFragment extends HarmoniaFragment {

    private static final String TAG = "Drawer Fragment";
    public final int THRESHOLD = 100;
    public ViewPager2 vp = null;
    private int numOfPages;
    private GestureDetectorCompat gd;

    public DrawerFragment() {
        super(R.layout.drawer_fragment);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Load all apps into Arraylist, then find how many drawer pages are needed.
        //Each page in the drawer will hold twenty apps at most, a 5x4 grid (rows x cols)
        numOfPages = (Util.loadAllApps(this).size() / 20) + 1;

        gd = new GestureDetectorCompat(this.getActivity(), new HarmoniaGestureDetector());
        HarmoniaGestureDetector.add(this);
    }

    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.drawer_fragment, container, false);
        if (v == null || getActivity() == null)
            return null;

        //Initialize view pager to scroll horizontally
        vp = v.findViewById(R.id.drawer_view_pager);
        vp.setAdapter(new DrawerPageAdapter(this.getActivity(), numOfPages));
        vp.setUserInputEnabled(false);
        vp.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        vp.canScrollHorizontally(1);
        vp.setCurrentItem(0);
        vp.setVisibility(View.VISIBLE);
        vp.setOffscreenPageLimit(2);
        vp.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                ((DrawerPageAdapter) vp.getAdapter()).setPageOnScreen(position);
            }
        });

        vp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gd.onTouchEvent(motionEvent);
            }
        });

        return v;
    }

    public int getCurrentPageIndex() {
        return vp != null ? vp.getCurrentItem() : -1;
    }

    public int getLastPageIndex() {
        return vp != null ? vp.getAdapter().getItemCount() - 1 : -1;
    }

    public HarmoniaFragment getCurrentPage() {
        return vp != null ? (HarmoniaFragment) ((DrawerPageAdapter) vp.getAdapter()).createFragment(vp.getCurrentItem()) : null;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
        float e1y = event1.getY(), e2y = event2.getY();
        float e1x = event1.getX(), e2x = event2.getX();
        float xTranslation = e2x - e1x, yTranslation = e2y - e1y;

        if (Math.abs(xTranslation) > Math.abs(yTranslation)) //Fling more horizontal than vertical
        {
            //Horizontal flings will move between pages of the drawer, sent to the viewpager in DrawerFragment.
            if (xTranslation > THRESHOLD && getCurrentPageIndex() > 0) //Rightward fling
                vp.setCurrentItem(getCurrentPageIndex() - 1);
            else if (xTranslation < -THRESHOLD && getCurrentPageIndex() < getLastPageIndex()) //Leftward fling
                vp.setCurrentItem(getCurrentPageIndex() + 1);
        } else if (Math.abs(yTranslation) > Math.abs(xTranslation)) //Fling more vertical than horizontal
            //Vertical flings will move between home screen and app drawer, sent to the viewpager in MainActivity.
            if (yTranslation > THRESHOLD) //Downward fling
            {
                MainActivity main = (MainActivity) getActivity();
                if (main != null)
                    main.setPage(0);
                else
                    Log.d(TAG, "Main Activity Reference in Null");
            }
        return true;
    }

    @NonNull
    public String toString() {
        if (vp == null)
            return "";
        String s = "Drawer Fragment. Children: ";
        for (int i = 0; i < vp.getAdapter().getItemCount(); i++) {
            s += ((DrawerPageAdapter) this.vp.getAdapter()).getFragment(i).toString() + "\n";
        }
        return s;
    }

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

        public void setPageOnScreen(int index) {
            for (int i = 0; i < fragments.size(); i++)
                if (i == index)
                    ((DrawerPageFragment) fragments.get(i)).setOnScreen();
                else
                    ((DrawerPageFragment) fragments.get(i)).setOffScreen();
        }
    }
}
