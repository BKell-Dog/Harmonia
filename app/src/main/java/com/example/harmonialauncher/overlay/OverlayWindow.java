package com.example.harmonialauncher.overlay;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import static android.content.Context.WINDOW_SERVICE;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.example.harmonialauncher.R;

public class OverlayWindow {

    private static final String TAG = OverlayWindow.class.getSimpleName();
    private Context context;
    private View mView;
    private WindowManager.LayoutParams mParams;
    private WindowManager mWindowManager;
    private LayoutInflater layoutInflater;

    public OverlayWindow(Context context) {
        this.context = context;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Set the layout parameters of the window
            mParams = new WindowManager.LayoutParams(
                    // Set the window to fill the screen
                    WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.MATCH_PARENT,
                    // Display it on top of other application windows
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    // Don't let it grab the input focus or any touch event, let touch events pass straight through
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    // Make the underlying application window visible through any transparent parts
                    PixelFormat.TRANSLUCENT);
        }

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // Inflating the view with the custom layout we created
        mView = layoutInflater.inflate(R.layout.overlay, null);
        // Define the position of the window within the screen
        mParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
        mWindowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
    }

    public void open() {
        try {
            // Check if the view is already inflated or present in the window
            if (mView.getWindowToken() == null) {
                if (mView.getParent() == null) {
                    Log.d(TAG, "OVERLAY START");
                    mWindowManager.addView(mView, mParams);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

    }

    public void close() {
        if (mWindowManager == null)
            mWindowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);

        try {
            // Remove the view from the window
            mWindowManager.removeView(mView);
            // Invalidate the view
            mView.invalidate();
            // Remove all views
            ((ViewGroup) mView.getParent()).removeAllViews();

            // The above steps are necessary when you are adding and removing
            // the view simultaneously, it might give some exceptions
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    public void setBlur(Float newBlurRadius)
    {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            if (newBlurRadius == null || newBlurRadius == 0f) {
                mView.setRenderEffect(null);
                return;
            }
            RenderEffect effect = RenderEffect.createBlurEffect(newBlurRadius, newBlurRadius, Shader.TileMode.MIRROR);
            mView.setRenderEffect(effect);
        }
    }
}
