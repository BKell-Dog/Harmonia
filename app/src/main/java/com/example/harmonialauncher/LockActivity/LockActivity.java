package com.example.harmonialauncher.LockActivity;

import android.os.Bundle;

import com.example.harmonialauncher.R;
import com.example.harmonialauncher.lockManager.HarmoniaActivity;

public class LockActivity extends HarmoniaActivity {

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lock_activity);
    }

}
