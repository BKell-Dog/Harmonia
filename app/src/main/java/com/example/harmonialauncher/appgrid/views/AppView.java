package com.example.harmonialauncher.appgrid.views;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.example.harmonialauncher.appgrid.AppObject;
import com.example.harmonialauncher.R;
import com.example.harmonialauncher.Utils.Util;
import com.example.harmonialauncher.gesture.SingleTapDetector;
import com.example.harmonialauncher.lock.Lockable;

public class AppView extends LinearLayout implements Lockable {  //in future: extends CellElement or CellView
    private static final String TAG = AppView.class.getSimpleName();

    private boolean locked = false;
    protected String packageName = null;

    private SingleTapDetector std;

    public AppView(Context context) {
        super(context);
        setupAttributes();
    }

    public AppView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setupAttributes();
    }

    public AppView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupAttributes();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AppView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setupAttributes();
    }

    private void setupAttributes() {
        setId(R.id.app_layout);
        setTag("app_layout");
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        setBackgroundColor(getResources().getColor(R.color.clear));
        setOrientation(LinearLayout.VERTICAL);
        setPadding(10, 10, 10, 10);
        setWeightSum(100f);

        std = new SingleTapDetector(getContext());
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event == null || std == null)
            return true;
        //boolean singleTap = std.onTouch(null, event);
        if (/*singleTap && */!locked && packageName != null && event.getActionMasked() == MotionEvent.ACTION_UP && getContext() != null)
            Util.openApp(getContext(), packageName);
        return true;
    }

    public String getAppName() {
        TextView text = (TextView) ((LinearLayout) getChildAt(1)).getChildAt(0);
        return text.getText().toString();
    }

    public Drawable getImageDrawable() {
        return getImageView().getDrawable();
    }

    public ImageView getImageView() {
        return (ImageView) ((LinearLayout) getChildAt(0)).getChildAt(0);
    }

    public String getText() {
        return getTextView().getText().toString();
    }

    public TextView getTextView() {
        return (TextView) ((LinearLayout) getChildAt(1)).getChildAt(0);
    }

    public View getView() {
        return this;
    }

    public void setIconLayoutParams(LinearLayout.LayoutParams params) {
        getImageView().setLayoutParams(params);
    }

    public void setImageDrawable(Drawable d) {
        ImageView image = getImageView();
        image.setImageDrawable(d);
    }

    public void setImageViewDimens(int width, int height) {
        setIconLayoutParams(new LinearLayout.LayoutParams(width, height));
    }

    public void setText(String t) {
        TextView text = getTextView();
        text.setText(t);
    }

    public void setTextLayoutParams(LinearLayout.LayoutParams params) {
        getTextView().setLayoutParams(params);
    }

    public View remake() {
        removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View v = inflater.inflate(R.layout.app, (ViewGroup) getRootView());
        if (v instanceof ViewGroup) {
            ViewGroup view = (ViewGroup) v;
            for (int i = 0; i < view.getChildCount(); i++) {
                addView(view.getChildAt(i));
            }
        }
        return this;
    }

    public void update() {
        setImageDrawable(getImageDrawable());
        setText(getText());
    }

    @Override
    public void lock() {
        locked = true;
        update();
    }

    @Override
    public void unlock() {
        locked = false;
        update();
    }

    @Override
    public boolean isLocked() {
        return locked;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageName() {
        return packageName;
    }


    public static class AppViewFactory {
        @SuppressLint("UseCompatLoadingForDrawables")
        public static AppView createAppView(AppObject a, Context context) {
            AppView appView = new AppView(context);
            appView.setText(a.getName());
            if (a.getImage() == null)
                if (a.getImageId() == 0)
                    try {
                        appView.setImageDrawable(context.getPackageManager().getApplicationIcon(a.getPackageName()));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        appView.setImageDrawable(Util.getDrawableByResource(context, R.drawable.error_icon));
                    }
                else
                    appView.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), a.getImageId(), null));
            else
                appView.setImageDrawable(a.getImage());
            return appView;
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        public static AppView createAppView(AppObject a, Context context, @Nullable AttributeSet attrs) {
            AppView appView = new AppView(context, attrs);
            appView.setText(a.getName());
            if (a.getImage() == null)
                if (a.getImageId() == 0)
                    try {
                        appView.setImageDrawable(context.getPackageManager().getApplicationIcon(a.getPackageName()));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        appView.setImageDrawable(Util.getDrawableByResource(context, R.drawable.error_icon));
                    }
                else
                    appView.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), a.getImageId(), null));
            else
                appView.setImageDrawable(a.getImage());
            return appView;
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        public static AppView createAppView(AppObject a, Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            AppView appView = new AppView(context, attrs, defStyleAttr);
            appView.setText(a.getName());
            if (a.getImage() == null)
                if (a.getImageId() == 0)
                    try {
                        appView.setImageDrawable(context.getPackageManager().getApplicationIcon(a.getPackageName()));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        appView.setImageDrawable(Util.getDrawableByResource(context, R.drawable.error_icon));
                    }
                else
                    appView.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), a.getImageId(), null));
            else
                appView.setImageDrawable(a.getImage());
            return appView;
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public static AppView createAppView(AppObject a, Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            AppView appView = new AppView(context, attrs, defStyleAttr, defStyleRes);
            appView.setText(a.getName());
            if (a.getImage() == null)
                if (a.getImageId() == 0)
                    try {
                        appView.setImageDrawable(context.getPackageManager().getApplicationIcon(a.getPackageName()));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        appView.setImageDrawable(Util.getDrawableByResource(context, R.drawable.error_icon));
                    }
                else
                    appView.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), a.getImageId(), null));
            else
                appView.setImageDrawable(a.getImage());
            return appView;
        }
    }
}
