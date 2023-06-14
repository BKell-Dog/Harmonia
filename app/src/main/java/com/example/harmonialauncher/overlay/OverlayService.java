package com.example.harmonialauncher.overlay;

import android.accessibilityservice.AccessibilityService;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.harmonialauncher.R;
import com.example.harmonialauncher.database.AppDao;
import com.example.harmonialauncher.database.AppDatabase;
import com.example.harmonialauncher.database.AppEntity;

import java.util.ArrayList;
import java.util.List;

public class OverlayService extends AccessibilityService {
    private static final String TAG = OverlayService.class.getSimpleName();
    String CHANNEL_ID = TAG;

    private OverlayWindow window = null;

    // Blurred app database variables
    private LiveData<List<AppEntity>> appList;
    private ArrayList<String> blurredAppList = new ArrayList<>();
    private Observer<List<AppEntity>> observer = new Observer<List<AppEntity>>() {
        @Override
        public void onChanged(List<AppEntity> appEntities) {
            blurredAppList.clear();
            for (AppEntity ae : appEntities)
                if (ae.blurInterval != null)
                    blurredAppList.add(ae.packageName);
        }
    };

    @Override
    public void onServiceConnected()
    {
        AppDatabase db = AppDatabase.getDatabase(this);
        AppDao dao = db.appDao();
        appList = dao.getAppList();
        appList.observeForever(observer);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            // The foreground app has changed, check which app is now in the foreground
            String packageName = event.getPackageName().toString();
            Log.d(TAG, "onAccessibilityEvent: " + packageName);

            // Check if app package name is in the list of blurrable apps.
            // If true, show blur overlay.
            // Else, ignore.
            Log.d(TAG, "onAccessibilityEvent: " + blurredAppList);
            if (blurredAppList.contains(packageName) || packageName.equalsIgnoreCase("com.google.android.gm"))
                startOverlay();
            else
                endOverlay();
        }
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    public boolean onUnbind(Intent intent)
    {
        appList.removeObserver(observer);
        return super.onUnbind(intent);
    }

    public void startOverlay() {
        Log.d(TAG, "START OVERLAY WINDOW");
        window = new OverlayWindow(this);
        window.open();
    }

    public void endOverlay() {
        if (window != null)
            window.close();
    }
}
