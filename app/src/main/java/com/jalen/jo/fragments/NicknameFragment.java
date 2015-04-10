package com.jalen.jo.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.jalen.jo.R;
import com.jalen.jo.views.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 *
 * Use the {@link NicknameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NicknameFragment extends BaseFragment implements View.OnClickListener {
    //        M
    private String mNickname;
    //        V
    private EditText etNickname;
    private CircleImageView ivUsericon;
    private Button btnSave;

    public NicknameFragment() {
        super();
    }

    public static NicknameFragment newInstance(){
        NicknameFragment fragment = new NicknameFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_account_nickname_edit, container, false);


        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etNickname = (EditText) view.findViewById(R.id.et_account_edit_nickname);
        ivUsericon = (CircleImageView) view.findViewById(R.id.civ_account_edit_usericon);
        btnSave = (Button) view.findViewById(R.id.btn_account_edit_save);

        btnSave.setOnClickListener(this);
        ivUsericon.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_account_edit_save:
//                弹出对话框，用户选择头像
                showDialog4SelectPic();
                break;
            case R.id.civ_account_edit_usericon:

                break;
        }
    }

    private void showDialog4SelectPic() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        mBuilder.setTitle(R.string.dialog_title_pick)
                .setItems(R.array.pick_type, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
    }
}
