package com.jalen.jo.fragments;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jalen.jo.R;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by jh on 2015/3/27.
 */
public class BaseFragment extends Fragment {
    public String tag;
    private AlertDialog mDialog;

    public BaseFragment(){
        super();
        tag = this.getClass().getSimpleName();
    }

    /**
     * 关闭一个对话框
     */
    public void dismissDialog() {
        if (mDialog != null && mDialog.isShowing()){
            mDialog.dismiss();
            mDialog = null;
        }
    }

    /**
     * 显示一个对话框
     * @param msg   对话框内容
     */
    public void showDialog(CharSequence msg) {
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
     * 从AccountManager中获取账户邮箱列表,用于自动填充
     * @return 邮箱集合 Set<String>
     */
    public Set<String> readEmailFromAccountManager() {
        // 获取当前手机设备中的邮箱类账账号
        Set<String> emails = new HashSet<String>();
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
        return emails;
        // 现在邮箱集合中的记录是不重复的邮箱地址了
    }

    /**
     * 显示一条toast信息
     * @param msg   toast内容
     * @param e     异常
     * @param b     是否要显示这条信息
     */
    public void showMessage(CharSequence msg, Exception e, boolean b) {
        Log.i(tag, msg.toString());
        if(b){
            Toast.makeText(
                    getActivity(),
                    msg,
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}

