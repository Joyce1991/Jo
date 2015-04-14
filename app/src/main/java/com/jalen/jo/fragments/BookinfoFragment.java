package com.jalen.jo.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jalen.jo.R;
import com.jalen.jo.beans.Book;
import com.jalen.jo.beans.ErrorDouban;
import com.jalen.jo.http.JoRestClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BookinfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BookinfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookinfoFragment extends Fragment {

    public static final String EXTRA_BOOK_ISBN = "com.jalen.jo.fragments.BookinfoFragment.BookISBN";

    private AlertDialog mDialog;
    private ImageView ivBookPic;    // 图书图片
    private TextView tvBookTitle;   // 图书标题
    private TextView tvBookSubtitle;// 图书子标题
    private TextView tvBookAuthor;  // 图书作者
    private TextView tvBookTranslator;  // 译者
    private TextView tvBookPages;   // 页数
    private TextView tvBookBrief;   // 图书简介
    private TextView tvBookCatalog; // 图书目录
    private TextView tvBookAuthorinfo;  // 图书作者信息

    private ImageButton ibBookBrief;        // 图书简介expand按钮
    private ImageButton ibBookCatalog;      // 图书目录expand按钮
    private ImageButton ibBookAuthorinfo;   // 图书作者信息expand按钮

    // TODO: Rename and change types of parameters
    private String mParamISBN;
    private Book mBook;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * <br/>
     * 使用这个工厂方法来创建一个新的BookinfoFragment对象
     * @param param1 参数1.
     * @return 一个新的 BookinfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BookinfoFragment newInstance(String param1) {
        BookinfoFragment fragment = new BookinfoFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_BOOK_ISBN, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public BookinfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParamISBN = getArguments().getString(EXTRA_BOOK_ISBN);
        }

        // 获取图书信息
        showDialog(getText(R.string.dialog_loading_query));
        JsonHttpResponseHandler responseHandler = new BookinfoJsonHttpResponseHandler();
        JoRestClient.getFromDouban(mParamISBN, null, responseHandler);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_book_info, container, false);

        ivBookPic = (ImageView) rootView.findViewById(R.id.iv_book_info_pic);
        tvBookTitle = (TextView) rootView.findViewById(R.id.tv_book_info_title);
        tvBookSubtitle = (TextView) rootView.findViewById(R.id.tv_book_info_title_subtitle);
        tvBookAuthor = (TextView) rootView.findViewById(R.id.tv_book_info_author);
        tvBookTranslator = (TextView) rootView.findViewById(R.id.tv_book_info_translator);
        tvBookPages = (TextView) rootView.findViewById(R.id.tv_book_info_pages);
        tvBookBrief = (TextView) rootView.findViewById(R.id.tv_book_info_brief);
        tvBookCatalog = (TextView) rootView.findViewById(R.id.tv_book_info_catalog);
        tvBookAuthorinfo = (TextView) rootView.findViewById(R.id.tv_book_info_author_info);

        ibBookBrief = (ImageButton) rootView.findViewById(R.id.ib_book_info_brief);
        ibBookCatalog = (ImageButton) rootView.findViewById(R.id.ib_book_info_catalog);
        ibBookAuthorinfo = (ImageButton) rootView.findViewById(R.id.ib_book_info_author_info);

        return rootView;
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
        public void onFragmentInteraction(int id);
    }

    /**
     * 关闭一个对话框
     */
    private void dismissDialog() {
        if (mDialog != null && mDialog.isShowing()){
            mDialog.dismiss();
            mDialog = null;
        }
    }

    /**
     * 显示一个对话框
     * @param msg   对话框内容
     */
    private void showDialog(CharSequence msg) {
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
     * 获取图书信息响应结果处理
     */
    private class BookinfoJsonHttpResponseHandler extends JsonHttpResponseHandler {

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);

            switch (statusCode){
                case 200:
                    mBook = JSON.parseObject(response.toString(), Book.class);

                    ImageLoader.getInstance().displayImage(mBook.getImage(), ivBookPic);
                    tvBookAuthor.setText(mBook.getAuthor().get(0));
                    tvBookTitle.setText(mBook.getTitle());
                    tvBookSubtitle.setText(mBook.getSubtitle());
                    tvBookTranslator.setText(mBook.getTranslator().size() == 0 ? "" : mBook.getTranslator().get(0));
                    tvBookPages.setText(mBook.getPages());
                    tvBookBrief.setText(mBook.getSummary());
                    tvBookCatalog.setText(mBook.getCatalog());
                    tvBookAuthorinfo.setText(mBook.getAuthor_intro());
                    break;
            }

            dismissDialog();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            ErrorDouban errorDouban = JSON.parseObject(errorResponse.toString(), ErrorDouban.class);
            switch (statusCode){
                case 404:
                    // not found
                    // 告诉activity没有找到书籍信息，让它替换fragment（booknotfoundfragment）
                    mListener.onFragmentInteraction(R.id.book_not_found);
                    break;
            }
            dismissDialog();
        }
    }
}
