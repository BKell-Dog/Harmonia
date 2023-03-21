package com.example.harmonialauncher.blur;

import static com.example.harmonialauncher.preferences.PreferenceData.STYLE_GREYSCALE;
import static com.example.harmonialauncher.preferences.PreferenceData.STYLE_NORMAL;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

import com.example.harmonialauncher.preferences.PreferenceData;
import com.example.harmonialauncher.R;

public class WallpaperView extends androidx.appcompat.widget.AppCompatImageView implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = WallpaperView.class.getSimpleName();

    private float blurRadius = 1;
    private Shader.TileMode shaderMode = Shader.TileMode.MIRROR;
    private RenderEffect effect;
    private boolean greyscale, dimmed = false;
    private SharedPreferences prefs;

    public WallpaperView(Context context) {
        super(context);
        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String preference = prefs.getString(getResources().getString(R.string.set_app_screen_style_key), STYLE_NORMAL + "");
        if (preference.equalsIgnoreCase(""))
            preference = PreferenceData.LAYOUT_GRID + "";
        int prefInt  = Integer.parseInt(preference);
        greyscale = (prefInt == STYLE_GREYSCALE);
        prefs.registerOnSharedPreferenceChangeListener(this);
    }

    public WallpaperView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String preference = prefs.getString(getResources().getString(R.string.set_app_screen_style_key), STYLE_NORMAL + "");
        if (preference.equalsIgnoreCase(""))
            preference = PreferenceData.LAYOUT_GRID + "";
        int prefInt  = Integer.parseInt(preference);
        greyscale = (prefInt == STYLE_GREYSCALE);
        prefs.registerOnSharedPreferenceChangeListener(this);
    }

    public WallpaperView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String preference = prefs.getString(getResources().getString(R.string.set_app_screen_style_key), STYLE_NORMAL + "");
        if (preference.equalsIgnoreCase(""))
            preference = PreferenceData.LAYOUT_GRID + "";
        int prefInt  = Integer.parseInt(preference);
        greyscale = (prefInt == STYLE_GREYSCALE);
        prefs.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDraw(Canvas canvas) {
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

    private void updateEffect() {
        Drawable background = getDrawable();
        if (background == null)
            return;

        float[] defaultMatrix = new float[]{1, 0, 0, 0, 0,
                0, 1, 0, 0, 0,
                0, 0, 1, 0, 0,
                0, 0, 0, 1, 0};

        ColorMatrix matrix = new ColorMatrix(defaultMatrix);

        //Handle wallpaper greyscale
        if (greyscale)
            matrix.setSaturation(0);
        else
            matrix.setSaturation(1);

        //Handle wallpaper dim
        float rl = 0.213f, gl = 0.715f, bl = 0.072f;    //Red luminance, green luminance, and blue luminance
        float l = 0.70f;                                   //Luminance scale factor
        if (dimmed) {
            float[] dimMatrix = new float[]{rl * l + gl + bl, gl * l - gl, bl * l - bl, 0, 0,
                    rl * l - rl, gl * l + rl + bl, bl * l - bl, 0, 0,
                    rl * l - rl, gl * l - gl, bl * l + rl + gl, 0, 0,
                    0, 0, 0, 1, 0};
            matrix.setConcat(matrix, new ColorMatrix(dimMatrix));
        }

        background.setColorFilter(new ColorMatrixColorFilter(matrix));
        setImageDrawable(background);

        //Handle wallpaper blur
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

    public boolean isDimmed() {
        return dimmed;
    }

    public void setDimmed(boolean dim) {
        dimmed = dim;
        updateEffect();
        invalidate();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equalsIgnoreCase(getResources().getString(R.string.set_app_screen_style_key))) {
            int style = Integer.parseInt(sharedPreferences.getString(key, PreferenceData.STYLE_NORMAL + ""));
            if (style == PreferenceData.STYLE_NORMAL)
                greyscale = false;
            else if (style == STYLE_GREYSCALE)
                greyscale = true;
        }
    }

    public void printMatrix(float[] matrix) {
        StringBuilder s = new StringBuilder();
        s.append("[");
        for (float v : matrix)
            s.append(v).append(", ");
        s.append("]");
        Log.d(TAG, "printMatrix: " + s.toString());
    }
}
