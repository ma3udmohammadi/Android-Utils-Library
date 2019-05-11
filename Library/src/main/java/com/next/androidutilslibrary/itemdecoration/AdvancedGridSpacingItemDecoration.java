package com.next.androidutilslibrary.itemdecoration;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

public class AdvancedGridSpacingItemDecoration extends RecyclerView.ItemDecoration
{
	private Context context;
	private int spacing;
	private boolean rtl;

	public AdvancedGridSpacingItemDecoration(Context context, boolean rtl, int spacing)
	{
		this.context = context;
		this.spacing = dpToPx(spacing);
		this.rtl = rtl;
	}

	private int dpToPx(int dp)
	{
		Resources r = context.getResources();
		return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
	}

	@Override
	public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
	{
		super.getItemOffsets(outRect, view, parent, state);

		GridLayoutManager gridLayoutManager = (GridLayoutManager) parent.getLayoutManager();
		GridLayoutManager.LayoutParams params = (GridLayoutManager.LayoutParams) view.getLayoutParams();
		int spanCount = gridLayoutManager.getSpanCount();

		int spanIndex = params.getSpanIndex();
		int spanSize = params.getSpanSize();
		if (spanIndex == 0)
		{
			outRect.left = spacing;
		} else
		{
			outRect.left = spacing / 2;
		}

		if (spanIndex + spanSize == spanCount)
		{
			outRect.right = spacing;
		} else
		{
			outRect.right = spacing / 2;
		}
		if (rtl)
		{
			int tmp = outRect.left;
			outRect.left = outRect.right;
			outRect.right = tmp;
		}

		// just add some vertical padding as well
		outRect.top = spacing / 2;
		outRect.bottom = spacing / 2;
	}
}