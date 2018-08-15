package comnaufalmahfudzismail.dicoding.moviecataloguemade.Activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.*;


import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.API.MovieApiClient;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Adapter.DateTime;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.BuildConfig;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Entity.DetailMovie.DetailMovie;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Entity.Movie;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Database.FavoriteBuilder;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Database.MovieContractBuilder;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.R;
import retrofit2.*;

import static comnaufalmahfudzismail.dicoding.moviecataloguemade.Database.MovieContractBuilder.*;
import static comnaufalmahfudzismail.dicoding.moviecataloguemade.Database.MovieContractBuilder.MovieColumns.*;

public class DetailActivity extends AppCompatActivity
{
	@BindView(R.id.tablyout_detail)
	Toolbar toolbar;

	@BindView(R.id.tv_genres)
	TextView detail_genre;

	@BindView(R.id.movie_desc)
	TextView detail_desc;

	@BindView(R.id.tv_release_date)
	TextView detail_date;

	@BindView(R.id.tv_vote)
	TextView detail_vote;

	@BindView(R.id.title_movies)
	TextView detail_title;

	@BindView(R.id.img_poster)
	ImageView detail_img_poster;

	@BindView(R.id.img_poster_belongs)
	ImageView detail_img_post_prod;

	@BindViews({
			R.id.img_star1,
			R.id.img_star2,
			R.id.img_star3,
			R.id.img_star4,
			R.id.img_star5
	})
	ImageView[] star_img = new ImageView[5];

	@BindView(R.id.tv_title_belongs)
	TextView prod_title;

	@BindView(R.id.tv_budget)
	TextView prod_budget;

	@BindView(R.id.tv_revenue)
	TextView prod_revenue;

	@BindView(R.id.tv_companies)
	TextView prod_companies;

	@BindView(R.id.tv_countries)
	TextView prod_countries;

	@BindView(R.id.float_favorite)
	FloatingActionButton float_fav;

	@BindView(R.id.progress_detail)
	ProgressBar progressBar;

	private static final String TAG = "DetailActivity";

	private Movie movie = new Movie();
	private Call<DetailMovie> call;
	private DetailMovie detailMovie = new DetailMovie();
	private MovieApiClient movieApiClient = new MovieApiClient();
	public static final String MOVIE_ITEM = "movie_item";
	private Gson gson = new Gson();


	private Boolean isFavorite = false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		ButterKnife.bind(this);

		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		progressBar.setVisibility(View.VISIBLE);

		if(savedInstanceState != null)
		{
			movie = (Movie) savedInstanceState.getSerializable("movie");
			detailMovie = (DetailMovie) savedInstanceState.getSerializable("detailMovie");
			if (detailMovie != null)
			{
				fetchingUI(detailMovie);
			}
		}
		else
		{
			String movie_item = getIntent().getStringExtra(MOVIE_ITEM);
			GetDetailData(movie_item);
		}

