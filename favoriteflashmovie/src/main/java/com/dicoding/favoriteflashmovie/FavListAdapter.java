package com.dicoding.favoriteflashmovie;

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

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavListAdapter extends RecyclerView.Adapter<FavListAdapter.FavHolder>
{

	private Cursor list_movies;
	private Context context;

	public FavListAdapter(Cursor items, Context context)
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
	public FavHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
	{
		View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_fav, viewGroup, false);
		return new FavHolder(view);
	}

	public MovieItem getMovie(int position)
	{
		if (!list_movies.moveToPosition(position))
			throw new IllegalStateException("Position Invalid");
		return new MovieItem(list_movies);
	}

	@Override
	public void onBindViewHolder(@NonNull FavHolder favHolder, int i)
	{
		final MovieItem movies = getMovie(i);

		String image_url = BuildConfig.BASE_URL_IMG + "/w342//" + movies.getPosterPath();

		Glide.with(context)
				.load(image_url)
				.placeholder(android.R.drawable.sym_def_app_icon)
				.error(android.R.drawable.sym_def_app_icon)
				.into(favHolder.movieImage);

		favHolder.movieTitle.setText(movies.getTitle());
		favHolder.data.setText(DateTime.getLongDate(movies.getReleaseDate()));
		favHolder.movieDescription.setText(movies.getOverview());


	}

	@Override
	public int getItemCount()
	{
		if (list_movies == null) return 0;

		return list_movies.getCount();
	}

	class FavHolder extends RecyclerView.ViewHolder
	{
		@BindView(R.id.fav_photo)
		ImageView movieImage;

		@BindView(R.id.fav_title)
		TextView movieTitle;

		@BindView(R.id.fav_date)
		TextView data;

		@BindView(R.id.fav_desc)
		TextView movieDescription;

		public FavHolder(@NonNull View itemView)
		{
			super(itemView);
			ButterKnife.bind(this, itemView);
		}
	}
}
