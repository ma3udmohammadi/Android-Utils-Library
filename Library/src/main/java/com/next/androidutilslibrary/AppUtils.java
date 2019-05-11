package com.next.androidutilslibrary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class AppUtils
{
	private AppUtils()
	{
		throw new UnsupportedOperationException("u can't instantiate me...");
	}

	public static boolean isInstallApp(final String action, final String category)
	{
		Intent intent = new Intent(action);
		intent.addCategory(category);
		PackageManager pm = Utils.getApp().getPackageManager();
		ResolveInfo info = pm.resolveActivity(intent, 0);
		return info != null;
	}

	public static boolean isInstallApp(final String packageName)
	{
		return !isSpace(packageName) && IntentUtils.getLaunchAppIntent(packageName) != null;
	}

	public static void installApp(final String filePath, final String authority)
	{
		installApp(FileUtils.getFileByPath(filePath), authority);
	}

	public static void installApp(final File file, final String authority)
	{
		if (!FileUtils.isFileExists(file)) return;
		Utils.getApp().startActivity(IntentUtils.getInstallAppIntent(file, authority));
	}

	public static void installApp(final Activity activity, final String filePath, final String authority, final int requestCode)
	{
		installApp(activity, FileUtils.getFileByPath(filePath), authority, requestCode);
	}

	public static void installApp(final Activity activity, final File file, final String authority, final int requestCode)
	{
		if (!FileUtils.isFileExists(file)) return;
		activity.startActivityForResult(IntentUtils.getInstallAppIntent(file, authority), requestCode);
	}

	public static boolean installAppSilent(final String filePath)
	{
		File file = FileUtils.getFileByPath(filePath);
		if (!FileUtils.isFileExists(file)) return false;
		String command = "LD_LIBRARY_PATH=/vendor/lib:/system/lib pm install " + filePath;
		ShellUtils.CommandResult commandResult = ShellUtils.execCmd(command, !isSystemApp(), true);
		return commandResult.successMsg != null && commandResult.successMsg.toLowerCase().contains("success");
	}

	public static void uninstallApp(final String packageName)
	{
		if (isSpace(packageName)) return;
		Utils.getApp().startActivity(IntentUtils.getUninstallAppIntent(packageName));
	}

	public static void uninstallApp(final Activity activity, final String packageName, final int requestCode)
	{
		if (isSpace(packageName)) return;
		activity.startActivityForResult(IntentUtils.getUninstallAppIntent(packageName), requestCode);
	}

	public static boolean uninstallAppSilent(final String packageName, final boolean isKeepData)
	{
		if (isSpace(packageName)) return false;
		String command = "LD_LIBRARY_PATH=/vendor/lib:/system/lib pm uninstall " + (isKeepData ? "-k " : "") + packageName;
		ShellUtils.CommandResult commandResult = ShellUtils.execCmd(command, !isSystemApp(), true);
		return commandResult.successMsg != null && commandResult.successMsg.toLowerCase().contains("success");
	}

	public static boolean isAppRoot()
	{
		ShellUtils.CommandResult result = ShellUtils.execCmd("echo root", true);
		if (result.result == 0)
		{
			return true;
		}
		if (result.errorMsg != null)
		{
			Log.d("AppUtils", "isAppRoot() called" + result.errorMsg);
		}
		return false;
	}

	public static void launchApp(final String packageName)
	{
		if (isSpace(packageName)) return;
		Utils.getApp().startActivity(IntentUtils.getLaunchAppIntent(packageName));
	}

	public static void launchApp(final Activity activity, final String packageName, final int requestCode)
	{
		if (isSpace(packageName)) return;
		activity.startActivityForResult(IntentUtils.getLaunchAppIntent(packageName), requestCode);
	}

	public static void exitApp()
	{
		List<Activity> activityList = Utils.sActivityList;
		for (int i = activityList.size() - 1; i >= 0; --i)
		{
			activityList.get(i).finish();
			activityList.remove(i);
		}
		System.exit(0);
	}

	public static String getAppPackageName()
	{
		return Utils.getApp().getPackageName();
	}

	public static void getAppDetailsSettings()
	{
		getAppDetailsSettings(Utils.getApp().getPackageName());
	}

	public static void getAppDetailsSettings(final String packageName)
	{
		if (isSpace(packageName)) return;
		Utils.getApp().startActivity(IntentUtils.getAppDetailsSettingsIntent(packageName));
	}

	public static String getAppName()
	{
		return getAppName(Utils.getApp().getPackageName());
	}

	public static String getAppName(final String packageName)
	{
		if (isSpace(packageName)) return null;
		try
		{
			PackageManager pm = Utils.getApp().getPackageManager();
			PackageInfo pi = pm.getPackageInfo(packageName, 0);
			return pi == null ? null : pi.applicationInfo.loadLabel(pm).toString();
		} catch (PackageManager.NameNotFoundException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public static Drawable getAppIcon()
	{
		return getAppIcon(Utils.getApp().getPackageName());
	}

	public static Drawable getAppIcon(final String packageName)
	{
		if (isSpace(packageName)) return null;
		try
		{
			PackageManager pm = Utils.getApp().getPackageManager();
			PackageInfo pi = pm.getPackageInfo(packageName, 0);
			return pi == null ? null : pi.applicationInfo.loadIcon(pm);
		} catch (PackageManager.NameNotFoundException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public static String getAppPath()
	{
		return getAppPath(Utils.getApp().getPackageName());
	}

	public static String getAppPath(final String packageName)
	{
		if (isSpace(packageName)) return null;
		try
		{
			PackageManager pm = Utils.getApp().getPackageManager();
			PackageInfo pi = pm.getPackageInfo(packageName, 0);
			return pi == null ? null : pi.applicationInfo.sourceDir;
		} catch (PackageManager.NameNotFoundException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public static String getAppVersionName()
	{
		return getAppVersionName(Utils.getApp().getPackageName());
	}

	public static String getAppVersionName(final String packageName)
	{
		if (isSpace(packageName)) return null;
		try
		{
			PackageManager pm = Utils.getApp().getPackageManager();
			PackageInfo pi = pm.getPackageInfo(packageName, 0);
			return pi == null ? null : pi.versionName;
		} catch (PackageManager.NameNotFoundException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public static int getAppVersionCode()
	{
		return getAppVersionCode(Utils.getApp().getPackageName());
	}

	public static int getAppVersionCode(final String packageName)
	{
		if (isSpace(packageName)) return -1;
		try
		{
			PackageManager pm = Utils.getApp().getPackageManager();
			PackageInfo pi = pm.getPackageInfo(packageName, 0);
			return pi == null ? -1 : pi.versionCode;
		} catch (PackageManager.NameNotFoundException e)
		{
			e.printStackTrace();
			return -1;
		}
	}

	public static boolean isSystemApp()
	{
		return isSystemApp(Utils.getApp().getPackageName());
	}

	public static boolean isSystemApp(final String packageName)
	{
		if (isSpace(packageName)) return false;
		try
		{
			PackageManager pm = Utils.getApp().getPackageManager();
			ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);
			return ai != null && (ai.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
		} catch (PackageManager.NameNotFoundException e)
		{
			e.printStackTrace();
			return false;
		}
	}

	public static boolean isAppDebug()
	{
		return isAppDebug(Utils.getApp().getPackageName());
	}

	public static boolean isAppDebug(final String packageName)
	{
		if (isSpace(packageName)) return false;
		try
		{
			PackageManager pm = Utils.getApp().getPackageManager();
			ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);
			return ai != null && (ai.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
		} catch (PackageManager.NameNotFoundException e)
		{
			e.printStackTrace();
			return false;
		}
	}

	public static Signature[] getAppSignature()
	{
		return getAppSignature(Utils.getApp().getPackageName());
	}

	public static Signature[] getAppSignature(final String packageName)
	{
		if (isSpace(packageName)) return null;
		try
		{
			PackageManager pm = Utils.getApp().getPackageManager();
			@SuppressLint("PackageManagerGetSignatures")
			PackageInfo pi = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
			return pi == null ? null : pi.signatures;
		} catch (PackageManager.NameNotFoundException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public static String getAppSignatureSHA1()
	{
		return getAppSignatureSHA1(Utils.getApp().getPackageName());
	}

	public static String getAppSignatureSHA1(final String packageName)
	{
		Signature[] signature = getAppSignature(packageName);
		if (signature == null) return null;
		return EncryptUtils.encryptSHA1ToString(signature[0].toByteArray()).
				replaceAll("(?<=[0-9A-F]{2})[0-9A-F]{2}", ":$0");
	}

	public static boolean isAppForeground()
	{
		ActivityManager manager = (ActivityManager) Utils.getApp().getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> info = manager.getRunningAppProcesses();
		if (info == null || info.size() == 0) return false;
		for (ActivityManager.RunningAppProcessInfo aInfo : info)
		{
			if (aInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND)
			{
				return aInfo.processName.equals(Utils.getApp().getPackageName());
			}
		}
		return false;
	}

	public static boolean isAppForeground(final String packageName)
	{
		return !isSpace(packageName) && packageName.equals(ProcessUtils.getForegroundProcessName());
	}

	public static class AppInfo
	{
		private String name;
		private Drawable icon;
		private String packageName;
		private String packagePath;
		private String versionName;
		private int versionCode;
		private boolean isSystem;

		public Drawable getIcon()
		{
			return icon;
		}

		public void setIcon(final Drawable icon)
		{
			this.icon = icon;
		}

		public boolean isSystem()
		{
			return isSystem;
		}

		public void setSystem(final boolean isSystem)
		{
			this.isSystem = isSystem;
		}

		public String getName()
		{
			return name;
		}

		public void setName(final String name)
		{
			this.name = name;
		}

		public String getPackageName()
		{
			return packageName;
		}

		public void setPackageName(final String packageName)
		{
			this.packageName = packageName;
		}

		public String getPackagePath()
		{
			return packagePath;
		}

		public void setPackagePath(final String packagePath)
		{
			this.packagePath = packagePath;
		}

		public int getVersionCode()
		{
			return versionCode;
		}

		public void setVersionCode(final int versionCode)
		{
			this.versionCode = versionCode;
		}

		public String getVersionName()
		{
			return versionName;
		}

		public void setVersionName(final String versionName)
		{
			this.versionName = versionName;
		}


		public AppInfo(String packageName, String name, Drawable icon, String packagePath,
		               String versionName, int versionCode, boolean isSystem)
		{
			this.setName(name);
			this.setIcon(icon);
			this.setPackageName(packageName);
			this.setPackagePath(packagePath);
			this.setVersionName(versionName);
			this.setVersionCode(versionCode);
			this.setSystem(isSystem);
		}

		@Override
		public String toString()
		{
			return "pkg name: " + getPackageName() +
					"\napp name: " + getName() +
					"\napp path: " + getPackagePath() +
					"\napp v name: " + getVersionName() +
					"\napp v code: " + getVersionCode() +
					"\nis system: " + isSystem();
		}
	}

	public static AppInfo getAppInfo()
	{
		return getAppInfo(Utils.getApp().getPackageName());
	}

	public static AppInfo getAppInfo(final String packageName)
	{
		try
		{
			PackageManager pm = Utils.getApp().getPackageManager();
			PackageInfo pi = pm.getPackageInfo(packageName, 0);
			return getBean(pm, pi);
		} catch (PackageManager.NameNotFoundException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	private static AppInfo getBean(final PackageManager pm, final PackageInfo pi)
	{
		if (pm == null || pi == null) return null;
		ApplicationInfo ai = pi.applicationInfo;
		String packageName = pi.packageName;
		String name = ai.loadLabel(pm).toString();
		Drawable icon = ai.loadIcon(pm);
		String packagePath = ai.sourceDir;
		String versionName = pi.versionName;
		int versionCode = pi.versionCode;
		boolean isSystem = (ApplicationInfo.FLAG_SYSTEM & ai.flags) != 0;
		return new AppInfo(packageName, name, icon, packagePath, versionName, versionCode, isSystem);
	}

	public static List<AppInfo> getAppsInfo()
	{
		List<AppInfo> list = new ArrayList<>();
		PackageManager pm = Utils.getApp().getPackageManager();
		List<PackageInfo> installedPackages = pm.getInstalledPackages(0);
		for (PackageInfo pi : installedPackages)
		{
			AppInfo ai = getBean(pm, pi);
			if (ai == null) continue;
			list.add(ai);
		}
		return list;
	}

	public static boolean cleanAppData(final String... dirPaths)
	{
		File[] dirs = new File[dirPaths.length];
		int i = 0;
		for (String dirPath : dirPaths)
		{
			dirs[i++] = new File(dirPath);
		}
		return cleanAppData(dirs);
	}

	public static boolean cleanAppData(final File... dirs)
	{
		boolean isSuccess = CleanUtils.cleanInternalCache();
		isSuccess &= CleanUtils.cleanInternalDbs();
		isSuccess &= CleanUtils.cleanInternalSP();
		isSuccess &= CleanUtils.cleanInternalFiles();
		isSuccess &= CleanUtils.cleanExternalCache();
		for (File dir : dirs)
		{
			isSuccess &= CleanUtils.cleanCustomCache(dir);
		}
		return isSuccess;
	}

	// Todo: this method is general for alot of util libs. move to EmptyUtils.java
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