package mrerror.popularmovies;


import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mrerror.popularmovies.data.MovieColumns;
import mrerror.popularmovies.data.MoviesDataBase;
import mrerror.popularmovies.data.MoviesProvider;

/**
 * Created by ahmed on 19/07/16.
 */
public class MoviesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
	private static final String LOG_TAG = MoviesFragment.class.getSimpleName();
	private RecyclerView recyclerView;
	private recyclerAdapter adapter;
	private static final int CURSOR_LOADER_ID = 0;
	private static final String[] MOVIES_COLUMNS = {
			// In this case the id needs to be fully qualified with a table name, since
			// the content provider joins the location & weather tables in the background
			// (both have an _id column)
			// On the one hand, that's annoying.  On the other, you can search the weather table
			// using the location set by the user, which is only in the Location table.
			// So the convenience is worth it.
			MoviesDataBase.Movies + "." + MovieColumns._ID,
			MoviesDataBase.Movies + "." + MovieColumns.MOVIE_ID,
			MovieColumns.POSTER
	};

	// These indices are tied to FORECAST_COLUMNS.  If FORECAST_COLUMNS changes, these
	// must change.
	static final int COL_MOVIE_ID = 0;
	static final int COL_MOVIE_POSTER = 1;

	public MoviesFragment(){
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v =inflater.inflate(R.layout.moviesfragment,container,false);
		recyclerView = (RecyclerView)v.findViewById(R.id.content);

		GridLayoutManager gridLayoutManager  = new GridLayoutManager(getContext(),2);
		recyclerView.setLayoutManager(gridLayoutManager);
		adapter = new recyclerAdapter(getContext(),null);
		recyclerView.setAdapter(adapter);
		return v;
	}


	@Override
	public void onStart() {
		super.onStart();
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
		FetchDataFromApi(sharedPreferences.getString(getString(R.string.order_pref_key),getString(R.string.order_pref_default)));
	}

	public void FetchDataFromApi(String order){

	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getLoaderManager().initLoader(CURSOR_LOADER_ID,null,this);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args){
		Log.e("hii","started");
		return new CursorLoader(getActivity(), MoviesProvider.Movies.CONTENT_URI,
				MOVIES_COLUMNS,
				null,
				null,
				null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data){
		adapter.swapCursor(data);

	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader){
		adapter.swapCursor(null);
	}
}

