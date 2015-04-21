package com.jalen.jo.library;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.AVCloudQueryResult;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.CloudQueryCallback;
import com.avos.avoscloud.FindCallback;
import com.jalen.jo.R;
import com.jalen.jo.beans.Book;
import com.jalen.jo.fragments.BaseFragment;

import java.util.List;

/**
 * 图书馆信息界面（包含书的信息）
 */
public class LibraryBookDisplayFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final int COUNTER_MAX = 2;

    private String mLibraryObjectId;
    private int counter = 0;   // 计数器->记录已经加载好了几个数据
    // 数据模型
    private JoLibrary mLibrary;
    private List<Book> mBooks;

    private OnFragmentInteractionListener mListener;

    // TODO: Rename and change types and number of parameters
    public static LibraryBookDisplayFragment newInstance(String param1) {
        LibraryBookDisplayFragment fragment = new LibraryBookDisplayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public LibraryBookDisplayFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLibraryObjectId = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_library_bookdisplay, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 初始化视图


        if (AVUser.getCurrentUser() != null){
            showDialog(getText(R.string.dialog_loading_query));
            counter = 0;
            // 1.根据图书馆ObjectId获取图书馆信息
            AVQuery<AVObject> queryLibrary = new AVQuery<AVObject>("Library");
            queryLibrary.whereEqualTo("objectId", mLibraryObjectId);
            queryLibrary.findInBackground(new FindCallback<AVObject>() {
                public void done(List<AVObject> avObjects, AVException e) {
                    counter++;
                    if (e == null) {
                        // 解析出图书馆对象
                        AVObject avObject = avObjects.get(0);
                        String jsonString = avObject.toJSONObject().toString();
                        mLibrary = JSON.parseObject(jsonString, JoLibrary.class);
                        // 根据图书馆书架显示UI

                    } else {
                        showMessage(getText(R.string.toast_update_failed), e, true);
                    }
                    updateUI();
                }
            });
            // 2.根据图书馆ObjectId获取与该图书馆收藏的图书
            // select * from Book where objectId = (select bookObjectId from LibraryCollectionTable libraryObjectId = ?)
            AVQuery.doCloudQueryInBackground("select bookObjectId from LibraryCollectionTable libraryObjectId = ?",
                    new CloudQueryCallback<AVCloudQueryResult>() {

                        @Override
                        public void done(AVCloudQueryResult result, AVException parseException) {
                            counter++;
                            if (parseException == null) {
                                List avObjects = result.getResults();
                            }else {
                                showMessage(getText(R.string.toast_update_failed), parseException, true);
                            }
                        }

                    }, mLibraryObjectId);
/*
            AVQuery<AVObject> innerQuery = new AVQuery<AVObject>("LibraryCollectionTable");
            innerQuery.whereEqualTo("libraryObjectId", mLibraryObjectId);
            AVQuery<AVObject> bookQuery = new AVQuery<AVObject>("Book");
            bookQuery.whereMatchesQuery("objectId", innerQuery);
            bookQuery.findInBackground(new FindCallback<AVObject>() {
                public void done(List<AVObject> avObjects, AVException e) {
                    counter++;
                    if (e == null) {
                        if (avObjects.size() > 0){
                            // 解析出图书对象集合
                            AVObject avObject = avObjects.get(0);
                            String jsonString = avObject.toJSONObject().toString();
                            Book mBook = JSON.parseObject(jsonString, Book.class);
                        }

                        // 根据图书馆书架显示UI

                    } else {
                        showMessage(getText(R.string.toast_update_failed), e, true);
                    }
                    updateUI();
                }
            });
*/
        }


    }

    /**
     * 更新UI
     */
    private void updateUI() {
        if (counter == 2){
            // 关闭对话框
            dismissDialog();
        }
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_library_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_new:
                // 创建新的图书馆

                break;
            case R.id.action_search:
                // 搜索有哪些图书馆

                break;
            case R.id.action_scan:
                // 扫一扫

                break;
            case R.id.action_settings:
                // 设置

                break;
            case R.id.action_display_style:
                // 显示风格样式选择

                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
