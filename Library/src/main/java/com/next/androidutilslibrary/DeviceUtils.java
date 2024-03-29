package com.next.androidutilslibrary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;

import java.io.File;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public final class DeviceUtils
{
	private DeviceUtils()
	{
		throw new UnsupportedOperationException("u can't instantiate me...");
	}

	public static boolean isDeviceRooted()
	{
		String su = "su";
		String[] locations = {"/system/bin/", "/system/xbin/", "/sbin/", "/system/sd/xbin/", "/system/bin/failsafe/",
				"/data/local/xbin/", "/data/local/bin/", "/data/local/"};
		for (String location : locations)
		{
			if (new File(location + su).exists())
			{
				return true;
			}
		}
		return false;
	}

	public static int getSDKVersion()
	{
		return android.os.Build.VERSION.SDK_INT;
	}

	@SuppressLint("HardwareIds")
	public static String getAndroidID()
	{
		return Settings.Secure.getString(Utils.getApp().getContentResolver(), Settings.Secure.ANDROID_ID);
	}

	public static String getMacAddress()
	{
		String macAddress = getMacAddressByWifiInfo();
		if (!"02:00:00:00:00:00".equals(macAddress))
		{
			return macAddress;
		}
		macAddress = getMacAddressByNetworkInterface();
		if (!"02:00:00:00:00:00".equals(macAddress))
		{
			return macAddress;
		}
		macAddress = getMacAddressByFile();
		if (!"02:00:00:00:00:00".equals(macAddress))
		{
			return macAddress;
		}
		return "please open wifi";
	}

	@SuppressLint("HardwareIds")
	private static String getMacAddressByWifiInfo()
	{
		try
		{
			@SuppressLint("WifiManagerLeak")
			WifiManager wifi = (WifiManager) Utils.getApp().getSystemService(Context.WIFI_SERVICE);
			if (wifi != null)
			{
				WifiInfo info = wifi.getConnectionInfo();
				if (info != null) return info.getMacAddress();
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return "02:00:00:00:00:00";
	}

	private static String getMacAddressByNetworkInterface()
	{
		try
		{
			List<NetworkInterface> nis = Collections.list(NetworkInterface.getNetworkInterfaces());
			for (NetworkInterface ni : nis)
			{
				if (!ni.getName().equalsIgnoreCase("wlan0")) continue;
				byte[] macBytes = ni.getHardwareAddress();
				if (macBytes != null && macBytes.length > 0)
				{
					StringBuilder res1 = new StringBuilder();
					for (byte b : macBytes)
					{
						res1.append(String.format("%02x:", b));
					}
					return res1.deleteCharAt(res1.length() - 1).toString();
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return "02:00:00:00:00:00";
	}

	private static String getMacAddressByFile()
	{
		ShellUtils.CommandResult result = ShellUtils.execCmd("getprop wifi.interface", false);
		if (result.result == 0)
		{
			String name = result.successMsg;
			if (name != null)
			{
				result = ShellUtils.execCmd("cat /sys/class/net/" + name + "/address", false);
				if (result.result == 0)
				{
					if (result.successMsg != null)
					{
						return result.successMsg;
					}
				}
			}
		}
		return "02:00:00:00:00:00";
	}

	public static String getManufacturer()
	{
		return Build.MANUFACTURER;
	}

	public static String getModel()
	{
		String model = Build.MODEL;
		if (model != null)
		{
			model = model.trim().replaceAll("\\s*", "");
		} else
		{
			model = "";
		}
		return model;
	}

	public static void shutdown()
	{
		ShellUtils.execCmd("reboot -p", true);
		Intent intent = new Intent("android.intent.action.ACTION_REQUEST_SHUTDOWN");
		intent.putExtra("android.intent.extra.KEY_CONFIRM", false);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Utils.getApp().startActivity(intent);
	}

	public static void reboot()
	{
		ShellUtils.execCmd("reboot", true);
		Intent intent = new Intent(Intent.ACTION_REBOOT);
		intent.putExtra("nowait", 1);
		intent.putExtra("interval", 1);
		intent.putExtra("window", 0);
		Utils.getApp().sendBroadcast(intent);
	}

	public static void reboot(final String reason)
	{
		PowerManager mPowerManager = (PowerManager) Utils.getApp().getSystemService(Context.POWER_SERVICE);
		try
		{
			mPowerManager.reboot(reason);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void reboot2Recovery()
	{
		ShellUtils.execCmd("reboot recovery", true);
	}

	public static void reboot2Bootloader()
	{
		ShellUtils.execCmd("reboot bootloader", true);
	}
}