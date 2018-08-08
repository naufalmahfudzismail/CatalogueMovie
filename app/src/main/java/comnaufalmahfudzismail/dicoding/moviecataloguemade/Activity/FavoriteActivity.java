package comnaufalmahfudzismail.dicoding.moviecataloguemade.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Adapter.MoviesFavoriteAdapter;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Class.Movie;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Database.MovieContractBuilder;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.R;

public class FavoriteActivity extends AppCompatActivity
{

	@BindView(R.id.rcy_fav)
	RecyclerView rcy_fav;

	@BindView(R.id.tablyout_fav)
	Toolbar toolbar;

	private Cursor list;
	private Movie movie;
	private MoviesFavoriteAdapter adapter;

	private static final String TAG = "FavoriteActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorite);
		ButterKnife.bind(this);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setSupportActionBar(toolbar);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(R.string.favorite);

		set_rcy();

		new loadDataAsync().execute();
	}

	private void set_rcy()
	{
		adapter = new MoviesFavoriteAdapter(list, getApplicationContext());
		rcy_fav.setLayoutManager(new GridLayoutManager(this, 2));
		rcy_fav.setAdapter(adapter);

	}


	@Override
	public void onResume()
	{
		super.onResume();
		new loadDataAsync().execute();
	}

	@SuppressLint("StaticFieldLeak")
	private class loadDataAsync extends AsyncTask<Void, Void, Cursor>
	{

		@Override
		protected Cursor doInBackground(Void... voids)
		{
			return getContentResolver().query(
					MovieContractBuilder.CONTENT_URI,
					null,
					null,
					null,
					null
			);
		}

		@Override
		protected void onPostExecute(Cursor cursor)
		{
			super.onPostExecute(cursor);

			list = cursor;
			adapter.replaceAll(list);
		}
	}
}
