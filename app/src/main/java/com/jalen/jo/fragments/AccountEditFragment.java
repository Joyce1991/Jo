package com.jalen.jo.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.jalen.jo.R;

/**
 * 账户资料编辑
 * Created by jh on 2015/3/3.
 */
public class AccountEditFragment extends Fragment implements View.OnClickListener {
    //        M
    private String mNickname;
    //        V
    private EditText etNickname;
    private Button btnSave;

    public AccountEditFragment() {
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
