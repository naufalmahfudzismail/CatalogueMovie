package comnaufalmahfudzismail.dicoding.moviecataloguemade.API;

import android.content.Context;
import android.os.Handler;
import android.support.v4.content.AsyncTaskLoader;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import comnaufalmahfudzismail.dicoding.moviecataloguemade.BuildConfig;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Class.Movie;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Class.MovieResponse;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.R;
import cz.msebera.android.httpclient.Header;


public class MovieAsyncTask extends AsyncTaskLoader<MovieResponse>
{
	private MovieResponse movies;
	private boolean hasResult = false;
	private String API = BuildConfig.API_KEY;
	private String url;

	public MovieAsyncTask(Context context, int currentPage, int mode, String region)
	{
		super(context);
		switch (mode)
		{
			case 1:
				url = linkMaker("movie/now_playing?", currentPage, API, region, null);
				break;
			case 2:
				url = linkMaker("movie/upcoming?", currentPage, API, region, null);
				break;
		}
		onContentChanged();
	}

	public MovieAsyncTask(Context context, int mode, int currentPage, String judul_film, String region)
	{
		super(context);
		switch (mode)
		{
			case 3:
				url = linkMaker("search/movie?", currentPage, API, region, judul_film);
				break;
			case 4:
				url = linkMaker("movie/popular?", currentPage, API, region, null);
				break;
		}
		onContentChanged();
	}

	@Override
	public void deliverResult(MovieResponse data)
	{
		this.movies = data;
		this.hasResult = true;
		super.deliverResult(data);
	}

	@Override
	public MovieResponse loadInBackground()
	{
		SyncHttpClient client = new SyncHttpClient();
		final MovieResponse movieResponse = new MovieResponse();
		final List<Movie> moviesList = new ArrayList<>();
		final int[] totalPages = new int[1];
		final int[] currentPage = new int[1];
		final int[] totalResults = new int[1];

		client.get(url, new AsyncHttpResponseHandler()
		{
			@Override
			public void onStart()
			{
				super.onStart();
				setUseSynchronousMode(true);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
			{
				try
				{
					String result = new String(responseBody);
					JSONObject responseObject = new JSONObject(result);

					JSONArray movies = responseObject.getJSONArray("results");
					for (int i = 0; i < movies.length(); i++)
					{
						JSONObject movie = movies.getJSONObject(i);
						Movie movieItem = new Movie(movie);
						moviesList.add(movieItem);
					}

					totalPages[0] = responseObject.getInt("total_pages");
					totalResults[0] = responseObject.getInt("total_results");
					currentPage[0] = responseObject.getInt("page");

					movieResponse.setResults(moviesList);
					movieResponse.setPage(currentPage[0]);
					movieResponse.setTotalPages(totalPages[0]);
					movieResponse.setTotalResults(totalResults[0]);

				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error)
			{
				Handler handler = new Handler(getContext().getMainLooper());
				handler.post(new Runnable()
				{
					public void run()
					{
						Toast.makeText(getContext(), R.string.gagal_koneksi, Toast.LENGTH_SHORT).show();
					}
				});

			}
		});

		return movieResponse;
	}

	private String linkMaker(String mode, int currentPage, String API, String region, String Query)
	{
		String link;
		if (Query == null)
		{
			link = BuildConfig.BASE_URL + mode + "api_key=" + API + "&language=en-US&page=" + currentPage + "&region=" + region;
		} else
		{
			link = BuildConfig.BASE_URL + mode + "api_key=" + API + "&language=en-US&query=" + Query + "&page=" + currentPage + "&region=" + region;
		}

		return link;
	}

	@Override
	protected void onStartLoading()
	{
		if (takeContentChanged())
			forceLoad();
		else if (hasResult)
			deliverResult(movies);
	}

	@Override
	protected void onReset()
	{
		super.onReset();
		onStopLoading();
		if (hasResult)
		{
			movies = null;
			hasResult = false;
		}
	}
}
