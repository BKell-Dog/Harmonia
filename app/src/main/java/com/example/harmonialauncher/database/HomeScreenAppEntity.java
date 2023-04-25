package com.example.harmonialauncher.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.harmonialauncher.appgrid.AppObject;
import com.example.harmonialauncher.appgrid.viewmodels.AppGridViewModel;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "home_screen_apps", primaryKeys = {"package_name", "page_index"},
        indices = {@Index(value = {"package_name", "page_index"}, unique = true)})
public class HomeScreenAppEntity {

    @NonNull
    @ColumnInfo(name="package_name")
    public String packageName;

    @NonNull
    @ColumnInfo(name="page_index") // This setup only allows for a single home screen page. For multiple pages, will have to alter DB design and HSEntity.
    public Integer pageIndex;

    public HomeScreenAppEntity(@NonNull String packageName, @NonNull Integer pageIndex)
    {
        this.packageName = packageName;
        this.pageIndex = pageIndex;
    }

    public static ArrayList<HomeScreenAppEntity> toHSAE(@NonNull List<AppObject> apps)
    {
        ArrayList<HomeScreenAppEntity> hsaeList = new ArrayList<>();
        for (int i = 0; i < apps.size() && i < AppGridViewModel.NUMOFAPPSONPAGE; i++)
        {
            hsaeList.add(new HomeScreenAppEntity(apps.get(i).getPackageName(), i));
        }
        return hsaeList;
    }
}
