package com.example.harmonialauncher.Settings;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.harmonialauncher.Activities.HarmoniaActivity;
import com.example.harmonialauncher.R;

public class SettingsActivity extends HarmoniaActivity {

    public static final String TAG = SettingsActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_page);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.lock_list_item, null);
        TextView text = v.findViewById(R.id.app_name);
        ImageView icon = v.findViewById(R.id.app_icon);
        TextView timer = v.findViewById(R.id.lock_timer_text);
        Button button = v.findViewById(R.id.edit_button);

    }

}
