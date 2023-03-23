package com.example.harmonialauncher.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Upsert;

import java.util.List;

@Dao
public interface AppDao {
    @Upsert
    void upsert(AppEntity appEntity);

    @Update
    void update(AppEntity appEntity);

    @Delete
    void delete(AppEntity appEntity);

    @Query("DELETE FROM app_table")
    void deleteAll();

    @Query("SELECT * FROM app_table")
    LiveData<List<AppEntity>> getAppList();

    @Query("SELECT * FROM app_table ORDER BY package_name ASC")
    LiveData<List<AppEntity>> getAppListAlphabetized();
}
