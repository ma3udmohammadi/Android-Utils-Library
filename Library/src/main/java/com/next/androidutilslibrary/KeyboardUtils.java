package com.next.androidutilslibrary;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public final class KeyboardUtils
{
	private KeyboardUtils()
	{
		throw new UnsupportedOperationException("u can't instantiate me...");
	}

	public static void showSoftInput(final Activity activity)
	{
		View view = activity.getCurrentFocus();
		if (view == null) view = new View(activity);
		InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
		if (imm == null) return;
		imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
	}

	public static void showSoftInput(final View view)
	{
		view.setFocusable(true);
		view.setFocusableInTouchMode(true);
		view.requestFocus();
		InputMethodManager imm = (InputMethodManager) Utils.getApp().getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm == null) return;
		imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
	}

	public static void hideSoftInput(final Activity activity)
	{
		View view = activity.getCurrentFocus();
		if (view == null) view = new View(activity);
		InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
		if (imm == null) return;
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	public static void hideSoftInput(final View view)
	{
		InputMethodManager imm = (InputMethodManager) Utils.getApp().getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm == null) return;
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	public static void toggleSoftInput()
	{
		InputMethodManager imm = (InputMethodManager) Utils.getApp().getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm == null) return;
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
	}

	public static void clickBlankArea2HideSoftInput()
	{
		Log.d("tips", "U should copy the following code.");
	    /*
        @Override
        public boolean dispatchTouchEvent(MotionEvent ev) {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                View v = getCurrentFocus();
                if (isShouldHideKeyboard(v, ev)) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
            return super.dispatchTouchEvent(ev);
        }

        // 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘
        private boolean isShouldHideKeyboard(View v, MotionEvent event) {
            if (v != null && (v instanceof EditText)) {
                int[] l = {0, 0};
                v.getLocationInWindow(l);
                int left = l[0],
                        top = l[1],
                        bottom = top + v.getHeight(),
                        right = left + v.getWidth();
                return !(event.getX() > left && event.getX() < right
                        && event.getY() > top && event.getY() < bottom);
            }
            return false;
        }
        */
	}
}