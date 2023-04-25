package com.example.harmonialauncher.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Upsert;

import java.util.List;

@Dao
public interface LockDao {
    @Upsert
    void upsert(LockEntity lockEntity);

    @Insert
    void insert(LockEntity lockEntity);

    @Update
    void update(LockEntity lockEntity);

    @Delete
    void delete(LockEntity lockEntity);

    @Query("DELETE FROM lock_table")
    void deleteAll();

    @Query("SELECT * FROM lock_table")
    LiveData<List<LockEntity>> getLockedList();

    @Query("SELECT * FROM lock_table ORDER BY locked_until ASC")
    LiveData<List<LockEntity>> getLockedListByTime();
}
