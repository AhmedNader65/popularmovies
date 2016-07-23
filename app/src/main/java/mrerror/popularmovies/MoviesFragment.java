package mrerror.popularmovies;


import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mrerror.popularmovies.models.Movies;
import mrerror.popularmovies.models.MySingleton;

/**
 * Created by ahmed on 19/07/16.
 */
public class MoviesFragment extends Fragment {
	private static final String TAG = MoviesFragment.class.getSimpleName();
	private ArrayList<Movies> movies;
	private RecyclerView recyclerView;

	public MoviesFragment(){
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v =inflater.inflate(R.layout.moviesfragment,container,false);
		recyclerView = (RecyclerView)v.findViewById(R.id.content);

		StaggeredGridLayoutManager gridLayoutManager  = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
		recyclerView.setLayoutManager(gridLayoutManager);
		return v;
	}


	@Override
	public void onStart() {
		super.onStart();
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
		FetchDataFromApi(sharedPreferences.getString(getString(R.string.order_pref_key),getString(R.string.order_pref_default)));
	}

	public void FetchDataFromApi(String order){
		Uri.Builder uri = Uri.parse("https://api.themoviedb.org/3/movie/"+order+"?api_key="+getString(R.string.api_key)).buildUpon();
		Log.v(TAG,uri.toString());
		final JsonObjectRequest jsObjRequest = new JsonObjectRequest
				(Request.Method.GET, uri.toString(), null, new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						Log.v(TAG,response.toString());
						try {
							movies = new ArrayList<>();
							 String title,thumbnail, synopsis,rating,date ;
							JSONArray jsonArray = response.getJSONArray("results");
							for(int i = 0 ; i < jsonArray.length();i++){
								JSONObject movieObj = jsonArray.getJSONObject(i);
								title = movieObj.getString("title");
								thumbnail = movieObj.getString("poster_path");
								synopsis = movieObj.getString("overview");
								rating = movieObj.getString("vote_average");
								date = movieObj.getString("release_date");
								addMovie(title,thumbnail,synopsis,rating,date);
								recyclerAdapter adapter = new recyclerAdapter(getContext(), movies);
								recyclerView.setAdapter(adapter);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.v(TAG,error.toString());

					}
				});
		MySingleton.getInstance(getActivity()).addToRequestQueue(jsObjRequest);
	}
	private void addMovie(String title,String thumbnail,String synopsis,String rating,String date){
		Movies newMovie = new Movies();
		newMovie.setTitle(title);
		newMovie.setThumbnail(thumbnail);
		newMovie.setSynopsis(synopsis);
		newMovie.setRating(rating);
		newMovie.setDate(date);
		movies.add(newMovie);
	}
}

