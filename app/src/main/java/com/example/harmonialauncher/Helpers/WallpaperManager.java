package com.example.harmonialauncher.Helpers;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.example.harmonialauncher.R;
import com.example.harmonialauncher.Utils.Util;

public class WallpaperManager {

    private static Drawable wallpaper = null;

    private static final int DEFAULT_WALLPAPER_RESOURCE = R.drawable.default_wallpaper;

    public static Drawable getWallpaper(Context c)
    {
        if (wallpaper == null)
            return Util.getDrawableByResource(c, DEFAULT_WALLPAPER_RESOURCE);
        else
            return wallpaper;
    }

    public static void setWallpaper(Drawable d)
    {
        wallpaper = d;
    }

    public static Drawable getDefaultWallpaper(Context c)
    {return Util.getDrawableByResource(c, DEFAULT_WALLPAPER_RESOURCE);}
}
