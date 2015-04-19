package com.jalen.jo.library;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.ProgressCallback;
import com.avos.avoscloud.SaveCallback;
import com.jalen.jo.R;
import com.jalen.jo.activities.SigninActivity;
import com.jalen.jo.cropimage.CropImageIntentBuilder;
import com.jalen.jo.fragments.BaseFragment;
import com.jalen.jo.http.JoRestClient;
import com.jalen.jo.utils.Uriutil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 图书馆创建界面
 */
public class LibraryCreateFragment extends BaseFragment implements View.OnClickListener {
    // 请求码
    private static final int REQUEST_CODE_PICK_IMAGE = 0x0001;
    private static final int REQUEST_CODE_CAPTURE_CAMERA = 0x0002;
    private static final int REQUEST_CROP_PICTURE = 0x0003;

    private String mLibraryPic; // 图书馆照片url
    private DisplayImageOptions mDisplayImageOptions;

    private ImageView ivLibraryPic; // 图书馆图片
    private ProgressBar pbProgress; // 上传进度
    private Spinner mSpinner;   // 图书馆类型
    private EditText edtLibraryName;    // 图书馆名称
    private EditText edtLibraryBrief;   //图书馆简介


    private OnFragmentInteractionListener mListener;

    public LibraryCreateFragment() {
        // Required empty public constructor
    }

