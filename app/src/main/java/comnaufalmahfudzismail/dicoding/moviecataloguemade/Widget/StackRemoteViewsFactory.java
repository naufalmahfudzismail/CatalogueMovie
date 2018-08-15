package comnaufalmahfudzismail.dicoding.moviecataloguemade.Widget;

import android.Manifest;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import comnaufalmahfudzismail.dicoding.moviecataloguemade.BuildConfig;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Database.MovieContractBuilder;

import java.util.*;
import java.util.concurrent.ExecutionException;

import comnaufalmahfudzismail.dicoding.moviecataloguemade.Entity.Movie;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.R;

import static android.provider.CalendarContract.SyncState.CONTENT_URI;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory
{

	private Context mContext;
	private int mAppWidgetId;
	private Cursor cursor;

	public StackRemoteViewsFactory(Context mContext, Intent intent)
	{
		this.mContext = mContext;
		mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
				AppWidgetManager.INVALID_APPWIDGET_ID);
	}

	@Override
	public void onCreate()
	{

	}

	@Override
	public void onDataSetChanged()
	{
		if (cursor != null)
		{
			cursor.close();
		}

		final long identityToken = Binder.clearCallingIdentity();

		cursor = mContext.getContentResolver().query(MovieContractBuilder.CONTENT_URI, null, null, null, null);

		Binder.restoreCallingIdentity(identityToken);
	}

	@Override
	public void onDestroy()
	{
		if (cursor != null)
		{
			cursor.close();
		}
	}

	@Override
	public int getCount()
	{
		if(cursor == null)
		{
			return 0;
		}
		else return cursor.getCount();
	}

	@Override
	public RemoteViews getViewAt(int position)
	{
		if (position == AdapterView.INVALID_POSITION ||
				cursor == null || !cursor.moveToPosition(position))
		{
			return null;
		}

		Movie movie = getItem(position);
		RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
		Bitmap bmp = null;

		try
		{
			bmp = Glide.with(mContext)
					.load(BuildConfig.BASE_URL_IMG + "/w342//" + movie.getPosterPath())
					.asBitmap()
					.into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();

		} catch (InterruptedException | ExecutionException e)
		{
			Log.d("Widget Load Error", e.toString());
		}

		Log.e("PFFFTT", "getViewAt: " + movie.getTitle());

		rv.setImageViewBitmap(R.id.movie_poster, bmp);
		rv.setTextViewText(R.id.tv_movie_title, movie.getTitle());
		return rv;
	}

	@Override
	public RemoteViews getLoadingView()
	{
		return null;
	}

	@Override
	public int getViewTypeCount()
	{
		return 1;
	}

	@Override
	public long getItemId(int position)
	{
		if(cursor.moveToPosition(position))
		{
			return cursor.getLong(0);
		}
		else return position;
	}

	private Movie getItem(int position)
	{
		if (!cursor.moveToPosition(position))
		{
			throw new IllegalStateException("Position invalid!");
		}

		return new Movie(cursor);
	}

	@Override
	public boolean hasStableIds()
	{
		return true;
	}
}
