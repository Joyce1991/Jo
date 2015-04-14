package com.jalen.jo.library;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.jalen.jo.R;
import com.jalen.jo.fragments.BaseFragment;
import com.jalen.jo.http.JoRestClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * 图书馆创建界面
 */
public class LibraryCreateFragment extends BaseFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // 请求码
    private static final int REQUEST_CODE_PICK_IMAGE = 0x0001;
    private static final int REQUEST_CODE_CAPTURE_CAMERA = 0x0002;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ImageView ivLibraryPic; // 图书馆图片
    ProgressBar pbProgress; // 上传进度

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LibraryCreateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LibraryCreateFragment newInstance(String param1, String param2) {
        LibraryCreateFragment fragment = new LibraryCreateFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public LibraryCreateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_library_create, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageButton btnPicPick = (ImageButton) view.findViewById(R.id.btn_pic_pick);
        Spinner mSpinner = (Spinner) view.findViewById(R.id.library_type_pick);
        ivLibraryPic = (ImageView) view.findViewById(R.id.library_pic);
        pbProgress = (ProgressBar) view.findViewById(R.id.progress_pic);

        btnPicPick.setOnClickListener(this);
        SpinnerAdapter mSpinnerAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.spinner_items_library_stype));
        mSpinner.setAdapter(mSpinnerAdapter);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_pic_pick:
                showSelectDialog(R.string.dialog_title_library_pic, R.array.dialog_items_library_pic);
                break;

        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CODE_PICK_IMAGE:
                if (resultCode == Activity.RESULT_OK){
                    //   判断data是否为空，data中的是否有数据
                    if (data != null && data.getData() != null){
                        Uri uri = data.getData();

                        Cursor cursor = getActivity().getContentResolver().query(uri,
                                new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
                        cursor.moveToFirst();

                        final String imageFilePath = cursor.getString(0);
                        cursor.close();
//                    ImageLoader.getInstance().displayImage(imageFilePath, ivLibraryPic);
//                        上传这张图片到服务器
                        uploadImage(uri, JoRestClient.FILE_URL);
                        ivLibraryPic.setImageURI(uri);
                    }
                }

                break;
            case REQUEST_CODE_CAPTURE_CAMERA:
                if (resultCode == Activity.RESULT_OK){
                    //  判断data是否为空，data中的是否有数据
                    if (data != null && data.getData() != null){
                        Uri uri = data.getData();
                        ivLibraryPic.setImageURI(uri);
                    }
                }

                break;
            default:

                break;
        }
    }

    /**
     * 上传指定图片到服务器
     * @param uri 图片路径
     * @param url 请求的相对路径
     * @return 该图片在服务器端的路径
     */
    private String uploadImage(Uri uri, String url) {
        // 根据文件的uri的到文件路径
        Cursor cursor = getActivity().getContentResolver().query(uri,
                new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
        cursor.moveToFirst();
        String imageFilePath = cursor.getString(0);
        // 把这个文件作为参数放到请求参数中
        File myFile = new File(imageFilePath);
        RequestParams params = new RequestParams();
        try {
            params.put("library_picture", myFile);
        } catch(FileNotFoundException e) {
            Log.i(tag, "has no find this file");
        }
        AsyncHttpResponseHandler mHandler = new FileUploadHttpResponseHandler();

        JoRestClient.post(url+myFile.getName(), params, mHandler);
        return null;
    }

    /**
     * 显示一个对话框
     * @param titleId 对话框标题资源id
     * @param itemsId  对话框items资源id
     */
    private void showSelectDialog(int titleId, int itemsId){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        mBuilder.setTitle(titleId).setItems(itemsId, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
//                        从图库选择图片
                        getImageFromAlbum();

                        break;
                    case 1:
//                        拍摄图片
                        getImageFromCamera();
                        break;
                }
            }
        });
        mBuilder.create().show();
    }

    /**
     * 从图库选择图片
     */
    private void getImageFromAlbum() {
//        Intent intent = new Intent(Intent.ACTION_PICK);
        Intent intent = new Intent();
//        判断运行系统的版本号是否达到4.4级别
        if(Build.VERSION.SDK_INT >= 4.4){
            // 通过SAF框架进行访问
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/jpeg");//相片类型
        }else{
//            防止进入了一些如es文件管理器等第三方应用
            intent.setAction(Intent.ACTION_PICK);
            intent.setType("image/jpeg");
        }

        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    /**
     * 拍摄图片
     */
    private void getImageFromCamera() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivityForResult(getImageByCamera, REQUEST_CODE_CAPTURE_CAMERA);
        }
        else {
            Toast.makeText(getActivity(), R.string.toast_sdcard_unmount, Toast.LENGTH_LONG).show();
        }
    }

    private class FileUploadHttpResponseHandler extends AsyncHttpResponseHandler {

        @Override
        public void onStart() {
            super.onStart();
//            显示进度条
            pbProgress.setVisibility(View.VISIBLE);
            pbProgress.setProgress(0);
        }

        @Override
        public void onFinish() {
            super.onFinish();
//            关闭对进度条
            pbProgress.setVisibility(View.GONE);
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            Log.i(tag, "success, status code is : " + statusCode);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            Log.i(tag, "failure, status code is : " + statusCode);
        }

        @Override
        public void onProgress(int bytesWritten, int totalSize) {
            super.onProgress(bytesWritten, totalSize);
            pbProgress.setProgress(bytesWritten*100/totalSize);
        }
    }
}
