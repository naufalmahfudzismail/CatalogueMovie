package comnaufalmahfudzismail.dicoding.moviecataloguemade.Database;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContractBuilder
{
	public static final String TABLE_MOVIES = "table_movies";
	public static final String CONTENT_AUTHORITY = "comnaufalmahfudzismail.dicoding.moviecataloguemade";

	public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
			.authority(CONTENT_AUTHORITY)
			.appendPath(TABLE_MOVIES)
			.build();

	public static final class MovieColumns implements BaseColumns
	{
		public static final String COLUMN_MOVIE_ID = "_id";
		public static final String COLUMN_MOVIE_TITLE = "movieTitle";
		public static final String COLUMN_MOVIE_OVERVIEW = "movieOverview";
		public static final String COLUMN_MOVIE_VOTE_COUNT = "movieVoteCount";
		public static final String COLUMN_MOVIE_VOTE_AVERAGE = "movieVoteAverage";
		public static final String COLUMN_MOVIE_RELEASE_DATE = "movieReleaseDate";
		public static final String COLUMN_MOVIE_POSTER_PATH = "moviePosterPath";
		public static final String COLUMN_MOVIE_BACKDROP_PATH = "movieBackdropPath";
	}

	public static String getColumnString(Cursor cursor, String columnName)
	{
		return cursor.getString(cursor.getColumnIndex(columnName));
	}

	public static int getColumnInt(Cursor cursor, String columnName)
	{
		return cursor.getInt(cursor.getColumnIndex(columnName));
	}

	public static double getColumnDouble(Cursor cursor, String columnName)
	{
		return cursor.getDouble(cursor.getColumnIndex(columnName));
	}

	public static long getColumnLong(Cursor cursor, String columnName)
	{
		return cursor.getLong(cursor.getColumnIndex(columnName));
	}
}
