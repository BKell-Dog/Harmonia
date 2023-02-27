package com.example.harmonialauncher.Blur;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.Nullable;

public class WallpaperView extends androidx.appcompat.widget.AppCompatImageView {
    private static final String TAG = WallpaperView.class.getSimpleName();

    private float blurRadius = 1;
    private Shader.TileMode shaderMode = Shader.TileMode.MIRROR;
    private RenderEffect effect;

    public WallpaperView(Context context) {
        super(context);
    }

    public WallpaperView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WallpaperView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        if (effect == null)
            updateEffect();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            setRenderEffect(effect);
        }
        super.onDraw(canvas);
    }

    public float getBlurRadius() {
        return blurRadius;
    }

    public void setBlurRadius(float blurRadius) {
        this.blurRadius = blurRadius;
        Log.d(TAG, "setBlurRadius: " + blurRadius);
        updateEffect();
        invalidate();
    }

    private void updateEffect()
    {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            float trueBlurRadius = (blurRadius > 0) ? blurRadius : 1;
            RenderEffect effect = RenderEffect.createBlurEffect(trueBlurRadius, trueBlurRadius, shaderMode);
        }
    }

    public Shader.TileMode getShaderMode() {
        return shaderMode;
    }

    public void setShaderMode(Shader.TileMode shaderMode) {
        this.shaderMode = shaderMode;
    }
}
