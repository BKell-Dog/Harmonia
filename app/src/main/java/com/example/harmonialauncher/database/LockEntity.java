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

@Entity(tableName = "lock_table", primaryKeys = {"app_package_name", "url"})
public class LockEntity {

    @ColumnInfo(name="app_package_name")
    @NonNull
    public String appPackageName;

    @ColumnInfo(name="url")
    @NonNull
    public String URL;

    @ColumnInfo(name="locked_until")
    @NonNull
    public Long lockedUntil;

    public LockEntity(@NonNull String appPackageName, @NonNull String URL, @NonNull Long lockedUntil)
    {
        this.appPackageName = appPackageName;
        this.URL = URL;
        this.lockedUntil = lockedUntil;
    }

    public String toString()
    {
        return "LockedEntity: " + appPackageName + " -- " + URL + " -- " + lockedUntil;
    }
}
