package com.example.harmonialauncher.Database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class AppRepository {

    private AppDao mAppDao;
    private LiveData<List<AppEntity>> mAllApps;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public AppRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mAppDao = db.appDao();
        mAllApps = mAppDao.getAlphabetizedWords();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<AppEntity>> getAllApps() {
        return mAllApps;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    void insert(AppEntity appEntity) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mAppDao.insert(appEntity);
        });
    }
}