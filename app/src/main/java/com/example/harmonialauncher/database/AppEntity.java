package com.example.harmonialauncher.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.harmonialauncher.appgrid.AppObject;

import java.util.ArrayList;
import java.util.List;

@Entity (tableName = "app_table")
public class AppEntity {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name="package_name")
    public String packageName;

    @NonNull
    @ColumnInfo(name="app_name")
    public String appName;

    @ColumnInfo(name="app_name_shortened")
    public String appNameShortened;

    @NonNull
    @ColumnInfo(name="image_id")
    public Integer imageId;

    /**
     * Blur interval refers to the amount of time this app will be allowed to be on screen before
     * becoming blurred through the full screen blur overlay feature. A null value represents that
     * the app will never be blurred, while a non-null value means that after that interval the
     * blurriness will increment, continuing until the app is unusable.
     */
    @Nullable
    @ColumnInfo(name="blur_interval")
    public Integer blurInterval = null;

    public AppEntity(@NonNull String appName, @NonNull String packageName, @Nullable String appNameShortened, @NonNull Integer imageId)
    {
        this.appName = appName;
        this.packageName = packageName;
        this.appNameShortened = appNameShortened;
        this.imageId = imageId;
    }

    @Ignore
    public AppEntity(@NonNull String appName, @NonNull String packageName, Integer imageId)
    {
        this(appName, packageName, null, imageId);
    }

    public void setBlurInterval(@Nullable Integer blurInterval)
    {
        this.blurInterval = blurInterval;
    }

    public static class Factory
    {
        public static ArrayList<AppEntity> toAppEntities(@NonNull List<AppObject> objects) {
            ArrayList<AppEntity> apps = new ArrayList<>();
            for (AppObject obj : objects)
                apps.add(toAppEntity(obj));
            return apps;
        }

        public static AppEntity toAppEntity(@NonNull AppObject obj)
        {
            return new AppEntity(obj.getName(), obj.getPackageName(), obj.getImageId());
        }
    }
}
