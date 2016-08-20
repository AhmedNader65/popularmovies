package mrerror.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import mrerror.popularmovies.sync.PopularmoviesSyncAdapter;

public class MainActivity extends AppCompatActivity implements recyclerAdapter.OnItemClick{
	private static final String DETAILFRAGMENT_TAG = "DF";
	String mOrder ;
	public static boolean mTwoPane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mOrder = Utility.getOrder(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if(findViewById(R.id.movie_detail_fragment)!=null){
			mTwoPane = true;
			if(savedInstanceState==null){
				getSupportFragmentManager().beginTransaction().replace(R.id.movie_detail_fragment,new DetailFragment(),DETAILFRAGMENT_TAG).commit();
			}
		}else{
			mTwoPane= false;
		}

		PopularmoviesSyncAdapter.initializeSyncAdapter(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.id.action_settings){
			startActivity(new Intent(MainActivity.this,SettingsActivity.class));
		}
		return true;
	}
	@Override
	protected void onResume() {
		super.onResume();
		String order = Utility.getOrder( this );
		// update the location in our second pane using the fragment manager
		if (order != null && !order.equals(mOrder)) {

				MoviesFragment mf = (MoviesFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_movies);
				if (null != mf) {
					mf.onOrderChanged();
				}
				DetailFragment df = (DetailFragment) getSupportFragmentManager().findFragmentByTag(DETAILFRAGMENT_TAG);
				if (null != df) {
					df.onOrderChanged();
				}

			mOrder = order;
		}
	}

	@Override
	public void OnItemClicked(Long movieId) {
		if (mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			Bundle args = new Bundle();
			args.putInt("id",movieId.intValue());

			DetailFragment fragment = new DetailFragment();
			fragment.setArguments(args);

			getSupportFragmentManager().beginTransaction()
					.replace(R.id.movie_detail_fragment, fragment, DETAILFRAGMENT_TAG)
					.commit();
		} else {
			Intent intent = new Intent(this, DetailActivity.class)
					.putExtra("id",movieId.intValue());
			startActivity(intent);
		}
	}
}
