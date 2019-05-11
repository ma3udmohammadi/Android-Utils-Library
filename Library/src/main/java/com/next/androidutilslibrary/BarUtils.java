package com.next.androidutilslibrary;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import androidx.annotation.ColorInt;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import java.lang.reflect.Method;

public final class BarUtils
{
	private static final int DEFAULT_ALPHA = 112;
	private static final String TAG_COLOR = "TAG_COLOR";
	private static final String TAG_ALPHA = "TAG_ALPHA";
	private static final int TAG_OFFSET = -123;

	private BarUtils()
	{
		throw new UnsupportedOperationException("u can't instantiate me...");
	}

	public static int getStatusBarHeight()
	{
		Resources resources = Utils.getApp().getResources();
		int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
		return resources.getDimensionPixelSize(resourceId);
	}

	public static void addMarginTopEqualStatusBarHeight(@NonNull View view)
	{
		Object haveSetOffset = view.getTag(TAG_OFFSET);
		if (haveSetOffset != null && (Boolean) haveSetOffset) return;
		ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
		layoutParams.setMargins(layoutParams.leftMargin,
				layoutParams.topMargin + getStatusBarHeight(),
				layoutParams.rightMargin,
				layoutParams.bottomMargin);
		view.setTag(TAG_OFFSET, true);
	}

	public static void subtractMarginTopEqualStatusBarHeight(@NonNull View view)
	{
		Object haveSetOffset = view.getTag(TAG_OFFSET);
		if (haveSetOffset == null || !(Boolean) haveSetOffset) return;
		ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
		layoutParams.setMargins(layoutParams.leftMargin,
				layoutParams.topMargin - getStatusBarHeight(),
				layoutParams.rightMargin,
				layoutParams.bottomMargin);
		view.setTag(TAG_OFFSET, false);
	}

	public static void setStatusBarColor(@NonNull final Activity activity, @ColorInt final int color)
	{
		setStatusBarColor(activity, color, DEFAULT_ALPHA, false);
	}

	public static void setStatusBarColor(@NonNull final Activity activity, @ColorInt final int color,
	                                     @IntRange(from = 0, to = 255) final int alpha)
	{
		setStatusBarColor(activity, color, alpha, false);
	}

	public static void setStatusBarColor(@NonNull final Activity activity, @ColorInt final int color,
	                                     @IntRange(from = 0, to = 255) final int alpha, final boolean isDecor)
	{
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
		hideAlphaView(activity);
		transparentStatusBar(activity);
		addStatusBarColor(activity, color, alpha, isDecor);
	}

	public static void setStatusBarAlpha(@NonNull final Activity activity)
	{
		setStatusBarAlpha(activity, DEFAULT_ALPHA, false);
	}

	public static void setStatusBarAlpha(@NonNull final Activity activity, @IntRange(from = 0, to = 255) final int alpha)
	{
		setStatusBarAlpha(activity, alpha, false);
	}

	public static void setStatusBarAlpha(@NonNull final Activity activity,
	                                     @IntRange(from = 0, to = 255) final int alpha, final boolean isDecor)
	{
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
		hideColorView(activity);
		transparentStatusBar(activity);
		addStatusBarAlpha(activity, alpha, isDecor);
	}

	public static void setStatusBarColor(@NonNull final View fakeStatusBar, @ColorInt final int color)
	{
		setStatusBarColor(fakeStatusBar, color, DEFAULT_ALPHA);
	}

	public static void setStatusBarColor(@NonNull final View fakeStatusBar, @ColorInt final int color,
	                                     @IntRange(from = 0, to = 255) final int alpha)
	{
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
		fakeStatusBar.setVisibility(View.VISIBLE);
		transparentStatusBar((Activity) fakeStatusBar.getContext());
		ViewGroup.LayoutParams layoutParams = fakeStatusBar.getLayoutParams();
		layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
		layoutParams.height = BarUtils.getStatusBarHeight();
		fakeStatusBar.setBackgroundColor(getStatusBarColor(color, alpha));
	}

