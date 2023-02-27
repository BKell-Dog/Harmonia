package com.example.harmonialauncher.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.harmonialauncher.AppGrid.DrawerPageAdapter;
import com.example.harmonialauncher.Helpers.FlingDetector;
import com.example.harmonialauncher.Interfaces.PageHolder;
import com.example.harmonialauncher.R;
import com.example.harmonialauncher.AppGrid.AppGridViewModel;
import com.example.harmonialauncher.ViewModels.DrawerViewModel;
import com.example.harmonialauncher.Views.FlingCatcher;

public class DrawerFragment extends HarmoniaFragment implements PageHolder {

    private static final String TAG = DrawerFragment.class.getSimpleName();
    public ViewPager2 vp = null;
    private DrawerViewModel vm;
    private FlingCatcher fc;

    public DrawerFragment() {
        super(R.layout.horiz_app_pager);
    }

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        AppGridViewModel agvm = new ViewModelProvider(requireActivity()).get(AppGridViewModel.class);
        vm = new ViewModelProvider(requireActivity(), new DrawerViewModel.DrawerViewModelFactory(this.getActivity().getApplication(), agvm.getNumOfPages())).get(DrawerViewModel.class);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.horiz_app_pager, container, false);
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
        vm.setCurrentPage(0);
        vp.setCurrentItem(vm.getCurrentPage());
        vp.setOffscreenPageLimit(5);

        return v;
    }

    public void incrementPage() {
        Log.d(TAG, "incrementPage: FLING DETECTED");
        vm.setCurrentPage(vm.getCurrentPage() + 1);
        update();
    }

    public void decrementPage() {
        Log.d(TAG, "decrementPage: FLING DETECTED");
        vm.setCurrentPage(vm.getCurrentPage() - 1);
        update();
    }

    public void update()
    {
        vp.setCurrentItem(vm.getCurrentPage());
    }

    @NonNull
    public String toString() {
        if (vp == null)
            return "";
        StringBuilder s = new StringBuilder("Drawer Fragment. Children: " + vm.getNumOfPages());
        return s.toString();
    }
}
