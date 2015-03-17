package com.jalen.jo.activities;

import android.app.AlertDialog;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jalen.jo.R;

public class BookInfoActivity extends ActionBarActivity {
    public static final String EXTRA_BOOK_ISBN = "com.jalen.jo.activities.BookInfoActivity.BookISBN";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        private AlertDialog mDialog;

        public PlaceholderFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_book_info, container, false);
            return rootView;
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
    }


}
