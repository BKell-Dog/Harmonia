package com.example.harmonialauncher.appgrid;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.harmonialauncher.Adapters.PageAdapter;
import com.example.harmonialauncher.Utils.Util;

public class DrawerPageAdapter extends PageAdapter {
    private static final String TAG = DrawerPageAdapter.class.getSimpleName();

    public DrawerPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity, (int) Math.ceil((double)Util.loadAllApps(fragmentActivity).size() / 20d));
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return (position >= 0 && position < pageCount) ? new AppGridFragment(AppGridViewModel.TYPE_DRAWER, position) : new AppGridFragment(AppGridViewModel.TYPE_DRAWER, 0);
    }
}
