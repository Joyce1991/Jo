package com.jalen.jo.library;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.jalen.jo.R;
import com.jalen.jo.fragments.BaseFragment;
import com.jalen.jo.scan.CaptureActivity;
import com.jalen.jo.views.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * 图书馆主界面
 * <br/>
 * 这个fragment嵌套其他的fragment slidlayout+viewpager（viewpager中放fragment）
 */
public class AllLibraryFragment extends BaseFragment {
    /**
     * 用于替代actionbar的tabs
     */
    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;
    /**
     * Tab标题
     */
    private String[] mTabTitles;
    /**
     * 选项卡数据对象
     */
    private List<TabItem> mTabs = new ArrayList<TabItem>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTabTitles = getResources().getStringArray(R.array.tabs_all_library);
        for (String mTabTitle : mTabTitles){
            mTabs.add(new TabItem(mTabTitle, getResources().getColor(R.color.green_dark),
                    getResources().getColor(R.color.green_dark)));
        }
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_library, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // ViewPager配置
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new SimpleFragmentPagerAdapter(getChildFragmentManager()));

        // SlidingTabLayout配置
        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(mViewPager);
        mSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return mTabs.get(position).getIndicatorColor();
            }
            @Override
            public int getDividerColor(int position) {
                return mTabs.get(position).getDividerColor();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        setActionBarTitle(R.string.title_section1);
        inflater.inflate(R.menu.menu_library, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_new:
                startLibraryCreate();
                return true;
            case R.id.action_scan:
                Intent captureIntent = new Intent(getActivity(), CaptureActivity.class);
                startActivity(captureIntent);
                return true;
            case R.id.action_search:
                showMessage("点击了搜索页", null, true);
                return true;
            case R.id.action_display_style:
                showMessage("点击了样式", null, true);
                return true;
            case R.id.action_order_style:
                showMessage("排序样式", null, true);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 启动图书馆创建流程
     */
    private void startLibraryCreate() {
        // 启动创建图书馆页面
        showMessage(getText(R.string.onclick_library_create), null, true);
        Intent intentLibraryCreate = new Intent(getActivity(), LibraryCreateActivity.class);
        startActivity(intentLibraryCreate);
    }

    private class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {
        public SimpleFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mTabs.get(position).createFragment();
        }

        @Override
        public int getCount() {
            return mTabTitles.length;
        }
    }
}
