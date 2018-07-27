package comnaufalmahfudzismail.dicoding.moviecataloguemade.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Activity.DetailActivity;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.BuildConfig;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Class.DetailMovie.DetailMovie;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Class.Movie;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.R;

public class MoviesUpcomingAdapter extends RecyclerView.Adapter<MoviesUpcomingAdapter.UpMovieViewHolder>
{
	private List<Movie> movies;
	private Context context;
	private static Movie movie;

	public static Movie getMovie()
	{
		return movie;
	}

	public List<Movie> getMovies()
	{
		return movies;
	}

	public void setMovies(List<Movie> movies)
	{
		this.movies = movies;
	}

	public void replaceAll(List<Movie> items)
	{
		movies.clear();
		movies = items;

		notifyDataSetChanged();
	}

	public void updateData(List<Movie> items)
	{
		movies.addAll(items);
		notifyDataSetChanged();
	}

	public static void setMovie(Movie movie)
	{
		MoviesUpcomingAdapter.movie = movie;
	}

	public MoviesUpcomingAdapter(List<Movie> movies, Context context)
	{
		this.movies = movies;
		this.context = context;
	}

	//A view holder inner class where we get reference to the views in the layout using their ID
	static class UpMovieViewHolder extends RecyclerView.ViewHolder
	{
		@BindView(R.id.up_photo)
		ImageView movieImage;

		@BindView(R.id.up_title)
		TextView movieTitle;

		@BindView(R.id.up_date)
		TextView data;

		@BindView(R.id.up_desc)
		TextView movieDescription;

		@BindView(R.id.up_detail)
		Button detail;

		@BindView(R.id.up_share)
		Button share;


		UpMovieViewHolder(View v)
		{
			super(v);
			ButterKnife.bind(this, v);
		}
	}

	@NonNull
	@Override
	public MoviesUpcomingAdapter.UpMovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
	                                                                  int viewType)
	{
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_upcoming_movie, parent, false);
		return new UpMovieViewHolder(view);
	}

	@SuppressLint("SetTextI18n")
	@Override
	public void onBindViewHolder(@NonNull UpMovieViewHolder holder, @SuppressLint("RecyclerView") final int position)
	{
		String image_url = BuildConfig.BASE_URL_IMG + "/w342//" + movies.get(position).getPosterPath();
		Glide.with(context)
				.load(image_url)
				.placeholder(android.R.drawable.sym_def_app_icon)
				.error(android.R.drawable.sym_def_app_icon)
				.into(holder.movieImage);
		holder.movieTitle.setText(movies.get(position).getTitle());
		holder.data.setText(movies.get(position).getReleaseDate());
		holder.movieDescription.setText(movies.get(position).getOverview());

		holder.detail.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(context, DetailActivity.class);
				intent.putExtra(DetailActivity.MOVIE_ITEM, new Gson().toJson(movies.get(position)));
				context.startActivity(intent);
			}
		});

		holder.share.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/plain");
				intent.putExtra(Intent.EXTRA_TITLE, movies.get(position).getTitle());
				intent.putExtra(Intent.EXTRA_SUBJECT, movies.get(position).getTitle());
				intent.putExtra(Intent.EXTRA_TEXT, movies.get(position).getTitle() + "\n\n" + movies.get(position).getOverview());
				context.startActivity(Intent.createChooser(intent, context.getResources().getString(R.string.share)));
			}
		});

	}

	@Override
	public int getItemCount()
	{
		return movies.size();
	}
}