package comnaufalmahfudzismail.dicoding.moviecataloguemade.Activity;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.text.NumberFormat;
import java.util.List;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.API.MovieApiService;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Adapter.DateTime;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Adapter.MoviesAdapter;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.BuildConfig;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Class.DetailMovie.DetailMovie;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Class.Movie;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.R;
import retrofit2.*;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailActivity extends AppCompatActivity
{
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

	private Movie movie;
	private Call<DetailMovie> call;
	private Retrofit retrofit;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		movie = MoviesAdapter.getMovie();
		ButterKnife.bind(this);
		GetDetailData();

	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();

		if (call != null) call.cancel();
	}

	private void GetDetailData()
	{
		GetDetailDataFromServer(String.valueOf(movie.getId()));
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

	}

	private void GetDetailDataFromServer(String id_movie)
	{
		if (retrofit == null)
		{
			retrofit = new Retrofit.Builder()
					.baseUrl(BuildConfig.BASE_URL)
					.addConverterFactory(GsonConverterFactory.create())
					.build();
		}

		MovieApiService movieApiService = retrofit.create(MovieApiService.class);
		call = movieApiService.getDetailMovie(id_movie, BuildConfig.API_KEY);
		call.enqueue(new Callback<DetailMovie>()
		{
			@SuppressLint("SetTextI18n")
			@Override
			public void onResponse(Call<DetailMovie> call, Response<DetailMovie> response)
			{
				DetailMovie d_movie = response.body();

				int size = 0;

				StringBuilder genres = new StringBuilder();
				size = d_movie.getGenres().size();
				for (int i = 0; i < size; i++)
				{
					genres.append("✸ ").append(d_movie.getGenres().get(i).getName()).append(i + 1 < size ? "\n" : "");
				}
				detail_genre.setText(genres.toString());

				if (d_movie.getBelongsToCollection() != null)
				{
					Glide.with(DetailActivity.this)
							.load(BuildConfig.BASE_URL_IMG + "/w154" + d_movie.getBelongsToCollection().getPosterPath())
							.into(detail_img_post_prod);

					prod_title.setText(d_movie.getBelongsToCollection().getName());
				}

				String budget;
				String revenue;

				if (d_movie.getBudget() <= 0) budget = "Unknown";
				else budget = "$ " + NumberFormat.getIntegerInstance().format(d_movie.getBudget());
				if (d_movie.getRevenue() <= 0) revenue = "Unknown";
				else
					revenue = "$ " + NumberFormat.getIntegerInstance().format(d_movie.getRevenue());

				prod_budget.setText(budget);
				prod_revenue.setText(revenue);

				StringBuilder companies = new StringBuilder();
				size = d_movie.getProductionCompanies().size();

				for (int i = 0; i < size; i++)
				{
					companies.append("✸ ").append(d_movie.getProductionCompanies().get(i).getName()).append(i + 1 < size ? "\n" : "");
				}

				prod_companies.setText(companies.toString());

				StringBuilder countries = new StringBuilder();
				size = d_movie.getProductionCountries().size();

				for (int i = 0; i < size; i++)
				{
					countries.append("✸ ").append(d_movie.getProductionCountries().get(i).getName()).append(i + 1 < size ? "\n" : "");
				}

				prod_countries.setText(countries.toString());
			}

			@Override
			public void onFailure(Call<DetailMovie> call, Throwable t)
			{

				Toast.makeText(DetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
	}
}
