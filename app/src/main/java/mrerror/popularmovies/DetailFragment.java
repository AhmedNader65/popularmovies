package mrerror.popularmovies;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import mrerror.popularmovies.data.MovieColumns;
import mrerror.popularmovies.data.MoviesDataBase;
import mrerror.popularmovies.data.MoviesProvider;


/**
 * Created by ahmed on 19/07/16.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
	private static final String[] DETAIL_COLUMNS = {
			MoviesDataBase.Movies + "." + MovieColumns._ID,
			MovieColumns.POSTER,
			MovieColumns.OVERVIEW,
			MovieColumns.DATE,
			MovieColumns.TITLE,
			MovieColumns.VOTE,
			MovieColumns.MOVIE_ID
			// This works because the WeatherProvider returns location data joined with
			// weather data, even though they're stored in two different tables.
	};


	public static final int COL_ID = 0;
	public static final int COL_MOVIES_POSTER = 1;
	public static final int COL_MOVIES_OVERVIEW = 2;
	public static final int COL_MOVIES_DATE= 3;
	public static final int COL_MOVIES_TITLE = 4;
	public static final int COL_MOVIES_VOTE = 5;
	public static final int COL_MOVIES_ID = 6;


	private static final int DETAIL_LOADER = 0;

	private Uri mUri;
	ImageView thumbnail;
	TextView title,date,rate,overview;
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getLoaderManager().initLoader(DETAIL_LOADER,null,this);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.detailfragment,container,false);
		 thumbnail = (ImageView)v.findViewById(R.id.thumbnail);
		title = (TextView) v.findViewById(R.id.title);
		date = (TextView) v.findViewById(R.id.date);
		 rate = (TextView) v.findViewById(R.id.rate);
		 overview = (TextView) v.findViewById(R.id.overview);
//		Picasso.with(getContext()).load(link).into(thumbnail);
		return v;
	}
	@Override
	public Loader onCreateLoader(int id, Bundle args) {
		Long movie_id = getActivity().getIntent().getLongExtra("id",0);

			mUri = MoviesProvider.Movies.withId(movie_id);
			Log.e("wooow", "hghg3");
			if (mUri != null) {
				Log.e("wooow", mUri.toString());
				return new CursorLoader(getActivity(), mUri, DETAIL_COLUMNS, null, null, null);
			}

		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
			if (!data.moveToFirst()) {
				return;
			}

			int movieId = data.getInt(COL_ID);
			Toast.makeText(getContext(), movieId + " gfgd", Toast.LENGTH_SHORT).show();
			Glide.with(getContext()).load("http://image.tmdb.org/t/p/w500/" + data.getString(COL_MOVIES_POSTER)).into(thumbnail);
			title.setText(data.getString(COL_MOVIES_TITLE));
			date.setText(data.getString(COL_MOVIES_DATE));
			rate.setText(data.getString(COL_MOVIES_VOTE));
			overview.setText(data.getString(COL_MOVIES_OVERVIEW));
			Log.e("hii", "id is " + movieId);

	}

	@Override
	public void onLoaderReset(Loader loader) {

	}
}
