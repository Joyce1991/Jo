package com.jalen.jo.fragments;

import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jalen.jo.R;

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
}

