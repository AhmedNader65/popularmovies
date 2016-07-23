package mrerror.popularmovies;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by ahmed on 19/07/16.
 */
public class DetailFragment extends Fragment {

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.detailfragment,container,false);
		ImageView thumbnail = (ImageView)v.findViewById(R.id.thumbnail);
		TextView title = (TextView) v.findViewById(R.id.title);
		TextView date = (TextView) v.findViewById(R.id.date);
		TextView rate = (TextView) v.findViewById(R.id.rate);
		TextView overview = (TextView) v.findViewById(R.id.overview);
		Intent intent = getActivity().getIntent();
		title.setText(intent.getStringExtra("title"));
		date.setText(intent.getStringExtra("date"));
		rate.setText(intent.getStringExtra("rating")+"/10");
		overview.setText(intent.getStringExtra("overview"));

		String link = "http://image.tmdb.org/t/p/w500/"+intent.getStringExtra("thumbnail");

		Picasso.with(getContext()).load(link).into(thumbnail);
		return v;
	}
}
