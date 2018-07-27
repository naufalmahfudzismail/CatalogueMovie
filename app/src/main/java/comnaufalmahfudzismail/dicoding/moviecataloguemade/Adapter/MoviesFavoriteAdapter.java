package comnaufalmahfudzismail.dicoding.moviecataloguemade.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Activity.DetailActivity;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.BuildConfig;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Class.Movie;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.R;

public class MoviesFavoriteAdapter extends RecyclerView.Adapter<MoviesFavoriteAdapter.FavoriteMovieHolder>
{
	private Cursor list_movies;
	private Context context;

	public MoviesFavoriteAdapter(Cursor items, Context context)
	{
		this.context = context;
		replaceAll(items);
	}

	public void replaceAll(Cursor items)
	{
		list_movies = items;
		notifyDataSetChanged();
	}


	@NonNull
	@Override
	public FavoriteMovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
	{
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_favorite, parent, false);
		return new FavoriteMovieHolder(view);
	}

	public Movie getMovie(int position)
	{
		if (!list_movies.moveToPosition(position))
			throw new IllegalStateException("Position Invalid");
		return new Movie(list_movies);
	}

	@Override
	public void onBindViewHolder(@NonNull FavoriteMovieHolder holder, @SuppressLint("RecyclerView") int position)
	{
		final Movie movies = getMovie(position);

		String image_url = BuildConfig.BASE_URL_IMG + "/w342//" + movies.getPosterPath();

		Glide.with(context)
				.load(image_url)
				.placeholder(android.R.drawable.sym_def_app_icon)
				.error(android.R.drawable.sym_def_app_icon)
				.into(holder.movieImage);

		holder.movieTitle.setText(movies.getTitle());
		holder.data.setText(DateTime.getLongDate(movies.getReleaseDate()));
		holder.movieDescription.setText(movies.getOverview());

		holder.detail.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(context, DetailActivity.class);
				intent.putExtra(DetailActivity.MOVIE_ITEM, new Gson().toJson(movies));
				context.startActivity(intent);
			}
		});
	}

	@Override
	public int getItemCount()
	{
		if (list_movies == null) return 0;

		return list_movies.getCount();
	}

	class FavoriteMovieHolder extends RecyclerView.ViewHolder
	{
		@BindView(R.id.fav_photo)
		ImageView movieImage;

		@BindView(R.id.fav_title)
		TextView movieTitle;

		@BindView(R.id.fav_date)
		TextView data;

		@BindView(R.id.fav_desc)
		TextView movieDescription;

		@BindView(R.id.fav_detail)
		Button detail;

		@BindView(R.id.fav_share)
		Button share;


		FavoriteMovieHolder(View itemView)
		{
			super(itemView);
			ButterKnife.bind(this, itemView);
		}
	}
}
