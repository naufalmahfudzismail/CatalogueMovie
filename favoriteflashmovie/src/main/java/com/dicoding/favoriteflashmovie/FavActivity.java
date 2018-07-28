package com.dicoding.favoriteflashmovie;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavActivity extends AppCompatActivity
{

	@BindView(R.id.rcy_fav)
	RecyclerView rcy_fav;

	private FavListAdapter favListAdapter;
	private Cursor ListFav;

	private static final String TAG = "FavActivity";


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fav);
		ButterKnife.bind(this);
		getSupportActionBar().setTitle(getResources().getString(R.string.favorite));
		setRcy_fav();
		new LoadDataAsync().execute();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		new LoadDataAsync().execute();
	}

	private void setRcy_fav()
	{
		favListAdapter = new FavListAdapter(ListFav, this);
		rcy_fav.setLayoutManager(new LinearLayoutManager(this));
		rcy_fav.addItemDecoration(new DividerItemDecoration(rcy_fav.getContext(), DividerItemDecoration.VERTICAL));
		rcy_fav.setAdapter(favListAdapter);

		ItemClickSupport.addTo(rcy_fav).setOnItemClickListener(new ItemClickSupport.OnItemClickListener()
		{

			@Override
			public void onItemClicked(RecyclerView recyclerView, int position, View v)
			{
				MovieItem movie = favListAdapter.getMovie(position);

				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/plain");
				intent.putExtra(Intent.EXTRA_TITLE, movie.getTitle());
				intent.putExtra(Intent.EXTRA_SUBJECT, movie.getTitle());
				intent.putExtra(Intent.EXTRA_TEXT, movie.getTitle() + "\n\n" + movie.getOverview());
				startActivity(Intent.createChooser(intent,getResources().getString(R.string.share)));

			}
		});

	}

	@SuppressLint("StaticFieldLeak")
	private class LoadDataAsync extends AsyncTask<Void, Void, Cursor>
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

			ListFav = cursor;
			favListAdapter.replaceAll(ListFav);
		}
	}


}
