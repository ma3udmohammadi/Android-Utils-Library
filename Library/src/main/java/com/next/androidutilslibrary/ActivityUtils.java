package com.next.androidutilslibrary;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.AnimRes;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import android.view.View;

import java.util.List;

public final class ActivityUtils
{
	private ActivityUtils()
	{
		throw new UnsupportedOperationException("u can't instantiate me...");
	}

	public static boolean isActivityExists(@NonNull final String packageName, @NonNull final String className)
	{
		Intent intent = new Intent();
		intent.setClassName(packageName, className);
		return !(Utils.getApp().getPackageManager().resolveActivity(intent, 0) == null ||
				intent.resolveActivity(Utils.getApp().getPackageManager()) == null ||
				Utils.getApp().getPackageManager().queryIntentActivities(intent, 0).size() == 0);
	}

	public static void startActivity(@NonNull final Class<?> clz)
	{
		Context context = getActivityOrApp();
		startActivity(context, null, context.getPackageName(), clz.getName(), null);
	}

	public static void startActivity(@NonNull final Class<?> clz, @NonNull final Bundle options)
	{
		Context context = getActivityOrApp();
		startActivity(context, null, context.getPackageName(), clz.getName(), options);
	}

	public static void startActivity(@NonNull final Class<?> clz, @AnimRes final int enterAnim, @AnimRes final int exitAnim)
	{
		Context context = getActivityOrApp();
		startActivity(context, null, context.getPackageName(), clz.getName(),
				getOptionsBundle(context, enterAnim, exitAnim));
	}

	public static void startActivity(@NonNull final Activity activity,
	                                 @NonNull final Class<?> clz)
	{
		startActivity(activity, null, activity.getPackageName(), clz.getName(), null);
	}

	public static void startActivity(@NonNull final Activity activity, @NonNull final Class<?> clz, @NonNull final Bundle options)
	{
		startActivity(activity, null, activity.getPackageName(), clz.getName(), options);
	}

	public static void startActivity(@NonNull final Activity activity, @NonNull final Class<?> clz, @NonNull final View... sharedElements)
	{
		startActivity(activity, null, activity.getPackageName(), clz.getName(), getOptionsBundle(activity, sharedElements));
	}

	public static void startActivity(@NonNull final Activity activity, @NonNull final Class<?> clz,
	                                 @AnimRes final int enterAnim, @AnimRes final int exitAnim)
	{
		startActivity(activity, null, activity.getPackageName(), clz.getName(),
				getOptionsBundle(activity, enterAnim, exitAnim));
	}

	public static void startActivity(@NonNull final Bundle extras, @NonNull final Class<?> clz)
	{
		Context context = getActivityOrApp();
		startActivity(context, extras, context.getPackageName(), clz.getName(), null);
	}

	public static void startActivity(@NonNull final Bundle extras, @NonNull final Class<?> clz, @NonNull final Bundle options)
	{
		Context context = getActivityOrApp();
		startActivity(context, extras, context.getPackageName(), clz.getName(), options);
	}

	public static void startActivity(@NonNull final Bundle extras, @NonNull final Class<?> clz,
	                                 @AnimRes final int enterAnim, @AnimRes final int exitAnim)
	{
		Context context = getActivityOrApp();
		startActivity(context, extras, context.getPackageName(), clz.getName(),
				getOptionsBundle(context, enterAnim, exitAnim));
	}

	public static void startActivity(@NonNull final Bundle extras, @NonNull final Activity activity, @NonNull final Class<?> clz)
	{
		startActivity(activity, extras, activity.getPackageName(), clz.getName(), null);
	}

	public static void startActivity(@NonNull final Bundle extras, @NonNull final Activity activity,
	                                 @NonNull final Class<?> clz, @NonNull final Bundle options)
	{
		startActivity(activity, extras, activity.getPackageName(), clz.getName(), options);
	}

	public static void startActivity(@NonNull final Bundle extras, @NonNull final Activity activity,
	                                 @NonNull final Class<?> clz, @NonNull final View... sharedElements)
	{
		startActivity(activity, extras, activity.getPackageName(), clz.getName(), getOptionsBundle(activity, sharedElements));
	}

	public static void startActivity(@NonNull final Bundle extras, @NonNull final Activity activity, @NonNull final Class<?> clz,
	                                 @AnimRes final int enterAnim, @AnimRes final int exitAnim)
	{
		startActivity(activity, extras, activity.getPackageName(), clz.getName(),
				getOptionsBundle(activity, enterAnim, exitAnim));
	}

	public static void startActivity(@NonNull final String pkg, @NonNull final String cls)
	{
		startActivity(getActivityOrApp(), null, pkg, cls, null);
	}

	public static void startActivity(@NonNull final String pkg, @NonNull final String cls, @NonNull final Bundle options)
	{
		startActivity(getActivityOrApp(), null, pkg, cls, options);
	}

