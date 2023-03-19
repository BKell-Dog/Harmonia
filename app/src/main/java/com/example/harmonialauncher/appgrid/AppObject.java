package com.example.harmonialauncher.appgrid;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

import com.example.harmonialauncher.lock.Lockable;

public class AppObject implements Lockable {
    private String name, packageName;
    private Drawable image = null;
    private Boolean isAppInDrawer;
    private int imageId;
    private boolean locked = false;

    public AppObject(String packageName, String name, int imageId, Boolean isAppInDrawer) {
        this.name = name;
        this.packageName = packageName;
        this.imageId = imageId;
        this.isAppInDrawer = isAppInDrawer;
    }

    public AppObject(String packageName, String name, int imageId, Drawable image, Boolean isAppInDrawer) {
        this.name = name;
        this.packageName = packageName;
        this.image = image;
        this.imageId = imageId;
        this.isAppInDrawer = isAppInDrawer;
    }

    //This constructor is primarily used for info on other downloaded apps, and should not be used
    //whenever possible.
    public AppObject(String packageName, String name, Drawable image, Boolean isAppInDrawer) {
        this.name = name;
        this.packageName = packageName;
        this.image = image;
        this.isAppInDrawer = isAppInDrawer;
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

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int ID) {
        imageId = ID;
    }

    public Boolean getIsAppInDrawer() {
        return isAppInDrawer;
    }

    public void setIsAppInDrawer(Boolean isAppInDrawer) {
        this.isAppInDrawer = isAppInDrawer;
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
}