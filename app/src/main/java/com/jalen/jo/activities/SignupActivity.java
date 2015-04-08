package com.jalen.jo.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.internal.view.menu.MenuBuilder;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;
import com.jalen.jo.R;
import com.jalen.jo.adapters.SimpleListAdapter;
import com.jalen.jo.fragments.BaseFragment;
import com.jalen.jo.utils.VerifyUtil;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class SignupActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_bymobile);
        // 用Toolbar替换actionbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
    public static class PlaceholderFragment extends BaseFragment implements View.OnClickListener {
        // M
        private String mEmail;
        private String mUsername;
        private String mPwd;
        private Set<String> emails;
        // V
        private AutoCompleteTextView etEmail;
        private EditText etPwd;
        private Button btnSigup;


        public PlaceholderFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);


            emails = new HashSet<String>();
            Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
            Account[] accounts = AccountManager.get(getActivity()).getAccounts();
            for (Account account : accounts) {
                if (emailPattern.matcher(account.name).matches()) {
                    String accountName = account.name;
                    String accountType = account.type;
                    System.out.println("name:" + accountName + "\n" + "type:" + accountType);
                    emails.add(accountName);
                }
            }
            // 现在邮箱集合中的记录是不重复的邮箱地址了
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_signup, container, false);

            etEmail = (AutoCompleteTextView) rootView.findViewById(R.id.et_signup_inputemail);
            etPwd = (EditText) rootView.findViewById(R.id.et_signup_inputpwd);
            btnSigup = (Button) rootView.findViewById(R.id.btn_signup_onekeyregistration);
            btnSigup.setOnClickListener(this);

            String[] emailStrs =  emails.toArray(new String[emails.size()]);
            etEmail.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.simple_text, emailStrs));
            EditText editText = new EditText(getActivity());

            return rootView;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_signup_onekeyregistration:
//                    获取输入的手机号码
                    mEmail = etEmail.getText().toString().trim();
//                    验证手机号码的数据格式
                    if (!VerifyUtil.getIntance().isEmail(mEmail)){
//                        提示用户邮箱输入错误
                        Toast.makeText(getActivity(), getText(R.string.toast_email_input_error), Toast.LENGTH_SHORT).show();
                    }
//                    当前方式用户名和邮箱一致
                    mUsername = mEmail;
//                    获取输入的密码
                    mPwd = etPwd.getText().toString().trim();
//                    提交用户注册信息进行注册
                    sigupUser(mUsername, mPwd, null, mEmail);
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
        private void sigupUser(@NonNull String mUsername,@NonNull String mPwd,@Nullable String mPhone,@Nullable String mEmail) {
            final AVUser user = new AVUser();
            user.setUsername(mUsername);
            user.setPassword(mPwd);
            if (mPhone != null){
                user.setMobilePhoneNumber(mPhone);
            }
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
                        Intent intentAccount = new Intent(getActivity(), AccountActivity.class);
                        intentAccount.putExtra(AccountActivity.EXTRA_FRAGMENT_ID, R.id.fragment_nickname);
                        startActivity(intentAccount);
                        getActivity().finish();
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



    }
}
