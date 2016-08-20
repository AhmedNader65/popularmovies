package mrerror.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import mrerror.popularmovies.models.Trailer;

/**
 * Created by ahmed on 20/08/16.
 */
public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.MyViewHolder>  implements View.OnClickListener{
	ArrayList<Trailer> mVideos;
	Context mContext;
	int pos;
	@Override
	public void onClick(View view) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("http://www.youtube.com/watch?v="+mVideos.get(pos).getKey()));
		mContext.startActivity(intent);

	}

	public class MyViewHolder extends RecyclerView.ViewHolder{
		public TextView trailerNum;

		public MyViewHolder(View view) {
			super(view);
			trailerNum = (TextView)view.findViewById(R.id.trailerNumber);
		}
	}


	public VideosAdapter(Context context,ArrayList<Trailer> videos) {
		this.mVideos = videos;
		this.mContext = context;
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.trailers_list_item, parent, false);

		return new MyViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, int position) {
		holder.trailerNum.setText("Trailer "+(position+1));
		pos = position;
		holder.trailerNum.setOnClickListener(this);
	}

	@Override
	public int getItemCount() {
		return mVideos.size();
	}
//	Context mContext;
//	ArrayList<String> mVideos;
//	public VideosAdapter(Context context, ArrayList<String> videos){
//		this.mContext = context;
//		this.mVideos = videos;
//	}
//	@Override
//	public int getCount() {
//		return mVideos.size();
//	}
//
//	@Override
//	public Object getItem(int i) {
//		return null;
//	}
//
//	@Override
//	public long getItemId(int i) {
//		return 0;
//	}
//
//	@Override
//	public View getView(final int i, View view, ViewGroup viewGroup) {
//		LayoutInflater inflater = LayoutInflater.from(mContext);
//		View v = inflater.inflate(R.layout.trailers_list_item,viewGroup,false);
//		TextView trailerNum = (TextView)v.findViewById(R.id.trailerNumber);
//		trailerNum.setText("Trailer "+i);
//		Log.e("list number",getCount()+"");
//		v.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				Intent intent = new Intent();
//				intent.setAction(Intent.ACTION_VIEW);
//				intent.setData(Uri.parse("http://www.youtube.com/watch?v="+mVideos.get(i)));
//				Log.e("list number","http://www.youtube.com/watch?v="+mVideos.get(i));
//				mContext.startActivity(intent);
//			}
//		});
//		return v;
//	}
}