		loadDataSQLite(movie);

	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		outState.putSerializable("detailMovie", detailMovie);
		outState.putSerializable("movie", movie);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		if (call != null) call.cancel();
	}

	private void GetDetailData(String movie_item)
	{

		movie = gson.fromJson(movie_item, Movie.class);
		GetDetailDataFromServer(String.valueOf(movie.getId()));
		Log.d(TAG, String.valueOf(movie.getId()));
	}

	private void GetDetailDataFromServer(String id_movie)
	{
		call = movieApiClient.getService().getDetailMovie(id_movie);
		call.enqueue(new Callback<DetailMovie>()
		{
			@Override
			public void onResponse(Call<DetailMovie> call, Response<DetailMovie> response)
			{
				if( response.isSuccessful())
				{
					detailMovie = response.body();
					fetchingUI(detailMovie);

				}
				else
				{
					Toast.makeText(getApplicationContext(), R.string.gagal_koneksi, Toast.LENGTH_SHORT).show();
				}
			}
			@Override
			public void onFailure(Call<DetailMovie> call, Throwable t)
			{

				Toast.makeText(DetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void fetchingUI(DetailMovie detailMovie)
	{
		String unknown = getResources().getString(R.string.tidak_diketahui);

		int size = 0;

		StringBuilder genres = new StringBuilder();
		size = detailMovie.getGenres().size();
		for (int i = 0; i < size; i++)
		{
			genres.append("✸ ").append(detailMovie.getGenres().get(i).getName()).append(i + 1 < size ? "\n" : "");
		}

		detail_genre.setText(genres.toString());

		if (detailMovie.getBelongsToCollection() != null)
		{
			Glide.with(DetailActivity.this)
					.load(BuildConfig.BASE_URL_IMG + "/w154" + detailMovie.getBelongsToCollection().getPosterPath())
					.into(detail_img_post_prod);

			prod_title.setText(detailMovie.getBelongsToCollection().getName());
		} else
		{
			prod_title.setText(unknown);
		}

		String budget;
		String revenue;

		if (detailMovie.getBudget() <= 0) budget = getString(R.string.tidak_diketahui);
		else
			budget = "$ " + NumberFormat.getIntegerInstance().format(detailMovie.getBudget());
		if (detailMovie.getRevenue() <= 0) revenue = getString(R.string.tidak_diketahui);
		else
			revenue = "$ " + NumberFormat.getIntegerInstance().format(detailMovie.getRevenue());

		prod_budget.setText(budget);
		prod_revenue.setText(revenue);

		if (detailMovie.getProductionCompanies() != null)
		{

			StringBuilder companies = new StringBuilder();
			size = detailMovie.getProductionCompanies().size();

			for (int i = 0; i < size; i++)
			{
				companies.append("✸ ").append(detailMovie.getProductionCompanies().get(i).getName()).append(i + 1 < size ? "\n" : "");
			}

			prod_companies.setText(companies.toString());
		} else
		{
			prod_companies.setText(unknown);
		}

		if (detailMovie.getProductionCountries() != null)
		{

			StringBuilder countries = new StringBuilder();
			size = detailMovie.getProductionCountries().size();

			for (int i = 0; i < size; i++)
			{
				countries.append("✸ ").append(detailMovie.getProductionCountries().get(i).getName()).append(i + 1 < size ? "\n" : "");
			}

			prod_countries.setText(countries.toString());
		} else
		{
			prod_countries.setText(unknown);
		}

		getSupportActionBar().setTitle(movie.getTitle());

		detail_title.setText(movie.getTitle());

		Glide.with(DetailActivity.this)
				.load(BuildConfig.BASE_URL_IMG + "/w185" + movie.getPosterPath())
				.into(detail_img_poster);

		detail_date.setText(DateTime.getLongDate(movie.getReleaseDate()));
		detail_vote.setText(String.valueOf(movie.getVoteAverage()));
		detail_desc.setText(movie.getOverview());

		double userRating = movie.getVoteAverage() / 2;
		int integerPart = (int) userRating;

		// Fill stars
		for (int i = 0; i < integerPart; i++)
		{
			star_img[i].setImageResource(R.drawable.ic_star_black_24dp);
		}

		// Fill half star
		if (Math.round(userRating) > integerPart)
		{
			star_img[integerPart].setImageResource(R.drawable.ic_star_half_black_24dp);
		}

		progressBar.setVisibility(View.GONE);
	}

	private void loadDataSQLite(Movie movie)
	{
		FavoriteBuilder favoriteHelper = new FavoriteBuilder(this);
		favoriteHelper.open();

		Cursor cursor = getContentResolver().query(
				Uri.parse(MovieContractBuilder.CONTENT_URI + "/" + movie.getId()),
				null,
				null,
				null,
				null
		);

		if (cursor != null)
		{
			if (cursor.moveToFirst()) isFavorite = true;
			cursor.close();
		}
		setFavoriteFabIcon();
		favoriteHelper.close();
	}

	public void setFavoriteFabIcon()
	{
		if (isFavorite)
		{
			float_fav.setImageResource(R.drawable.ic_star_white_24dp);
		} else
		{
			float_fav.setImageResource(R.drawable.ic_star_border_white_24dp);
		}
	}


	private void FavoriteSave()
	{
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_MOVIE_ID, movie.getId());
		cv.put(COLUMN_MOVIE_TITLE, movie.getTitle());
		cv.put(COLUMN_MOVIE_BACKDROP_PATH, movie.getBackdropPath());
		cv.put(COLUMN_MOVIE_POSTER_PATH, movie.getPosterPath());
		cv.put(COLUMN_MOVIE_RELEASE_DATE, movie.getReleaseDate());
		cv.put(COLUMN_MOVIE_VOTE_AVERAGE, movie.getVoteAverage());
		cv.put(COLUMN_MOVIE_OVERVIEW, movie.getOverview());
		cv.put(COLUMN_MOVIE_VOTE_COUNT, movie.getVoteCount());

		getContentResolver().insert(CONTENT_URI, cv);
		Toast.makeText(this, R.string.toast_add_fav, Toast.LENGTH_SHORT).show();
	}

	private void FavoriteRemove()
	{
		getContentResolver().delete(
				Uri.parse(CONTENT_URI + "/" + movie.getId()),
				null,
				null
		);
		Toast.makeText(this, R.string.toast_remove_fav, Toast.LENGTH_SHORT).show();
	}

	@OnClick(R.id.float_favorite)
	public void Click()
	{
		if (isFavorite)
		{
			FavoriteRemove();
		} else
		{

			FavoriteSave();
		}
		isFavorite = !isFavorite;
		setFavoriteFabIcon();
	}
}
