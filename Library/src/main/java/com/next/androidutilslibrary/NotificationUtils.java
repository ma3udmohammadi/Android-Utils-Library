package com.next.androidutilslibrary;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;

public class NotificationUtils extends ContextWrapper
{
	private NotificationManager notificationManager;
	private Context context;
	private String channelId;
	private String channelName;
	private boolean makeSound;
	private boolean vibrate;

	public NotificationUtils(Context context)
	{
		super(context);
		this.context = context;
	}

	public void createChannel(String channelId, String channelName, boolean makeSound, boolean vibrate, boolean showBadge)
	{
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
		{
			this.channelId = channelId;
			this.channelName = channelName;
			this.makeSound = makeSound;
			this.vibrate = vibrate;
			// create android channel
			NotificationChannel newChannel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);

			// Sets whether notifications posted to this channel should display notification lights
			// newChannel.enableLights(false);
			// Sets whether notification posted to this channel should vibrate.
			if (!vibrate)
				newChannel.enableVibration(false);
			if (!makeSound)
				newChannel.setSound(null, null);
			if (!showBadge)
				newChannel.setShowBadge(false);
			// Sets the notification light color for notifications posted to this channel
			// newChannel.setLightColor(Color.RED);
			// Sets whether notifications posted to this channel appear on the lockscreen or not
			// newChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
			getNotificationManager().createNotificationChannel(newChannel);
		}
	}

	private NotificationManager getNotificationManager()
	{
		if (notificationManager == null)
		{
			notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		}
		return notificationManager;
	}

	public void sendNotificationInChannel(int notificationId, Intent resultIntent, NotificationCompat.Builder builder)
	{
		PendingIntent pi = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		// for notification click action, also required on Gingerbread and below
		builder.setContentIntent(pi);

		getNotificationManager().notify(notificationId, builder.build());
	}

	public void sendNotificationInChannel(String title, String body, int icon, boolean ongoing, int notificationId, Intent resultIntent)
	{
		sendNotificationInChannel(notificationId, resultIntent, getDefaultNotificationBuilder(title, body, icon, ongoing, channelId));
	}

	public void sendBigNotificationInChannel(String title, String body, int icon, boolean ongoing, int notificationId)
	{
		Intent resultIntent = new Intent(context, context.getClass());
		NotificationCompat.Builder builder = getDefaultNotificationBuilder(title, body, icon, ongoing, channelId);
		convertToBigNotificationBuilder(builder);
		sendNotificationInChannel(notificationId, resultIntent, builder);
	}

	public NotificationCompat.Builder getDefaultNotificationBuilder(String title, String body, int icon, boolean ongoing, String channelId)
	{
		Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

		NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId);
		// set title
		builder.setContentTitle(title);
		// set body
		builder.setContentText(body);
		// set ticker
		builder.setTicker("Notification");
		// set notification sound
		builder.setSound(defaultSoundUri);
		if (!makeSound)
			builder.setSound(null);
		if (!vibrate)
			builder.setVibrate(new long[]{0L});
		builder.setDefaults(Notification.DEFAULT_LIGHTS);
		builder.setOngoing(ongoing);
		// set small icon
		builder.setSmallIcon(icon);
		// set auto cancel behaviour
		builder.setAutoCancel(true);
		return builder;
	}

	private void convertToBigNotificationBuilder(NotificationCompat.Builder builder)
	{
		/* Add Big View Specific Configuration */
		NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

		String[] events = new String[6];
		events[0] = "This is 1st line...";
		events[1] = "This is 2nd line...";
		events[2] = "This is 3rd line...";
		events[3] = "This is 4th line...";
		events[4] = "This is 5th line...";
		events[5] = "This is 6th line...";

		// Sets a title for the Inbox style big view
		inboxStyle.setBigContentTitle("Big Title Details:");

		// Moves events into the big view
		for (final String event : events)
		{
			inboxStyle.addLine(event);
		}

		builder.setStyle(inboxStyle);
	}

	public void closeNotification(int notificationId)
	{
		notificationManager.cancel(notificationId);
	}
}