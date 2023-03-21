package com.example.harmonialauncher.launcher;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import androidx.preference.PreferenceManager;

import com.example.harmonialauncher.Activities.HarmoniaActivity;
import com.example.harmonialauncher.Activities.MainActivity;
import com.example.harmonialauncher.R;
import com.example.harmonialauncher.applist.AppListActivity;
import com.example.harmonialauncher.error.ErrorMessageDialog;
import com.example.harmonialauncher.preferences.PreferenceData;

public class Launcher extends HarmoniaActivity {
    private static final String TAG = Launcher.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedinstanceState)
    {
        super.onCreate(savedinstanceState);
        setContentView(R.layout.empty);

        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            String layoutString = prefs.getString(getResources().getString(R.string.set_screen_layout_key), PreferenceData.LAYOUT_GRID + "");
            int layout = Integer.parseInt(layoutString);

            if (layout == PreferenceData.LAYOUT_GRID)
            {
                Log.i(TAG, "Attempting to start App Grid Activity");
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                this.finish();
            }
            else if (layout == PreferenceData.LAYOUT_LIST)
            {
                Log.i(TAG, "Attempting to start App Layout Activity");
                Intent intent = new Intent(this, AppListActivity.class);
                startActivity(intent);
                this.finish();
            }
            else
            {
                Log.i(TAG, "Attempting to start App Grid Activity by default. Check default preferences");
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                this.finish();
            }

        }
        catch (Exception e)
        {
            Log.e(TAG, "Error finding which activity to start.");
            ErrorMessageDialog.showDialog(this, e);
        }


    }
}
