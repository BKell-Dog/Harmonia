package com.example.harmonialauncher.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {HomeScreenAppEntity.class}, version = 1, exportSchema = false)
public abstract class HomeScreenDatabase extends RoomDatabase {

    public abstract HomeScreenDao hsDao();

    private static volatile HomeScreenDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static HomeScreenDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (HomeScreenDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    HomeScreenDatabase.class, "home_screen_database")
                            //.fallbackToDestructiveMigration() //Uncomment this line to handle migrations. It will destroy database and rebuild it from scratch.
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
