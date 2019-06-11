package com.next.androidutils;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.next.androidutilslibrary.NotificationUtils;
import com.next.androidutilslibrary.Utils;

public class MainActivity extends AppCompatActivity
{
	NotificationUtils notificationUtils;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Utils.initialize(getApplicationContext());
		notificationUtils = new NotificationUtils(this);

		Intent resultIntent = new Intent(this, MainActivity.class);
		// channel is only created once and can't be changed after that, so if you want to edit  re-install the app or change channel id
		notificationUtils.createChannel("PERSONAL", "Personal", false, false, false);
		notificationUtils.sendNotificationInChannel("Timer is running ...", "05:03",
				R.drawable.ic_timer_black_24dp, true, 101, resultIntent);
		notificationUtils.sendNotificationInChannel("Timer is running ...", "05:05",
				R.drawable.ic_timer_black_24dp, true, 102, resultIntent);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		notificationUtils.closeNotification(101);
		notificationUtils.closeNotification(102);
	}
}