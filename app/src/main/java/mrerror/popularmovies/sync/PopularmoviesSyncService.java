package mrerror.popularmovies.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class PopularmoviesSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static PopularmoviesSyncAdapter sPopularmoviesSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.d("PopularSyncService", "onCreate - PopularSyncService");
        synchronized (sSyncAdapterLock) {
            if (sPopularmoviesSyncAdapter == null) {
                sPopularmoviesSyncAdapter = new PopularmoviesSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sPopularmoviesSyncAdapter.getSyncAdapterBinder();
    }
}