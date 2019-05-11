package com.next.androidutilslibrary;

import android.content.Context;
import android.os.storage.StorageManager;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class SDCardUtils
{
	private SDCardUtils()
	{
		throw new UnsupportedOperationException("u can't instantiate me...");
	}

	public static boolean isSDCardEnable()
	{
		return !getSDCardPaths().isEmpty();
	}

	@SuppressWarnings("TryWithIdenticalCatches")
	public static List<String> getSDCardPaths(boolean removable)
	{
		List<String> paths = new ArrayList<>();
		StorageManager mStorageManager = (StorageManager) Utils.getApp()
				.getSystemService(Context.STORAGE_SERVICE);
		try
		{
			Class<?> storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
			Method getVolumeList = StorageManager.class.getMethod("getVolumeList");
			Method getPath = storageVolumeClazz.getMethod("getPath");
			Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
			Object result = getVolumeList.invoke(mStorageManager);
			final int length = Array.getLength(result);
			for (int i = 0; i < length; i++)
			{
				Object storageVolumeElement = Array.get(result, i);
				String path = (String) getPath.invoke(storageVolumeElement);
				boolean res = (Boolean) isRemovable.invoke(storageVolumeElement);
				if (removable == res)
				{
					paths.add(path);
				}
			}
		} catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		} catch (InvocationTargetException e)
		{
			e.printStackTrace();
		} catch (NoSuchMethodException e)
		{
			e.printStackTrace();
		} catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		return paths;
	}

	@SuppressWarnings("TryWithIdenticalCatches")
	public static List<String> getSDCardPaths()
	{
		StorageManager storageManager = (StorageManager) Utils.getApp()
				.getSystemService(Context.STORAGE_SERVICE);
		List<String> paths = new ArrayList<>();
		try
		{
			Method getVolumePathsMethod = StorageManager.class.getMethod("getVolumePaths");
			getVolumePathsMethod.setAccessible(true);
			Object invoke = getVolumePathsMethod.invoke(storageManager);
			paths = Arrays.asList((String[]) invoke);
		} catch (NoSuchMethodException e)
		{
			e.printStackTrace();
		} catch (IllegalAccessException e)
		{
			e.printStackTrace();
		} catch (InvocationTargetException e)
		{
			e.printStackTrace();
		}
		return paths;
	}
}