	public static void startActivity(@NonNull final String pkg, @NonNull final String cls,
	                                 @AnimRes final int enterAnim, @AnimRes final int exitAnim)
	{
		Context context = getActivityOrApp();
		startActivity(context, null, pkg, cls, getOptionsBundle(context, enterAnim, exitAnim));
	}

	public static void startActivity(@NonNull final Activity activity, @NonNull final String pkg, @NonNull final String cls)
	{
		startActivity(activity, null, pkg, cls, null);
	}

	public static void startActivity(@NonNull final Activity activity, @NonNull final String pkg,
	                                 @NonNull final String cls, @NonNull final Bundle options)
	{
		startActivity(activity, null, pkg, cls, options);
	}

	public static void startActivity(@NonNull final Activity activity, @NonNull final String pkg,
	                                 @NonNull final String cls, @NonNull final View... sharedElements)
	{
		startActivity(activity, null, pkg, cls, getOptionsBundle(activity, sharedElements));
	}

	public static void startActivity(@NonNull final Activity activity, @NonNull final String pkg, @NonNull final String cls,
	                                 @AnimRes final int enterAnim, @AnimRes final int exitAnim)
	{
		startActivity(activity, null, pkg, cls, getOptionsBundle(activity, enterAnim, exitAnim));
	}

	public static void startActivity(@NonNull final Bundle extras, @NonNull final String pkg, @NonNull final String cls)
	{
		startActivity(getActivityOrApp(), extras, pkg, cls, null);
	}

	public static void startActivity(@NonNull final Bundle extras, @NonNull final String pkg,
	                                 @NonNull final String cls, @NonNull final Bundle options)
	{
		startActivity(getActivityOrApp(), extras, pkg, cls, options);
	}

	public static void startActivity(@NonNull final Bundle extras, @NonNull final String pkg, @NonNull final String cls,
	                                 @AnimRes final int enterAnim, @AnimRes final int exitAnim)
	{
		Context context = getActivityOrApp();
		startActivity(context, extras, pkg, cls, getOptionsBundle(context, enterAnim, exitAnim));
	}

	public static void startActivity(@NonNull final Bundle extras, @NonNull final Activity activity,
	                                 @NonNull final String pkg, @NonNull final String cls)
	{
		startActivity(activity, extras, pkg, cls, null);
	}

	public static void startActivity(@NonNull final Bundle extras, @NonNull final Activity activity,
	                                 @NonNull final String pkg, @NonNull final String cls, @NonNull final Bundle options)
	{
		startActivity(activity, extras, pkg, cls, options);
	}

	public static void startActivity(@NonNull final Bundle extras, @NonNull final Activity activity,
	                                 @NonNull final String pkg, @NonNull final String cls, @NonNull final View... sharedElements)
	{
		startActivity(activity, extras, pkg, cls, getOptionsBundle(activity, sharedElements));
	}

	public static void startActivity(@NonNull final Bundle extras, @NonNull final Activity activity,
	                                 @NonNull final String pkg, @NonNull final String cls,
	                                 @AnimRes final int enterAnim, @AnimRes final int exitAnim)
	{
		startActivity(activity, extras, pkg, cls, getOptionsBundle(activity, enterAnim, exitAnim));
	}

	public static void startActivity(@NonNull final Intent intent)
	{
		startActivity(intent, getActivityOrApp(), null);
	}

	public static void startActivity(@NonNull final Intent intent, @NonNull final Bundle options)
	{
		startActivity(intent, getActivityOrApp(), options);
	}

	public static void startActivity(@NonNull final Intent intent, @AnimRes final int enterAnim, @AnimRes final int exitAnim)
	{
		Context context = getActivityOrApp();
		startActivity(intent, context, getOptionsBundle(context, enterAnim, exitAnim));
	}

	public static void startActivity(@NonNull final Activity activity, @NonNull final Intent intent)
	{
		startActivity(intent, activity, null);
	}

	public static void startActivity(@NonNull final Activity activity, @NonNull final Intent intent, @NonNull final Bundle options)
	{
		startActivity(intent, activity, options);
	}

	public static void startActivity(@NonNull final Activity activity, @NonNull final Intent intent, @NonNull final View... sharedElements)
	{
		startActivity(intent, activity, getOptionsBundle(activity, sharedElements));
	}

	public static void startActivity(@NonNull final Activity activity, @NonNull final Intent intent,
	                                 @AnimRes final int enterAnim, @AnimRes final int exitAnim)
	{
		startActivity(intent, activity, getOptionsBundle(activity, enterAnim, exitAnim));
	}

	public static void startHomeActivity()
	{
		Intent homeIntent = new Intent(Intent.ACTION_MAIN);
		homeIntent.addCategory(Intent.CATEGORY_HOME);
		startActivity(homeIntent);
	}

