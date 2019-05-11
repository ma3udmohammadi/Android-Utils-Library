package com.next.androidutilslibrary;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.peak.androidutilslibrary.constant.MemoryConstants;
import com.peak.androidutilslibrary.constant.TimeConstants;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public final class ConvertUtils
{
	private ConvertUtils()
	{
		throw new UnsupportedOperationException("u can't instantiate me...");
	}

	private static final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

	public static String bytes2HexString(final byte[] bytes)
	{
		if (bytes == null) return null;
		int len = bytes.length;
		if (len <= 0) return null;
		char[] ret = new char[len << 1];
		for (int i = 0, j = 0; i < len; i++)
		{
			ret[j++] = hexDigits[bytes[i] >>> 4 & 0x0f];
			ret[j++] = hexDigits[bytes[i] & 0x0f];
		}
		return new String(ret);
	}

	public static byte[] hexString2Bytes(String hexString)
	{
		if (isSpace(hexString)) return null;
		int len = hexString.length();
		if (len % 2 != 0)
		{
			hexString = "0" + hexString;
			len = len + 1;
		}
		char[] hexBytes = hexString.toUpperCase().toCharArray();
		byte[] ret = new byte[len >> 1];
		for (int i = 0; i < len; i += 2)
		{
			ret[i >> 1] = (byte) (hex2Dec(hexBytes[i]) << 4 | hex2Dec(hexBytes[i + 1]));
		}
		return ret;
	}

	private static int hex2Dec(final char hexChar)
	{
		if (hexChar >= '0' && hexChar <= '9')
		{
			return hexChar - '0';
		} else if (hexChar >= 'A' && hexChar <= 'F')
		{
			return hexChar - 'A' + 10;
		} else
		{
			throw new IllegalArgumentException();
		}
	}

	public static byte[] chars2Bytes(final char[] chars)
	{
		if (chars == null || chars.length <= 0) return null;
		int len = chars.length;
		byte[] bytes = new byte[len];
		for (int i = 0; i < len; i++)
		{
			bytes[i] = (byte) (chars[i]);
		}
		return bytes;
	}

	public static char[] bytes2Chars(final byte[] bytes)
	{
		if (bytes == null) return null;
		int len = bytes.length;
		if (len <= 0) return null;
		char[] chars = new char[len];
		for (int i = 0; i < len; i++)
		{
			chars[i] = (char) (bytes[i] & 0xff);
		}
		return chars;
	}

	public static long timeSpan2Millis(final long timeSpan, @TimeConstants.Unit final int unit)
	{
		return timeSpan * unit;
	}

	public static long millis2TimeSpan(final long millis, @TimeConstants.Unit final int unit)
	{
		return millis / unit;
	}

	@SuppressLint("DefaultLocale")
	public static String millis2FitTimeSpan(long millis, int precision)
	{
		if (millis <= 0 || precision <= 0) return null;
		StringBuilder sb = new StringBuilder();
		String[] units = {"天", "小时", "分钟", "秒", "毫秒"};
		int[] unitLen = {86400000, 3600000, 60000, 1000, 1};
		precision = Math.min(precision, 5);
		for (int i = 0; i < precision; i++)
		{
			if (millis >= unitLen[i])
			{
				long mode = millis / unitLen[i];
				millis -= mode * unitLen[i];
				sb.append(mode).append(units[i]);
			}
		}
		return sb.toString();
	}

	public static String bytes2Bits(final byte[] bytes)
	{
		StringBuilder sb = new StringBuilder();
		for (byte aByte : bytes)
		{
			for (int j = 7; j >= 0; --j)
			{
				sb.append(((aByte >> j) & 0x01) == 0 ? '0' : '1');
			}
		}
		return sb.toString();
	}

	public static byte[] bits2Bytes(String bits)
	{
		int lenMod = bits.length() % 8;
		int byteLen = bits.length() / 8;
		// 不是8的倍数前面补0
		if (lenMod != 0)
		{
			for (int i = lenMod; i < 8; i++)
			{
				bits = "0" + bits;
			}
			byteLen++;
		}
		byte[] bytes = new byte[byteLen];
		for (int i = 0; i < byteLen; ++i)
		{
			for (int j = 0; j < 8; ++j)
			{
				bytes[i] <<= 1;
				bytes[i] |= bits.charAt(i * 8 + j) - '0';
			}
		}
		return bytes;
	}

	public static ByteArrayOutputStream input2OutputStream(final InputStream is)
	{
		if (is == null) return null;
		try
		{
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			byte[] b = new byte[MemoryConstants.KB];
			int len;
			while ((len = is.read(b, 0, MemoryConstants.KB)) != -1)
			{
				os.write(b, 0, len);
			}
			return os;
		} catch (IOException e)
		{
			e.printStackTrace();
			return null;
		} finally
		{
			CloseUtils.closeIO(is);
		}
	}

	public ByteArrayInputStream output2InputStream(final OutputStream out)
	{
		if (out == null) return null;
		return new ByteArrayInputStream(((ByteArrayOutputStream) out).toByteArray());
	}

	public static byte[] inputStream2Bytes(final InputStream is)
	{
		if (is == null) return null;
		return input2OutputStream(is).toByteArray();
	}

	public static InputStream bytes2InputStream(final byte[] bytes)
	{
		if (bytes == null || bytes.length <= 0) return null;
		return new ByteArrayInputStream(bytes);
	}

	public static byte[] outputStream2Bytes(final OutputStream out)
	{
		if (out == null) return null;
		return ((ByteArrayOutputStream) out).toByteArray();
	}

	public static OutputStream bytes2OutputStream(final byte[] bytes)
	{
		if (bytes == null || bytes.length <= 0) return null;
		ByteArrayOutputStream os = null;
		try
		{
			os = new ByteArrayOutputStream();
			os.write(bytes);
			return os;
		} catch (IOException e)
		{
			e.printStackTrace();
			return null;
		} finally
		{
			CloseUtils.closeIO(os);
		}
	}

	public static String inputStream2String(final InputStream is, final String charsetName)
	{
		if (is == null || isSpace(charsetName)) return null;
		try
		{
			return new String(inputStream2Bytes(is), charsetName);
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public static InputStream string2InputStream(final String string, final String charsetName)
	{
		if (string == null || isSpace(charsetName)) return null;
		try
		{
			return new ByteArrayInputStream(string.getBytes(charsetName));
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public static String outputStream2String(final OutputStream out, final String charsetName)
	{
		if (out == null || isSpace(charsetName)) return null;
		try
		{
			return new String(outputStream2Bytes(out), charsetName);
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public static OutputStream string2OutputStream(final String string, final String charsetName)
	{
		if (string == null || isSpace(charsetName)) return null;
		try
		{
			return bytes2OutputStream(string.getBytes(charsetName));
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public static byte[] bitmap2Bytes(final Bitmap bitmap, final Bitmap.CompressFormat format)
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

	public static byte[] drawable2Bytes(final Drawable drawable, final Bitmap.CompressFormat format)
	{
		return drawable == null ? null : bitmap2Bytes(drawable2Bitmap(drawable), format);
	}

	public static Drawable bytes2Drawable(final byte[] bytes)
	{
		return bytes == null ? null : bitmap2Drawable(bytes2Bitmap(bytes));
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

	public static int dp2px(final float dpValue)
	{
		final float scale = Utils.getApp().getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	public static int px2dp(final float pxValue)
	{
		final float scale = Utils.getApp().getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static int sp2px(final float spValue)
	{
		final float fontScale = Utils.getApp().getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	public static int px2sp(final float pxValue)
	{
		final float fontScale = Utils.getApp().getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
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