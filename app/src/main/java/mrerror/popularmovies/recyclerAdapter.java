package mrerror.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by ahmed on 19/07/16.
 */
public class recyclerAdapter  extends CursorRecyclerViewAdapter<recyclerAdapter.ViewHolder>{
	public interface OnItemClick{
		public void OnItemClicked(Long movieId);
	}
	static Context mContext;
	public recyclerAdapter(Context context,Cursor cursor){
		super(context,cursor);
		this.mContext = context;
	}

	public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		public ImageView mImageView;
		Cursor cursor;
		public ViewHolder(View view) {
			super(view);
			view.setOnClickListener(this);
			mImageView = (ImageView) view.findViewById(R.id.movie_img);
		}
		public void setData(Cursor cursor) {
			this.cursor = cursor;
			final MyListItem myListItem = MyListItem.fromCursor(cursor);
//			Picasso.with(mContext).load(myListItem.getLink())
//					.networkPolicy(NetworkPolicy.OFFLINE).into(mImageView);
			Glide.with(mContext).load(myListItem.getLink()).into(mImageView);
		}

		@Override
		public void onClick(View view) {
			cursor.moveToPosition(getPosition());
//					Intent intent = new Intent(mContext, DetailActivity.class)
//			.putExtra("id",cursor.getLong(MoviesFragment.COL_MOVIE_ID));
//					mContext.startActivity(intent);
			((OnItemClick)mContext).OnItemClicked(cursor.getLong(MoviesFragment.COL_MOVIE_ID));
			MoviesFragment.mPosition=(getPosition());
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.cardview, parent, false);
		ViewHolder vh = new ViewHolder(itemView);
		return vh;
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
		viewHolder.setData(cursor);
	}
}