	public static List<Activity> getActivityList()
	{
		return Utils.sActivityList;
	}


	public static String getLauncherActivity()
	{
		return getLauncherActivity(Utils.getApp().getPackageName());
	}

	public static String getLauncherActivity(@NonNull final String packageName)
	{
		Intent intent = new Intent(Intent.ACTION_MAIN, null);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PackageManager pm = Utils.getApp().getPackageManager();
		List<ResolveInfo> info = pm.queryIntentActivities(intent, 0);
		for (ResolveInfo aInfo : info)
		{
			if (aInfo.activityInfo.packageName.equals(packageName))
			{
				return aInfo.activityInfo.name;
			}
		}
		return "no " + packageName;
	}

	public static Activity getTopActivity()
	{
		if (Utils.sTopActivityWeakRef != null)
		{
			Activity activity = Utils.sTopActivityWeakRef.get();
			if (activity != null)
			{
				return activity;
			}
		}
		List<Activity> activities = Utils.sActivityList;
		int size = activities.size();
		return size > 0 ? activities.get(size - 1) : null;
	}

	public static boolean isActivityExistsInStack(@NonNull final Activity activity)
	{
		List<Activity> activities = Utils.sActivityList;
		for (Activity aActivity : activities)
		{
			if (aActivity.equals(activity))
			{
				return true;
			}
		}
		return false;
	}

	public static boolean isActivityExistsInStack(@NonNull final Class<?> clz)
	{
		List<Activity> activities = Utils.sActivityList;
		for (Activity aActivity : activities)
		{
			if (aActivity.getClass().equals(clz))
			{
				return true;
			}
		}
		return false;
	}

	public static void finishActivity(@NonNull final Activity activity)
	{
		finishActivity(activity, false);
	}

	public static void finishActivity(@NonNull final Activity activity, final boolean isLoadAnim)
	{
		activity.finish();
		if (!isLoadAnim)
		{
			activity.overridePendingTransition(0, 0);
		}
	}

	public static void finishActivity(@NonNull final Activity activity,
	                                  @AnimRes final int enterAnim, @AnimRes final int exitAnim)
	{
		activity.finish();
		activity.overridePendingTransition(enterAnim, exitAnim);
	}

	public static void finishActivity(@NonNull final Class<?> clz)
	{
		finishActivity(clz, false);
	}

	public static void finishActivity(@NonNull final Class<?> clz, final boolean isLoadAnim)
	{
		List<Activity> activities = Utils.sActivityList;
		for (Activity activity : activities)
		{
			if (activity.getClass().equals(clz))
			{
				activity.finish();
				if (!isLoadAnim)
				{
					activity.overridePendingTransition(0, 0);
				}
			}
		}
	}

	public static void finishActivity(@NonNull final Class<?> clz,
	                                  @AnimRes final int enterAnim, @AnimRes final int exitAnim)
	{
		List<Activity> activities = Utils.sActivityList;
		for (Activity activity : activities)
		{
			if (activity.getClass().equals(clz))
			{
				activity.finish();
				activity.overridePendingTransition(enterAnim, exitAnim);
			}
		}
	}

	public static boolean finishToActivity(@NonNull final Activity activity, final boolean isIncludeSelf)
	{
		return finishToActivity(activity, isIncludeSelf, false);
	}

	public static boolean finishToActivity(@NonNull final Activity activity,
	                                       final boolean isIncludeSelf, final boolean isLoadAnim)
	{
		List<Activity> activities = Utils.sActivityList;
		for (int i = activities.size() - 1; i >= 0; --i)
		{
			Activity aActivity = activities.get(i);
			if (aActivity.equals(activity))
			{
				if (isIncludeSelf)
				{
					finishActivity(aActivity, isLoadAnim);
				}
				return true;
			}
			finishActivity(aActivity, isLoadAnim);
		}
		return false;
	}

	public static boolean finishToActivity(@NonNull final Activity activity, final boolean isIncludeSelf,
	                                       @AnimRes final int enterAnim, @AnimRes final int exitAnim)
	{
		List<Activity> activities = Utils.sActivityList;
		for (int i = activities.size() - 1; i >= 0; --i)
		{
			Activity aActivity = activities.get(i);
			if (aActivity.equals(activity))
			{
				if (isIncludeSelf)
				{
					finishActivity(aActivity, enterAnim, exitAnim);
				}
				return true;
			}
			finishActivity(aActivity, enterAnim, exitAnim);
		}
		return false;
	}

	public static boolean finishToActivity(@NonNull final Class<?> clz, final boolean isIncludeSelf)
	{
		return finishToActivity(clz, isIncludeSelf, false);
	}

