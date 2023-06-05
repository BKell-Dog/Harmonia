package com.example.harmonialauncher.overlay;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class OverlayWorker extends Worker {
    private static final String TAG = OverlayWorker.class.getSimpleName();

    private OverlayWindow window = null;

    public OverlayWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "doWork: ");
        // Query Activity Manager for list of running activities.

        // Find foreground activity.

        // Check foreground activity against database of blurred apps.

        // Update window blurriness.


        return Result.success();
    }

    public void setWindow(OverlayWindow window)
    {
        this.window = window;
    }
}
