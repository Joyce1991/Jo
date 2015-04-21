package com.jalen.jo.library;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.jalen.jo.R;
import com.jalen.jo.fragments.BaseFragment;
import com.jalen.jo.utils.MatrixWriter;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link LibraryCreateSuccessFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LibraryCreateSuccessFragment extends BaseFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM_LIBRARY = "library_object_id";
    private static final String PREFIX_LIBRARY_QRCODE = "library:";

    // TODO: Rename and change types of parameters
    private String mLibraryObjectId;
    private String mLibraryQRCodeContents;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param libraryObjectId Parameter 1.
     * @return A new instance of fragment LibraryCreateSuccessFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LibraryCreateSuccessFragment newInstance(String libraryObjectId) {
        LibraryCreateSuccessFragment fragment = new LibraryCreateSuccessFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_LIBRARY, libraryObjectId);
        fragment.setArguments(args);
        return fragment;
    }

    public LibraryCreateSuccessFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mLibraryObjectId = getArguments().getString(ARG_PARAM_LIBRARY);
        }

        // 根据mLibraryObjectId+“library”生成二维码
        mLibraryQRCodeContents = PREFIX_LIBRARY_QRCODE + mLibraryObjectId;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_library_create_success, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_share:


                break;
            case R.id.action_print:

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_library_create_success, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView ivLibraryQRCode = (ImageView) view.findViewById(R.id.library_qrcode);
        Button btnComplete = (Button) view.findViewById(R.id.btn_complete);

        ivLibraryQRCode.setImageBitmap(generateQRCodeBitmap(mLibraryQRCodeContents));
        btnComplete.setOnClickListener(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_complete:
                getActivity().finish();
                break;
        }
    }

    /**
     * 生成QR_CODE二维码
     * @param contents  二维码内容
     * @return  {@link Bitmap}
     */
    private Bitmap generateQRCodeBitmap(String contents){
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        BarcodeFormat format = BarcodeFormat.QR_CODE;
        int width = 400;
        int height = 400;
        Map hints = new HashMap();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try {
            BitMatrix mBitMatrix = multiFormatWriter.encode(mLibraryQRCodeContents, format, width, height, hints);
            Bitmap bitmap = MatrixWriter.toBitmap(mBitMatrix);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }
}
