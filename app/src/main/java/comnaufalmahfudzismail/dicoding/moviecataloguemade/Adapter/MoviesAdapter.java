package comnaufalmahfudzismail.dicoding.moviecataloguemade.Adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.*;

import butterknife.BindView;
import butterknife.ButterKnife;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Activity.DetailActivity;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.BuildConfig;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Entity.Movie;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.R;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>
{
	private List<Movie> movies = new ArrayList<>();
	private static Movie movie;

	public static Movie getMovie()
	{
		return movie;
	}

	public void replaceAll(List<Movie> items)
	{
		movies.clear();
		movies = items;
		notifyDataSetChanged();
	}

	public List<Movie> getMovies()
	{
		return movies;
	}

	public void setMovies(List<Movie> movies)
	{
		this.movies = movies;
	}

	public void updateData(List<Movie> items)
	{
		movies.addAll(items);
		notifyDataSetChanged();
	}

	public static void setMovie(Movie movie)
	{
		MoviesAdapter.movie = movie;
	}

	public MoviesAdapter()
	{

	}

	//A view holder inner class where we get reference to the views in the layout using their ID
	static class MovieViewHolder extends RecyclerView.ViewHolder
	{
		@BindView(R.id.movies_layout)
		RelativeLayout moviesLayout;

		@BindView(R.id.title)
		TextView movieTitle;

		@BindView(R.id.date)
		TextView data;

		@BindView(R.id.description)
		TextView movieDescription;

		@BindView(R.id.rating)
		TextView rating;

		@BindView(R.id.number_movie)
		TextView number;

		@BindView(R.id.movie_image)
		ImageView movieImage;

		MovieViewHolder(View v)
		{
			super(v);
			ButterKnife.bind(this, v);
		}
	}

	@NonNull
	@Override
	public MoviesAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
	                                                        int viewType)
	{
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_movie, parent, false);
		return new MovieViewHolder(view);
	}

	@SuppressLint("SetTextI18n")
	@Override
	public void onBindViewHolder(@NonNull final MovieViewHolder holder, @SuppressLint("RecyclerView") final int position)
	{
		String image_url = BuildConfig.BASE_URL_IMG + "/w342//" + movies.get(position).getPosterPath();
		Glide.with(holder.itemView.getContext())
				.load(image_url)
				.placeholder(android.R.drawable.sym_def_app_icon)
				.error(android.R.drawable.sym_def_app_icon)
				.into(holder.movieImage);

		holder.number.setText("" + (movies.indexOf(movies.get(position)) + 1));

		holder.movieTitle.setText(movies.get(position).getTitle());
		holder.data.setText(movies.get(position).getReleaseDate());
		holder.movieDescription.setText(movies.get(position).getOverview());
		holder.rating.setText(movies.get(position).getVoteAverage().toString());

		holder.moviesLayout.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(holder.itemView.getContext(), DetailActivity.class);
				intent.putExtra(DetailActivity.MOVIE_ITEM, new Gson().toJson(movies.get(position)));
				holder.itemView.getContext().startActivity(intent);
			}
		});

	}

	@Override
	public int getItemCount()
	{
		return movies.size();
	}
}