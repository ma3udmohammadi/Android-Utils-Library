package com.next.androidutilslibrary.itemdecoration;

import android.graphics.Rect;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * Created by masoud on 8/17/2017.
 */

public class SearchResultItemDecoration extends RecyclerView.ItemDecoration
{
	private boolean rtl;
	private int space;
	private int numberOfColumns;

	public SearchResultItemDecoration(boolean rtl, int space, int numberOfColumns)
	{
		this.rtl = rtl;
		this.space = space;
		this.numberOfColumns = numberOfColumns;
	}

	@Override
	public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
	{
		super.getItemOffsets(outRect, view, parent, state);

		if (rtl)
		{
			addSpaceToViewRtl(outRect, parent.getChildAdapterPosition(view), parent);
		} else
		{
			addSpaceToViewLtr(outRect, parent.getChildAdapterPosition(view), parent);
		}
	}

	private void addSpaceToViewLtr(Rect outRect, int position, RecyclerView parent)
	{
		if (parent == null)
			return;
		GridLayoutManager grid = (GridLayoutManager) parent.getLayoutManager();
		int spanSize = grid.getSpanSizeLookup().getSpanSize(position);

		if (spanSize == numberOfColumns)
		{
			outRect.left = space;
		} else
		{
			int allSpanSize = 0;
			for (int i = 0; i < position; i++)
			{
				allSpanSize += grid.getSpanSizeLookup().getSpanSize(i);
			}
			int currentModuloResult = allSpanSize % numberOfColumns;
			if (currentModuloResult == 0)
			{
				outRect.left = space;
			}
		}
		outRect.right = space;
		outRect.top = space;
	}

	private void addSpaceToViewRtl(Rect outRect, int position, RecyclerView parent)
	{
		if (parent == null)
			return;
		GridLayoutManager grid = (GridLayoutManager) parent.getLayoutManager();
		int spanSize = grid.getSpanSizeLookup().getSpanSize(position);

		if (spanSize == numberOfColumns)
		{
			outRect.right = space;
		} else
		{
			int allSpanSize = 0;
			for (int i = 0; i < position; i++)
			{
				allSpanSize += grid.getSpanSizeLookup().getSpanSize(i);
			}
			int currentModuloResult = allSpanSize % numberOfColumns;
			if (currentModuloResult == 0)
			{
				outRect.right = space;
			}
		}
		outRect.left = space;
		outRect.top = space;
	}
}