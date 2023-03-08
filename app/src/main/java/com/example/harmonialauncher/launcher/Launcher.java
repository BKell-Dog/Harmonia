package com.example.harmonialauncher.launcher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.PreferenceManager;

import com.example.harmonialauncher.Activities.HarmoniaActivity;
import com.example.harmonialauncher.Activities.MainActivity;
import com.example.harmonialauncher.R;
import com.example.harmonialauncher.applist.AppListActivity;
import com.example.harmonialauncher.preferences.PreferenceData;

public class Launcher extends HarmoniaActivity {

    @Override
    public void onCreate(Bundle savedinstanceState)
    {
        super.onCreate(savedinstanceState);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String layoutString = prefs.getString(getResources().getString(R.string.set_screen_layout_key), PreferenceData.LAYOUT_GRID + "");
        int layout = Integer.parseInt(layoutString);

        if (layout == PreferenceData.LAYOUT_GRID)
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            this.finish();
        }
        else if (layout == PreferenceData.LAYOUT_LIST)
        {
            Intent intent = new Intent(this, AppListActivity.class);
            startActivity(intent);
            this.finish();
        }
    }
}
