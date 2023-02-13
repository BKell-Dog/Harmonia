package com.example.harmonialauncher.LockManager;

import android.os.Build;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.harmonialauncher.Interfaces.Lockable;
import com.example.harmonialauncher.R;
import com.example.harmonialauncher.Utils.Util;

public class LockScreen {

    private static final String TAG = LockScreen.class.getSimpleName();

    public void showLockScreenWindow(View parent, Lockable l) {
        //Inflate lock screen view
        final View popupView = LayoutInflater.from(parent.getContext()).inflate(R.layout.lock_popup, null);

        //Specify lock screen text and progress bar percentage
        final TextView lsText = popupView.findViewById(R.id.lock_screen_progress_text);
        final ProgressBar lsBar = popupView.findViewById(R.id.lock_screen_progress_bar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            lsBar.setMin(0);
        lsBar.setMax(100);

        //Create the popup window
        int width = Util.getAppUsableScreenSize(parent.getContext()).x - 100;
        int height = Util.getAppUsableScreenSize(parent.getContext()).y - 100;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //Show the popup window
        popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);

        final long totalTime =100; //LockManager.getTimeRemaining(l);

        new CountDownTimer(/*LockManager.getTimeRemaining(l)*/ 30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long minutes = millisUntilFinished / 60000;
                long seconds = (millisUntilFinished / 1000) - (minutes * 60);

                //Log.d(TAG, "millisUntilFinished: " + millisUntilFinished + "\nSecondsUntilFinished: "  + (millisUntilFinished / 1000)
                //         + "\nSeconds: " + seconds + "\nMinutes: " + minutes + "\nNow: " + System.currentTimeMillis());

                //Ensure timer text is in the proper format, always with four digits
                if (minutes >= 10 && seconds >= 10)
                    lsText.setText(minutes + ":" + seconds);
                else if (minutes < 10 && seconds >= 10)
                    lsText.setText("0" + minutes + ":" + seconds);
                else if (minutes >= 10 && seconds < 10)
                    lsText.setText(minutes + ":0" + seconds);
                else if (minutes < 10 && seconds < 10)
                    lsText.setText("0" + minutes + ":0" + seconds);

                lsBar.setProgress((int) (millisUntilFinished / totalTime));
            }

            @Override
            public void onFinish() {
                popupWindow.dismiss();
            }
        }.start();
    }

}
