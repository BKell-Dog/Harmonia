package com.example.harmonialauncher.appgrid;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.harmonialauncher.Adapters.PageAdapter;
import com.example.harmonialauncher.Utils.Util;

import java.util.ArrayList;

public class DrawerPageAdapter extends PageAdapter {
    private static final String TAG = DrawerPageAdapter.class.getSimpleName();
    private ArrayList<AppObject> appList;

    public DrawerPageAdapter(@NonNull FragmentActivity fragmentActivity, @NonNull ArrayList<AppObject> appList) {
        super(fragmentActivity, (int) Math.ceil(((double)appList.size()) / ((double)AppGridViewModel.NUMOFAPPSONPAGE)));
        this.appList = appList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position < 0 && position > pageCount)
            position = 0;

        int start = position * AppGridViewModel.NUMOFAPPSONPAGE,
                end = (position + 1) * AppGridViewModel.NUMOFAPPSONPAGE;

        ArrayList<AppObject> appSubList = new ArrayList<>();
        for (int i = start; i < appList.size() && i < end; i++)
            appSubList.add(appList.get(i));

        return new AppGridFragment(appSubList);
    }
}
