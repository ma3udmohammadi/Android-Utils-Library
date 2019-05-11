package com.next.androidutilslibrary;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class ServiceUtils
{
	private ServiceUtils()
	{
		throw new UnsupportedOperationException("u can't instantiate me...");
	}

	public static Set getAllRunningService()
	{
		ActivityManager activityManager = (ActivityManager) Utils.getApp().getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> info = activityManager.getRunningServices(0x7FFFFFFF);
		Set<String> names = new HashSet<>();
		if (info == null || info.size() == 0) return null;
		for (RunningServiceInfo aInfo : info)
		{
			names.add(aInfo.service.getClassName());
		}
		return names;
	}

	public static void startService(final String className)
	{
		try
		{
			startService(Class.forName(className));
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void startService(final Class<?> cls)
	{
		Intent intent = new Intent(Utils.getApp(), cls);
		Utils.getApp().startService(intent);
	}

	public static boolean stopService(final String className)
	{
		try
		{
			return stopService(Class.forName(className));
		} catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	public static boolean stopService(final Class<?> cls)
	{
		Intent intent = new Intent(Utils.getApp(), cls);
		return Utils.getApp().stopService(intent);
	}

	public static void bindService(final String className, final ServiceConnection conn, final int flags)
	{
		try
		{
			bindService(Class.forName(className), conn, flags);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void bindService(final Class<?> cls, final ServiceConnection conn, final int flags)
	{
		Intent intent = new Intent(Utils.getApp(), cls);
		Utils.getApp().bindService(intent, conn, flags);
	}

	public static void unbindService(final ServiceConnection conn)
	{
		Utils.getApp().unbindService(conn);
	}

	public static boolean isServiceRunning(final String className)
	{
		ActivityManager activityManager = (ActivityManager) Utils.getApp().getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> info = activityManager.getRunningServices(0x7FFFFFFF);
		if (info == null || info.size() == 0) return false;
		for (RunningServiceInfo aInfo : info)
		{
			if (className.equals(aInfo.service.getClassName())) return true;
		}
		return false;
	}
}