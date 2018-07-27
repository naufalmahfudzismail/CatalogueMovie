package comnaufalmahfudzismail.dicoding.moviecataloguemade.Database;

import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static android.provider.BaseColumns._ID;
import static comnaufalmahfudzismail.dicoding.moviecataloguemade.Database.MovieContractBuilder.TABLE_MOVIES;
import static comnaufalmahfudzismail.dicoding.moviecataloguemade.Database.MovieContractBuilder.CONTENT_URI;
import static comnaufalmahfudzismail.dicoding.moviecataloguemade.Database.MovieContractBuilder.CONTENT_AUTHORITY;


public class MovieProvider extends ContentProvider
{
	private SQLiteDatabase db;
	private FavoriteBuilder favoriteBuilder;

	private static final int MOVIES = 100;
	private static final int MOVIES_WITH_ID = 101;

	private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

	static
	{
		uriMatcher.addURI(CONTENT_AUTHORITY, TABLE_MOVIES, MOVIES);
		uriMatcher.addURI(CONTENT_AUTHORITY, TABLE_MOVIES + "/#", MOVIES_WITH_ID);
	}

	@Override
	public boolean onCreate()
	{
		favoriteBuilder = new FavoriteBuilder(getContext());
		favoriteBuilder.open();
		return true;
	}

	@Nullable
	@Override
	public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder)
	{
		Cursor cursor;
		switch (uriMatcher.match(uri))
		{
			case MOVIES:
				cursor = favoriteBuilder.queryProvider();
				break;

			case MOVIES_WITH_ID:
				cursor = favoriteBuilder.queryByIdProvider(uri.getLastPathSegment());
				break;

			default:
				cursor = null;
				break;
		}

		if (cursor != null)
			cursor.setNotificationUri(getContext().getContentResolver(), uri);

		return cursor;
	}

	@Nullable
	@Override
	public String getType(@NonNull Uri uri)
	{
		return null;
	}

	@Nullable
	@Override
	public Uri insert(@NonNull Uri uri, @Nullable ContentValues values)
	{
		long added;
		switch (uriMatcher.match(uri))
		{
			case MOVIES:
				added = favoriteBuilder.insertProvider(values);
				break;

			default:
				added = 0;
				break;
		}

		if (added > 0)
		{
			getContext().getContentResolver().notifyChange(uri, null);
		}

		return Uri.parse(CONTENT_URI + "/" + added);
	}

	@Override
	public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs)
	{
		int deleted;
		switch (uriMatcher.match(uri))
		{
			case MOVIES_WITH_ID:
				deleted = favoriteBuilder.deleteProvider(uri.getLastPathSegment());
				break;

			default:
				deleted = 0;
				break;
		}

		if (deleted > 0)
		{
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return deleted;
	}

	@Override
	public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs)
	{
		int updated;
		switch (uriMatcher.match(uri))
		{
			case MOVIES_WITH_ID:
				updated = favoriteBuilder.updateProvider(uri.getLastPathSegment(), values);
				break;

			default:
				updated = 0;
				break;
		}

		if (updated > 0)
		{
			getContext().getContentResolver().notifyChange(uri, null);
		}

		return updated;
	}
}
