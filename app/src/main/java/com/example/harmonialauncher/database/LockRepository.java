package com.example.harmonialauncher.database;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.harmonialauncher.lock.LockStatusChangeListener;

import java.util.List;

public class LockRepository {
    private final String TAG = LockRepository.class.getSimpleName();

    private LiveData<List<LockEntity>> mLockedList;
    private LockDao mLockDao;

    public LockRepository(Application application)
    {
        LockDatabase db = LockDatabase.getDatabase(application);
        mLockDao = db.lockDao();
        mLockedList = mLockDao.getLockedList();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<LockEntity>> getLockedList() {
        return mLockedList;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void upsert(LockEntity lockEntity) {
        LockDatabase.databaseWriteExecutor.execute(() -> {
            mLockDao.upsert(lockEntity);
        });
    }

    public void upsert(List<LockEntity> locked)
    {
        for (LockEntity l : locked)
            upsert(l);
    }

    public void update(LockEntity lockEntity) {
        LockDatabase.databaseWriteExecutor.execute(() -> {
            mLockDao.update(lockEntity);
        });
    }

    public void remove(LockEntity lockEntity) {
        LockDatabase.databaseWriteExecutor.execute(() -> {
            mLockDao.delete(lockEntity);
        });
    }

}
