package com.next.androidutilslibrary;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by masoud mohammadi on 10/2/2017 9:50 AM.
 */
public class ResourceUtils
{
	public static int getResourcesIdByName(String name, String folder, Context context)
	{
		int nameResourceID = context.getResources().getIdentifier(name, folder, context.getApplicationInfo().packageName);
		if (nameResourceID == 0)
		{
			throw new IllegalArgumentException("No resource found with name " + name + " in " + folder);
		} else
		{
			return nameResourceID;
		}
	}

	public static int getDrawableResourceByName(String name, String folder, Context context)
	{
		return getResourcesIdByName(name, folder, context);
	}

	public static String getStringResourceByName(String name, String folder, Context context)
	{
		return context.getString(getResourcesIdByName(name, folder, context));
	}

	public static android.graphics.drawable.Drawable getDrawableFromAssets(Context context, String path)
	{
		try
		{
			InputStream ims = context.getAssets().open(path);
			android.graphics.drawable.Drawable d = android.graphics.drawable.Drawable.createFromStream(ims, null);
			ims.close();
			return d;
		} catch (IOException ex)
		{
			return null;
		}
	}

	public List<Drawable> loadDrawables(Class<?> clz)
	{
		List<Drawable> drawables = new ArrayList<>();
		final Field[] fields = clz.getDeclaredFields();
		for (Field field : fields)
		{
			try
			{
				Drawable drawable = new Drawable(field.getName(), field.getInt(clz));
				drawables.add(drawable);
			} catch (Exception ignored)
			{
				Log.i("ResourceUtils", "couldn't read one or more drawable(s).");
			}
		}
		return drawables;
	}

	public class Drawable
	{
		private String name;
		private int id;

		public Drawable()
		{

		}

		public Drawable(String name, int id)
		{
			this.name = name;
			this.id = id;
		}

		public int getId()
		{
			return id;
		}

		public void setId(int id)
		{
			this.id = id;
		}

		public String getName()
		{
			return name;
		}

		public void setName(String name)
		{
			this.name = name;
		}
	}
}