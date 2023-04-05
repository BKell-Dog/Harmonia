package com.example.harmonialauncher.appgrid;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

import com.example.harmonialauncher.database.AppEntity;
import com.example.harmonialauncher.Utils.Util;
import com.example.harmonialauncher.lock.Lockable;

import java.util.ArrayList;
import java.util.List;

public class AppObject implements Lockable {
    private String name, packageName;
    private Drawable image = null;
    private Integer imageId = null;
    private boolean locked = false;

    public AppObject(String packageName, String name, int imageId) {
        this.name = name;
        this.packageName = packageName;
        this.imageId = imageId;
    }

    public AppObject(String packageName, String name, int imageId, Drawable image) {
        this.name = name;
        this.packageName = packageName;
        this.image = image;
        this.imageId = imageId;
    }

    //This constructor is primarily used for info on other downloaded apps, and should not be used
    //whenever possible.
    public AppObject(String packageName, String name, Drawable image) {
        this.name = name;
        this.packageName = packageName;
        this.image = image;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(int ID) {
        imageId = ID;
    }

    @NonNull
    public String toString() {
        String s = "";
        s += "App Name: " + name + "\n";
        s += "Package Name: " + packageName + "\n";
        return s;
    }

    @Override
    public void lock() {
        locked = true;
    }

    @Override
    public void unlock() {
        locked = false;
    }

    @Override
    public boolean isLocked() {
        return locked;
    }

    public static class Factory {
        public static ArrayList<AppObject> toAppObjects(@NonNull Context context, @NonNull List<AppEntity> entities) {
            ArrayList<AppObject> apps = new ArrayList<>();
            for (AppEntity entity : entities)
                apps.add(toAppObject(context, entity));
            return apps;
        }

        public static AppObject toAppObject(@NonNull Context context, @NonNull AppEntity entity)
        {
            AppObject app = new AppObject(entity.packageName, entity.appName, entity.imageId);
            try {
                app.setImage(Util.getDrawableByResource(context, entity.imageId));
            } catch (Exception e)
            {
                app.setImage(null);
            }
            return app;
        }
    }
}