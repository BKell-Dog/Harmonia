package com.example.harmonialauncher.lockManager;

import androidx.fragment.app.Fragment;

import com.example.harmonialauncher.lockManager.Lockable;

import java.util.HashMap;

public class HarmoniaFragment extends Fragment implements Lockable {

    protected boolean locked = false;

    public HarmoniaFragment(int resource) {
        super(resource);
    }

    public void lock() {
        locked = true;
    }

    public void unlock() {
        locked = false;
    }

    public boolean isLocked() {
        return locked;
    }
}
