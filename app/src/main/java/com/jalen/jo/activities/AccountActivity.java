package com.jalen.jo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.jalen.jo.R;
import com.jalen.jo.fragments.AccountCentralFragment;
import com.jalen.jo.fragments.AccountEditFragment;
import com.jalen.jo.fragments.BaseFragment;
import com.jalen.jo.fragments.NicknameFragment;

/**
 * 账户资料管理
 */
public class AccountActivity extends BaseActivity {
    public static final String EXTRA_FRAGMENT_ID = "com.jalen.jo.activities.fragmentintent.fragment_id";

    private Intent mIntent; // 启动Intent
    private int mFragmentID;    // fragment_id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_central);
        // 用Toolbar替换actionbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 获取fragment id，根据fragment id创建fragment
        mIntent = getIntent();
        mFragmentID = mIntent.getIntExtra(EXTRA_FRAGMENT_ID, 0);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, createFragmentById(mFragmentID))
                    .commit();
        }
    }

    /**
     *  根据id创建fragment
     * @param mFragmentID
     * @return
     */
    public BaseFragment createFragmentById(int mFragmentID){
        BaseFragment fragment = null;
        switch (mFragmentID){
            case R.id.fragment_nickname:
                fragment = NicknameFragment.newInstance();
                break;
            case R.id.fragment_accountedit:
                fragment = AccountEditFragment.newInstance("param1", "param2");
                break;
            default:
                fragment = AccountCentralFragment.newInstance();
                break;
        }
        return fragment;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_account_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
