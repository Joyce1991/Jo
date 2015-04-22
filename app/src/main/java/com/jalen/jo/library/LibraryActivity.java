package com.jalen.jo.library;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.jalen.jo.R;
import com.jalen.jo.activities.BaseActivity;

public class LibraryActivity extends BaseActivity implements LibraryBookDisplayFragment.OnFragmentInteractionListener{
    public static final String EXTRA_LIBRARY_ID = "com.jalen.jo.library.LibraryActivity.libraryObjectId";
    public static final String EXTRA_FRAGMENT_ID = "com.jalen.jo.library.LibraryActivity.fragmentId";
    // 数据模型层
    private JoLibrary mJoLibrary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        // 用Toolbar替换actionbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // 获取intent中的数据
        String extraLibraryObjectId = getIntent().getStringExtra(EXTRA_LIBRARY_ID);
        int mFragmentId = getIntent().getIntExtra(EXTRA_FRAGMENT_ID, R.id.fragment_library_bookdisplay);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, createFragmentById(mFragmentId, extraLibraryObjectId))
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_library, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case android.R.id.home:
                Toast.makeText(getApplicationContext(),"点击了 UP", Toast.LENGTH_SHORT).show();
                onBackPressed();
                return true;
            case R.id.action_settings:

                return true;
        }
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
