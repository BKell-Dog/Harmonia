package com.example.harmonialauncher;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.harmonialauncher.Settings.SettingsPageAdapter;

public class SettingsFragment extends Fragment {
    private static final String TAG = "Settings Fragment";
    private Context CONTEXT;

    public SettingsFragment() {
        super(R.layout.settings_screen);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CONTEXT = getActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.settings_screen, container, false);

        ViewPager2 vp = (ViewPager2) v.findViewById(R.id.settings_view_pager);
        vp.setAdapter(new SettingsPageAdapter(this.getActivity()));
        vp.setUserInputEnabled(false);
        vp.setCurrentItem(0);

        return v;
    }
}
