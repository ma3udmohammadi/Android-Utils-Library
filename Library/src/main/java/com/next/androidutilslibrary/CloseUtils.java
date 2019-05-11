package com.next.androidutilslibrary;

import java.io.Closeable;
import java.io.IOException;

public final class CloseUtils
{
	private CloseUtils()
	{
		throw new UnsupportedOperationException("u can't instantiate this class.");
	}

	public static void closeIO(final Closeable... closeables)
	{
		if (closeables == null) return;
		for (Closeable closeable : closeables)
		{
			if (closeable != null)
			{
				try
				{
					closeable.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	public static void closeIOQuietly(final Closeable... closeables)
	{
		if (closeables == null) return;
		for (Closeable closeable : closeables)
		{
			if (closeable != null)
			{
				try
				{
					closeable.close();
				} catch (IOException ignored)
				{
				}
			}
		}
	}
}