    public static LibraryCreateFragment newInstance(String param1, String param2) {
        LibraryCreateFragment fragment = new LibraryCreateFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
//        imageloader显示配置
        DisplayImageOptions.Builder mBuilder = new  DisplayImageOptions.Builder();
        mBuilder.cacheInMemory(true);
        mDisplayImageOptions = mBuilder.build();
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
        mSpinner = (Spinner) view.findViewById(R.id.library_type_pick);
        ivLibraryPic = (ImageView) view.findViewById(R.id.library_pic);
        pbProgress = (ProgressBar) view.findViewById(R.id.progress_pic);
        edtLibraryName = (EditText) view.findViewById(R.id.edt_library_name);
        edtLibraryBrief = (EditText) view.findViewById(R.id.edt_library_brief);

        btnPicPick.setOnClickListener(this);
        SpinnerAdapter mSpinnerAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.spinner_items_library_stype));
        mSpinner.setAdapter(mSpinnerAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_library_create, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:
                // 点击了保存按钮，试图创建图书馆
                Toast.makeText(getActivity(), "点击了保存按钮", Toast.LENGTH_SHORT).show();
                createLibrary();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createLibrary() {
        AVUser currentUser = AVUser.getCurrentUser();
        if (currentUser != null) {
            // 允许用户使用应用
            // 图书馆名称不能为空
            String mLibraryName = edtLibraryName.getText().toString().trim();
            if (mLibraryName.isEmpty() || mLibraryName == null){
                Toast.makeText(getActivity(), R.string.toast_library_name_empty, Toast.LENGTH_SHORT).show();
            }
            String mLibraryBrief = edtLibraryBrief.getText().toString().trim();

            final AVObject mLibrary = new AVObject("Library");
            mLibrary.put("libraryName", mLibraryName);
            mLibrary.put("libraryPic", mLibraryPic);
            if (!mLibraryBrief.isEmpty() && mLibraryBrief != null){
                mLibrary.put("libraryBrief", mLibraryBrief);
            }
            mLibrary.put("libraryType", mSpinner.getSelectedItem());


            mLibrary.put("libraryManager", AVUser.getCurrentUser().getUsername());
            showDialog(getResources().getString(R.string.dialog_loading_create));
            mLibrary.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    dismissDialog();
                    if (e != null){
                        // 请求失败
                        Toast.makeText(getActivity(), R.string.toast_library_create_failed, Toast.LENGTH_SHORT).show();
                        Log.i(tag, e.getMessage());
                    }else {
                        // 请求成功
                        Toast.makeText(getActivity(), R.string.toast_library_create_successed, Toast.LENGTH_SHORT).show();
                        Message msg = Message.obtain();
                        msg.what = R.id.fragment_library_create_success;
                        msg.obj = mLibrary.getObjectId();
                        onButtonPressed(msg);
                    }
                }
            });
        } else {
            //缓存用户对象为空时， 可打开用户注册界面…
            Toast.makeText(getActivity(), getResources().getText(R.string.toast_no_sign_in), Toast.LENGTH_SHORT).show();
            Intent intentSignin = new Intent(getActivity(), SigninActivity.class);
            startActivity(intentSignin);
        }

    }

    /**
     * 创建一个图书馆id，具有唯一性
     * @return
     */
    private String createLibraryId() {
        return null;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Message msg) {
        if (mListener != null) {
            mListener.onFragmentInteraction(msg);
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
        public void onFragmentInteraction(Message msg);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        File outFile = new File(getActivity().getFilesDir(), "library.jpg");
        switch (requestCode){
            case REQUEST_CODE_PICK_IMAGE:
                //
                if (resultCode == Activity.RESULT_OK){
                    //   判断data是否为空，data中的是否有数据
                    if (data != null && data.getData() != null){

                        Uri outputUri = Uri.fromFile(outFile);
                        // 构建图片裁剪Intent
                        Uri inputUri = data.getData();
                        CropImageIntentBuilder mIntentBuilder = new CropImageIntentBuilder(3, 2, 720, 480, outputUri);
                        mIntentBuilder.setOutlineColor(getResources().getColor(R.color.white)); // 设置边框颜色
                        mIntentBuilder.setSourceImage(inputUri);    // 设置待裁剪的图片源
                        // 去到裁剪页面
                        startActivityForResult(mIntentBuilder.getIntent(getActivity()), REQUEST_CROP_PICTURE);

/*//                    ImageLoader.getInstance().displayImage(imageFilePath, ivLibraryPic);
//                        上传这张图片到服务器
                        uploadImage(uri, JoRestClient.FILE_URL);
                        // 图片太大显示不了
//                        ivLibraryPic.setImageURI(uri);
                        ImageLoader.getInstance().displayImage(uri.toString(), ivLibraryPic);*/
                    }
                }

                break;
            case REQUEST_CROP_PICTURE:
                if (resultCode == Activity.RESULT_OK){
                    if (data != null && data.getData() != null){
                        Uri uri = data.getData();
                        // 上传这张图片
                        uploadImage(uri, JoRestClient.FILE_URL);
                        ImageLoader.getInstance().displayImage(uri.toString(), ivLibraryPic);

                    }
                }
                break;
            case REQUEST_CODE_CAPTURE_CAMERA:
                if (resultCode == Activity.RESULT_OK){
                    //  判断data是否为空，data中的是否有数据
                    if (data != null){
                        Uri uri = data.getData();
                        ImageLoader.getInstance().displayImage(uri.toString(), ivLibraryPic);
                    }
                }

                break;
            default:

                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 上传指定图片到服务器
     * @param uri 图片路径
     * @param url 请求的相对路径
     * @return 该图片在服务器端的路径
     */
    private String uploadImage(Uri uri, String url) {
        final String filePath = Uriutil.getPath(getActivity(), uri);
        File myFile = new File(filePath);
        // 方法一：leancloud reset API
/*
        // 把这个文件作为参数放到请求参数中

        RequestParams params = new RequestParams();
        try {
            params.put("library_picture", myFile, "multipart/form-data");
        } catch(FileNotFoundException e) {
            Log.i(tag, "has no find this file");
        }
        AsyncHttpResponseHandler mHandler = new FileUploadHttpResponseHandler();

//        JoRestClient.post(url + myFile.getName(), params, mHandler);
        JoRestClient.post(url, params, mHandler);
*/

        // 方法二：leancloud SDK
        AVFile file = null;
        try {
            file = AVFile.withAbsoluteLocalPath("library.jpg", filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (file != null) {
            final AVFile finalFile = file;
            file.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    Log.i(tag, "upload success, image's url is：" + finalFile.getUrl());
                    mLibraryPic = finalFile.getUrl();
                }

            }, new ProgressCallback() {
                @Override
                public void done(Integer integer) {
                    Log.i(tag, "progress integer is:" + integer);
                    pbProgress.setProgress(integer);
                }
            });
        }
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
                        selectImageFromGallery();

                        break;
                    case 1:
//                        拍摄图片
                        captureImageFromCamera();
                        break;
                }
            }
        });
        mBuilder.create().show();
    }

    /**
     * 从图库选择图片
     */
    private void selectImageFromGallery() {
        final Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, getString(R.string.dialog_image_chooser)), REQUEST_CODE_PICK_IMAGE);
    }

    /**
     * 拍摄图片
     */
    private void captureImageFromCamera() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivityForResult(getImageByCamera, REQUEST_CODE_CAPTURE_CAMERA);
        }
        else {
            Toast.makeText(getActivity(), R.string.toast_sdcard_unmount, Toast.LENGTH_LONG).show();
        }
    }

/*
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
*/
}
