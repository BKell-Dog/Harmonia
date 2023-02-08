package com.example.harmonialauncher.Views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.harmonialauncher.R;

public class AppView extends LinearLayout {  //extends CellElement or CellView
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

    private void setupAttributes()
    {
        setId(R.id.app_layout);
        setTag("app_layout");
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        setBackgroundColor(getResources().getColor(R.color.clear));
        setOrientation(LinearLayout.VERTICAL);
        setPadding(10, 10, 10, 10);
        setWeightSum(100f);
    }

    public String getAppName()
    {
        TextView text = (TextView) ( (LinearLayout)getChildAt(1) ).getChildAt(0);
        return text.getText().toString();
    }

    public Drawable getImageDrawable()
    {
        return getImageView().getDrawable();
    }

    public ImageView getImageView()
    {
        return (ImageView) ( (LinearLayout)getChildAt(0) ).getChildAt(0);
    }

    public String getText()
    {
        return getTextView().getText().toString();
    }

    public TextView getTextView()
    {
        return (TextView) ( (LinearLayout)getChildAt(1) ).getChildAt(0);
    }

    public View getView()
    {return this;}

    public void setIconLayoutParams(LinearLayout.LayoutParams params)
    {
        getImageView().setLayoutParams(params);
    }

    public void setImageDrawable(Drawable d)
    {
        ImageView image = getImageView();
        image.setImageDrawable(d);
    }

    public void setText(String t)
    {
        TextView text = getTextView();
        text.setText(t);
    }

    public void setTextLayoutParams(LinearLayout.LayoutParams params)
    {
        getTextView().setLayoutParams(params);
    }

    public View remake()
    {
        removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View v = inflater.inflate(R.layout.app, (ViewGroup) getRootView());
        if (v instanceof ViewGroup) {
            ViewGroup view = (ViewGroup) v;
            for (int i = 0; i < view.getChildCount(); i++)
            {
                addView(view.getChildAt(i));
            }
        }
        return this;
    }


}
