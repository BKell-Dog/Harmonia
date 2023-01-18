package com.example.harmonialauncher;

import android.graphics.drawable.Drawable;

import androidx.appcompat.content.res.AppCompatResources;

import com.example.harmonialauncher.lockManager.Lockable;

public class AppObject implements Lockable {
    private String name, packageName;
    private Drawable image = null;
    private Boolean isAppInDrawer;
    private int imageId;
    private int height = -1, width = -1;
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

    public String getName() {
        return name;
    }

    public Drawable getImage() {
        return image;
    }

    public int getImageId() {
        return imageId;
    }

    public Boolean getIsAppInDrawer() {
        return isAppInDrawer;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public void setImageId(int ID) {
        imageId = ID;
    }

    public void setIsAppInDrawer(Boolean isAppInDrawer) {
        this.isAppInDrawer = isAppInDrawer;
    }

    public void setWidth(int w) {
        width = w;
    }

    public void setHeight(int h) {
        height = h;
    }

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