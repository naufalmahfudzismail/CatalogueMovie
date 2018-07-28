package com.dicoding.favoriteflashmovie;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.dicoding.favoriteflashmovie.MovieContractBuilder.MovieColumns.COLUMN_MOVIE_BACKDROP_PATH;
import static com.dicoding.favoriteflashmovie.MovieContractBuilder.MovieColumns.COLUMN_MOVIE_ID;
import static com.dicoding.favoriteflashmovie.MovieContractBuilder.MovieColumns.COLUMN_MOVIE_OVERVIEW;
import static com.dicoding.favoriteflashmovie.MovieContractBuilder.MovieColumns.COLUMN_MOVIE_POSTER_PATH;
import static com.dicoding.favoriteflashmovie.MovieContractBuilder.MovieColumns.COLUMN_MOVIE_RELEASE_DATE;
import static com.dicoding.favoriteflashmovie.MovieContractBuilder.MovieColumns.COLUMN_MOVIE_TITLE;
import static com.dicoding.favoriteflashmovie.MovieContractBuilder.MovieColumns.COLUMN_MOVIE_VOTE_AVERAGE;
import static com.dicoding.favoriteflashmovie.MovieContractBuilder.MovieColumns.COLUMN_MOVIE_VOTE_COUNT;
import static com.dicoding.favoriteflashmovie.MovieContractBuilder.TABLE_MOVIES;


public class DatabaseMovieBuilder extends SQLiteOpenHelper
{
	private static final String DB_NAME = "Catalogue_movies";
	private static final int DB_VERSION = 1;

	private static final String CREATE_MOVIE_TABLE = "CREATE TABLE "+ TABLE_MOVIES + " " +
			"(" +
			COLUMN_MOVIE_ID + " integer primary key autoincrement, "+
			COLUMN_MOVIE_TITLE +" text not null, " +
			COLUMN_MOVIE_BACKDROP_PATH +" text not null, " +
			COLUMN_MOVIE_POSTER_PATH +" text not null, " +
			COLUMN_MOVIE_RELEASE_DATE +" text not null, " +
			COLUMN_MOVIE_VOTE_AVERAGE +" text not null, " +
			COLUMN_MOVIE_OVERVIEW+" text not null, " +
			COLUMN_MOVIE_VOTE_COUNT +" text not null " +
			") ;";


	private Context context;

	public DatabaseMovieBuilder(Context context)
	{
		super(context, DB_NAME, null, DB_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(CREATE_MOVIE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIES);
		onCreate(db);
	}
}
