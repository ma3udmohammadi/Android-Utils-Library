package com.next.androidutilslibrary;

import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

public final class SizeUtils
{
	private SizeUtils()
	{
		throw new UnsupportedOperationException("u can't instantiate me...");
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

	public static float applyDimension(final int unit, final float value, final DisplayMetrics metrics)
	{
		switch (unit)
		{
			case TypedValue.COMPLEX_UNIT_PX:
				return value;
			case TypedValue.COMPLEX_UNIT_DIP:
				return value * metrics.density;
			case TypedValue.COMPLEX_UNIT_SP:
				return value * metrics.scaledDensity;
			case TypedValue.COMPLEX_UNIT_PT:
				return value * metrics.xdpi * (1.0f / 72);
			case TypedValue.COMPLEX_UNIT_IN:
				return value * metrics.xdpi;
			case TypedValue.COMPLEX_UNIT_MM:
				return value * metrics.xdpi * (1.0f / 25.4f);
		}
		return 0;
	}

	public static void forceGetViewSize(final View view, final onGetSizeListener listener)
	{
		view.post(new Runnable()
		{
			@Override
			public void run()
			{
				if (listener != null)
				{
					listener.onGetSize(view);
				}
			}
		});
	}

	public interface onGetSizeListener
	{
		void onGetSize(View view);
	}

	public static int[] measureView(final View view)
	{
		ViewGroup.LayoutParams lp = view.getLayoutParams();
		if (lp == null)
		{
			lp = new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT
			);
		}
		int widthSpec = ViewGroup.getChildMeasureSpec(0, 0, lp.width);
		int lpHeight = lp.height;
		int heightSpec;
		if (lpHeight > 0)
		{
			heightSpec = View.MeasureSpec.makeMeasureSpec(lpHeight, View.MeasureSpec.EXACTLY);
		} else
		{
			heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		}
		view.measure(widthSpec, heightSpec);
		return new int[]{view.getMeasuredWidth(), view.getMeasuredHeight()};
	}

	public static int getMeasuredWidth(final View view)
	{
		return measureView(view)[0];
	}

	public static int getMeasuredHeight(final View view)
	{
		return measureView(view)[1];
	}
}