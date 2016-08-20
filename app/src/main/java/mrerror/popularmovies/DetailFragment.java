package mrerror.popularmovies;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mrerror.popularmovies.data.MovieColumns;
import mrerror.popularmovies.data.MoviesDataBase;
import mrerror.popularmovies.data.MoviesProvider;
import mrerror.popularmovies.models.MySingleton;
import mrerror.popularmovies.models.Review;
import mrerror.popularmovies.models.Trailer;


/**
 * Created by ahmed on 19/07/16.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
	private ShareActionProvider mShareActionProvider;
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
	private static final String[] FAV_COLUMNS = {
			MoviesDataBase.FavMovies + "." + MovieColumns._ID,
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
	public static final int COL_MOVIES_DATE = 3;
	public static final int COL_MOVIES_TITLE = 4;
	public static final int COL_MOVIES_VOTE = 5;
	public static final int COL_MOVIES_ID = 6;


	private static final int DETAIL_LOADER = 0;
	private static final int FAVORITE_LOADER = 1;
	private static final int FAVORITE_LOADER2 = 2;
	private static final String LOG_TAG = DetailFragment.class.getSimpleName();
	int _id, isFav=0,movieId;
	private Uri mUri;
	ImageView thumbnail, favIco;
	TextView title, date, rate, overview, favText,TrailerText,ReviewsText;
	private ArrayList<Trailer> videos;
	private ArrayList<Review> reviews;
	RecyclerView trailerList, reviewsList;
	VideosAdapter adapter;
	ReviewsAdapter adapter2;
	private View v;
	Cursor cursor;
	private String firstTrailer ;
	public DetailFragment() {
		setHasOptionsMenu(true);
	}
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if(Utility.getOrder(getContext()).equals("favorites")){
			getLoaderManager().initLoader(FAVORITE_LOADER2, null, this);

		}else {
			getLoaderManager().initLoader(DETAIL_LOADER, null, this);
		}
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

		Bundle arguments = getArguments();
		if (arguments != null) {
			_id = arguments.getInt("id");
		}

		v = inflater.inflate(R.layout.detailfragment, container, false);

		thumbnail = (ImageView) v.findViewById(R.id.thumbnail);
		favIco = (ImageView) v.findViewById(R.id.favIco);
		title = (TextView) v.findViewById(R.id.title);
		date = (TextView) v.findViewById(R.id.date);
		rate = (TextView) v.findViewById(R.id.rate);
		overview = (TextView) v.findViewById(R.id.overview);
		favText = (TextView) v.findViewById(R.id.favText);
		TrailerText = (TextView) v.findViewById(R.id.TrailerText);
		ReviewsText = (TextView) v.findViewById(R.id.ReviewsText);
		trailerList = (RecyclerView) v.findViewById(R.id.trailersList);
		reviewsList = (RecyclerView) v.findViewById(R.id.reviewsList);
		favIco.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (isFav == 0) {
					favIco.setImageResource(R.drawable.ic_star);
					favText.setText("Unfavorite");
					Uri FavUri = MoviesProvider.Favorites.CONTENT_URI;
					if (FavUri != null) {
						insertData();
						isFav = 1;
					}
				} else {
					favIco.setImageResource(R.drawable.ic_star_border);
					favText.setText("Favorite");
					removeData(movieId);
					isFav = 0;
				}
			}
		});
		return v;
	}

	public void insertData() {
		if(!cursor.moveToFirst()){
			return;
		}
		ContentValues contentValues = new ContentValues();
		contentValues.put(MovieColumns.TITLE, cursor.getString(COL_MOVIES_TITLE));
		contentValues.put(MovieColumns.POSTER, cursor.getString(COL_MOVIES_POSTER));
		contentValues.put(MovieColumns.OVERVIEW, cursor.getString(COL_MOVIES_OVERVIEW));
		contentValues.put(MovieColumns.VOTE, cursor.getString(COL_MOVIES_VOTE));
		contentValues.put(MovieColumns.DATE, cursor.getString(COL_MOVIES_DATE));
		contentValues.put(MovieColumns.MOVIE_ID, cursor.getString(COL_MOVIES_ID));
		getContext().getContentResolver().insert(MoviesProvider.Favorites.CONTENT_URI, contentValues);

	}
	public void removeData(int movie_id) {

		getContext().getContentResolver().delete(MoviesProvider.Favorites.CONTENT_URI,MovieColumns.MOVIE_ID+"=?",new
		String[]{String.valueOf(movie_id)});
	}

	@Override
	public Loader onCreateLoader(int id, Bundle args) {
		if(id==0) {
			mUri = MoviesProvider.Movies.withId(_id);
			if (mUri != null) {
				return new CursorLoader(getActivity(), mUri, DETAIL_COLUMNS, null, null, null);
			}
		}else{
			mUri = MoviesProvider.Favorites.withId(getActivity().getIntent().getIntExtra("id",0));
			return new CursorLoader(getActivity(),mUri,FAV_COLUMNS,null,null,null);
		}
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		if(loader.getId()==0) {
			if (!data.moveToFirst()) {
				return;
			}
			this.cursor = data;
			movieId = data.getInt(COL_MOVIES_ID);
			Glide.with(getContext()).load("http://image.tmdb.org/t/p/w185/" + data.getString(COL_MOVIES_POSTER)).into(thumbnail);
			title.setText(data.getString(COL_MOVIES_TITLE));
			date.setText("Released: " + data.getString(COL_MOVIES_DATE).substring(0, 4));
			rate.setText(data.getString(COL_MOVIES_VOTE) + "/10");
			overview.setText(data.getString(COL_MOVIES_OVERVIEW));
			if (isFav == 0) {
				favIco.setImageResource(R.drawable.ic_star_border);
				favText.setText("Favorite");
			} else {
				favIco.setImageResource(R.drawable.ic_star);
				favText.setText("Unfavorite");
			}
			getLoaderManager().initLoader(FAVORITE_LOADER, null, this);
			getVideos(movieId);
			getReviews(movieId);


		}else if(loader.getId()==1){
			if(data.moveToFirst()){
				data.moveToFirst();
				do{
					int mMovieId = data.getInt(COL_MOVIES_ID);
					if(mMovieId == movieId){
						isFav = 1;
						favIco.setImageResource(R.drawable.ic_star);
						favText.setText("Unfavorite");
						break;
					}
				}while (data.moveToNext());
			}
		}else{
			if (!data.moveToFirst()) {
				return;
			}

			this.cursor = data;
			movieId = data.getInt(COL_MOVIES_ID);
			Glide.with(getContext()).load("http://image.tmdb.org/t/p/w185/" + data.getString(COL_MOVIES_POSTER)).into(thumbnail);
			title.setText(data.getString(COL_MOVIES_TITLE));
			date.setText("Released: " + data.getString(COL_MOVIES_DATE).substring(0, 4));
			rate.setText(data.getString(COL_MOVIES_VOTE) + "/10");
			overview.setText(data.getString(COL_MOVIES_OVERVIEW));
			favIco.setVisibility(View.GONE);
			favText.setVisibility(View.GONE);
			TrailerText.setVisibility(View.GONE);
			ReviewsText.setVisibility(View.GONE);
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.detail, menu);
		MenuItem item = menu.findItem(R.id.action_share);
		mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
		if (firstTrailer != null) {
			mShareActionProvider.setShareIntent(createShareForecastIntent());
		}
	}

	private void getVideos(int movieId) {

		Uri.Builder uri2 = Uri.parse("https://api.themoviedb.org/3/movie/"+movieId+"/videos?api_key="+getContext().getString(R.string.api_key)).buildUpon();
		JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, uri2.toString(), null, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				Log.v(LOG_TAG,response.toString());
				try {
					videos = new ArrayList<>();
					String key;
					JSONArray jsonArray = response.getJSONArray("results");
					for(int i = 0 ; i < jsonArray.length();i++){
						JSONObject videoJson = jsonArray.getJSONObject(i);
						if(videoJson.getString("type").equals("Trailer")) {

							Trailer trailer = new Trailer();
							key = videoJson.getString("key");
							trailer.setKey(key);
							videos.add(trailer);
						}
					}
					adapter = new VideosAdapter(getContext(),videos);
					if(videos.size()==0){
						TrailerText.setVisibility(View.GONE);
					}else{
						TrailerText.setVisibility(View.VISIBLE);
					}
					LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
					trailerList.setLayoutManager(linearLayoutManager);
					trailerList.setAdapter(adapter);
					firstTrailer =  videos.get(0).getKey();
					if (mShareActionProvider != null) {
						mShareActionProvider.setShareIntent(createShareForecastIntent());
					}

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
		MySingleton.getInstance(getContext()).addToRequestQueue(request);

	}
	public void getReviews(int movieId){

		Uri.Builder uri2 = Uri.parse("https://api.themoviedb.org/3/movie/"+movieId+"/reviews?api_key="+getContext().getString(R.string.api_key)).buildUpon();
		JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, uri2.toString(), null, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				Log.v(LOG_TAG,response.toString());
				try {
					reviews = new ArrayList<>();
					String author;
					String content;
					JSONArray jsonArray = response.getJSONArray("results");
					for(int i = 0 ; i < jsonArray.length();i++){
						JSONObject reviewJson = jsonArray.getJSONObject(i);
						Review review = new Review();
						author = reviewJson.getString("author");
						content = reviewJson.getString("content");
						review.setAuthor(author);
						review.setContent(content);
						reviews.add(review);

					}

					adapter2 = new ReviewsAdapter(getContext(),reviews);
					if(reviews.size()==0){
						ReviewsText.setVisibility(View.GONE);
					}else{
						ReviewsText.setVisibility(View.VISIBLE);
					}
					LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
					reviewsList.setLayoutManager(linearLayoutManager);
					reviewsList.setAdapter(adapter2);

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
		MySingleton.getInstance(getContext()).addToRequestQueue(request);
	}
	@Override
	public void onLoaderReset(Loader loader) {

	}
	void onOrderChanged( ) {
			v.setVisibility(View.GONE);
	}

	private Intent createShareForecastIntent() {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT,"check this Trailer! https://www.youtube.com/watch?v="+firstTrailer);
		return intent;
	}}
