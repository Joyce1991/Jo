package com.jalen.jo.activities;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.jalen.jo.R;

import java.util.HashMap;
import java.util.Map;

public class SigninActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        // 用Toolbar替换actionbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_signin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
/*
            case android.R.id.home:
                // 点击Up键调用NavUtils.navigateUpFromSameTask(this)方法，
                // 这个方法只能适用于父activity和子activity在同一个task中。
                NavUtils.navigateUpFromSameTask(this);
                return true;
*/
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements View.OnClickListener {
        private String TAG = "SigninActivity.PlaceholderFragment";
        //    M
        private String mUsername = "";
        private String mPwd = "";
        private Boolean isPwdVisible = false;

        //    V
        private EditText etUser;
        private EditText etPwd;
        private Button btnSignin;
        private CheckBox ckbVisible;
        private AlertDialog mDialog;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_signin, container, false);

            etUser = (EditText) rootView.findViewById(R.id.et_signin_user);
            etPwd = (EditText) rootView.findViewById(R.id.et_signin_pwd);
            ckbVisible = (CheckBox) rootView.findViewById(R.id.ckb_signin_pwdvisible);
            btnSignin = (Button) rootView.findViewById(R.id.btn_signin_sigin);

            etUser.setOnClickListener(this);
            ckbVisible.setOnClickListener(this);
            btnSignin.setOnClickListener(this);

//            用缓存初始化数据
            Map<String, ?> data = readCacheData();
            if (data.size() > 0){
                mUsername = (String) data.get("username");
                mPwd = (String) data.get("password");
                isPwdVisible = (Boolean) data.get("isPwdVisible");
                etUser.setText(mUsername);
                etPwd.setText(mPwd);
                setPwdVisibility(isPwdVisible);
            }

            return rootView;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.et_signin_user:


                    break;
                case R.id.ckb_signin_pwdvisible:
                    isPwdVisible = ckbVisible.isChecked();
//                    设置密码编辑框的可见性
                   setPwdVisibility(isPwdVisible);
                    break;

                case R.id.btn_signin_sigin:
//                    登录
                    mUsername = etUser.getText().toString().trim();
                    mPwd = etPwd.getText().toString().trim();
//                    显示“加载”对话框
                    showDialog(getText(R.string.dialog_loading_signin_text));
                    // 退出当前用户的session
                    AVUser.logOut();
                    AVUser.logInInBackground(mUsername, mPwd, new LogInCallback() {
                        public void done(AVUser user, AVException e) {
                            if (user != null) {
                                // 登录成功
                                showMessage(getText(R.string.toast_signin_success), e, true);
//                                隐藏“加载”对话框
                                dismissDialog();
                            } else {
                                // 登录失败
                                showMessage(getText(R.string.toast_signin_failed), e, true);
//                                隐藏“加载”对话框
                                dismissDialog();

                            }
                        }
                    });

//                    缓存输入框的内容
                    Map<String, Object> map = new HashMap<>();
                    map.put("username", mUsername);
                    map.put("password", mPwd);
                    map.put("isPwdVisible", isPwdVisible);
                    cacheData(map);
                    break;
            }
        }


        /**
         * 设置密码的可见性
         * @param isVisible 是否可见
         */
        private void setPwdVisibility(Boolean isVisible){
            ckbVisible.setChecked(isVisible);
            if (isVisible){
//                        设置为可见密码
                etPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            }else{
//                        设置密码为不可见
                etPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        }
        /**
         * 存储一串键值对
         * @param data   待存储的键值对
         */
        private void cacheData(Map<String, ?> data){
            SharedPreferences sp = getActivity().getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor mEditor = sp.edit();
            for (Map.Entry<String, ?> entry : data.entrySet()){
                if (entry.getValue() instanceof  Boolean){
                    mEditor.putBoolean(entry.getKey(), (Boolean) entry.getValue());
                }else if (entry.getValue() instanceof  Integer){
                    mEditor.putInt(entry.getKey(), (Integer) entry.getValue());
                }else{
                    mEditor.putString(entry.getKey(), (String) entry.getValue());
                }
            }
            mEditor.commit();
            mEditor.clear();
        }

        /**
         * 读取当前页的存储数据
         * @return 一个Map<String, ?>对象
         */
        private Map<String, ?> readCacheData(){
            Map<String, String> data = new HashMap<>();
            SharedPreferences sp = getActivity().getPreferences(MODE_PRIVATE);
            return sp.getAll();
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

        /**
         * 显示一条toast信息
         * @param msg   toast内容
         * @param e     异常
         * @param b     是否要显示这条信息
         */
        private void showMessage(CharSequence msg, AVException e, boolean b) {
            Log.i(TAG, msg.toString());
            if(b){
                Toast.makeText(
                        getActivity(),
                        msg,
                        Toast.LENGTH_SHORT
                ).show();
            }
        }
    }
}
