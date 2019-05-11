package com.next.androidutilslibrary;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.provider.Settings;
import androidx.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public final class ScreenUtils
{
	private ScreenUtils()
	{
		throw new UnsupportedOperationException("u can't instantiate me...");
	}

	public static int getScreenWidth()
	{
		return Utils.getApp().getResources().getDisplayMetrics().widthPixels;
	}

	public static int getScreenHeight()
	{
		return Utils.getApp().getResources().getDisplayMetrics().heightPixels;
	}

	public static float getScreenDensity()
	{
		return Utils.getApp().getResources().getDisplayMetrics().density;
	}

	public static int getScreenDensityDpi()
	{
		return Utils.getApp().getResources().getDisplayMetrics().densityDpi;
	}

	public static void setFullScreen(@NonNull final Activity activity)
	{
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
		activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	public static void setLandscape(@NonNull final Activity activity)
	{
		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}

	public static void setPortrait(@NonNull final Activity activity)
	{
		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	public static boolean isLandscape()
	{
		return Utils.getApp().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
	}

	public static boolean isPortrait()
	{
		return Utils.getApp().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
	}

	public static int getScreenRotation(@NonNull final Activity activity)
	{
		switch (activity.getWindowManager().getDefaultDisplay().getRotation())
		{
			default:
			case Surface.ROTATION_0:
				return 0;
			case Surface.ROTATION_90:
				return 90;
			case Surface.ROTATION_180:
				return 180;
			case Surface.ROTATION_270:
				return 270;
		}
	}

	public static Bitmap screenShot(@NonNull final Activity activity)
	{
		return screenShot(activity, false);
	}

	public static Bitmap screenShot(@NonNull final Activity activity, boolean isDeleteStatusBar)
	{
		View decorView = activity.getWindow().getDecorView();
		decorView.setDrawingCacheEnabled(true);
		decorView.buildDrawingCache();
		Bitmap bmp = decorView.getDrawingCache();
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		Bitmap ret;
		if (isDeleteStatusBar)
		{
			Resources resources = activity.getResources();
			int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
			int statusBarHeight = resources.getDimensionPixelSize(resourceId);
			ret = Bitmap.createBitmap(bmp, 0, statusBarHeight, dm.widthPixels, dm.heightPixels - statusBarHeight);
		} else
		{
			ret = Bitmap.createBitmap(bmp, 0, 0, dm.widthPixels, dm.heightPixels);
		}
		decorView.destroyDrawingCache();
		return ret;
	}

	public static boolean isScreenLock()
	{
		KeyguardManager km = (KeyguardManager) Utils.getApp().getSystemService(Context.KEYGUARD_SERVICE);
		return km.inKeyguardRestrictedInputMode();
	}

	public static void setSleepDuration(final int duration)
	{
		Settings.System.putInt(Utils.getApp().getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, duration);
	}

	public static int getSleepDuration()
	{
		try
		{
			return Settings.System.getInt(Utils.getApp().getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT);
		} catch (Settings.SettingNotFoundException e)
		{
			e.printStackTrace();
			return -123;
		}
	}

	public static boolean isTablet()
	{
		return (Utils.getApp().getResources().getConfiguration().screenLayout
				& Configuration.SCREENLAYOUT_SIZE_MASK)
				>= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}
}