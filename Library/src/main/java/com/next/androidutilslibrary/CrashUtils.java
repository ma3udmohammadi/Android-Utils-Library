package com.next.androidutilslibrary;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class CrashUtils
{
	private static String defaultDir;
	private static String dir;
	private static String versionName;
	private static int versionCode;
	private static ExecutorService sExecutor;
	private static final String FILE_SEP = System.getProperty("file.separator");
	private static final Format FORMAT = new SimpleDateFormat("MM-dd HH-mm-ss", Locale.getDefault());
	private static final String CRASH_HEAD;
	private static final UncaughtExceptionHandler DEFAULT_UNCAUGHT_EXCEPTION_HANDLER;
	private static final UncaughtExceptionHandler UNCAUGHT_EXCEPTION_HANDLER;

	static
	{
		try
		{
			PackageInfo pi = Utils.getApp().getPackageManager().getPackageInfo(Utils.getApp().getPackageName(), 0);
			if (pi != null)
			{
				versionName = pi.versionName;
				versionCode = pi.versionCode;
			}
		} catch (PackageManager.NameNotFoundException e)
		{
			e.printStackTrace();
		}

		CRASH_HEAD = "\n************* Crash Log Head ****************" +
				"\nDevice Manufacturer: " + Build.MANUFACTURER +
				"\nDevice Model       : " + Build.MODEL +
				"\nAndroid Version    : " + Build.VERSION.RELEASE +
				"\nAndroid SDK        : " + Build.VERSION.SDK_INT +
				"\nApp VersionName    : " + versionName +
				"\nApp VersionCode    : " + versionCode +
				"\n************* Crash Log Head ****************\n\n";

		DEFAULT_UNCAUGHT_EXCEPTION_HANDLER = Thread.getDefaultUncaughtExceptionHandler();

		UNCAUGHT_EXCEPTION_HANDLER = new UncaughtExceptionHandler()
		{
			@Override
			public void uncaughtException(final Thread t, final Throwable e)
			{
				if (e == null)
				{
					android.os.Process.killProcess(android.os.Process.myPid());
					System.exit(0);
					return;
				}
				Date now = new Date(System.currentTimeMillis());
				String fileName = FORMAT.format(now) + ".txt";
				final String fullPath = (dir == null ? defaultDir : dir) + fileName;
				if (!createOrExistsFile(fullPath)) return;
				if (sExecutor == null)
				{
					sExecutor = Executors.newSingleThreadExecutor();
				}
				sExecutor.execute(new Runnable()
				{
					@Override
					public void run()
					{
						PrintWriter pw = null;
						try
						{
							pw = new PrintWriter(new FileWriter(fullPath, false));
							pw.write(CRASH_HEAD);
							e.printStackTrace(pw);
							Throwable cause = e.getCause();
							while (cause != null)
							{
								cause.printStackTrace(pw);
								cause = cause.getCause();
							}
						} catch (IOException e)
						{
							e.printStackTrace();
						} finally
						{
							if (pw != null)
							{
								pw.close();
							}
						}
					}
				});
				if (DEFAULT_UNCAUGHT_EXCEPTION_HANDLER != null)
				{
					DEFAULT_UNCAUGHT_EXCEPTION_HANDLER.uncaughtException(t, e);
				}
			}
		};
	}

	private CrashUtils()
	{
		throw new UnsupportedOperationException("u can't instantiate me...");
	}

	public static void init()
	{
		init("");
	}

	public static void init(@NonNull final File crashDir)
	{
		init(crashDir.getAbsolutePath());
	}

	public static void init(final String crashDir)
	{
		if (isSpace(crashDir))
		{
			dir = null;
		} else
		{
			dir = crashDir.endsWith(FILE_SEP) ? crashDir : crashDir + FILE_SEP;
		}
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
				&& Utils.getApp().getExternalCacheDir() != null)
			defaultDir = Utils.getApp().getExternalCacheDir() + FILE_SEP + "crash" + FILE_SEP;
		else
		{
			defaultDir = Utils.getApp().getCacheDir() + FILE_SEP + "crash" + FILE_SEP;
		}
		Thread.setDefaultUncaughtExceptionHandler(UNCAUGHT_EXCEPTION_HANDLER);
	}

	private static boolean createOrExistsFile(final String filePath)
	{
		File file = new File(filePath);
		if (file.exists()) return file.isFile();
		if (!createOrExistsDir(file.getParentFile())) return false;
		try
		{
			return file.createNewFile();
		} catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
	}

	private static boolean createOrExistsDir(final File file)
	{
		return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
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