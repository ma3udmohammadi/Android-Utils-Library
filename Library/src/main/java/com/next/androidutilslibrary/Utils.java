package com.next.androidutilslibrary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import androidx.annotation.NonNull;
import android.util.DisplayMetrics;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

public final class Utils
{
	@SuppressLint("StaticFieldLeak")
	private static Context context;
	static WeakReference<Activity> sTopActivityWeakRef;
	static List<Activity> sActivityList = new LinkedList<>();

	public static void initialize(@NonNull Context context)
	{
		Utils.context = context;
	}

	public static Context getApp()
	{
		synchronized (Utils.class)
		{
			if (Utils.context == null)
				throw new NullPointerException("Call Base.initialize(context) within your Application onCreate() method.");

			return Utils.context.getApplicationContext();
		}
	}

	public static Resources getResources()
	{
		return Utils.getApp().getResources();
	}

	public static Resources.Theme getTheme()
	{
		return Utils.getApp().getTheme();
	}

	public static AssetManager getAssets()
	{
		return Utils.getApp().getAssets();
	}

	public static Configuration getConfiguration()
	{
		return Utils.getResources().getConfiguration();
	}

	public static DisplayMetrics getDisplayMetrics()
	{
		return Utils.getResources().getDisplayMetrics();
	}

	private static void setTopActivityWeakRef(Activity activity)
	{
		if (sTopActivityWeakRef == null || !activity.equals(sTopActivityWeakRef.get()))
		{
			sTopActivityWeakRef = new WeakReference<>(activity);
		}
	}

	private static boolean isSpace(final String s)
	{
		if (s == null) return true;
		for (int i = 0, len = s.length(); i < len; ++i)
		{
			if (!Character.isWhitespace(s.charAt(i)))
			{
				return false;
			}
		}
		return true;
	}
}