package com.next.androidutilslibrary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public final class NetworkUtils
{
	private static final int NETWORK_TYPE_GSM = 16;
	private static final int NETWORK_TYPE_TD_SCDMA = 17;
	private static final int NETWORK_TYPE_IWLAN = 18;

	private NetworkUtils()
	{
		throw new UnsupportedOperationException("u can't instantiate me...");
	}

	public enum NetworkType
	{
		NETWORK_WIFI,
		NETWORK_4G,
		NETWORK_3G,
		NETWORK_2G,
		NETWORK_UNKNOWN,
		NETWORK_NO
	}

	public static void openWirelessSettings()
	{
		Utils.getApp().startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
	}

	private static NetworkInfo getActiveNetworkInfo()
	{
		return ((ConnectivityManager) Utils.getApp().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
	}

	public static boolean isConnected()
	{
		NetworkInfo info = getActiveNetworkInfo();
		return info != null && info.isConnected();
	}

	public static boolean isAvailableByPing()
	{
		return isAvailableByPing(null);
	}

	public static boolean isAvailableByPing(String ip)
	{
		if (ip == null || ip.length() <= 0)
		{
			ip = "223.5.5.5";
		}
		ShellUtils.CommandResult result = ShellUtils.execCmd(String.format("ping -c 1 %s", ip), false);
		boolean ret = result.result == 0;
		if (result.errorMsg != null)
		{
			Log.d("NetworkUtils", "isAvailableByPing() called" + result.errorMsg);
		}
		if (result.successMsg != null)
		{
			Log.d("NetworkUtils", "isAvailableByPing() called" + result.successMsg);
		}
		return ret;
	}

	public static boolean getDataEnabled()
	{
		try
		{
			TelephonyManager tm = (TelephonyManager) Utils.getApp().getSystemService(Context.TELEPHONY_SERVICE);
			Method getMobileDataEnabledMethod = tm.getClass().getDeclaredMethod("getDataEnabled");
			if (null != getMobileDataEnabledMethod)
			{
				return (boolean) getMobileDataEnabledMethod.invoke(tm);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public static void setDataEnabled(final boolean enabled)
	{
		try
		{
			TelephonyManager tm = (TelephonyManager) Utils.getApp().getSystemService(Context.TELEPHONY_SERVICE);
			Method setMobileDataEnabledMethod = tm.getClass().getDeclaredMethod("setDataEnabled", boolean.class);
			if (null != setMobileDataEnabledMethod)
			{
				setMobileDataEnabledMethod.invoke(tm, enabled);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static boolean is4G()
	{
		NetworkInfo info = getActiveNetworkInfo();
		return info != null && info.isAvailable() && info.getSubtype() == TelephonyManager.NETWORK_TYPE_LTE;
	}

	public static boolean getWifiEnabled()
	{
		@SuppressLint("WifiManagerLeak")
		WifiManager wifiManager = (WifiManager) Utils.getApp().getSystemService(Context.WIFI_SERVICE);
		return wifiManager.isWifiEnabled();
	}

	public static void setWifiEnabled(final boolean enabled)
	{
		@SuppressLint("WifiManagerLeak")
		WifiManager wifiManager = (WifiManager) Utils.getApp().getSystemService(Context.WIFI_SERVICE);
		if (enabled)
		{
			if (!wifiManager.isWifiEnabled())
			{
				wifiManager.setWifiEnabled(true);
			}
		} else
		{
			if (wifiManager.isWifiEnabled())
			{
				wifiManager.setWifiEnabled(false);
			}
		}
	}

	public static boolean isWifiConnected()
	{
		ConnectivityManager cm = (ConnectivityManager) Utils.getApp()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		return cm != null && cm.getActiveNetworkInfo() != null
				&& cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
	}

	public static boolean isWifiAvailable()
	{
		return getWifiEnabled() && isAvailableByPing();
	}

	public static String getNetworkOperatorName()
	{
		TelephonyManager tm = (TelephonyManager) Utils.getApp().getSystemService(Context.TELEPHONY_SERVICE);
		return tm != null ? tm.getNetworkOperatorName() : null;
	}

	public static NetworkType getNetworkType()
	{
		NetworkType netType = NetworkType.NETWORK_NO;
		NetworkInfo info = getActiveNetworkInfo();
		if (info != null && info.isAvailable())
		{
			if (info.getType() == ConnectivityManager.TYPE_WIFI)
			{
				netType = NetworkType.NETWORK_WIFI;
			} else if (info.getType() == ConnectivityManager.TYPE_MOBILE)
			{
				switch (info.getSubtype())
				{
					case NETWORK_TYPE_GSM:
					case TelephonyManager.NETWORK_TYPE_GPRS:
					case TelephonyManager.NETWORK_TYPE_CDMA:
					case TelephonyManager.NETWORK_TYPE_EDGE:
					case TelephonyManager.NETWORK_TYPE_1xRTT:
					case TelephonyManager.NETWORK_TYPE_IDEN:
						netType = NetworkType.NETWORK_2G;
						break;

					case NETWORK_TYPE_TD_SCDMA:
					case TelephonyManager.NETWORK_TYPE_EVDO_A:
					case TelephonyManager.NETWORK_TYPE_UMTS:
					case TelephonyManager.NETWORK_TYPE_EVDO_0:
					case TelephonyManager.NETWORK_TYPE_HSDPA:
					case TelephonyManager.NETWORK_TYPE_HSUPA:
					case TelephonyManager.NETWORK_TYPE_HSPA:
					case TelephonyManager.NETWORK_TYPE_EVDO_B:
					case TelephonyManager.NETWORK_TYPE_EHRPD:
					case TelephonyManager.NETWORK_TYPE_HSPAP:
						netType = NetworkType.NETWORK_3G;
						break;

					case NETWORK_TYPE_IWLAN:
					case TelephonyManager.NETWORK_TYPE_LTE:
						netType = NetworkType.NETWORK_4G;
						break;
					default:

						String subtypeName = info.getSubtypeName();
						if (subtypeName.equalsIgnoreCase("TD-SCDMA")
								|| subtypeName.equalsIgnoreCase("WCDMA")
								|| subtypeName.equalsIgnoreCase("CDMA2000"))
						{
							netType = NetworkType.NETWORK_3G;
						} else
						{
							netType = NetworkType.NETWORK_UNKNOWN;
						}
						break;
				}
			} else
			{
				netType = NetworkType.NETWORK_UNKNOWN;
			}
		}
		return netType;
	}

	public static String getIPAddress(final boolean useIPv4)
	{
		try
		{
			for (Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces(); nis.hasMoreElements(); )
			{
				NetworkInterface ni = nis.nextElement();
				if (!ni.isUp()) continue;
				for (Enumeration<InetAddress> addresses = ni.getInetAddresses(); addresses.hasMoreElements(); )
				{
					InetAddress inetAddress = addresses.nextElement();
					if (!inetAddress.isLoopbackAddress())
					{
						String hostAddress = inetAddress.getHostAddress();
						boolean isIPv4 = hostAddress.indexOf(':') < 0;
						if (useIPv4)
						{
							if (isIPv4) return hostAddress;
						} else
						{
							if (!isIPv4)
							{
								int index = hostAddress.indexOf('%');
								return index < 0 ? hostAddress.toUpperCase() : hostAddress.substring(0, index).toUpperCase();
							}
						}
					}
				}
			}
		} catch (SocketException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static String getDomainAddress(final String domain)
	{
		InetAddress inetAddress;
		try
		{
			inetAddress = InetAddress.getByName(domain);
			return inetAddress.getHostAddress();
		} catch (UnknownHostException e)
		{
			e.printStackTrace();
			return null;
		}
	}
}