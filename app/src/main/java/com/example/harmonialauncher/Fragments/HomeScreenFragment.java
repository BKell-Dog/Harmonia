package com.example.harmonialauncher.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.example.harmonialauncher.Adapters.AppGridAdapter;
import com.example.harmonialauncher.Listeners.LockStatusChangeListener;
import com.example.harmonialauncher.R;
import com.example.harmonialauncher.ViewModels.AppGridViewModel;

/*
This class will manage the GridView which displays the apps, its construction and popualtion, and will
generate the apps to display as well. It will manage the default and preset packages, as well as the
pressing of buttons and opening of apps. This screen is the home screen and launcher.
 */

public class HomeScreenFragment extends AppGridFragment implements LockStatusChangeListener.LockStatusListener {
    private final static String TAG = HomeScreenFragment.class.getSimpleName();
    private AppGridViewModel vm;

    public HomeScreenFragment() {
        super(AppGridViewModel.TYPE_HOME, 0);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        vm = new ViewModelProvider(requireActivity()).get(AppGridViewModel.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new AppGridAdapter(CONTEXT, R.layout.app, vm.getAppList(AppGridViewModel.TYPE_HOME, 0));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}