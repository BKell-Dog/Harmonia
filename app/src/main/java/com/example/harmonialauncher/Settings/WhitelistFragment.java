package com.example.harmonialauncher.Settings;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.example.harmonialauncher.Util;
import com.example.harmonialauncher.lockManager.HarmoniaFragment;
import com.example.harmonialauncher.R;

public class WhitelistFragment extends HarmoniaFragment {

    private static final String TAG = "Whitelist Fragment";
    private Context CONTEXT;

    public WhitelistFragment() {
        super(com.example.harmonialauncher.R.layout.whitelist_screen);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CONTEXT = getActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.whitelist_screen, container, false);

        //Set proper padding so that everything appears on screen
        ConstraintLayout ll = (ConstraintLayout) v.findViewById(R.id.whitelist_screen);
        ll.setPadding(0, Util.getNavigationBarSize(CONTEXT).y, 0, Util.getNavigationBarSize(CONTEXT).y);

        //Set list view adapter to show the menu items
        ListView lv = (ListView) v.findViewById(R.id.whitelist_list_view);
        WhitelistAdapter adapter = new WhitelistAdapter(CONTEXT, R.layout.list_menu_item, Util.loadAllApps());
        lv.setAdapter(adapter);

        lv.setFooterDividersEnabled(true);

        Button b = (Button) v.findViewById(R.id.whitelist_accept_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToSettings();
            }
        });

        return v;
    }

    private void backToSettings() {
        ((ViewPager2) getActivity().findViewById(R.id.settings_view_pager)).setCurrentItem(0);
    }
}
