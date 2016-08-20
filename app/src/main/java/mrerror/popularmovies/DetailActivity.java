package mrerror.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		Bundle arguments = new Bundle();
		DetailFragment detailFragment =new DetailFragment();
		detailFragment.setArguments(arguments);
		arguments.putInt("id", getIntent().getIntExtra("id",0));
		detailFragment.setArguments(arguments);
		if(savedInstanceState == null){
			getSupportFragmentManager().beginTransaction()
					.add(R.id.movie_detail_fragment, detailFragment)
					.commit();
		}
		getSupportActionBar().setElevation(0f);
	}
}
