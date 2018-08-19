package comnaufalmahfudzismail.dicoding.moviecataloguemade.Fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindString;
import butterknife.ButterKnife;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.API.MovieApiClient;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.API.MovieApiService;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Broadcast.DailyAlarmReceiver;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Broadcast.UpcomingAlarmReceiver;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.BuildConfig;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Entity.Movie;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Entity.MovieResponse;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Entity.Region;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.R;
import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentSetting extends PreferenceFragment implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener
{
	@BindString(R.string.key_setting_locale)
	String keySettingLocale;
	@BindString(R.string.key_reminder_daily)
	String keyDailyReminder;
	@BindString(R.string.key_reminder_upcoming)
	String keyUpcomingReminder;

	Call<MovieResponse> call;

	List<Movie> allMovies = new ArrayList<>();
	List<Movie> movieReminded = new ArrayList<>();

	private int Page = 1;
	private int totalPage = 1;

	private UpcomingAlarmReceiver alarmReceiver = new UpcomingAlarmReceiver();
	private DailyAlarmReceiver dailyAlarmReceiver = new DailyAlarmReceiver();

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		ButterKnife.bind(this, getActivity());

		SwitchPreference switchDailyReminder = (SwitchPreference) findPreference(keyDailyReminder);
		switchDailyReminder.setOnPreferenceChangeListener(this);
		SwitchPreference switchUpcomingReminder = (SwitchPreference) findPreference(keyUpcomingReminder);
		switchUpcomingReminder.setOnPreferenceChangeListener(this);
		findPreference(keySettingLocale).setOnPreferenceClickListener(this);
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue)
	{
		String key = preference.getKey();
		boolean isOn = (boolean) newValue;

		if (key.equals(keyDailyReminder))
		{
			if (isOn)
			{
				dailyAlarmReceiver.setRepeatingAlarm(getActivity());
			} else
			{
				dailyAlarmReceiver.cancelAlarm(getActivity());
			}
		} else
		{
			if (isOn)
			{
				@SuppressLint("StaticFieldLeak")
				AsyncTask<String, Integer, String> asyncTask = new AsyncTask<String, Integer, String>()
				{
					@Override
					protected String doInBackground(String... params)
					{
						getUpcomingMovie();
						return "done";
					}

					@Override
					protected void onPostExecute(String s)
					{
						super.onPostExecute(s);
					}
				};
				asyncTask.execute();

			} else
			{
				alarmReceiver.cancelAlarm(getActivity());

			}
		}

		return true;
	}

	@Override
	public boolean onPreferenceClick(Preference preference)
	{
		String key = preference.getKey();

		if (key.equals(keySettingLocale))
		{
			Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
			startActivity(intent);
			return true;
		}

		return false;
	}

	private void getUpcomingMovie()
	{
		SyncHttpClient client = new SyncHttpClient();
		final List<Movie> moviesList = new ArrayList<>();
		final int[] totalPages = new int[1];

		String api = BuildConfig.API_KEY;
		String region = Region.region;
		String mode = "movie/upcoming?";
		String baseUrl = BuildConfig.BASE_URL;

		for (int i = 0; i < totalPage; i++)
		{

			String url = baseUrl + mode + "api_key=" + api + "&language=en-US&page=" + Page + "&region=" + region;

			client.get(url, new AsyncHttpResponseHandler()
			{
				@Override
				public void onStart()
				{
					super.onStart();
					setUseSynchronousMode(true);
				}

				@Override
				public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
				{
					try
					{
						String result = new String(responseBody);
						JSONObject responseObject = new JSONObject(result);

						JSONArray movies = responseObject.getJSONArray("results");
						for (int i = 0; i < movies.length(); i++)
						{
							JSONObject movie = movies.getJSONObject(i);
							Movie movieItem = new Movie(movie);
							moviesList.add(movieItem);
						}

						totalPages[0] = responseObject.getInt("total_pages");

						totalPage = totalPages[0];
						allMovies.addAll(moviesList);
						Page++;
						Log.d("alarmReceiver", "Add Movie" + moviesList.size());

					} catch (Exception e)
					{
						e.printStackTrace();
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error)
				{

				}
			});

			Log.d("alarmReceiver", url);
		}


		Log.d("alarmReceiver", "Add Movie : " + allMovies.size());

		Date currentTime = Calendar.getInstance().getTime();
		@SuppressLint("SimpleDateFormat")
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String currentDate = sdf.format(currentTime);
		Log.e("currentDate", currentDate);

		for (int i = 0; i < allMovies.size(); i++)
		{
			Movie movie = allMovies.get(i);
			if (movie.getReleaseDate().equals(currentDate))
			{
				movieReminded.add(movie);
				Log.d("alarm", movie.toString());
			}
		}

		Log.d("alarm", String.valueOf(movieReminded.size()));

		alarmReceiver.setRepeatingAlarm(getActivity(), movieReminded);
	}
}
