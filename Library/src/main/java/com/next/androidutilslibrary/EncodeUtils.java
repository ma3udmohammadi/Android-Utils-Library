package com.next.androidutilslibrary;

import android.os.Build;
import android.text.Html;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public final class EncodeUtils
{
	// Todo: separate encrypt abd decrypt
	private EncodeUtils()
	{
		throw new UnsupportedOperationException("u can't instantiate this class.");
	}

	public static String urlEncode(final String input)
	{
		return urlEncode(input, "UTF-8");
	}

	public static String urlEncode(final String input, final String charset)
	{
		try
		{
			return URLEncoder.encode(input, charset);
		} catch (UnsupportedEncodingException e)
		{
			return input;
		}
	}

	public static String urlDecode(final String input)
	{
		return urlDecode(input, "UTF-8");
	}

	public static String urlDecode(final String input, final String charset)
	{
		try
		{
			return URLDecoder.decode(input, charset);
		} catch (UnsupportedEncodingException e)
		{
			return input;
		}
	}

	public static byte[] base64Encode(final String input)
	{
		return base64Encode(input.getBytes());
	}

	public static byte[] base64Encode(final byte[] input)
	{
		return Base64.encode(input, Base64.NO_WRAP);
	}

	public static String base64Encode2String(final byte[] input)
	{
		return Base64.encodeToString(input, Base64.NO_WRAP);
	}

	public static byte[] base64Decode(final String input)
	{
		return Base64.decode(input, Base64.NO_WRAP);
	}

	public static byte[] base64Decode(final byte[] input)
	{
		return Base64.decode(input, Base64.NO_WRAP);
	}

	public static byte[] base64UrlSafeEncode(final String input)
	{
		return Base64.encode(input.getBytes(), Base64.URL_SAFE);
	}

	public static String htmlEncode(final CharSequence input)
	{
		StringBuilder sb = new StringBuilder();
		char c;
		for (int i = 0, len = input.length(); i < len; i++)
		{
			c = input.charAt(i);
			switch (c)
			{
				case '<':
					sb.append("&lt;"); //$NON-NLS-1$
					break;
				case '>':
					sb.append("&gt;"); //$NON-NLS-1$
					break;
				case '&':
					sb.append("&amp;"); //$NON-NLS-1$
					break;
				case '\'':
					sb.append("&#39;"); //$NON-NLS-1$
					break;
				case '"':
					sb.append("&quot;"); //$NON-NLS-1$
					break;
				default:
					sb.append(c);
			}
		}
		return sb.toString();
	}

	@SuppressWarnings("deprecation")
	public static CharSequence htmlDecode(final String input)
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
		{
			return Html.fromHtml(input, Html.FROM_HTML_MODE_LEGACY);
		} else
		{
			return Html.fromHtml(input);
		}
	}
}