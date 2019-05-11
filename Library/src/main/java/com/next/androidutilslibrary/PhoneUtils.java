package com.next.androidutilslibrary;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.SystemClock;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class PhoneUtils
{
	private PhoneUtils()
	{
		throw new UnsupportedOperationException("u can't instantiate me...");
	}

	public static boolean isPhone()
	{
		TelephonyManager tm = (TelephonyManager) Utils.getApp().getSystemService(Context.TELEPHONY_SERVICE);
		return tm != null && tm.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE;
	}

	@SuppressLint("HardwareIds")
	public static String getIMEI()
	{
		TelephonyManager tm = (TelephonyManager) Utils.getApp().getSystemService(Context.TELEPHONY_SERVICE);
		return tm != null ? tm.getDeviceId() : null;
	}

	@SuppressLint("HardwareIds")
	public static String getIMSI()
	{
		TelephonyManager tm = (TelephonyManager) Utils.getApp().getSystemService(Context.TELEPHONY_SERVICE);
		return tm != null ? tm.getSubscriberId() : null;
	}

	public static int getPhoneType()
	{
		TelephonyManager tm = (TelephonyManager) Utils.getApp().getSystemService(Context.TELEPHONY_SERVICE);
		return tm != null ? tm.getPhoneType() : -1;
	}

	public static boolean isSimCardReady()
	{
		TelephonyManager tm = (TelephonyManager) Utils.getApp().getSystemService(Context.TELEPHONY_SERVICE);
		return tm != null && tm.getSimState() == TelephonyManager.SIM_STATE_READY;
	}

	public static String getSimOperatorName()
	{
		TelephonyManager tm = (TelephonyManager) Utils.getApp().getSystemService(Context.TELEPHONY_SERVICE);
		return tm != null ? tm.getSimOperatorName() : null;
	}

	public static String getSimOperatorByMnc()
	{
		TelephonyManager tm = (TelephonyManager) Utils.getApp().getSystemService(Context.TELEPHONY_SERVICE);
		String operator = tm != null ? tm.getSimOperator() : null;
		if (operator == null) return null;
		switch (operator)
		{
			case "46000":
			case "46002":
			case "46007":
				return "中国移动";
			case "46001":
				return "中国联通";
			case "46003":
				return "中国电信";
			default:
				return operator;
		}
	}

	@SuppressLint("HardwareIds")
	public static String getPhoneStatus()
	{
		TelephonyManager tm = (TelephonyManager) Utils.getApp()
				.getSystemService(Context.TELEPHONY_SERVICE);
		String str = "";
		str += "DeviceId(IMEI) = " + tm.getDeviceId() + "\n";
		str += "DeviceSoftwareVersion = " + tm.getDeviceSoftwareVersion() + "\n";
		str += "Line1Number = " + tm.getLine1Number() + "\n";
		str += "NetworkCountryIso = " + tm.getNetworkCountryIso() + "\n";
		str += "NetworkOperator = " + tm.getNetworkOperator() + "\n";
		str += "NetworkOperatorName = " + tm.getNetworkOperatorName() + "\n";
		str += "NetworkType = " + tm.getNetworkType() + "\n";
		str += "PhoneType = " + tm.getPhoneType() + "\n";
		str += "SimCountryIso = " + tm.getSimCountryIso() + "\n";
		str += "SimOperator = " + tm.getSimOperator() + "\n";
		str += "SimOperatorName = " + tm.getSimOperatorName() + "\n";
		str += "SimSerialNumber = " + tm.getSimSerialNumber() + "\n";
		str += "SimState = " + tm.getSimState() + "\n";
		str += "SubscriberId(IMSI) = " + tm.getSubscriberId() + "\n";
		str += "VoiceMailNumber = " + tm.getVoiceMailNumber() + "\n";
		return str;
	}

	public static void dial(final String phoneNumber)
	{
		Utils.getApp().startActivity(IntentUtils.getDialIntent(phoneNumber));
	}

	public static void call(final String phoneNumber)
	{
		Utils.getApp().startActivity(IntentUtils.getCallIntent(phoneNumber));
	}

	public static void sendSms(final String phoneNumber, final String content)
	{
		Utils.getApp().startActivity(IntentUtils.getSendSmsIntent(phoneNumber, content));
	}

	public static void sendSmsSilent(final String phoneNumber, final String content)
	{
		if (StringUtils.isEmpty(content)) return;
		PendingIntent sentIntent = PendingIntent.getBroadcast(Utils.getApp(), 0, new Intent(), 0);
		SmsManager smsManager = SmsManager.getDefault();
		if (content.length() >= 70)
		{
			List<String> ms = smsManager.divideMessage(content);
			for (String str : ms)
			{
				smsManager.sendTextMessage(phoneNumber, null, str, sentIntent, null);
			}
		} else
		{
			smsManager.sendTextMessage(phoneNumber, null, content, sentIntent, null);
		}
	}

	public static List<HashMap<String, String>> getAllContactInfo()
	{
		SystemClock.sleep(3000);
		ArrayList<HashMap<String, String>> list = new ArrayList<>();

		ContentResolver resolver = Utils.getApp().getContentResolver();

		Uri raw_uri = Uri.parse("content://com.android.contacts/raw_contacts");
		Uri date_uri = Uri.parse("content://com.android.contacts/data");

		Cursor cursor = resolver.query(raw_uri, new String[]{"contact_id"}, null, null, null);
		try
		{
			if (cursor != null)
			{
				while (cursor.moveToNext())
				{
					String contact_id = cursor.getString(0);
					if (!StringUtils.isEmpty(contact_id))
					{
						Cursor c = resolver.query(date_uri, new String[]{"data1", "mimetype"}, "raw_contact_id=?",
								new String[]{contact_id}, null);
						HashMap<String, String> map = new HashMap<String, String>();

						if (c != null)
						{
							while (c.moveToNext())
							{
								String data1 = c.getString(0);
								String mimetype = c.getString(1);

								if (mimetype.equals("vnd.android.cursor.item/phone_v2"))
								{
									map.put("phone", data1);
								} else if (mimetype.equals("vnd.android.cursor.item/name"))
								{
									map.put("name", data1);
								}
							}
						}
						list.add(map);
						if (c != null)
						{
							c.close();
						}
					}
				}
			}
		} finally
		{
			if (cursor != null)
			{
				cursor.close();
			}
		}
		return list;
	}

	public static void getContactNum()
	{
		Log.d("tips", "U should copy the following code.");
		/*
        Intent intent = new Intent();
        intent.setAction("android.intent.action.PICK");
        intent.setType("vnd.android.cursor.dir/phone_v2");
        startActivityForResult(intent, 0);

        @Override
        protected void onActivityResult ( int requestCode, int resultCode, Intent data){
            super.onActivityResult(requestCode, resultCode, data);
            if (data != null) {
                Uri uri = data.getData();
                String num = null;
                // 创建内容解析者
                ContentResolver contentResolver = getContentResolver();
                Cursor cursor = contentResolver.query(uri,
                        null, null, null, null);
                while (cursor.moveToNext()) {
                    num = cursor.getString(cursor.getColumnIndex("data1"));
                }
                cursor.close();
                num = num.replaceAll("-", "");//替换的操作,555-6 -> 5556
            }
        }
        */
	}

	public static void getAllSMS()
	{
		ContentResolver resolver = Utils.getApp().getContentResolver();

		Uri uri = Uri.parse("content://sms");
		Cursor cursor = resolver.query(uri, new String[]{"address", "date", "type", "body"}, null, null, null);
		int count = cursor.getCount();
		XmlSerializer xmlSerializer = Xml.newSerializer();
		try
		{
			xmlSerializer.setOutput(new FileOutputStream(new File("/mnt/sdcard/backupsms.xml")), "utf-8");
			xmlSerializer.startDocument("utf-8", true);
			xmlSerializer.startTag(null, "smss");
			while (cursor.moveToNext())
			{
				SystemClock.sleep(1000);
				xmlSerializer.startTag(null, "sms");
				xmlSerializer.startTag(null, "address");
				String address = cursor.getString(0);
				xmlSerializer.text(address);
				xmlSerializer.endTag(null, "address");
				xmlSerializer.startTag(null, "date");
				String date = cursor.getString(1);
				xmlSerializer.text(date);
				xmlSerializer.endTag(null, "date");
				xmlSerializer.startTag(null, "type");
				String type = cursor.getString(2);
				xmlSerializer.text(type);
				xmlSerializer.endTag(null, "type");
				xmlSerializer.startTag(null, "body");
				String body = cursor.getString(3);
				xmlSerializer.text(body);
				xmlSerializer.endTag(null, "body");
				xmlSerializer.endTag(null, "sms");
				System.out.println("address:" + address + "   date:" + date + "  type:" + type + "  body:" + body);
			}
			xmlSerializer.endTag(null, "smss");
			xmlSerializer.endDocument();
			xmlSerializer.flush();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}