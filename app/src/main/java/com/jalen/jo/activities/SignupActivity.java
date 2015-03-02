package com.jalen.jo.activities;

import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SignUpCallback;
import com.jalen.jo.BaseActivity;
import com.jalen.jo.R;
import com.jalen.jo.utils.VerifyUtil;

public class SignupActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_bymobile);
        // 设置actionbar的Up按钮可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_signup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()){
/*
            case android.R.id.home:
                Toast.makeText(getApplicationContext(), "点击了Up按钮", Toast.LENGTH_SHORT).show();
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
        // M
        private String mPhone;
        private String mUsername;
        private String mNickname;
        private String mPwd;
        // V
        private EditText etPhone;
        private EditText etPwd;
        private Button btnSigup;
        private AlertDialog mDialog;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_signup, container, false);

            etPhone = (EditText) rootView.findViewById(R.id.et_signup_inputphone);
            etPwd = (EditText) rootView.findViewById(R.id.et_signup_inputpwd);
            btnSigup = (Button) rootView.findViewById(R.id.btn_signup_onekeyregistration);
            btnSigup.setOnClickListener(this);


            return rootView;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_signup_onekeyregistration:
//                    获取输入的手机号码
                    mPhone = etPhone.getText().toString().trim();
//                    验证手机号码的数据格式
                    if (!VerifyUtil.getIntance().isPhoneNumber(mPhone)){
//                        提示用户手机号码输入错误
                        Toast.makeText(getActivity(), getText(R.string.toast_phone_input_error), Toast.LENGTH_SHORT).show();

                    }
//                    当前方式用户名和手机号码一致
                    mUsername = mPhone;
//                    获取输入的密码
                    mPwd = etPwd.getText().toString().trim();
//                    提交用户注册信息进行注册
                    sigupUser(mUsername, mPwd, mPhone, null);
                    break;

            }
        }

        /**
         * 提交用户注册信息
         * @param mUsername 用户名称（必需）
         * @param mPwd  用户密码（必需）
         * @param mPhone    手机号码（可选）
         * @param mEmail    邮箱（可选）
         */
        private void sigupUser(String mUsername, String mPwd, String mPhone, String mEmail) {
            final AVUser user = new AVUser();
            user.setUsername(mUsername);
            user.setPassword("joyce1991");
            user.setEmail("steve@company.com");
            user.setMobilePhoneNumber(mPhone);
            if (mEmail != null){
                user.setEmail(mEmail);
            }
//            显示“加载”对话框
            showDialog(getText(R.string.dialog_loading_signup_text));
//            调用后台注册方法
            user.signUpInBackground(new SignUpCallback() {
                public void done(AVException e) {
                    if (e == null) {
//                        注册成功
                        Toast.makeText(getActivity(), getText(R.string.toast_signup_success), Toast.LENGTH_SHORT).show();
//                        页面跳转至账户管理中的昵称编写页面
                        user.getObjectId();
//                        关闭对话框
                        dismissDialog();
                    } else {
                        // 失败
                        Toast.makeText(getActivity(), getText(R.string.toast_signup_failed), Toast.LENGTH_SHORT).show();
                        dismissDialog();
                    }
                }

            });

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
