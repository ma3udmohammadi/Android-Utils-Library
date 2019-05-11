package com.next.androidutilslibrary;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by masoud mohammadi on 10/14/2017 11:16 AM.
 */
public final class ImageUtils
{
	private ImageUtils()
	{
		throw new UnsupportedOperationException("u can't instantiate this class.");
	}

	public static byte[] bitmap2Bytes(final Bitmap bitmap, final CompressFormat format)
	{
		if (bitmap == null) return null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(format, 100, baos);
		return baos.toByteArray();
	}

	public static Bitmap bytes2Bitmap(final byte[] bytes)
	{
		return (bytes == null || bytes.length == 0) ? null : BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	}

	public static Bitmap drawable2Bitmap(final Drawable drawable)
	{
		if (drawable instanceof BitmapDrawable)
		{
			BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
			if (bitmapDrawable.getBitmap() != null)
			{
				return bitmapDrawable.getBitmap();
			}
		}
		Bitmap bitmap;
		if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0)
		{
			bitmap = Bitmap.createBitmap(1, 1,
					drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
		} else
		{
			bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
					drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
		}
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	public static Drawable bitmap2Drawable(final Bitmap bitmap)
	{
		return bitmap == null ? null : new BitmapDrawable(Utils.getApp().getResources(), bitmap);
	}

	public static byte[] drawable2Bytes(final Drawable drawable, final CompressFormat format)
	{
		return drawable == null ? null : bitmap2Bytes(drawable2Bitmap(drawable), format);
	}

	public static Drawable bytes2Drawable(final byte[] bytes)
	{
		return bitmap2Drawable(bytes2Bitmap(bytes));
	}

	public static Bitmap view2Bitmap(final View view)
	{
		if (view == null) return null;
		Bitmap ret = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(ret);
		Drawable bgDrawable = view.getBackground();
		if (bgDrawable != null)
		{
			bgDrawable.draw(canvas);
		} else
		{
			canvas.drawColor(Color.WHITE);
		}
		view.draw(canvas);
		return ret;
	}

	public static boolean save(final Bitmap src, final String filePath, final CompressFormat format)
	{
		return save(src, getFileByPath(filePath), format, false);
	}

	public static boolean save(final Bitmap src, final File file, final CompressFormat format)
	{
		return save(src, file, format, false);
	}

	public static boolean save(final Bitmap src, final String filePath, final CompressFormat format, final boolean recycle)
	{
		return save(src, getFileByPath(filePath), format, recycle);
	}

	public static boolean save(final Bitmap src, final File file, final CompressFormat format, final boolean recycle)
	{
		if (isEmptyBitmap(src) || !createFileByDeleteOldFile(file)) return false;
		OutputStream os = null;
		boolean ret = false;
		try
		{
			os = new BufferedOutputStream(new FileOutputStream(file));
			ret = src.compress(format, 100, os);
			if (recycle && !src.isRecycled()) src.recycle();
		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{
			CloseUtils.closeIO(os);
		}
		return ret;
	}

	public static boolean isImage(final File file)
	{
		return file != null && isImage(file.getPath());
	}

	public static boolean isImage(final String filePath)
	{
		String path = filePath.toUpperCase();
		return path.endsWith(".PNG") || path.endsWith(".JPG") || path.endsWith(".JPEG") || path.endsWith(".BMP") || path.endsWith(".GIF");
	}

	public static String getImageType(final String filePath)
	{
		return getImageType(getFileByPath(filePath));
	}

	public static String getImageType(final File file)
	{
		if (file == null) return null;
		InputStream is = null;
		try
		{
			is = new FileInputStream(file);
			return getImageType(is);
		} catch (IOException e)
		{
			e.printStackTrace();
			return null;
		} finally
		{
			CloseUtils.closeIO(is);
		}
	}

	public static String getImageType(final InputStream is)
	{
		if (is == null) return null;
		try
		{
			byte[] bytes = new byte[8];
			return is.read(bytes, 0, 8) != -1 ? getImageType(bytes) : null;
		} catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public static String getImageType(final byte[] bytes)
	{
		if (isJPEG(bytes)) return "JPEG";
		if (isGIF(bytes)) return "GIF";
		if (isPNG(bytes)) return "PNG";
		if (isBMP(bytes)) return "BMP";
		return null;
	}

	private static boolean isJPEG(final byte[] b)
	{
		return b.length >= 2
				&& (b[0] == (byte) 0xFF) && (b[1] == (byte) 0xD8);
	}

	private static boolean isGIF(final byte[] b)
	{
		return b.length >= 6
				&& b[0] == 'G' && b[1] == 'I'
				&& b[2] == 'F' && b[3] == '8'
				&& (b[4] == '7' || b[4] == '9') && b[5] == 'a';
	}

	private static boolean isPNG(final byte[] b)
	{
		return b.length >= 8
				&& (b[0] == (byte) 137 && b[1] == (byte) 80
				&& b[2] == (byte) 78 && b[3] == (byte) 71
				&& b[4] == (byte) 13 && b[5] == (byte) 10
				&& b[6] == (byte) 26 && b[7] == (byte) 10);
	}

	private static boolean isBMP(final byte[] b)
	{
		return b.length >= 2 && (b[0] == 0x42) && (b[1] == 0x4d);
	}

	private static File getFileByPath(final String filePath)
	{
		return isSpace(filePath) ? null : new File(filePath);
	}

	private static boolean createFileByDeleteOldFile(final File file)
	{
		if (file == null) return false;
		if (file.exists() && !file.delete()) return false;
		if (!createOrExistsDir(file.getParentFile())) return false;
		try
		{
			return file.createNewFile();
		} catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
	}

	private static boolean createOrExistsDir(final File file)
	{
		return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
	}

	private static boolean isEmptyBitmap(final Bitmap src)
	{
		return src == null || src.getWidth() == 0 || src.getHeight() == 0;
	}

	// Todo: this method is general for a lot of util libs. move to GeneralUtils.java
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