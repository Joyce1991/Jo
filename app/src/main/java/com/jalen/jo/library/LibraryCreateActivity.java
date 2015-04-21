package com.jalen.jo.library;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.Toolbar;

import com.jalen.jo.R;
import com.jalen.jo.activities.BaseActivity;

/**
 * 图书馆创建模块
 */
public class LibraryCreateActivity extends BaseActivity implements LibraryCreateFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_create);
        // 用Toolbar替换actionbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, LibraryCreateFragment.newInstance("param1", "param2"))
                    .commit();
        }
    }



    @Override
    public void onFragmentInteraction(Message msg) {
        switch (msg.what){
            case R.id.fragment_library_create_success:
                String mLibraryObjectId = (String) msg.obj;
                getSupportFragmentManager().beginTransaction().replace(R.id.container, LibraryCreateSuccessFragment.newInstance(mLibraryObjectId)).commit();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
