package com.jalen.jo.library;

import android.support.v4.app.Fragment;

/**
 * Created by jh on 2015/3/27.
 */
public class TabItem {
    private final CharSequence mTitle;
    private final int mIndicatorColor;
    private final int mDividerColor;

    public TabItem(CharSequence title, int indicatorColor, int dividerColor) {
        mTitle = title;
        mIndicatorColor = indicatorColor;
        mDividerColor = dividerColor;
    }

    /**
     * @return A new {@link Fragment} to be displayed by a {@link android.support.v4.view.ViewPager}
     */
    Fragment createFragment() {
        return LibraryListFragment.newInstance(mTitle, mIndicatorColor, mDividerColor);
    }

    /**
     * @return the title which represents this tab. In this sample this is used directly by
     * {@link android.support.v4.view.PagerAdapter#getPageTitle(int)}
     */
    CharSequence getTitle() {
        return mTitle;
    }

    /**
     * @return the color to be used for indicator on the {@link com.jalen.jo.views.SlidingTabLayout}
     */
    public int getIndicatorColor() {
        return mIndicatorColor;
    }

    /**
     * @return the color to be used for right divider on the {@link com.jalen.jo.views.SlidingTabLayout}
     */
    public int getDividerColor() {
        return mDividerColor;
    }
}
