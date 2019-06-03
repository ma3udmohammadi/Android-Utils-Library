package com.next.androidutils;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

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
		notificationUtils.createChannel("PERSONAL", "Personal");
		notificationUtils.sendNotificationInDefaultChannel("Timer is running ...", "05:03", R.drawable.ic_timer_black_24dp, 101);
	}
}