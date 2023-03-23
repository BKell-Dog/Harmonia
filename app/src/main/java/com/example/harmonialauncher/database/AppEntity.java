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

    /*@ColumnInfo(name="home_page_index")
    public Integer homeIndex;

    @Nullable
    @ColumnInfo(name="drawer_page_num") // Indicates which page the app is on
    public Integer drawerPageNum;

    @NonNull
    @ColumnInfo(name="drawer_page_index") // Indicates where within the page the app lies
    public Integer drawerPageIndex;*/

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

    public static class Factory
    {
        public static ArrayList<AppEntity> toAppEntities(@NonNull Context context, @NonNull List<AppObject> objects) {
            ArrayList<AppEntity> apps = new ArrayList<>();
            for (AppObject obj : objects)
                apps.add(toAppEntity(context, obj));
            return apps;
        }

        public static AppEntity toAppEntity(@NonNull Context context, @NonNull AppObject obj)
        {
            return new AppEntity(obj.getName(), obj.getPackageName(), obj.getImageId());
        }
    }
}