	public static void setStatusBarAlpha(@NonNull final View fakeStatusBar)
	{
		setStatusBarAlpha(fakeStatusBar, DEFAULT_ALPHA);
	}

	public static void setStatusBarAlpha(@NonNull final View fakeStatusBar,
	                                     @IntRange(from = 0, to = 255) final int alpha)
	{
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
		fakeStatusBar.setVisibility(View.VISIBLE);
		transparentStatusBar((Activity) fakeStatusBar.getContext());
		ViewGroup.LayoutParams layoutParams = fakeStatusBar.getLayoutParams();
		layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
		layoutParams.height = BarUtils.getStatusBarHeight();
		fakeStatusBar.setBackgroundColor(Color.argb(alpha, 0, 0, 0));
	}

	public static void setStatusBarColor4Drawer(@NonNull final Activity activity, @NonNull final DrawerLayout drawer,
	                                            @NonNull final View fakeStatusBar, @ColorInt final int color,
	                                            final boolean isTop)
	{
		setStatusBarColor4Drawer(activity, drawer, fakeStatusBar, color, DEFAULT_ALPHA, isTop);
	}

	public static void setStatusBarColor4Drawer(@NonNull final Activity activity, @NonNull final DrawerLayout drawer,
	                                            @NonNull final View fakeStatusBar, @ColorInt final int color,
	                                            @IntRange(from = 0, to = 255) final int alpha, final boolean isTop)
	{
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
		drawer.setFitsSystemWindows(false);
		transparentStatusBar(activity);
		setStatusBarColor(fakeStatusBar, color, isTop ? alpha : 0);
		for (int i = 0, len = drawer.getChildCount(); i < len; i++)
		{
			drawer.getChildAt(i).setFitsSystemWindows(false);
		}
		if (isTop)
		{
			hideAlphaView(activity);
		} else
		{
			addStatusBarAlpha(activity, alpha, false);
		}
	}

	public static void setStatusBarAlpha4Drawer(@NonNull final Activity activity, @NonNull final DrawerLayout drawer,
	                                            @NonNull final View fakeStatusBar, final boolean isTop)
	{
		setStatusBarAlpha4Drawer(activity, drawer, fakeStatusBar, DEFAULT_ALPHA, isTop);
	}

	public static void setStatusBarAlpha4Drawer(@NonNull final Activity activity, @NonNull final DrawerLayout drawer,
	                                            @NonNull final View fakeStatusBar, @IntRange(from = 0, to = 255) final int alpha,
	                                            final boolean isTop)
	{
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
		drawer.setFitsSystemWindows(false);
		transparentStatusBar(activity);
		setStatusBarAlpha(fakeStatusBar, isTop ? alpha : 0);
		for (int i = 0, len = drawer.getChildCount(); i < len; i++)
		{
			drawer.getChildAt(i).setFitsSystemWindows(false);
		}
		if (isTop)
		{
			hideAlphaView(activity);
		} else
		{
			addStatusBarAlpha(activity, alpha, false);
		}
	}

	private static void addStatusBarColor(final Activity activity, final int color, final int alpha, boolean isDecor)
	{
		ViewGroup parent = isDecor ?
				(ViewGroup) activity.getWindow().getDecorView() :
				(ViewGroup) activity.findViewById(android.R.id.content);
		View fakeStatusBarView = parent.findViewWithTag(TAG_COLOR);
		if (fakeStatusBarView != null)
		{
			if (fakeStatusBarView.getVisibility() == View.GONE)
			{
				fakeStatusBarView.setVisibility(View.VISIBLE);
			}
			fakeStatusBarView.setBackgroundColor(getStatusBarColor(color, alpha));
		} else
		{
			parent.addView(createColorStatusBarView(parent.getContext(), color, alpha));
		}
	}

