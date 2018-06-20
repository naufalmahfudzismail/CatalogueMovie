package comnaufalmahfudzismail.dicoding.moviecataloguemade.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mancj.materialsearchbar.MaterialSearchBar;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.API.MovieApiService;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Adapter.ItemClickSupport;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Adapter.MoviesAdapter;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.BuildConfig;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Class.Movie;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Class.MovieResponse;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity
		implements MaterialSearchBar.OnSearchActionListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener
{

	@BindView(R.id.rcy_movie)
	RecyclerView recyclerView;

	@BindView(R.id.movie_search)
	MaterialSearchBar searchBar;

	@BindView(R.id.refresh_layout)
	SwipeRefreshLayout refreshLayout;

	@BindView(R.id.title_movies)
	TextView title;

	@BindView(R.id.total_movies)
	TextView size;

	private static final String TAG = "MainActivity";
	private static Retrofit retrofit = null;

	private List<Movie> movies;
	private Call<MovieResponse> call;

	private MoviesAdapter moviesAdapter;

	private String judul_film = "";
	private int currentPage = 1;
	private int totalPages = 1;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getSupportActionBar().setTitle("Katalog Film");
		init_widget();
		StartRefresh();
	}


	private void init_widget()
	{
		ButterKnife.bind(this);

		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

		ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener()
		{
			@Override
			public void onItemClicked(RecyclerView recyclerView, int position, View v)
			{

				MoviesAdapter.setMovie(movies.get(position));
				Intent intent = new Intent(MainActivity.this, DetailActivity.class);
				startActivity(intent);
			}
		});

		searchBar.setOnSearchActionListener(this);
		refreshLayout.setOnRefreshListener(this);
	}

	@SuppressLint("SetTextI18n")
	public void GetDataMovieAPI(final String judul_film)
	{


		if (retrofit == null)
		{
			retrofit = new Retrofit.Builder()
					.baseUrl(BuildConfig.BASE_URL)
					.addConverterFactory(GsonConverterFactory.create())
					.build();
		}

		MovieApiService movieApiService = retrofit.create(MovieApiService.class);

		if (judul_film.isEmpty())
		{
			String top = "Top Rated Movies";
			title.setText(top);
			call = movieApiService.getTopRatedMovies(BuildConfig.API_KEY);
		} else
		{
			call = movieApiService.getSearchMovie(BuildConfig.API_KEY, currentPage, judul_film);
			title.setText("Result : " + judul_film);
		}


		call.enqueue(new Callback<MovieResponse>()
		{
			@Override
			public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response)
			{
				if (response.isSuccessful())
				{
					totalPages = response.body().getTotalPages();
					movies = response.body().getResults();
					size.setText("Total : " + movies.size());

					moviesAdapter = new MoviesAdapter(movies, MainActivity.this);
					recyclerView.setAdapter(moviesAdapter);

					StopRefresh();

					Log.d(TAG, "Number of movies received: " + movies.size());

				} else
				{
					StopRefresh();
					Toast.makeText(MainActivity.this, "Failed to load data.\nPlease check your Internet connections!", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Call<MovieResponse> call, Throwable throwable)
			{
				StopRefresh();
				Toast.makeText(MainActivity.this, "Failed to load data.\nPlease check your Internet connections!", Toast.LENGTH_SHORT).show();
				Log.e(TAG, throwable.toString());

			}
		});
	}


	private void StartRefresh()
	{
		if (refreshLayout.isRefreshing()) return;
		refreshLayout.setRefreshing(true);

		GetDataMovieAPI(judul_film);
	}

	private void StopRefresh()
	{
		if (refreshLayout.isRefreshing()) refreshLayout.setRefreshing(false);
	}

	@Override
	public void onSearchStateChanged(boolean enabled)
	{

	}

	@Override
	public void onSearchConfirmed(CharSequence text)
	{
		judul_film = String.valueOf(text);
		onRefresh();
	}

	@Override
	public void onButtonClicked(int buttonCode)
	{

	}

	@Override
	public void onClick(View v)
	{

	}

	@Override
	public void onPointerCaptureChanged(boolean hasCapture)
	{

	}

	@Override
	public void onRefresh()
	{
		currentPage = 1;
		totalPages = 1;

		StopRefresh();
		StartRefresh();

	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		if (call != null) call.cancel();
	}


}
