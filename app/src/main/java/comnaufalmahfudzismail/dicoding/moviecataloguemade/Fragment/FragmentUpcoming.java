package comnaufalmahfudzismail.dicoding.moviecataloguemade.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.API.MovieApiService;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Activity.MainActivity;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Adapter.MoviesNowPlayAdapter;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Adapter.MoviesUpcomingAdapter;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.BuildConfig;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Class.Movie;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Class.MovieResponse;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FragmentUpcoming extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener
{
	@BindView(R.id.rcy_upcoming)
	RecyclerView recyclerView;

	@BindView(R.id.refresh_layout_up)
	SwipeRefreshLayout refreshLayout;


	private static final String TAG = "MainActivity";
	private static Retrofit retrofit = null;

	private List<Movie> movies;
	private Call<MovieResponse> call;

	private MoviesUpcomingAdapter moviesAdapter;

	private int currentPage = 1;
	private int totalPages = 1;

	View v;


	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
	{
		v = inflater.inflate(R.layout.upcoming_movie, container, false);
		init_widget();
		StartRefresh();
		return  v;
	}

	private void init_widget()
	{
		ButterKnife.bind(this, v);

		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
		refreshLayout.setOnRefreshListener(this);
	}

	@SuppressLint("SetTextI18n")
	public void GetDataMovieAPI()
	{

		String language = "en_US";

		if (retrofit == null)
		{
			retrofit = new Retrofit.Builder()
					.baseUrl(BuildConfig.BASE_URL)
					.addConverterFactory(GsonConverterFactory.create())
					.build();
		}

		MovieApiService movieApiService = retrofit.create(MovieApiService.class);
		call = movieApiService.getUpcomingMovie(BuildConfig.API_KEY, currentPage, "US", language);
		call.enqueue(new Callback<MovieResponse>()
		{
			@Override
			public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response)
			{
				if (response.isSuccessful())
				{
					totalPages = response.body().getTotalPages();
					movies = response.body().getResults();

					moviesAdapter = new MoviesUpcomingAdapter(movies, getActivity());
					recyclerView.setAdapter(moviesAdapter);

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
	}


	private void StartRefresh()
	{
		if (refreshLayout.isRefreshing()) return;
		refreshLayout.setRefreshing(true);
		GetDataMovieAPI();
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
	public void onDestroy()
	{
		super.onDestroy();
		if (call != null) call.cancel();
	}
	@Override
	public void onDetach()
	{
		super.onDetach();
		if (call != null) call.cancel();
	}


}
