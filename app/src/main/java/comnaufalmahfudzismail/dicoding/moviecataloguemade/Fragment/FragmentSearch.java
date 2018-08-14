package comnaufalmahfudzismail.dicoding.moviecataloguemade.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mancj.materialsearchbar.SimpleOnSearchActionListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.API.MovieApiClient;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.API.MovieApiService;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.API.MovieAsyncTask;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Activity.DetailActivity;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Activity.MainActivity;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Adapter.MoviesAdapter;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.BuildConfig;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Class.DetailMovie.DetailMovie;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Class.Movie;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Class.MovieResponse;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Class.Region;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FragmentSearch extends Fragment implements
		View.OnClickListener,
		SwipeRefreshLayout.OnRefreshListener, MaterialSearchBar.OnSearchActionListener,
		LoaderManager.LoaderCallbacks<MovieResponse>
{
	@BindView(R.id.movie_search)
	MaterialSearchBar searchBar;

	@BindView(R.id.rcy_movie)
	RecyclerView recyclerView;

	@BindView(R.id.refresh_layout)
	SwipeRefreshLayout refreshLayout;

	@BindView(R.id.title_movies)
	TextView title;

	@BindView(R.id.total_movies)
	TextView size;


	private static final String TAG = "MainActivity";
	private static final String TITLE = "title";
	private MoviesAdapter moviesAdapter;

	private MovieResponse movieResponse = new MovieResponse();
	private List<Movie> movies = new ArrayList<>();

	private Bundle bundle = new Bundle();

	private String judul_film = "";
	private int currentPage = 1;
	private int totalPages = 1;
	private int totalResult;
	private boolean isStarted = false;

	View v;


	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
	{
		v = inflater.inflate(R.layout.search_movie, container, false);
		setHasOptionsMenu(true);
		init_widget();
		MoreListScroll();
		bundle.putString(TITLE,judul_film);
		StartRefresh();
		return v;
	}

	@NonNull
	@Override
	public Loader<MovieResponse> onCreateLoader(int id, @Nullable Bundle args)
	{
		if (args != null)
		{
			judul_film = args.getString(TITLE);
		}
		refreshLayout.setRefreshing(true);

		if (judul_film.isEmpty())
			return new MovieAsyncTask(getContext(), 4, currentPage, judul_film, Region.region);
		else
			return new MovieAsyncTask(getContext(), 3, currentPage, judul_film, Region.region);
	}

	@Override
	public void onLoadFinished(@NonNull Loader<MovieResponse> loader, MovieResponse data)
	{
		movieResponse = data;
		movies = movieResponse.getResults();
		totalResult = movieResponse.getTotalResults();
		totalPages = movieResponse.getTotalPages();

		if (currentPage > 1)
		{
			moviesAdapter.updateData(movies);
		}
		else
			moviesAdapter.replaceAll(movies);
		fetchUI();
		StopRefresh();
	}

	@Override
	public void onLoaderReset(@NonNull Loader<MovieResponse> loader)
	{
	}

	private void init_widget()
	{
		ButterKnife.bind(this, v);
		moviesAdapter = new MoviesAdapter();
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
		refreshLayout.setOnRefreshListener(this);
		recyclerView.setAdapter(moviesAdapter);
		searchBar.setOnSearchActionListener(this);

	}

	/*public void GetDataMovieAPI(final String judul_film)
	{

		if (judul_film.isEmpty())
		{
			call = movieApiClient.getService().getPopularMovie(currentPage, Region.region);
		} else
		{
			call = movieApiClient.getService().getSearchMovie(currentPage, judul_film, Region.region);
		}

		call.enqueue(new Callback<MovieResponse>()
		{
			@Override
			public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response)
			{
				if (response.isSuccessful())
				{
					totalPages = response.body().getTotalPages();
					totalResult = response.body().getTotalResults();
					movieResponse = response.body();
					movies = movieResponse.getResults();
					fetchUI();

					Log.d(TAG, "Number of movies received: " + movies.size());


					StopRefresh();

				} else
				{
					StopRefresh();
					Toast.makeText(getActivity(), R.string.gagal_koneksi, Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Call<MovieResponse> call, Throwable throwable)
			{
				StopRefresh();
				Toast.makeText(getActivity(), R.string.gagal_koneksi, Toast.LENGTH_SHORT).show();
				Log.e(TAG, throwable.toString());

			}
		});
	}*/

	private void fetchUI()
	{

		if (judul_film.isEmpty())
		{
			String top = getString(R.string.popular);
			title.setText(top);
		} else
		{
			String hasil = getResources().getString(R.string.hasil) + judul_film;
			title.setText(hasil);
		}

		String sizeTxt;

		if (totalResult > 99) sizeTxt = getString(R.string.total_movie) + "99++";
		else sizeTxt = getString(R.string.total_movie) + totalResult;

		size.setText(sizeTxt);
	}

	private void MoreListScroll()
	{
		recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
		{
			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy)
			{
				super.onScrolled(recyclerView, dx, dy);

				LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

				int total = layoutManager.getItemCount();
				int NowVisible = layoutManager.getChildCount();
				int PastVisible = layoutManager.findFirstCompletelyVisibleItemPosition();
				int totalMovies = moviesAdapter.getMovies().size();

				if (PastVisible + NowVisible >= total && totalMovies <= 99)
				{
					if (currentPage < totalPages && currentPage <= 5)
					{
						currentPage++;
						StartRefresh();
					}

				}
			}
		});
	}


	private void StartRefresh()
	{
		if (refreshLayout.isRefreshing()) return;
		refreshLayout.setRefreshing(true);

		if (currentPage > 1 || isStarted)
		{
			getLoaderManager().restartLoader(0, bundle, this);
		} else
		{
			getLoaderManager().initLoader(0, bundle, this);
			isStarted = true;
		}

		//fetchUI();
	}

	private void StopRefresh()
	{
		if (refreshLayout.isRefreshing()) refreshLayout.setRefreshing(false);
	}


	@Override
	public void onClick(View v)
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
	public void onDetach()
	{
		super.onDetach();

	}
	@Override
	public void onSearchStateChanged(boolean enabled)
	{

	}
	@Override
	public void onSearchConfirmed(CharSequence text)
	{
		judul_film = String.valueOf(text);
		bundle.putString(TITLE, judul_film);
		StartRefresh();
	}

	@Override
	public void onButtonClicked(int buttonCode)
	{

	}

}
