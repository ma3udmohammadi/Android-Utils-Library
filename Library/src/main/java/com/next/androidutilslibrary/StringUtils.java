package com.next.androidutilslibrary;

public final class StringUtils
{
	private StringUtils()
	{
		throw new UnsupportedOperationException("u can't instantiate me...");
	}

	public static boolean isEmpty(final CharSequence s)
	{
		return s == null || s.length() == 0;
	}

	public static boolean isTrimEmpty(final String s)
	{
		return (s == null || s.trim().length() == 0);
	}

	public static boolean isSpace(final String s)
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

	public static boolean equals(final CharSequence a, final CharSequence b)
	{
		if (a == b) return true;
		int length;
		if (a != null && b != null && (length = a.length()) == b.length())
		{
			if (a instanceof String && b instanceof String)
			{
				return a.equals(b);
			} else
			{
				for (int i = 0; i < length; i++)
				{
					if (a.charAt(i) != b.charAt(i)) return false;
				}
				return true;
			}
		}
		return false;
	}

	public static boolean equalsIgnoreCase(final String a, final String b)
	{
		return a == null ? b == null : a.equalsIgnoreCase(b);
	}

	public static String null2Length0(final String s)
	{
		return s == null ? "" : s;
	}

	public static int length(final CharSequence s)
	{
		return s == null ? 0 : s.length();
	}

	public static String upperFirstLetter(final String s)
	{
		if (isEmpty(s) || !Character.isLowerCase(s.charAt(0))) return s;
		return String.valueOf((char) (s.charAt(0) - 32)) + s.substring(1);
	}

	public static String lowerFirstLetter(final String s)
	{
		if (isEmpty(s) || !Character.isUpperCase(s.charAt(0))) return s;
		return String.valueOf((char) (s.charAt(0) + 32)) + s.substring(1);
	}

	public static String reverse(final String s)
	{
		int len = length(s);
		if (len <= 1) return s;
		int mid = len >> 1;
		char[] chars = s.toCharArray();
		char c;
		for (int i = 0; i < mid; ++i)
		{
			c = chars[i];
			chars[i] = chars[len - i - 1];
			chars[len - i - 1] = c;
		}
		return new String(chars);
	}
}