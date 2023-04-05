package com.example.harmonialauncher.lock;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

import com.example.harmonialauncher.Utils.Util;
import com.example.harmonialauncher.appgrid.AppObject;

public class LockMedia {
    private static final String TAG = LockMedia.class.getSimpleName();

    public static final int APP = 0, WEBSITE = 1;
    private int type = -1;
    private String name = null, packageName = null, URL = null;
    private Drawable icon = null;

    public LockMedia(@NonNull String name, @NonNull Drawable icon, @NonNull Integer type, @NonNull String extra) {
        this.name = name;
        this.icon = icon;

        if (type == APP) {
            this.packageName = extra;
            this.type = APP;
        }
        else if (type == WEBSITE) {
            this.URL = extra;
            this.type = WEBSITE;
        }
    }

    public LockMedia(@NonNull AppObject app, @NonNull Context c)
    {
        name = app.getName();
        packageName = app.getPackageName();
        type = APP;
        icon = Util.getAppDrawable(app, c);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public Integer getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}