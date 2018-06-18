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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.*;

import comnaufalmahfudzismail.dicoding.moviecataloguemade.Activity.DetailActivity;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.BuildConfig;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Class.Movie;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.R;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>
{
	private List<Movie> movies;
	private Context context;
	private static Movie movie;

	public static Movie getMovie()
	{
		return movie;
	}

	public void replaceAll(List<Movie> items) {
		movies.clear();
		movies = items;
		notifyDataSetChanged();
	}

	public void updateData(List<Movie> items) {
		movies.addAll(items);
		notifyDataSetChanged();
	}

	public static void setMovie(Movie movie)
	{
		MoviesAdapter.movie = movie;
	}

	public MoviesAdapter(List<Movie> movies, Context context)
	{
		this.movies = movies;
		this.context = context;
	}

	//A view holder inner class where we get reference to the views in the layout using their ID
	static class MovieViewHolder extends RecyclerView.ViewHolder
	{
		RelativeLayout moviesLayout;
		TextView movieTitle;
		TextView data;
		TextView movieDescription;
		TextView rating;
		TextView number;
		ImageView movieImage;


		MovieViewHolder(View v)
		{
			super(v);
			moviesLayout =  v.findViewById(R.id.movies_layout);
			number  = v.findViewById(R.id.number_movie);
			movieImage =  v.findViewById(R.id.movie_image);
			movieTitle =  v.findViewById(R.id.title);
			data =  v.findViewById(R.id.date);
			movieDescription =  v.findViewById(R.id.description);
			rating =  v.findViewById(R.id.rating);
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
	public void onBindViewHolder(@NonNull MovieViewHolder holder, @SuppressLint("RecyclerView") final int position)
	{
		String image_url = BuildConfig.BASE_URL_IMG+ "/w342//" + movies.get(position).getPosterPath();
		Glide.with(context)
				.load(image_url)
				.placeholder(android.R.drawable.sym_def_app_icon)
				.error(android.R.drawable.sym_def_app_icon)
				.into(holder.movieImage);

		holder.number.setText(""+(movies.indexOf(movies.get(position))+1));

		holder.movieTitle.setText(movies.get(position).getTitle());
		holder.data.setText(movies.get(position).getReleaseDate());
		holder.movieDescription.setText(movies.get(position).getOverview());
		holder.rating.setText(movies.get(position).getVoteAverage().toString());

	}

	@Override
	public int getItemCount()
	{
		return movies.size();
	}
}