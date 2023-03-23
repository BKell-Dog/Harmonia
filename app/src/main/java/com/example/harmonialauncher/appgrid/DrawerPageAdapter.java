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
        super(fragmentActivity, (appList.size() / AppGridViewModel.NUMOFAPPSONPAGE));
        this.appList = appList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position < 0 && position > pageCount)
            position = 0;
        ArrayList<AppObject> list = new ArrayList<>(appList.subList(position * AppGridViewModel.NUMOFAPPSONPAGE, (position + 1) * AppGridViewModel.NUMOFAPPSONPAGE));
        Log.d(TAG, "createFragment: " + position + " ---" + list);
        return new AppGridFragment(list);
    }
}
