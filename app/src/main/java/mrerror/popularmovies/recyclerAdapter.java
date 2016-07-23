package mrerror.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import mrerror.popularmovies.models.Movies;

/**
 * Created by ahmed on 19/07/16.
 */
public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.myViewHolder>  {
	Context mContext ;
	LayoutInflater inflater;
	ArrayList<Movies> mMovies ;
	public recyclerAdapter(Context context, ArrayList<Movies> movies){
		this.mContext = context;
		this.inflater = LayoutInflater.from(context);
		this.mMovies = movies;
	}
	@Override
	public recyclerAdapter.myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = inflater.inflate(R.layout.cardview,parent,false);
		myViewHolder holder = new myViewHolder(v);
		return holder;
	}

	@Override
	public void onBindViewHolder(recyclerAdapter.myViewHolder holder, int position) {
		Movies movie = mMovies.get(position);
		holder.setData(movie);
	}

	@Override
	public int getItemCount() {
		return mMovies.size();
	}
	class myViewHolder extends RecyclerView.ViewHolder {
		ImageView img;
		public myViewHolder(View itemView) {
			super(itemView);
			img= (ImageView)itemView.findViewById(R.id.movie_img);
			img.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent intent = new Intent(mContext,DetailActivity.class);
					intent.putExtra("title",mMovies.get(getPosition()).getTitle());
					intent.putExtra("thumbnail",mMovies.get(getPosition()).getThumbnail());
					intent.putExtra("date",mMovies.get(getPosition()).getDate());
					intent.putExtra("rating",mMovies.get(getPosition()).getRating());
					intent.putExtra("overview",mMovies.get(getPosition()).getSynopsis());
					Log.v("hiiiiiiiiii",mMovies.get(getPosition()).getDate());
					mContext.startActivity(intent);
				}
			});

		}
		public void setData(Movies obj){
			String link = "http://image.tmdb.org/t/p/w500/"+obj.getThumbnail();

			Picasso.with(mContext).load(link).into(img);
		}
	}
}
