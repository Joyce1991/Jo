package com.jalen.jo.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jalen.jo.R;
import com.jalen.jo.library.AllLibraryFragment;
import com.jalen.jo.views.CircleImageView;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * 账户中心
 * Created by jh on 2015/3/3.
 */
public class AccountCentralFragment extends BaseFragment implements View.OnClickListener {
    private int POSITION_INITIAL = 0x0000;
    //        M
    private String mNickname;
    /**
     * fragment
     */
    private List<Fragment> fragments;
    private IFragmentReplaceListener mFragmentReplaceListener;
    /**
     * tab标题名称
     */
    private static final String[] tabs = { "书友", "消息"};
    //        V
    private CircleImageView civAccountPic;
    private TextView tvUsername;
    private TabPageIndicator indicator;
    private ViewPager pager;

    // TODO: Rename and change types and number of parameters
    public static AccountCentralFragment newInstance() {
        AccountCentralFragment fragment = new AccountCentralFragment();

        return fragment;
    }

    public AccountCentralFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mFragmentReplaceListener = (IFragmentReplaceListener)activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        fragments = new ArrayList<Fragment>();
        fragments.add(new AllLibraryFragment());
        fragments.add(UserFavoritesFragment.newInstance());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_account_central, container, false);
        findViewAndSet(rootView);
        return rootView;
    }

    private void findViewAndSet(View rootView) {
        civAccountPic = (CircleImageView) rootView.findViewById(R.id.civ_account_pic);
        tvUsername = (TextView) rootView.findViewById(R.id.tv_account_username);
/*
        indicator = (TabPageIndicator) rootView.findViewById(R.id.indicator_account);
        pager = (ViewPager) rootView.findViewById(R.id.pager_account);

        // indicator、pager
        indicator = (TabPageIndicator) rootView.findViewById(R.id.indicator_account);
        pager = (ViewPager) rootView.findViewById(R.id.pager_account);
        PagerAdapter pagerAdapter = new JoPagerAdapter(getFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.setOffscreenPageLimit(1);
        indicator.setViewPager(pager, POSITION_INITIAL);
*/
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_account_central, menu);
        // 设置页面标题
        setActionBarTitle(R.string.title_fragment_account_central);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_account_edit:
                mFragmentReplaceListener.requestReplaceFromFragment(R.id.fragment_accountedit);
                return true;
            case R.id.action_account_qrcode:

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_account_edit_save:

                break;

        }
    }

    private class JoPagerAdapter extends FragmentPagerAdapter {
        public JoPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return tabs.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }
    }
}
