package com.example.harmonialauncher.Activities;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.example.harmonialauncher.Helpers.AppObject;
import com.example.harmonialauncher.Utils.LockStatusChangeListener;
import com.example.harmonialauncher.R;
import com.example.harmonialauncher.Utils.Util;
import com.example.harmonialauncher.Utils.LockManager;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * This class will display a list of all apps and prompt the user to lock any of them for an amount
 * of time. Each item in this scrollview will consist of the following.
 * <p>
 * ____________________________________________________
 * | App Icon | App Name | App Lock Timer | Edit Button|
 * |          |          |                |            |
 * |  ...     |   ...    |      ...       |  ...       |
 */

//TODO: implement the above schema

public class LockActivity extends HarmoniaActivity {

    private static final String TAG = "Lock Activity";
    private final Context context = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lock_activity);

        //Populate LinearLayout with items
        //Each item consists of app icon, app name, and time picker.
        //At the bottom of the LinearLayout is a button which applies lock times to LockManager.
        LinearLayout ll = findViewById(R.id.lock_activity_linearlayout);
        ArrayList<AppObject> apps = Util.loadAllApps(this);
        for (AppObject a : apps) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.list_item, null);
            TextView text = v.findViewById(R.id.app_name);
            ImageView icon = v.findViewById(R.id.app_icon);
            TextView timer = v.findViewById(R.id.lock_timer_text);
            Button button = v.findViewById(R.id.edit_button);

            text.setText(a.getName());
            if (a.getImage() != null)
                icon.setImageDrawable(a.getImage());
            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                icon.setImageDrawable(getResources().getDrawable(a.getImageId(), null));
            else
                icon.setImageDrawable(getResources().getDrawable(a.getImageId()));
            icon.setLayoutParams(new RelativeLayout.LayoutParams(Util.getRealScreenSize(this).x / 4, Util.getRealScreenSize(this).x / 4));

            if (LockManager.isLocked(a))
                setTimerText(timer, (int) TimeUnit.HOURS.convert(LockManager.getTimeRemaining(a), TimeUnit.MILLISECONDS), (int) TimeUnit.MINUTES.convert(LockManager.getTimeRemaining(a) % 3600000, TimeUnit.MILLISECONDS));
            else
                timer.setText("00:00");

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final Button b = (Button) view;

                    // Create NumberPicker popup
                    final Dialog d = new Dialog(context);

                    //Disable default title generated by android
                    d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    //Allow user to cancel dialog by clicking anywhere outside dialog box
                    d.setCancelable(true);
                    d.setContentView(R.layout.time_picker);
                    d.show();

                    final NumberPicker hours = d.findViewById(R.id.numberPicker1);
                    final NumberPicker minutes = d.findViewById(R.id.numberPicker2);
                    Button ok = d.findViewById(R.id.submit_time_button);
                    Button cancel = d.findViewById(R.id.cancel_button);

                    hours.setMinValue(0);
                    hours.setMaxValue(99);
                    hours.setWrapSelectorWheel(false);
                    minutes.setMinValue(0);
                    minutes.setMaxValue(59);
                    minutes.setWrapSelectorWheel(false);

                    //Find app object associated with button press
                    LinearLayoutCompat parent = (LinearLayoutCompat) view.getParent().getParent();
                    String appName = null;
                    for (int i = 0; i < parent.getChildCount(); i++) {
                        if (parent.getChildAt(i).getId() == R.id.app_name_layout) {
                            appName = ((TextView) ((RelativeLayout) parent.getChildAt(i)).getChildAt(0)).getText().toString();
                            //This line shows a TextView within a RelativeLayout, which is wihtin the same LinearLayoutCompat at the
                            //RelativeLayout which holds our CheckBox. We extract the app name from that TextView.
                        }
                    }
                    if (appName == null)
                        return;
                    final AppObject app = Util.findAppByName(appName, context);

                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int hour = hours.getValue();
                            int minute = minutes.getValue();
                            int millis = (hour * 3600000) + (minute * 60000);
                            LockManager.lock(app.getPackageName(), millis);
                            LockManager.lock(app, millis);

                            setTimerText(timer, hour, minute);
                            Log.d(TAG, "LOCKED APP: " + app + " for time: " + millis);
                            d.dismiss();
                        }
                    });

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            d.cancel();
                        }
                    });
                }
            });

            ll.addView(v);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        LockStatusChangeListener.onStatusChanged();
    }

    /**
     * Method to properly format timer text.
     */
    private void setTimerText(TextView timer, int hour, int minute) {
        String h = "", m = "";
        if (hour < 10)
            h = "0" + hour;
        else
            h = "" + hour;

        if (minute < 10)
            m = "0" + minute;
        else
            m = "" + minute;

        timer.setText(h + ":" + m);
    }
}