package comnaufalmahfudzismail.dicoding.moviecataloguemade.Fragment;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.API.MovieAsyncTask;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Adapter.MoviesNowPlayAdapter;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Entity.Movie;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Entity.MovieResponse;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Entity.Region;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.R;

public class FragmentNowPlay extends Fragment implements
		View.OnClickListener,
		SwipeRefreshLayout.OnRefreshListener,
		LoaderManager.LoaderCallbacks<MovieResponse>
{
	@BindView(R.id.rcy_now_play)
	RecyclerView recyclerView;

	@BindView(R.id.refresh_layout_now)
	SwipeRefreshLayout refreshLayout;

	@BindView(R.id.progress_now)
	ProgressBar progressBar;


	private List<Movie> movies = new ArrayList<>();
	private MovieResponse movieResponse = new MovieResponse();

	private MoviesNowPlayAdapter moviesAdapter;

	private int currentPage = 1;
	private int totalPages = 1;
	private boolean isStarted = false;

	View v;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
	{
		v = inflater.inflate(R.layout.now_play_movie, container, false);
		init_widget();
		MoreListScroll();
		StartRefresh();
		return v;
	}

	@NonNull
	@Override //Method Execute After InitLoader or RestartLoader in StartRefresh
	public android.support.v4.content.Loader<MovieResponse> onCreateLoader(int id, @Nullable Bundle args)
	{
		return new MovieAsyncTask(getContext(), currentPage, 1, Region.region);
	}

	@Override //Method Execute After onCreateLoader Response succesfull
	public void onLoadFinished(@NonNull android.support.v4.content.Loader<MovieResponse> loader, MovieResponse data)
	{
		movieResponse = data;
		movies = movieResponse.getResults();
		totalPages = movieResponse.getTotalPages();
		if (currentPage > 1)
		{
			moviesAdapter.updateData(movies);
		} else
			moviesAdapter.replaceAll(movies);
		StopRefresh();
	}

	@Override
	public void onLoaderReset(@NonNull android.support.v4.content.Loader<MovieResponse> loader)
	{

	}

	private void init_widget()
	{
		ButterKnife.bind(this, v);

		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
		refreshLayout.setOnRefreshListener(this);

		moviesAdapter = new MoviesNowPlayAdapter();
		recyclerView.setAdapter(moviesAdapter);
	}

	/*public void GetDataMovieAPI()
	{
		call = movieApiClient.getService().getNowPlaying(currentPage, Region.region);
		call.enqueue(new Callback<MovieResponse>()
		{
			@Override
			public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response)
			{
				if (response.isSuccessful())
				{
					totalPages = response.body().getTotalPages();
					List<Movie> movies = response.body().getResults();

					if (currentPage > 1)
					{
						moviesAdapter.updateData(movies);
					} else moviesAdapter.replaceAll(movies);

					StopRefresh();

					Log.d(TAG, "Number of movies received: " + movies.size());

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
					if (currentPage < totalPages)
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
			getLoaderManager().restartLoader(0, null, this);
		} else
		{
			getLoaderManager().initLoader(0, null, this);
			isStarted = true;
		}
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

}






