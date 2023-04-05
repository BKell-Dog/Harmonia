package com.example.harmonialauncher.lock;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.harmonialauncher.Fragments.HarmoniaFragment;
import com.example.harmonialauncher.Helpers.TimeHelper;
import com.example.harmonialauncher.R;
import com.example.harmonialauncher.Utils.Util;
import com.example.harmonialauncher.appgrid.AppObject;
import com.example.harmonialauncher.database.AppEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * This class will display a list of all apps and prompt the user to lock any of them for an amount
 * of time. Each item in this scrollview will consist of the following.
 * <p>
 * ____________________________________________________
 * | App Icon | App Name | App Lock Timer | Edit Button|
 * |          |          |                |            |
 * |  ...     |   ...    |      ...       |  ...       |
 */
public class LockAppPicker extends HarmoniaFragment implements LockStatusChangeListener.LockStatusListener {
    private static final String TAG = LockAppPicker.class.getSimpleName();

    private LockActivityViewModel vm;
    private ArrayList<AppObject> appList = new ArrayList<>();
    private LinearLayout ll;

    public LockAppPicker(int resource, ArrayList<AppObject> apps) {
        super(resource);
        appList = apps;
    }

    public LockAppPicker() {
        super(R.layout.lock_app_picker);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        vm = new ViewModelProvider(this).get(LockActivityViewModel.class);
        vm.getAllApps().observe(this, new Observer<List<AppEntity>>() {
            @Override
            public void onChanged(List<AppEntity> appEntities) {
                appList.clear();
                for (AppEntity a : appEntities)
                    appList.add(AppObject.Factory.toAppObject(requireContext(), a));
            }
        });

        LockStatusChangeListener.add(this);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.lock_app_picker, container, false);

        //Set window params regarding SystemUI
        WindowCompat.setDecorFitsSystemWindows(requireActivity().getWindow(), false);
        WindowInsets insets = null;
        int navigationBarHeight = 0, statusBarHeight = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            insets = requireActivity().getWindowManager().getCurrentWindowMetrics().getWindowInsets();
            statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top; //in pixels
            navigationBarHeight = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom; //in pixels
        } else {
            navigationBarHeight = 150;
            statusBarHeight = 120;
        }
        ScrollView scrollView = (ScrollView) v.findViewById(R.id.lock_activity_scroll_view);
        scrollView.setPadding(0, statusBarHeight, 0, navigationBarHeight);

        ll = v.findViewById(R.id.lock_activity_linearlayout);
        populateLinearLayout(ll);

        return v;
    }

    private void populateLinearLayout(LinearLayout ll) {
        if (ll == null)
            return;
        if (appList == null || appList.size() == 0)
            return;

        TextView title = (TextView) ll.getChildAt(0);
        ll.removeAllViews();
        ll.addView(title);

        //Populate LinearLayout with items
        for (AppObject a : appList) {
            View v = createListItem(a);
            ll.addView(v);
        }
    }

    public View createListItem(AppObject a) {
        //Each item consists of app icon, app name, and time picker.

        LayoutInflater inflater = (LayoutInflater) requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.lock_list_item, null);
        TextView text = v.findViewById(R.id.app_name);
        ImageView icon = v.findViewById(R.id.app_icon);
        TextView timer = v.findViewById(R.id.lock_timer_text);

        final String appName = a.getName();

        text.setText(appName);

        Drawable image = null;
        if (a.getImage() != null)
            image = a.getImage();
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            try {
                image = ResourcesCompat.getDrawable(getResources(), a.getImageId(), null);
            } catch (Exception e) {
                try {
                    image = requireContext().getPackageManager().getApplicationIcon(a.getPackageName());
                } catch (PackageManager.NameNotFoundException ex) {
                    image = ResourcesCompat.getDrawable(getResources(), R.drawable.error_icon, null);
                }
            }
        else // i.e. VERSION < LOLLIPOP
            try {
                image = ResourcesCompat.getDrawable(getResources(), a.getImageId(), null);
            } catch (Exception e) {
                image = ResourcesCompat.getDrawable(getResources(), R.drawable.error_icon, null);
            }
        icon.setImageDrawable(image);
        icon.setLayoutParams(new RelativeLayout.LayoutParams(Util.getRealScreenSize(requireContext()).x / 4, Util.getRealScreenSize(requireContext()).x / 4));

        if (LockManager.isLocked(a)) {
            timer.setText(LockManager.getTimeRemaining(a.getPackageName()).getTimeFormatted(TimeHelper.HHMM));
            icon.setImageDrawable(Util.convertToGreyscale(image));
        }
        else
            timer.setText("00:00");

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppObject app = Util.findAppByName(appList, appName);

                // Create NumberPicker popup
                if (app != null) {
                    Dialog d = new LockTimeDialog(requireContext(), new LockMedia(app, requireContext()));
                    d.show();
                }
            }
        });
        return v;
    }

    public void setAppList(ArrayList<AppObject> apps)
    {
        appList = apps;
        populateLinearLayout(ll);
    }

    @Override
    public void onStatusChanged() {
        populateLinearLayout(ll);
    }
}

