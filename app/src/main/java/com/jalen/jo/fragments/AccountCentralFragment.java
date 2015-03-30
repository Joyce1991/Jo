package com.jalen.jo.fragments;

import android.app.AlertDialog;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jalen.jo.R;
import com.jalen.jo.library.AllLibraryFragment;
import com.jalen.jo.views.CircleImageView;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * 账户资料编辑
 * Created by jh on 2015/3/3.
 */
public class AccountCentralFragment extends Fragment implements View.OnClickListener {
    private int POSITION_INITIAL = 0x0001;
    //        M
    private String mNickname;
    /**
     * fragment
     */
    private List<Fragment> fragments;
    /**
     * tab标题名称
     */
    private static final String[] tabs = { "图书馆", "书架", "收藏的书" };
    //        V
    private CircleImageView civAccountPic;
    private TextView tvUsername;
    private TabPageIndicator indicator;
    private ViewPager pager;
    private AlertDialog mDialog;

    // TODO: Rename and change types and number of parameters
    public static AccountCentralFragment newInstance() {
        AccountCentralFragment fragment = new AccountCentralFragment();

        return fragment;
    }

    public AccountCentralFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragments = new ArrayList<Fragment>();
        fragments.add(new AllLibraryFragment());
        fragments.add(UserShelfFragment.newInstance());
        fragments.add(UserFavoritesFragment.newInstance());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_account_central, container, false);

        civAccountPic = (CircleImageView) rootView.findViewById(R.id.civ_account_pic);
        tvUsername = (TextView) rootView.findViewById(R.id.tv_account_username);
        indicator = (TabPageIndicator) rootView.findViewById(R.id.indicator_account);
        pager = (ViewPager) rootView.findViewById(R.id.pager_account);

        // indicator、pager
        indicator = (TabPageIndicator) rootView.findViewById(R.id.indicator_account);
        pager = (ViewPager) rootView.findViewById(R.id.pager_account);
        PagerAdapter pagerAdapter = new JoPagerAdapter(getFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.setOffscreenPageLimit(2);
        indicator.setViewPager(pager, POSITION_INITIAL);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_account_edit, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_account_edit_save:

                break;

        }
    }

    /**
     * 关闭一个对话框
     */
    private void dismissDialog() {
        if (mDialog != null && mDialog.isShowing()){
            mDialog.dismiss();
            mDialog = null;
        }
    }

    /**
     * 显示一个对话框
     * @param msg   对话框内容
     */
    private void showDialog(CharSequence msg) {
        if (mDialog != null){
            mDialog.show();
        }else{
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
            // Get the layout inflater
            LayoutInflater inflater = getActivity().getLayoutInflater();
            RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.dialog_loading, null);
            TextView tv_msg = (TextView) view.findViewById(R.id.tv_dialog_loading_text);
            tv_msg.setText(msg);
            mBuilder.setView(view);
            mDialog = mBuilder.create();
            mDialog.show();
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
