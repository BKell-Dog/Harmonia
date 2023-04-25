package com.example.harmonialauncher.appgrid;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.example.harmonialauncher.appgrid.viewmodels.DrawerViewModel;
import com.example.harmonialauncher.database.AppEntity;
import com.example.harmonialauncher.gesture.FlingDetector;
import com.example.harmonialauncher.gesture.FlingListener;
import com.example.harmonialauncher.R;
import com.example.harmonialauncher.gesture.FlingCatcher;

import java.util.ArrayList;
import java.util.List;

public class DrawerFragment extends HarmoniaFragment implements FlingListener {

    private static final String TAG = DrawerFragment.class.getSimpleName();
    private Context CONTEXT;
    private FragmentActivity activity;
    private ViewPager2 vp = null;
    private DrawerPageAdapter adapter;
    private DrawerViewModel vm;
    private FlingCatcher fc;
    private ArrayList<AppObject> appList = new ArrayList<>();

    public DrawerFragment() {
        super(R.layout.horiz_app_pager);
    }

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        CONTEXT = context;
        activity = this.getActivity();

        vm = new ViewModelProvider(requireActivity()).get(DrawerViewModel.class);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vm.getAppList().observe(getViewLifecycleOwner(), new Observer<List<AppEntity>>() {
            @Override
            public void onChanged(List<AppEntity> appEntities) {
                appList = AppObject.Factory.toAppObjects(CONTEXT, appEntities);
                adapter = new DrawerPageAdapter(activity, appList);
                vp.post(() -> vp.setAdapter(adapter));
            }
        });

        if (vm.getAppList().getValue() != null)
            appList = AppObject.Factory.toAppObjects(CONTEXT, vm.getAppList().getValue());

        adapter = new DrawerPageAdapter(this.getActivity(), appList);

        View v = inflater.inflate(R.layout.horiz_app_pager, container, false);
        if (v == null || getActivity() == null)
            return null;

        fc = v.findViewById(R.id.fling_detector);
        fc.setCallback(this);
        fc.setMode(FlingDetector.HORIZONTAL);

        //Initialize view pager to scroll horizontally
        vp = v.findViewById(R.id.drawer_view_pager);
        vp.setAdapter(adapter);
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
        Log.d(TAG, "flingRight: ");
    }

    @Override
    public void flingLeft() {
        vm.setCurrentPage(vm.getCurrentPage() + 1);
        update();
    }
}
