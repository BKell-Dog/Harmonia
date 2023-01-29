package com.example.harmonialauncher.Views;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

public class ViewPagerWrapper extends ViewGroup {
    private static final String TAG = ViewPagerWrapper.class.getSimpleName();
    private ViewPager2 vp;

    public ViewPagerWrapper(Context context) {
        super(context);
        vp = new ViewPager2(context);
        Log.d(TAG, "ViewPagerWrapper: Constructor 1");
    }

    public ViewPagerWrapper(Context context, AttributeSet attrs) {
        super(context, attrs);
        vp = new ViewPager2(context);
        Log.d(TAG, "ViewPagerWrapper: Constructor 2");
        setWillNotDraw(false);
    }

    public ViewPagerWrapper(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        vp = new ViewPager2(context);
        Log.d(TAG, "ViewPagerWrapper: Constructor 3");
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ViewPagerWrapper(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        vp = new ViewPager2(context);
        Log.d(TAG, "ViewPagerWrapper: Constructor 4");
    }
    
    @Override
    public boolean dispatchTouchEvent(MotionEvent event)
    {
        Log.d(TAG, "dispatchTouchEvent: ");
        return false;
    }

    public RecyclerView.Adapter getAdapter()
    {
        return vp.getAdapter();
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        Log.d(TAG, "onDraw: ");
        vp.draw(canvas);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event)
    {
        Log.d(TAG, "onInterceptTouchEvent: ");
        return false;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d(TAG, "onLayout: " + l + " -- " + t + " --- " + r + " -- " + b);
        this.setLayoutParams(new ViewGroup.LayoutParams(r-l, b-t));
    }

    @Override
    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec)
    {
        Log.d(TAG, "onMeasure: ");
        vp.measure(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        Log.d(TAG, "onTouchEvent: ");
        return false;
    }

    public void registerOnPageChangeCallback(@NonNull ViewPager2.OnPageChangeCallback callback)
    {
        vp.registerOnPageChangeCallback(callback);
    }

    public void setAdapter(@Nullable RecyclerView.Adapter a)
    {
        vp.setAdapter(a);
    }

    public void setCurrentItem(int x)
    {vp.setCurrentItem(x);}

    public void setOrientation(int orientation)
    {
        vp.setOrientation(orientation);
    }

    public void setUserInputEnabled(boolean b)
    {vp.setUserInputEnabled(b);}

    public void setVisibility(int visibility)
    {
        vp.setVisibility(visibility);
    }
}
