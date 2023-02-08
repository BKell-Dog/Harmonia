package com.example.harmonialauncher.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.harmonialauncher.Adapters.DrawerPageAdapter;
import com.example.harmonialauncher.Helpers.FlingDetector;
import com.example.harmonialauncher.Interfaces.PageHolder;
import com.example.harmonialauncher.Utils.HarmoniaGestureDetector;
import com.example.harmonialauncher.MainActivity;
import com.example.harmonialauncher.Adapters.PageAdapter;
import com.example.harmonialauncher.R;
import com.example.harmonialauncher.Utils.Util;
import com.example.harmonialauncher.ViewModels.DrawerPageViewModel;
import com.example.harmonialauncher.ViewModels.DrawerViewModel;
import com.example.harmonialauncher.Views.FlingCatcher;

import java.util.Objects;

public class DrawerFragment extends HarmoniaFragment implements PageHolder {

    private static final String TAG = DrawerFragment.class.getSimpleName();
    public final int THRESHOLD = 100;
    public ViewPager2 vp = null;
    private DrawerViewModel vm;
    private FlingCatcher fc;

    public DrawerFragment() {
        super(R.layout.drawer_fragment);
    }

    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
        vm = new ViewModelProvider(requireActivity()).get(DrawerViewModel.class);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.drawer_fragment, container, false);
        if (v == null || getActivity() == null)
            return null;

        fc = v.findViewById(R.id.fling_detector);
        fc.setCallback(this);
        fc.setMode(FlingDetector.HORIZONTAL);

        //Initialize view pager to scroll horizontally
        vp = v.findViewById(R.id.drawer_view_pager);
        vp.setAdapter(new DrawerPageAdapter(this.getActivity()));
        vp.setUserInputEnabled(false);
        vp.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        vp.canScrollHorizontally(1);
        vp.setCurrentItem(0);
        vp.setVisibility(View.VISIBLE);
        vp.setOffscreenPageLimit(7);
        vp.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                ((DrawerPageAdapter) vp.getAdapter()).setPageOnScreen(position);
            }
        });

        return v;
    }

    public int getCurrentPageIndex() {
        return vp != null ? vp.getCurrentItem() : -1;
    }

    public int getLastPageIndex() {
        return vp != null ? Objects.requireNonNull(vp.getAdapter()).getItemCount() - 1 : -1;
    }

    public HarmoniaFragment getCurrentPage() {
        return vp != null ? (HarmoniaFragment) ((DrawerPageAdapter) vp.getAdapter()).createFragment(vp.getCurrentItem()) : null;
    }

    public void incrementPage()
    {
        if (vp.getCurrentItem() < vp.getAdapter().getItemCount()) {
            vp.setCurrentItem(vp.getCurrentItem() + 1);
            vp.invalidate();
        }
    }

    public void decrementPage()
    {
        if (vp.getCurrentItem() > 0) {
            vp.setCurrentItem(vp.getCurrentItem() - 1);
            vp.invalidate();
        }
    }

    @NonNull
    public String toString() {
        if (vp == null)
            return "";
        StringBuilder s = new StringBuilder("Drawer Fragment. Children: ");
        for (int i = 0; i < Objects.requireNonNull(vp.getAdapter()).getItemCount(); i++) {
            s.append(((DrawerPageAdapter) this.vp.getAdapter()).getFragment(i)).append("\n");
        }
        return s.toString();
    }
}