	private static void addStatusBarAlpha(final Activity activity, final int alpha, boolean isDecor)
	{
		ViewGroup parent = isDecor ?
				(ViewGroup) activity.getWindow().getDecorView() :
				(ViewGroup) activity.findViewById(android.R.id.content);
		View fakeStatusBarView = parent.findViewWithTag(TAG_ALPHA);
		if (fakeStatusBarView != null)
		{
			if (fakeStatusBarView.getVisibility() == View.GONE)
			{
				fakeStatusBarView.setVisibility(View.VISIBLE);
			}
			fakeStatusBarView.setBackgroundColor(Color.argb(alpha, 0, 0, 0));
		} else
		{
			parent.addView(createAlphaStatusBarView(parent.getContext(), alpha));
		}
	}

	private static void hideColorView(final Activity activity)
	{
		ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
		View fakeStatusBarView = decorView.findViewWithTag(TAG_COLOR);
		if (fakeStatusBarView == null) return;
		fakeStatusBarView.setVisibility(View.GONE);
	}

	private static void hideAlphaView(final Activity activity)
	{
		ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
		View fakeStatusBarView = decorView.findViewWithTag(TAG_ALPHA);
		if (fakeStatusBarView == null) return;
		fakeStatusBarView.setVisibility(View.GONE);
	}

	private static int getStatusBarColor(final int color, final int alpha)
	{
		if (alpha == 0) return color;
		float a = 1 - alpha / 255f;
		int red = (color >> 16) & 0xff;
		int green = (color >> 8) & 0xff;
		int blue = color & 0xff;
		red = (int) (red * a + 0.5);
		green = (int) (green * a + 0.5);
		blue = (int) (blue * a + 0.5);
		return Color.argb(255, red, green, blue);
	}

	private static View createColorStatusBarView(final Context context, final int color, final int alpha)
	{
		View statusBarView = new View(context);
		statusBarView.setLayoutParams(new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight()));
		statusBarView.setBackgroundColor(getStatusBarColor(color, alpha));
		statusBarView.setTag(TAG_COLOR);
		return statusBarView;
	}

	private static View createAlphaStatusBarView(final Context context, final int alpha)
	{
		View statusBarView = new View(context);
		statusBarView.setLayoutParams(new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight()));
		statusBarView.setBackgroundColor(Color.argb(alpha, 0, 0, 0));
		statusBarView.setTag(TAG_ALPHA);
		return statusBarView;
	}

	private static void transparentStatusBar(final Activity activity)
	{
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
		Window window = activity.getWindow();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
		{
			int option = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
			window.getDecorView().setSystemUiVisibility(option);
			window.setStatusBarColor(Color.TRANSPARENT);
		} else
		{
			window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
	}

	public static int getActionBarHeight(@NonNull final Activity activity)
	{
		TypedValue tv = new TypedValue();
		if (activity.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
		{
			return TypedValue.complexToDimensionPixelSize(tv.data, activity.getResources().getDisplayMetrics());
		}
		return 0;
	}

	public static void showNotificationBar(@NonNull final Context context, final boolean isSettingPanel)
	{
		String methodName = isSettingPanel ? "expandSettingsPanel" : "expandNotificationsPanel";
		invokePanels(context, methodName);
	}

	public static void hideNotificationBar(@NonNull final Context context)
	{
		String methodName = "collapsePanels";
		invokePanels(context, methodName);
	}

	private static void invokePanels(@NonNull final Context context, final String methodName)
	{
		try
		{
			Object service = context.getSystemService("statusbar");
			Class<?> statusBarManager = Class.forName("android.app.StatusBarManager");
			Method expand = statusBarManager.getMethod(methodName);
			expand.invoke(service);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static int getNavBarHeight()
	{
		Resources res = Utils.getApp().getResources();
		int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
		if (resourceId != 0)
		{
			return res.getDimensionPixelSize(resourceId);
		} else
		{
			return 0;
		}
	}

	public static void hideNavBar(@NonNull final Activity activity)
	{
		if (getNavBarHeight() > 0)
		{
			View decorView = activity.getWindow().getDecorView();
			int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
					| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
			decorView.setSystemUiVisibility(uiOptions);
		}
	}

}