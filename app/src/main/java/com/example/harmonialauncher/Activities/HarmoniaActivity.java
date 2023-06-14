package com.example.harmonialauncher.Activities;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;
import android.provider.Settings;
import android.view.accessibility.AccessibilityManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.harmonialauncher.gesture.Gesturable;
import com.example.harmonialauncher.lock.Lockable;
import com.example.harmonialauncher.overlay.OverlayWindow;
import com.example.harmonialauncher.overlay.OverlayService;

import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class HarmoniaActivity extends AppCompatActivity implements Lockable, Gesturable {
    private static final String TAG = HarmoniaActivity.class.getSimpleName();

    protected boolean locked = false;
    protected boolean onScreen = false;

    public void lock() {
        locked = true;
    }

    public void lock(long millisLocked) {
        locked = true;
    }

    public void unlock() {
        locked = false;
    }

    public boolean isLocked() {
        return locked;
    }

    public long getTimeRemaining() {
        return 0;
    }

    public long getEndTime() {
        return 0;
    }

    //---------------------------------<Methods for Capturing Gestures>----------------------------
    public boolean onDown(MotionEvent event) {
        return false;
    }

    public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
        return false;
    }

    public void onLongPress(MotionEvent event) {
    }

    public boolean onSingleTapConfirmed(MotionEvent event) {
        return false;
    }

    // ------------------------------------<Methods for Tracking On Screen>-------------------------
    public void onStart() {
        onScreen = true;
        super.onStart();
    }

    public void onResume() {
        onScreen = true;
        super.onResume();
    }

    public void onDestroy() {
        onScreen = false;
        super.onDestroy();
    }

    public void onPause() {
        onScreen = false;
        super.onPause();
    }

    public void onStop() {
        onScreen = false;
        super.onStop();
    }

    public boolean isOnScreen() {
        return onScreen;
    }

    // ---------------------------------<Methods for Full Screen Overlay>---------------------------
    OverlayWindow window = null;

    public void checkOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                // Send user to the device settings
                Intent myIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                startActivity(myIntent);
            }
        }
    }

    public void checkAccessibilityServiecPermissions() {
        AccessibilityManager am = (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);
        List<AccessibilityServiceInfo> enabledServices = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK);

        boolean found = false;
        for (AccessibilityServiceInfo enabledService : enabledServices) {
            ServiceInfo enabledServiceInfo = enabledService.getResolveInfo().serviceInfo;
            if (enabledServiceInfo.packageName.equals(getPackageName()) && enabledServiceInfo.name.equals(OverlayService.class.getName())) {
                found = true;
            }
        }
        if (!found)
        {
            Intent myIntent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(myIntent);
        }
    }
}
