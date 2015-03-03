package com.jalen.jo.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.jalen.jo.R;
import com.jalen.jo.fragments.NicknameFragment;

/**
 * 账户资料管理
 */
public class AccountEditActivity extends ActionBarActivity {
    public static final String EXTRA_FRAGMENT_ID = "com.jalen.jo.activities.fragmentintent.fragment_id";

    private Intent mIntent; // 启动Intent
    private int mFragmentID;    // fragment_id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_edit);

        mIntent = getIntent();
        mFragmentID = mIntent.getIntExtra(EXTRA_FRAGMENT_ID, 0);



        if (savedInstanceState == null) {
          /*  getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new NicknameFragment())
                    .commit();*/
        }
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements View.OnClickListener {
//        M
        private String mNickname;
//        V
        private EditText etNickname;
        private Button btnSave;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_account_edit, container, false);

            etNickname = (EditText) rootView.findViewById(R.id.et_account_edit_nickname);
            btnSave = (Button) rootView.findViewById(R.id.btn_account_edit_save);

            btnSave.setOnClickListener(this);
            return rootView;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_account_edit_save:

                    break;
            }
        }
    }
}
