package com.example.harmonialauncher.appgrid;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.harmonialauncher.Fragments.HarmoniaFragment;
import com.example.harmonialauncher.database.AppEntity;
import com.example.harmonialauncher.gesture.FlingDetector;
import com.example.harmonialauncher.gesture.FlingListener;
import com.example.harmonialauncher.R;
import com.example.harmonialauncher.gesture.FlingCatcher;

import java.util.List;

public class DrawerFragment extends HarmoniaFragment implements FlingListener {

    private static final String TAG = DrawerFragment.class.getSimpleName();
    private Context CONTEXT;
    private FragmentActivity activity;
    public ViewPager2 vp = null;
    private DrawerViewModel vm;
    private FlingCatcher fc;

    public DrawerFragment() {
        super(R.layout.horiz_app_pager);
    }

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        CONTEXT = context;
        activity = this.getActivity();

        AppGridViewModel agvm = new ViewModelProvider(requireActivity()).get(AppGridViewModel.class);
        int numOfPages = 0;
        if (agvm.getDrawerScreenApps() != null)
            numOfPages = agvm.getDrawerScreenApps().size() / AppGridViewModel.NUMOFAPPSONPAGE;

        vm = new ViewModelProvider(requireActivity(), new DrawerViewModel.DrawerViewModelFactory(activity.getApplication(), numOfPages)).get(DrawerViewModel.class);

        Log.d(TAG, "onAttach: " + numOfPages);
        agvm.getAppList().observe(getActivity(), new Observer<List<AppEntity>>() {
            @Override
            public void onChanged(List<AppEntity> appEntities) {
                int numOfPages = appEntities.size() / AppGridViewModel.NUMOFAPPSONPAGE;
                vm = new ViewModelProvider(requireActivity(), new DrawerViewModel.DrawerViewModelFactory(activity.getApplication(), numOfPages)).get(DrawerViewModel.class);
                vp.setAdapter(new DrawerPageAdapter(activity));
            }
        });
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
        fc.setBackgroundColor(Color.RED);

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


    public void update()
    {
        vp.setCurrentItem(vm.getCurrentPage());
        vp.invalidate();
    }

    @NonNull
    public String toString() {
        if (vp == null)
            return "";
        StringBuilder s = new StringBuilder("Drawer Fragment. Children: " + vm.getNumOfPages());
        return s.toString();
    }

    @Override
    public void flingUp() {}

    @Override
    public void flingDown() {}

    @Override
    public void flingRight() {
        vm.setCurrentPage(vm.getCurrentPage() - 1);
        update();
    }

    @Override
    public void flingLeft() {
        vm.setCurrentPage(vm.getCurrentPage() + 1);
        update();
    }
}
