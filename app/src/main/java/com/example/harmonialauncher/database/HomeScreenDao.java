package com.example.harmonialauncher.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Upsert;

import java.util.List;

@Dao
public interface HomeScreenDao {

    @Query("SELECT EXISTS (SELECT 1 FROM home_screen_apps)")
    boolean exists();

    @Insert
    void insert(HomeScreenAppEntity hsEntity);

    @Upsert
    void upsert(HomeScreenAppEntity hsEntity);

    @Update
    void update(HomeScreenAppEntity hsEntity);

    @Delete
    void delete(HomeScreenAppEntity hsEntity);

    @Query("DELETE FROM home_screen_apps")
    void deleteAll();

    @Query("SELECT * FROM home_screen_apps")
    LiveData<List<HomeScreenAppEntity>> getAppList();
}
