package com.jalen.jo.activities;

import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.support.v4.widget.DrawerLayout;

import com.avos.avoscloud.AVAnalytics;
import com.jalen.jo.fragments.AboutFragment;
import com.jalen.jo.fragments.NavigationDrawerFragment;
import com.jalen.jo.R;
import com.jalen.jo.fragments.UserAlarmFragment;
import com.jalen.jo.fragments.UserFavoritesFragment;
import com.jalen.jo.library.AllLibraryFragment;
import com.jalen.jo.fragments.UserShelfFragment;


public class MainActivity extends BaseActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks{

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 用Toolbar替换actionbar
        mToolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(mToolbar);

//        跟踪统计应用的打开情况
        AVAnalytics.trackAppOpened(getIntent());
        // 拖拽设置
        initDawer();
    }

    /**
     * Dawer设置
     */
    private void initDawer() {
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        // 设置Drawer
        mNavigationDrawerFragment.setup(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (position){
            case 0:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new AllLibraryFragment()).commit();
                break;
            case 1:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, UserShelfFragment.newInstance()).commit();
                break;
            case 2:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, UserFavoritesFragment.newInstance()).commit();
                break;
            case 3:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, UserAlarmFragment.newInstance()).commit();
                break;
            case 4:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, AboutFragment.newInstance()).commit();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // 当Drawer处于关闭状态，显示content view相关的action
            getMenuInflater().inflate(R.menu.global, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

}
