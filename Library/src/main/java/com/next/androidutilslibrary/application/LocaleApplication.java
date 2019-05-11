package com.next.androidutilslibrary.application;

import android.app.Application;
import android.content.Context;

import com.next.androidutilslibrary.LocaleUtils;

/**
 * Created by kingdom2011 on 8/7/2017.
 */

public class LocaleApplication extends Application
{
	@Override
	protected void attachBaseContext(Context base)
	{
		super.attachBaseContext(LocaleUtils.onAttach(base, "en"));
	}
}