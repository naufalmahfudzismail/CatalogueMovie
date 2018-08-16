package comnaufalmahfudzismail.dicoding.moviecataloguemade.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindString;
import butterknife.ButterKnife;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.API.MovieApiClient;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.API.MovieApiService;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Broadcast.DailyAlarmReceiver;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Broadcast.UpcomingAlarmReceiver;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Entity.Movie;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Entity.MovieResponse;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Entity.Region;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.R;
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

	private SwitchPreference switchDailyReminder;
	private SwitchPreference switchUpcomingReminder;

	Call<MovieResponse> call;

	List<Movie> movieList = new ArrayList<>();
	List<Movie> movieReminded = new ArrayList<>();

	MovieApiClient client = new MovieApiClient();
	private int currentPage = 1;
	private int totalPage = 1;

	private UpcomingAlarmReceiver alarmReceiver = new UpcomingAlarmReceiver();
	private DailyAlarmReceiver dailyAlarmReceiver = new DailyAlarmReceiver();

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		ButterKnife.bind(this, getActivity());

		switchDailyReminder = (SwitchPreference) findPreference(keyDailyReminder);
		switchDailyReminder.setOnPreferenceChangeListener(this);
		switchUpcomingReminder = (SwitchPreference) findPreference(keyUpcomingReminder);
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
				getUpcomingMovie();
				alarmReceiver.setRepeatingAlarm(getActivity(), movieReminded);

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
		for (int i = currentPage; i <= totalPage; i++)
		{
			call = client.getService().getUpcomingMovie(currentPage, Region.region);
			call.enqueue(new Callback<MovieResponse>()
			{
				@Override
				public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response)
				{
					List<Movie> movies = response.body().getResults();
					totalPage = response.body().getTotalPages();

					Log.d("Number Movies", String.valueOf(movies.size()));
					movieList.addAll(movies);
					currentPage++;
				}

				@Override
				public void onFailure(Call<MovieResponse> call, Throwable t)
				{
					Log.d("Alarm", t.toString());
				}
			});
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		Date date = new Date();
		String currentDate = dateFormat.format(date);
		Log.e("currentDate", currentDate);

		for (int i = 0; i < movieList.size(); i++)
		{
			Movie movie = movieList.get(i);
			if (movie.getReleaseDate().equals(currentDate))
			{
				movieReminded.add(movie);
			}
		}
	}
}
