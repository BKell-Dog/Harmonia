package com.example.harmonialauncher.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface AppDao {

    // allowing the insert of the same word multiple times by passing a
    // conflict resolution strategy
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(AppEntity appEntity);

    @Query("DELETE FROM app_table")
    void deleteAll();

    @Query("SELECT * FROM app_table ORDER BY package_name ASC")
    LiveData<List<AppEntity>> getAlphabetizedWords();
}
