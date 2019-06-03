package com.next.androidutils;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.next.androidutilslibrary.NotificationUtils;
import com.next.androidutilslibrary.Utils;

public class MainActivity extends AppCompatActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Utils.initialize(getApplicationContext());
		NotificationUtils notificationUtils = new NotificationUtils(this);

		notificationUtils.createChannel("PERSONAL", "Personal", false, false, false);
		notificationUtils.sendNotificationInChannel("Timer is running ...", "05:03", R.drawable.ic_timer_black_24dp, true, 101);
		notificationUtils.sendNotificationInChannel("Timer is running ...", "05:05", R.drawable.ic_timer_black_24dp, true, 102);
	}
}