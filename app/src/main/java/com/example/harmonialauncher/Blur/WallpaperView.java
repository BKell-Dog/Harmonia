package com.example.harmonialauncher.Blur;

import android.content.Context;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class WallpaperView extends androidx.appcompat.widget.AppCompatImageView {
    private static final String TAG = WallpaperView.class.getSimpleName();

    private float blurRadius = 1;
    private DimValue dimValue = new DimValue(0);
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
        if (effect == null) {
            updateEffect();
        }
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
        updateEffect();
        invalidate();
    }

    private void updateEffect()
    {
        Drawable background = getDrawable();
        if (background == null)
            return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            background.setColorFilter(new BlendModeColorFilter(dimValue.getColorValue(), BlendMode.DARKEN));
        else
            background.setColorFilter(dimValue.getColorValue(), PorterDuff.Mode.DARKEN);
        setImageDrawable(background);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            float trueBlurRadius = (blurRadius > 1) ? blurRadius : 1;
            effect = RenderEffect.createBlurEffect(trueBlurRadius, trueBlurRadius, shaderMode);
        }
    }

    public Shader.TileMode getShaderMode() {
        return shaderMode;
    }

    public void setShaderMode(Shader.TileMode shaderMode) {
        this.shaderMode = shaderMode;
    }

    public DimValue getDimValue() {
        return dimValue;
    }

    public void setDimValue(DimValue dim) {
        this.dimValue = dim; //255 alpha = transparent
    }

    public static class DimValue
    {
        int dimColorValue;
        public static final int MAX_DIM = 255, NO_DIM = 0, HALF_DIM = 128, QUARTER_DIM = 64;
        public DimValue(int dim)
        {
            if (dim >= 0 && dim <= 255) {
                dimColorValue = Color.argb(dim, 0, 0, 0);
            }
            else
                throw new IndexOutOfBoundsException();
        }

        public int getColorValue()
        {return dimColorValue;}

        public String toString()
        {return "" + dimColorValue;}
    }
}
