package com.example.harmonialauncher.Database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "home_screen_apps", foreignKeys = {
        @ForeignKey(entity = AppEntity.class,
                    parentColumns = "package_name",
                    childColumns = "package_name",
                    onDelete = ForeignKey.CASCADE)
})
public class HomeScreenAppEntity {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name="page_index")
    public Integer pageIndex;

    public HomeScreenAppEntity(Integer pageIndex)
    {
        this.pageIndex = pageIndex;
    }

    public Integer getPageIndex()
    {return pageIndex;}

    public void setPageIndex(int newIndex)
    {
        pageIndex = newIndex;
    }
}