	public static boolean finishToActivity(@NonNull final Class<?> clz, final boolean isIncludeSelf, final boolean isLoadAnim)
	{
		List<Activity> activities = Utils.sActivityList;
		for (int i = activities.size() - 1; i >= 0; --i)
		{
			Activity aActivity = activities.get(i);
			if (aActivity.getClass().equals(clz))
			{
				if (isIncludeSelf)
				{
					finishActivity(aActivity, isLoadAnim);
				}
				return true;
			}
			finishActivity(aActivity, isLoadAnim);
		}
		return false;
	}

	public static boolean finishToActivity(@NonNull final Class<?> clz, final boolean isIncludeSelf,
	                                       @AnimRes final int enterAnim, @AnimRes final int exitAnim)
	{
		List<Activity> activities = Utils.sActivityList;
		for (int i = activities.size() - 1; i >= 0; --i)
		{
			Activity aActivity = activities.get(i);
			if (aActivity.getClass().equals(clz))
			{
				if (isIncludeSelf)
				{
					finishActivity(aActivity, enterAnim, exitAnim);
				}
				return true;
			}
			finishActivity(aActivity, enterAnim, exitAnim);
		}
		return false;
	}

	public static void finishOtherActivities(@NonNull final Class<?> clz)
	{
		finishOtherActivities(clz, false);
	}

	public static void finishOtherActivities(@NonNull final Class<?> clz,
	                                         final boolean isLoadAnim)
	{
		List<Activity> activities = Utils.sActivityList;
		boolean flag = false;
		for (int i = activities.size() - 1; i >= 0; i--)
		{
			Activity activity = activities.get(i);
			if (activity.getClass().equals(clz))
			{
				if (flag)
				{
					finishActivity(activity, isLoadAnim);
				} else
				{
					flag = true;
				}
			} else
			{
				finishActivity(activity, isLoadAnim);
			}
		}
	}

	public static void finishOtherActivities(@NonNull final Class<?> clz, @AnimRes final int enterAnim, @AnimRes final int exitAnim)
	{
		List<Activity> activities = Utils.sActivityList;
		boolean flag = false;
		for (int i = activities.size() - 1; i >= 0; i--)
		{
			Activity activity = activities.get(i);
			if (activity.getClass().equals(clz))
			{
				if (flag)
				{
					finishActivity(activity, enterAnim, exitAnim);
				} else
				{
					flag = true;
				}
			} else
			{
				finishActivity(activity, enterAnim, exitAnim);
			}
		}
	}

	public static void finishAllActivities()
	{
		finishAllActivities(false);
	}

	public static void finishAllActivities(final boolean isLoadAnim)
	{
		List<Activity> activityList = Utils.sActivityList;
		for (int i = activityList.size() - 1; i >= 0; --i)
		{
			Activity activity = activityList.get(i);
			activity.finish();
			if (!isLoadAnim)
			{
				activity.overridePendingTransition(0, 0);
			}
		}
	}

	public static void finishAllActivities(@AnimRes final int enterAnim, @AnimRes final int exitAnim)
	{
		List<Activity> activityList = Utils.sActivityList;
		for (int i = activityList.size() - 1; i >= 0; --i)
		{
			Activity activity = activityList.get(i);
			activity.finish();
			activity.overridePendingTransition(enterAnim, exitAnim);
		}
	}

	private static Context getActivityOrApp()
	{
		Activity topActivity = getTopActivity();
		return topActivity == null ? Utils.getApp() : topActivity;
	}

	private static void startActivity(final Context context, final Bundle extras,
	                                  final String pkg, final String cls, final Bundle options)
	{
		Intent intent = new Intent(Intent.ACTION_VIEW);
		if (extras != null) intent.putExtras(extras);
		intent.setComponent(new ComponentName(pkg, cls));
		startActivity(intent, context, options);
	}

	private static void startActivity(final Intent intent, final Context context, final Bundle options)
	{
		if (!(context instanceof Activity))
		{
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		}
		if (options != null)
		{
			context.startActivity(intent, options);
		} else
		{
			context.startActivity(intent);
		}
	}

	private static Bundle getOptionsBundle(final Context context, final int enterAnim, final int exitAnim)
	{
		return ActivityOptionsCompat.makeCustomAnimation(context, enterAnim, exitAnim).toBundle();
	}

	private static Bundle getOptionsBundle(final Activity activity, final View[] sharedElements)
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
		{
			int len = sharedElements.length;
			@SuppressWarnings("unchecked")
			Pair<View, String>[] pairs = new Pair[len];
			for (int i = 0; i < len; i++)
			{
				pairs[i] = Pair.create(sharedElements[i], sharedElements[i].getTransitionName());
			}
			return ActivityOptionsCompat.makeSceneTransitionAnimation(activity, pairs).toBundle();
		}
		return ActivityOptionsCompat.makeSceneTransitionAnimation(activity, null, null).toBundle();
	}
}