package com.example.harmonialauncher.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public abstract class PageAdapter extends FragmentStateAdapter {
    private static final String TAG = "Page Adapter";

    protected ArrayList<String> nameIndex = new ArrayList<String>();
    protected int pageCount;

    public PageAdapter(@NonNull FragmentActivity fragmentActivity, int pageCount) {
        super(fragmentActivity);
        this.pageCount = pageCount;
    }


    @NonNull
    public abstract Fragment createFragment(int position);


    @Override
    public int getItemCount() {
        return pageCount;
    }
}
