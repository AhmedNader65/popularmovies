package mrerror.popularmovies.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mrerror.popularmovies.R;
import mrerror.popularmovies.Utility;
import mrerror.popularmovies.data.MovieColumns;
import mrerror.popularmovies.data.MoviesProvider;
import mrerror.popularmovies.models.Movies;
import mrerror.popularmovies.models.MySingleton;

public class PopularmoviesSyncAdapter extends AbstractThreadedSyncAdapter {
    public final String LOG_TAG = PopularmoviesSyncAdapter.class.getSimpleName();
    private ArrayList<Movies> movies;
    // Interval at which to sync with the weather, in milliseconds.
// 60 seconds (1 minute)  180 = 3 hours
    public static final int SYNC_INTERVAL = 60*180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;
    public PopularmoviesSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }
    int pos ;
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(LOG_TAG, "onPerformSync Called.");
        String orderQuiery = Utility.getOrder(getContext());

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        Uri.Builder uri = Uri.parse("https://api.themoviedb.org/3/movie/"+orderQuiery+"?api_key="+getContext().getString(R.string.api_key)).buildUpon();
        Log.v(LOG_TAG,uri.toString());
        final JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, uri.toString(), null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v(LOG_TAG,response.toString());
                        try {
                            movies = new ArrayList<>();
                            String title,thumbnail, synopsis,rating,date ;
                            JSONArray jsonArray = response.getJSONArray("results");
                            for(int i = 0 ; i < jsonArray.length();i++){
                                pos = i;
                                JSONObject movieObj = jsonArray.getJSONObject(i);
                                title = movieObj.getString("title");
                                thumbnail = movieObj.getString("poster_path");
                                synopsis = movieObj.getString("overview");
                                rating = movieObj.getString("vote_average");
                                date = movieObj.getString("release_date");
                                final int id = movieObj.getInt("id");
                                addMovie(id,title,thumbnail,synopsis,rating,date);
                            }
                            deleteData();

                            insertData();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v(LOG_TAG,error.toString());

                    }
                });
        MySingleton.getInstance(getContext()).addToRequestQueue(jsObjRequest);
    }
    private void addMovie(int movieId,String title, String thumbnail, String synopsis, String rating, String date){
        Movies newMovie = new Movies();
        newMovie.setTitle(title);
        newMovie.setThumbnail(thumbnail);
        newMovie.setSynopsis(synopsis);
        newMovie.setRating(rating);
        newMovie.setDate(date);
        newMovie.setMovie_id(movieId);
        movies.add(newMovie);

    }
    public void insertData(){
        ArrayList<ContentProviderOperation> batchOperations = new ArrayList<>(movies.size());

        for (Movies movie : movies){
            Log.d(LOG_TAG, "insert3");
            ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(
                    MoviesProvider.Movies.CONTENT_URI);
            builder.withValue(MovieColumns.TITLE, movie.getTitle());
            builder.withValue(MovieColumns.POSTER, movie.getThumbnail());
            builder.withValue(MovieColumns.OVERVIEW, movie.getSynopsis());
            builder.withValue(MovieColumns.VOTE, movie.getRating());
            builder.withValue(MovieColumns.DATE, movie.getDate());
            builder.withValue(MovieColumns.MOVIE_ID, movie.getMovie_id());
            batchOperations.add(builder.build());
        }

        try{
            getContext().getContentResolver().applyBatch(MoviesProvider.AUTHORITY, batchOperations);
        } catch(RemoteException | OperationApplicationException e){
            Log.e(LOG_TAG, "Error applying batch insert", e);
        }
    }
    public void deleteData(){

        getContext().getContentResolver().delete(MoviesProvider.Movies.CONTENT_URI,null,null);

    }

    /**
     * Helper method to have the sync adapter sync immediately
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if ( null == accountManager.getPassword(newAccount) ) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
            onAccountCreated(newAccount,context);
        }
        return newAccount;
    }
    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }


    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        PopularmoviesSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }

}