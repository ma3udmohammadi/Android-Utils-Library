package com.next.androidutilslibrary;

import android.os.Build;
import androidx.collection.LongSparseArray;
import androidx.collection.SimpleArrayMap;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.util.SparseLongArray;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

public final class EmptyUtils
{
	private EmptyUtils()
	{
		throw new UnsupportedOperationException("u can't instantiate me...");
	}

	public static boolean isEmpty(final Object obj)
	{
		if (obj == null)
		{
			return true;
		}
		if (obj instanceof String && obj.toString().length() == 0)
		{
			return true;
		}
		if (obj.getClass().isArray() && Array.getLength(obj) == 0)
		{
			return true;
		}
		if (obj instanceof Collection && ((Collection) obj).isEmpty())
		{
			return true;
		}
		if (obj instanceof Map && ((Map) obj).isEmpty())
		{
			return true;
		}
		if (obj instanceof SimpleArrayMap && ((SimpleArrayMap) obj).isEmpty())
		{
			return true;
		}
		if (obj instanceof SparseArray && ((SparseArray) obj).size() == 0)
		{
			return true;
		}
		if (obj instanceof SparseBooleanArray && ((SparseBooleanArray) obj).size() == 0)
		{
			return true;
		}
		if (obj instanceof SparseIntArray && ((SparseIntArray) obj).size() == 0)
		{
			return true;
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
		{
			if (obj instanceof SparseLongArray && ((SparseLongArray) obj).size() == 0)
			{
				return true;
			}
		}
		if (obj instanceof LongSparseArray && ((LongSparseArray) obj).size() == 0)
		{
			return true;
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
		{
			if (obj instanceof android.util.LongSparseArray && ((android.util.LongSparseArray) obj).size() == 0)
			{
				return true;
			}
		}
		return false;
	}

	public static boolean isNotEmpty(final Object obj)
	{
		return !isEmpty(obj);
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