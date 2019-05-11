package com.next.androidutilslibrary;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;

import java.io.File;

public final class IntentUtils
{
	private IntentUtils()
	{
		throw new UnsupportedOperationException("u can't instantiate me...");
	}

	public static Intent getInstallAppIntent(final String filePath, final String authority)
	{
		return getInstallAppIntent(FileUtils.getFileByPath(filePath), authority);
	}

	public static Intent getInstallAppIntent(final File file, final String authority)
	{
		if (file == null) return null;
		Intent intent = new Intent(Intent.ACTION_VIEW);
		Uri data;
		String type = "application/vnd.android.package-archive";
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N)
		{
			data = Uri.fromFile(file);
		} else
		{
			intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			data = FileProvider.getUriForFile(Utils.getApp(), authority, file);
		}
		intent.setDataAndType(data, type);
		return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	}

	public static Intent getUninstallAppIntent(final String packageName)
	{
		Intent intent = new Intent(Intent.ACTION_DELETE);
		intent.setData(Uri.parse("package:" + packageName));
		return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	}

	public static Intent getLaunchAppIntent(final String packageName)
	{
		return Utils.getApp().getPackageManager().getLaunchIntentForPackage(packageName);
	}

	public static Intent getAppDetailsSettingsIntent(final String packageName)
	{
		Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
		intent.setData(Uri.parse("package:" + packageName));
		return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	}

	public static Intent getShareTextIntent(final String content)
	{
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, content);
		return intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	}

	public static Intent getShareImageIntent(final String content, final String imagePath)
	{
		return getShareImageIntent(content, FileUtils.getFileByPath(imagePath));
	}

	public static Intent getShareImageIntent(final String content, final File image)
	{
		if (!FileUtils.isFileExists(image)) return null;
		return getShareImageIntent(content, Uri.fromFile(image));
	}

	public static Intent getShareImageIntent(final String content, final Uri uri)
	{
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.putExtra(Intent.EXTRA_TEXT, content);
		intent.putExtra(Intent.EXTRA_STREAM, uri);
		intent.setType("image/*");
		return intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	}

	public static Intent getComponentIntent(final String packageName, final String className)
	{
		return getComponentIntent(packageName, className, null);
	}

	public static Intent getComponentIntent(final String packageName, final String className, final Bundle bundle)
	{
		Intent intent = new Intent(Intent.ACTION_VIEW);
		if (bundle != null) intent.putExtras(bundle);
		ComponentName cn = new ComponentName(packageName, className);
		intent.setComponent(cn);
		return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	}

	public static Intent getShutdownIntent()
	{
		Intent intent = new Intent(Intent.ACTION_SHUTDOWN);
		return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	}

	public static Intent getDialIntent(final String phoneNumber)
	{
		Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
		return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	}

	public static Intent getCallIntent(final String phoneNumber)
	{
		Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + phoneNumber));
		return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	}

	public static Intent getSendSmsIntent(final String phoneNumber, final String content)
	{
		Uri uri = Uri.parse("smsto:" + phoneNumber);
		Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
		intent.putExtra("sms_body", content);
		return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	}

	public static Intent getCaptureIntent(final Uri outUri)
	{
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outUri);
		return intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK);
	}
}