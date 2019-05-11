package com.next.androidutilslibrary;

import com.peak.androidutilslibrary.constant.RegexConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RegexUtils
{
	private RegexUtils()
	{
		throw new UnsupportedOperationException("u can't instantiate me...");
	}

	public static boolean isMobileSimple(final CharSequence input)
	{
		return isMatch(RegexConstants.REGEX_MOBILE_SIMPLE, input);
	}

	public static boolean isMobileExact(final CharSequence input)
	{
		return isMatch(RegexConstants.REGEX_MOBILE_EXACT, input);
	}

	public static boolean isTel(final CharSequence input)
	{
		return isMatch(RegexConstants.REGEX_TEL, input);
	}

	public static boolean isIDCard15(final CharSequence input)
	{
		return isMatch(RegexConstants.REGEX_ID_CARD15, input);
	}

	public static boolean isIDCard18(final CharSequence input)
	{
		return isMatch(RegexConstants.REGEX_ID_CARD18, input);
	}

	public static boolean isEmail(final CharSequence input)
	{
		return isMatch(RegexConstants.REGEX_EMAIL, input);
	}

	public static boolean isURL(final CharSequence input)
	{
		return isMatch(RegexConstants.REGEX_URL, input);
	}

	public static boolean isZh(final CharSequence input)
	{
		return isMatch(RegexConstants.REGEX_ZH, input);
	}

	public static boolean isUsername(final CharSequence input)
	{
		return isMatch(RegexConstants.REGEX_USERNAME, input);
	}

	public static boolean isDate(final CharSequence input)
	{
		return isMatch(RegexConstants.REGEX_DATE, input);
	}

	public static boolean isIP(final CharSequence input)
	{
		return isMatch(RegexConstants.REGEX_IP, input);
	}

	public static boolean isMatch(final String regex, final CharSequence input)
	{
		return input != null && input.length() > 0 && Pattern.matches(regex, input);
	}

	public static List<String> getMatches(final String regex, final CharSequence input)
	{
		if (input == null) return null;
		List<String> matches = new ArrayList<>();
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);
		while (matcher.find())
		{
			matches.add(matcher.group());
		}
		return matches;
	}

	public static String[] getSplits(final String input, final String regex)
	{
		if (input == null) return null;
		return input.split(regex);
	}

	public static String getReplaceFirst(final String input, final String regex, final String replacement)
	{
		if (input == null) return null;
		return Pattern.compile(regex).matcher(input).replaceFirst(replacement);
	}

	public static String getReplaceAll(final String input, final String regex, final String replacement)
	{
		if (input == null) return null;
		return Pattern.compile(regex).matcher(input).replaceAll(replacement);
	}
}