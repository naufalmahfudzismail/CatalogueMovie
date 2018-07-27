package comnaufalmahfudzismail.dicoding.moviecataloguemade.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import static android.provider.BaseColumns._ID;

public class FavoriteBuilder
{
	private static String TABLE_NAME = MovieContractBuilder.TABLE_MOVIES;

	private Context context;
	private DatabaseMovieBuilder databaseHelper;

	private SQLiteDatabase database;

	public FavoriteBuilder(Context context)
	{
		this.context = context;
	}

	public FavoriteBuilder open() throws SQLException
	{
		databaseHelper = new DatabaseMovieBuilder(context);
		database = databaseHelper.getWritableDatabase();
		return this;
	}

	public void close()
	{
		databaseHelper.close();
	}

	public  Cursor queryProvider()
	{
		return database.query(
				TABLE_NAME,
				null,
				null,
				null,
				null,
				null,
				_ID + " DESC"
		);
	}

	public Cursor queryByIdProvider(String id)
	{
		return database.query(
				TABLE_NAME,
				null,
				_ID + " = ?",
				new String[]{id},
				null,
				null,
				null
		);
	}

	public long insertProvider(ContentValues values)
	{
		return database.insert(
				TABLE_NAME,
				null,
				values
		);
	}

	public int updateProvider(String id, ContentValues values)
	{
		return database.update(
				TABLE_NAME,
				values,
				_ID + " = ?",
				new String[]{id}
		);
	}

	public int deleteProvider(String id)
	{
		return database.delete(
				TABLE_NAME,
				_ID + " = ?",
				new String[]{id}
		);
	}
}
