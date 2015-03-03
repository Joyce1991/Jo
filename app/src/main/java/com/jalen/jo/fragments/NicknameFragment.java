package com.jalen.jo.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.jalen.jo.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NicknameFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NicknameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NicknameFragment extends Fragment implements View.OnClickListener {
    //        M
    private String mNickname;
    //        V
    private EditText etNickname;
    private Button btnSave;

    public NicknameFragment() {
        super();
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
        switch (v.getId()) {
            case R.id.btn_account_edit_save:

                break;
        }
    }
}
