package comnaufalmahfudzismail.dicoding.moviecataloguemade.Broadcast;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.Calendar;
import java.util.List;

import comnaufalmahfudzismail.dicoding.moviecataloguemade.Activity.DetailActivity;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.Entity.Movie;
import comnaufalmahfudzismail.dicoding.moviecataloguemade.R;

public class UpcomingAlarmReceiver extends BroadcastReceiver
{

	private static final int NOTIFICATION_ID = 101;

	public static final String NOTIFICATION_CHANNEL_ID = "10001";

	private static int notifId = 1000;

	public UpcomingAlarmReceiver()
	{

	}

	@Override
	public void onReceive(Context context, Intent intent)
	{
		int notifId = intent.getIntExtra("id", 0);
		String title = intent.getStringExtra("title");

		showAlarmNotification(context, title, notifId);
	}

	private void showAlarmNotification(Context context, String title, int notifId)
	{
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(
				Context.NOTIFICATION_SERVICE);

		Intent intent = new Intent(context, DetailActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, notifId, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		Uri alarmRingtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
				.setSmallIcon(R.drawable.ic_alarm_black)
				.setContentTitle(title)
				.setContentText(String.valueOf(String.format(context.getString(R.string.upcoming_reminder_msg), title)))
				.setPriority(NotificationCompat.PRIORITY_HIGH)
				.setAutoCancel(true)
				.setColor(ContextCompat.getColor(context, android.R.color.transparent))
				.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
				.setSound(alarmRingtone);

		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
		{
			int importance = NotificationManager.IMPORTANCE_HIGH;
			NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
			notificationChannel.enableLights(true);
			notificationChannel.setLightColor(Color.RED);
			notificationChannel.enableVibration(true);
			notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

			builder.setChannelId(NOTIFICATION_CHANNEL_ID);
			if (notificationManager != null)
			{
				notificationManager.createNotificationChannel(notificationChannel);
			}
		}

		if (notificationManager != null)
		{
			notificationManager.notify(notifId, builder.build());
		}
	}

	public void setRepeatingAlarm(Context context, List<Movie> movies)
	{
		int delay = 0;

		for (int i = 0; i <movies.size(); i++)
		{
			cancelAlarm(context);
			AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			Intent intent = new Intent(context, UpcomingAlarmReceiver.class);
			intent.putExtra("title", movies.get(i).getTitle());
			intent.putExtra("id", notifId);

			PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY, 6);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);

			int SDK_INT = Build.VERSION.SDK_INT;
			if (SDK_INT < Build.VERSION_CODES.KITKAT)
			{
				if (alarmManager != null)
				{
					alarmManager.set(AlarmManager.RTC_WAKEUP,
							calendar.getTimeInMillis() + delay, pendingIntent);
				}
			} else if (SDK_INT > Build.VERSION_CODES.KITKAT && SDK_INT < Build.VERSION_CODES.M)
			{
				if (alarmManager != null)
				{
					alarmManager.setInexactRepeating(
							AlarmManager.RTC_WAKEUP,
							calendar.getTimeInMillis() + delay,
							AlarmManager.INTERVAL_DAY,
							pendingIntent
					);
				}
			} else if (SDK_INT >= Build.VERSION_CODES.M)
			{
				if (alarmManager != null)
				{
					alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
							calendar.getTimeInMillis() + delay, pendingIntent);
				}
			}

			notifId += 1;
			delay += 5000;
		}
	}

	public void cancelAlarm(Context context)
	{
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		if (alarmManager != null)
		{
			alarmManager.cancel(getPendingIntent(context));
		}
	}

	private static PendingIntent getPendingIntent(Context context)
	{
		Intent alarmIntent = new Intent(context, UpcomingAlarmReceiver.class);
		boolean isAlarmOn = (PendingIntent.getBroadcast(context, notifId, alarmIntent,
				PendingIntent.FLAG_NO_CREATE) != null);
		return PendingIntent.getBroadcast(context, 101, alarmIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);
	}
}
