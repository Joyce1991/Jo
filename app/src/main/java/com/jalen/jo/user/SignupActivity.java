package com.jalen.jo.user;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SignUpCallback;
import com.jalen.jo.R;
import com.jalen.jo.activities.AccountActivity;
import com.jalen.jo.activities.BaseActivity;
import com.jalen.jo.fragments.BaseFragment;
import com.jalen.jo.utils.VerifyUtil;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.UMQQSsoHandler;

import java.util.Set;

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
        // 整个平台的Controller, 负责管理整个SDK的配置、操作等处理
        private UMSocialService mController = UMServiceFactory
                .getUMSocialService(Constants.DESCRIPTOR);

        // M
        private String mEmail;
        private String mUsername;
        private String mPwd;
        private Set<String> emails;
        // V
        private AutoCompleteTextView etEmail;
        private EditText etPwd;
        private Button btnSigup;
        private ImageButton btnSiginByQQ;


        public PlaceholderFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            emails = readEmailFromAccountManager();
        }



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_signup, container, false);
            return rootView;
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            etEmail = (AutoCompleteTextView) view.findViewById(R.id.et_signup_inputemail);
            etPwd = (EditText) view.findViewById(R.id.et_signup_inputpwd);
            btnSiginByQQ = (ImageButton) view.findViewById(R.id.signin_qq);
            btnSigup = (Button) view.findViewById(R.id.btn_signup_onekeyregistration);
            btnSigup.setOnClickListener(this);
            btnSiginByQQ.setOnClickListener(this);

            String[] emailStrs =  emails.toArray(new String[emails.size()]);
            etEmail.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.simple_text, emailStrs));
            EditText editText = new EditText(getActivity());

            addQZoneQQPlatform();
        }

        private void addQZoneQQPlatform() {
            String appId = "1104552505";
            String appKey = "6ddc08752c41845c199cd8ca1ee9bed9";
//            String appKey = "wJS5uEuDxKFja95b";
            // 添加QQ支持, 并且设置QQ分享内容的target url
            UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(getActivity(),
                    appId, appKey);
            qqSsoHandler.setTargetUrl("http://www.umeng.com");
            qqSsoHandler.addToSocialSDK();
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
                case R.id.signin_qq:
                    login(SHARE_MEDIA.QQ);
                    break;

            }
        }

        /**
         * 授权。如果授权成功，则获取用户信息</br>
         */
        private void login(final SHARE_MEDIA platform) {
            mController.doOauthVerify(getActivity(), platform, new SocializeListeners.UMAuthListener() {

                @Override
                public void onStart(SHARE_MEDIA platform) {
                }

                @Override
                public void onError(SocializeException e, SHARE_MEDIA platform) {
                }

                @Override
                public void onComplete(Bundle value, SHARE_MEDIA platform) {
                    String uid = value.getString("uid");
                    AVUser.AVThirdPartyUserAuth userAuth = new AVUser.AVThirdPartyUserAuth(
                            value.getString("access_token"),
                            value.getString("expires_in"),
                            AVUser.AVThirdPartyUserAuth.SNS_TENCENT_WEIBO, uid);
                    AVUser.loginWithAuthData(userAuth, new LogInCallback<AVUser>() {
                        @Override
                        public void done(AVUser avUser, AVException e) {
                            showMessage("qq登录成功", null, true);
                        }
                    });
                    if (!TextUtils.isEmpty(uid)) {
                        getUserInfo(platform);
                    } else {
                        Toast.makeText(getActivity(), "授权失败...", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancel(SHARE_MEDIA platform) {
                }
            });
        }

        private void getUserInfo(SHARE_MEDIA platform) {

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
