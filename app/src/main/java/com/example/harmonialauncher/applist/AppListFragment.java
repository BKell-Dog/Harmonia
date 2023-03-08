package com.example.harmonialauncher.applist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.harmonialauncher.Fragments.HarmoniaFragment;
import com.example.harmonialauncher.Helpers.SingleTapDetector;
import com.example.harmonialauncher.R;
import com.example.harmonialauncher.Utils.Util;
import com.example.harmonialauncher.appgrid.AppGridViewModel;
import com.example.harmonialauncher.appgrid.AppObject;
import com.example.harmonialauncher.appgrid.Views.AppGridView;

import java.util.ArrayList;

public class AppListFragment extends Fragment {
    private final static String TAG = AppListFragment.class.getSimpleName();

    private Context CONTEXT;
    private AppGridViewModel vm;
    private SingleTapDetector std;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        vm = new ViewModelProvider(requireActivity()).get(AppGridViewModel.class);
        CONTEXT = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        std = new SingleTapDetector(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.app_list_view, container, false);



        return v;
    }
}
