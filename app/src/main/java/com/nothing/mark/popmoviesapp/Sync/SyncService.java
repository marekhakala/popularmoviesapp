package com.nothing.mark.popmoviesapp.Sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SyncService extends Service {
    private static final Object sSyncAdapterMutex = new Object();
    private static SyncAdapter sPopularMoviesSyncAdapter = null;

    @Override
    public void onCreate() {
        synchronized (sSyncAdapterMutex) {
            if (sPopularMoviesSyncAdapter == null)
                sPopularMoviesSyncAdapter = new SyncAdapter(getApplicationContext(), true);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sPopularMoviesSyncAdapter.getSyncAdapterBinder();
    }
}