package com.example.harmonialauncher.Database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

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

    @Nullable
    @ColumnInfo(name="page_num")
    public Integer pageNum;

    @NonNull
    @ColumnInfo(name="page_index")
    public Integer pageIndex;

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
}
