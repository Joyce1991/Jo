package com.jalen.jo.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v4.widget.DrawerLayout;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVUser;
import com.jalen.jo.R;
import com.jalen.jo.activities.AccountActivity;
import com.jalen.jo.user.SigninActivity;
import com.jalen.jo.user.SignupActivity;
import com.jalen.jo.adapters.SimpleListAdapter;
import com.jalen.jo.beans.DrawerOption;
import com.jalen.jo.views.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * 抽屉导航面板
 * 1、当前用户（头像、名称）
 * 2、书架（在读、想要读、已阅读）
 * 3、提醒（还书提醒）
 * 4、图书馆（图书馆创建、图书馆查询、图书馆加入）
 *
 */
public class NavigationDrawerFragment extends BaseFragment implements View.OnClickListener {

    /**
     * 记住选中的item的position
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    /**
     * 鉴于设计指南，你应该在当用户启动APP是显示这个抽屉面板给用户看，除非用户手动拓展了它，这个用来记录配置
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;
    private ArrayAdapter lvAdapter;

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
//    V
    private DrawerLayout mDrawerLayout;
    private RelativeLayout mDrawer;
    private Spinner mSpinner;
    private View mFragmentContainerView;
    private TextView mDrawerSignup; // 注册id
    private TextView mDrawerSignin; // 登录id
    private RelativeLayout llSignin;  // 已登录信息面板
    private LinearLayout llUnsignin;    // 未登录信息面板
    private CircleImageView civUsericon;   // 用户头像
    private TextView tvUsername;    // 用户名
    private ListView mDrawerListView;

//    M
    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;
    private List<DrawerOption> options;
    private android.support.v7.app.ActionBarDrawerToggle mActionBarDrawerToggle;

    public NavigationDrawerFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }
        // 初始化选项
        initOptions();
        // 初始化mCurrentSelectedPosition
        selectItem(mCurrentSelectedPosition);
    }

    /**
     * 初始化选项数据
     */
    private void initOptions() {
        options = new ArrayList<DrawerOption>();
        DrawerOption option = new DrawerOption(R.drawable.ic_action_map, "图书馆", 3);
        options.add(option);
        option = new DrawerOption(R.drawable.ic_menu_today, "书架", 2);
        options.add(option);
        option = new DrawerOption(R.drawable.ic_action_important, "关注的图书", 8);
        options.add(option);
        option = new DrawerOption(R.drawable.ic_action_alarms, "提醒", 2);
        options.add(option);
        option = new DrawerOption(R.drawable.ic_action_about, "关于", 0);
        options.add(option);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();

        // 重新加载user UI
        updateUserUI();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        加载XML布局
        mDrawer = (RelativeLayout) inflater.inflate(
                R.layout.fragment_navigation_drawer, container, false);
//        findviewbyid
        mDrawerSignup = (TextView) mDrawer.findViewById(R.id.tv_drawer_signup);
        mDrawerSignin = (TextView) mDrawer.findViewById(R.id.tv_drawer_signin);
        tvUsername = (TextView) mDrawer.findViewById(R.id.tv_drawer_username);
        llSignin = (RelativeLayout) mDrawer.findViewById(R.id.ll_drawer_signin);
        llUnsignin = (LinearLayout) mDrawer.findViewById(R.id.ll_drawer_unsignin);
        mDrawerListView = (ListView) mDrawer.findViewById(R.id.lv_drawer_options);

        updateUserUI();

        lvAdapter = new SimpleListAdapter(getActivity(), R.layout.adapter_drawer_options, options);
        mDrawerListView.setAdapter(lvAdapter);
        mDrawerListView.setSelector(R.drawable.item_bg);
        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });
        mDrawerListView.setItemChecked(mCurrentSelectedPosition, true); // 设置当前选择项

        return mDrawer;
    }

    private void updateUserUI() {
        //        加载用户信息
        if (AVUser.getCurrentUser() != null){
//            已登录
            llSignin.setVisibility(View.VISIBLE);
            llUnsignin.setVisibility(View.INVISIBLE);
            tvUsername.setText(AVUser.getCurrentUser().getUsername());
            llSignin.setOnClickListener(this);
        }else {
            llSignin.setVisibility(View.INVISIBLE);
            llUnsignin.setVisibility(View.VISIBLE);
            mDrawerSignin.setOnClickListener(this);
            mDrawerSignup.setOnClickListener(this);
        }
    }

    /**
     * 选择drawer菜单项目中的某个item
     * @param position item位置
     */
    private void selectItem(int position) {
        // 设置position位置为选定状态
        mCurrentSelectedPosition = position;
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mActionBarDrawerToggle.onConfigurationChanged(newConfig);
    }
    /**
     * Per the navigation drawer design guidelines, updates the action bar to show the global app
     * 'context', rather than just what's in the current screen.
     */
    private void showGlobalContextActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setTitle(R.string.app_name);
    }

    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_drawer_signin:
//                登录
                Toast.makeText(getActivity(), "点击了登录", Toast.LENGTH_SHORT).show();
                Intent signinIntent = new Intent(getActivity(), SigninActivity.class);
                startActivity(signinIntent);
                break;
            case R.id.tv_drawer_signup:
//                注册
                Toast.makeText(getActivity(), "点击了注册", Toast.LENGTH_SHORT).show();
                Intent signupIntent = new Intent(getActivity(), SignupActivity.class);
                startActivity(signupIntent);
                break;
            case R.id.ll_drawer_signin:
//                点击账户
                Toast.makeText(getActivity(), "点击了账户", Toast.LENGTH_SHORT).show();
                Intent accountIntent = new Intent(getActivity(), AccountActivity.class);
                accountIntent.putExtra(AccountActivity.EXTRA_FRAGMENT_ID, R.id.fragment_accountcentra);
                startActivity(accountIntent);
                break;
        }
    }

    public void setup(int fragmentId, DrawerLayout drawerLayout, Toolbar toolbar) {
        mFragmentContainerView = (View) getActivity().findViewById(fragmentId).getParent();
        mDrawerLayout = drawerLayout;

        mDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.main_colorPrimaryDark));

        mActionBarDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(getActivity(), mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) return;

                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) return;
                if (!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }
                showGlobalContextActionBar();
                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mActionBarDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     * <br/>
     *
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int position);
    }
}
