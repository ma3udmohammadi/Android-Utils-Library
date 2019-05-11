package com.next.androidutilslibrary;

import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;

public final class BottomNavigationViewUtils
{
	public static void disableShiftMode(BottomNavigationView view)
	{
		BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
		menuView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
		menuView.buildMenuView();
	}
}