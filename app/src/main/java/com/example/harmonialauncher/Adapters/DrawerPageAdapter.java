package com.example.harmonialauncher.Adapters;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.harmonialauncher.Fragments.AppGridFragment;
import com.example.harmonialauncher.Fragments.DrawerFragment;
import com.example.harmonialauncher.Fragments.DrawerPageFragment;
import com.example.harmonialauncher.Utils.Util;
import com.example.harmonialauncher.ViewModels.AppGridViewModel;

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
