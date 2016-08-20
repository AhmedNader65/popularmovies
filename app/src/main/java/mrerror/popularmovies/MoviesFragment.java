package mrerror.popularmovies;


import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mrerror.popularmovies.data.MovieColumns;
import mrerror.popularmovies.data.MoviesDataBase;
import mrerror.popularmovies.data.MoviesProvider;
import mrerror.popularmovies.sync.PopularmoviesSyncAdapter;

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
	private static final String[] FAV_COLUMNS = {
			// In this case the id needs to be fully qualified with a table name, since
			// the content provider joins the location & weather tables in the background
			// (both have an _id column)
			// On the one hand, that's annoying.  On the other, you can search the weather table
			// using the location set by the user, which is only in the Location table.
			// So the convenience is worth it.
			MoviesDataBase.FavMovies + "." + MovieColumns._ID,
			MoviesDataBase.FavMovies + "." + MovieColumns.MOVIE_ID,
			MovieColumns.POSTER
	};

	// These indices are tied to FORECAST_COLUMNS.  If FORECAST_COLUMNS changes, these
	// must change.
	static final int COL_ID = 0;
	static final int COL_MOVIE_ID = 1;
	static final int COL_MOVIE_POSTER = 2;
	private GridLayoutManager gridLayoutManager;
	public static  int mPosition;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v =inflater.inflate(R.layout.moviesfragment,container,false);
		recyclerView = (RecyclerView)v.findViewById(R.id.content);
		gridLayoutManager = new GridLayoutManager(getContext(), 2);
		if(savedInstanceState!=null){
			mPosition = savedInstanceState.getInt("pos");
		}
		recyclerView.setLayoutManager(gridLayoutManager);
		adapter = new recyclerAdapter(getContext(),null);
		recyclerView.setAdapter(adapter);
		return v;
	}


	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if(mPosition!=0)
			outState.putInt("pos",mPosition);
	}
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getLoaderManager().initLoader(CURSOR_LOADER_ID,null,this);
		if(MainActivity.mTwoPane) {
			gridLayoutManager .setSpanCount(3);
		}else{
			gridLayoutManager .setSpanCount(2);
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args){
		if(Utility.getOrder(getContext()).equals("favorites")){
			return new CursorLoader(getActivity(), MoviesProvider.Favorites.CONTENT_URI,
					FAV_COLUMNS,
					null,
					null,
					null);
		}else {
			return new CursorLoader(getActivity(), MoviesProvider.Movies.CONTENT_URI,
					MOVIES_COLUMNS,
					null,
					null,
					null);
		}
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data){
		adapter.swapCursor(data);
		if(mPosition !=0){
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					if(MainActivity.mTwoPane) {
						recyclerView.smoothScrollToPosition(mPosition);
					}
				}
			}, 2000);
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader){
		adapter.swapCursor(null);
	}
	void onOrderChanged( ) {
		updateMovies();
		getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
	}

	private void updateMovies() {
		PopularmoviesSyncAdapter.syncImmediately(getContext());
	}
}

