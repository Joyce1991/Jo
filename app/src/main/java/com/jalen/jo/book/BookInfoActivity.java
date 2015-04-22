package com.jalen.jo.book;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.jalen.jo.R;
import com.jalen.jo.activities.BaseActivity;

public class BookInfoActivity extends BaseActivity implements BookinfoFragment.OnFragmentInteractionListener {
    private String mParamISBN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);
        // 用Toolbar替换actionbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_book_info_toolbar);
        setSupportActionBar(toolbar);

        mParamISBN = getIntent().getStringExtra(BookinfoFragment.EXTRA_BOOK_ISBN);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, BookinfoFragment.newInstance(mParamISBN))
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_book_info, menu);
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

    @Override
    public void onFragmentInteraction(int id) {
        switch (id){
            case R.id.book_not_found:
//                替换fragment（booknotfoundfragment）
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, BookNotfoundFragment.newInstance(mParamISBN))
                        .commit();
                break;

            case R.id.book_found:

                break;
        }
    }
}
