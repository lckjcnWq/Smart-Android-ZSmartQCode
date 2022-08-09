package com.theswitchbot.common.widget.adapter;

import android.graphics.Rect;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by mingzhen on 2021/01/11.
 */
public class SpacingDecoration extends RecyclerView.ItemDecoration {
    private int mHorizontalSpacing = 0;
    private int mVerticalSpacing = 0;
    private boolean mIncludeEdge = false;

    private int mExtraLeft = 0;
    private int mExtraTop = 0;
    private int mExtraRight = 0;
    private int mExtraBottom = 0; //左上右下多余空格间隙


    public SpacingDecoration(int hSpacing, int vSpacing) {
        mHorizontalSpacing = hSpacing;
        mVerticalSpacing = vSpacing;
        mIncludeEdge = false;
    }


    /**
     *
     * @param hSpacing 左右空白间隙
     * @param vSpacing 上下空白间隙
     * @param includeEdge true: 边缘有空白间隙; false: 边缘没有空白间隙
     */
    public SpacingDecoration(int hSpacing, int vSpacing, boolean includeEdge) {
        mHorizontalSpacing = hSpacing;
        mVerticalSpacing = vSpacing;
        mIncludeEdge = includeEdge;
    }


    /**
     *
     * @param hSpacing
     * @param vSpacing
     * @param includeEdge
     * @param extraLeft
     * @param extraTop
     * @param extraRight
     * @param extraBottom
     */
    public SpacingDecoration(int hSpacing, int vSpacing, boolean includeEdge, int extraLeft, int extraTop,
                             int extraRight, int extraBottom) {
        mHorizontalSpacing = hSpacing;
        mVerticalSpacing = vSpacing;
        mIncludeEdge = includeEdge;

        mExtraLeft = extraLeft;
        mExtraTop = extraTop;
        mExtraRight = extraRight;
        mExtraBottom = extraBottom;
    }




    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        // Only handle the vertical situation
        int position = parent.getChildAdapterPosition(view);
        if (parent.getLayoutManager() instanceof GridLayoutManager) {
            GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
            int spanCount = layoutManager.getSpanCount();
            int column = position % spanCount;
            getGridItemOffsets(outRect, position, column, spanCount);
        } else if (parent.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) parent.getLayoutManager();
            int spanCount = layoutManager.getSpanCount();
            StaggeredGridLayoutManager.LayoutParams lp = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
            int column = lp.getSpanIndex();
            getGridItemOffsets(outRect, position, column, spanCount);
        } else if (parent.getLayoutManager() instanceof LinearLayoutManager) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
            int itemCount = layoutManager.getItemCount();

            outRect.left = mHorizontalSpacing;
            outRect.right = mHorizontalSpacing;
            if (mIncludeEdge) {
                if (position == 0) {
                    outRect.top = mVerticalSpacing;
                }
                outRect.bottom = mVerticalSpacing;
            } else {
                if (position > 0) {
                    outRect.bottom = mVerticalSpacing;
                }
            }

            //多余的位置间隙


            if (((LinearLayoutManager) parent.getLayoutManager()).getOrientation() == LinearLayoutManager.VERTICAL) {
                if (position == 0) {
                    outRect.top += mExtraTop;
                }

                outRect.left += mExtraLeft;
                outRect.right += mExtraRight;

                if (position == itemCount - 1) {
                    outRect.bottom += mExtraBottom;
                }
            }

            if (((LinearLayoutManager) parent.getLayoutManager()).getOrientation() == LinearLayoutManager.HORIZONTAL) {
                if (position == 0) {
                    outRect.left += mExtraLeft;
                }

                outRect.top += mExtraTop;
                outRect.bottom += mExtraBottom;

                if (position == itemCount - 1) {
                    outRect.right += mExtraRight;

                }
            }
        }
    }

    private void getGridItemOffsets(Rect outRect, int position, int column, int spanCount) {
        if (mIncludeEdge) {
            outRect.left = mHorizontalSpacing * (spanCount - column) / spanCount;
            outRect.right = mHorizontalSpacing * (column + 1) / spanCount;
            if (position < spanCount) {
                outRect.top = mVerticalSpacing;
            }
            outRect.bottom = mVerticalSpacing;
        } else {
            outRect.left = mHorizontalSpacing * column / spanCount;
            outRect.right = mHorizontalSpacing * (spanCount - 1 - column) / spanCount;
            if (position >= spanCount) {
                outRect.top = mVerticalSpacing;
            }
        }
    }
}
