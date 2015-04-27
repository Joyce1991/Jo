package com.jalen.jo.library;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.jalen.jo.R;
import com.jalen.jo.book.Book;
import com.jalen.jo.book.BookEntry;
import com.jalen.jo.fragments.BaseFragment;
import com.jalen.jo.utils.DisplayUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 图书馆信息界面（包含书的信息）
 */
public class LibraryBookDisplayFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final int COUNTER_MAX = 2;
    private static final double BOOK_RATIO = 1.3;  // 书本的宽高比

    private String mLibraryObjectId;
    private int counter = 0;   // 计数器->记录已经加载好了几个数据
    private int bookWidth = 0;
    private int bookHeight = 0;
    private DisplayImageOptions options;

    // 数据模型
    private JoLibrary mLibrary;
    private List<Book> mBooks;

    // 视图层控件
    private RecyclerView mRecyclerView;

    private BookListAdapter mAdapter;

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
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mLibraryObjectId = getArguments().getString(ARG_PARAM1);
        }

        mBooks = new ArrayList<Book>();

        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        builder.cacheInMemory(true);    // 内存缓存
        builder.cacheOnDisk(true);      // 硬盘缓存
        builder.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2);    // 图片与原图相比缩小2倍
        builder.showImageForEmptyUri(R.drawable.pic_empty_uri);  // 暂无图片
        builder.showImageOnFail(R.drawable.pic_fail_uri);   // 图片下载失败
        builder.displayer(new FadeInBitmapDisplayer(100));
        builder.postProcessor(new BitmapProcessor() {
            @Override
            public Bitmap process(Bitmap bitmap) {
                /** 1.图片剪切 -> 获取中间正方形区域 **/
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                int targetWidth = width*6/8;
                int targetHeight = height*6/8;
                bitmap = Bitmap.createBitmap(bitmap, width/8, height/8, targetWidth, targetHeight);
                /** 2.图片缩放 **/
                return bitmap;
            }
        }); // 加载到内存后，显示到内存前
        options = builder.build();

        //
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point outSize = new Point();
        display.getSize(outSize);
        int width = outSize.x;
        bookWidth = (width - DisplayUtil.Dp2Px(getActivity(), 8)*4)/3;
        bookHeight = (int) ((double)bookWidth * BOOK_RATIO);
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
        /** RecyclerView设置 **/
        mRecyclerView = (RecyclerView) view.findViewById(R.id.book_list);
        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 3, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new BookItemDecoration(getActivity()));
        mAdapter = new BookListAdapter(mBooks);
        mRecyclerView.setAdapter(mAdapter);

        if (AVUser.getCurrentUser() != null) {
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
                    } else {
                        showMessage(getText(R.string.toast_update_failed), e, true);
                    }
                    // 根据图书馆书架显示UI
                    updateUI();
                }
            });
            // 2.根据图书馆ObjectId获取与该图书馆收藏的图书
            AVQuery<AVObject> innerQuery = new AVQuery<AVObject>("LibraryCollectionTable");
            innerQuery.whereEqualTo("libraryObjectId", mLibraryObjectId);
            Set<String> selectKeys = new HashSet<String>();
            selectKeys.add("bookObjectId");
            innerQuery.selectKeys(selectKeys);
            innerQuery.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> avObjects, AVException e) {
                    if (avObjects == null || avObjects.size() == 0){
                        return;
                    }
                    // 整理出一个图书对象objectId集合
                    Set<String> bookObjectIds = new HashSet<String>();
                    for (AVObject avObject : avObjects) {
                        bookObjectIds.add(avObject.getString("bookObjectId"));
                    }
                    // 根据图书对象objectId集合查询图书对象集合
                    AVQuery<AVObject> bookQuery = new AVQuery<AVObject>("Book");
                    bookQuery.whereContainedIn("objectId", bookObjectIds);
                    bookQuery.findInBackground(new FindCallback<AVObject>() {
                        public void done(List<AVObject> avObjects, AVException e) {
                            counter++;
                            if (e == null) {
                                if (avObjects.size() > 0) {
                                    // 解析出图书对象集合
                                    for (AVObject avObject : avObjects) {
                                        Book mBook = new Book();
                                        String authorJson = avObject.getString(BookEntry.AUTHOR);
                                        if (avObject.getString(BookEntry.AUTHOR) != null) {
                                            mBook.setAuthor(JSON.parseArray(avObject.getString(BookEntry.AUTHOR), String.class));
                                        }
                                        mBook.setAuthor_intro(avObject.getString(BookEntry.AUTHOR_INTRO));
                                        mBook.setCatalog(avObject.getString(BookEntry.CATALOG));
                                        mBook.setCreatedAt(avObject.getString(BookEntry.CREATE_AT));
                                        mBook.setImage(avObject.getString(BookEntry.IMAGE));
                                        Map<String, String> images = new HashMap<String, String>();
                                        org.json.JSONObject jsonObject = avObject.getJSONObject(BookEntry.IMAGES);

                                        JSONObject jsonObject1 = JSON.parseObject(jsonObject.toString());
                                        for (Map.Entry<String, Object> entry : jsonObject1.entrySet()) {
                                            images.put(entry.getKey(), (String) entry.getValue());
                                        }
                                        mBook.setImages(images);
                                        mBook.setIsbn13(avObject.getString(BookEntry.ISBN_13));
                                        mBook.setObjectId(avObject.getObjectId());
                                        mBook.setPages(avObject.getString(BookEntry.PAGES));
                                        mBook.setPubdate(avObject.getString(BookEntry.PUBLISH_DATE));
                                        mBook.setPublisher(avObject.getString(BookEntry.PUBLISHER));
                                        mBook.setTitle(avObject.getString(BookEntry.TITLE));
                                        if (avObject.getString(BookEntry.TRANSLATOR) != null) {
                                            mBook.setTranslator(JSON.parseArray(avObject.getString(BookEntry.TRANSLATOR), String.class));
                                        }
                                        mBook.setSubtitle(avObject.getString(BookEntry.SUBTITLE));

                                        mBooks.add(mBook);
                                    }
                                }

                                // 根据图书馆书架显示UI
                                showMessage(getText(R.string.toast_update_success), e, true);
                            } else {
                                showMessage(getText(R.string.toast_update_failed), e, true);
                            }
                            updateUI();
                        }
                    });
                }
            });

        }
    }

    /**
     * 更新UI
     */
    private void updateUI() {
        if (counter == 2) {
            // 关闭对话框
            dismissDialog();
            // 设置actionbar title
            if (mLibrary != null) {
                setActionBarTitle(mLibrary.getLibraryName());
                // 加载图书集合
                updateBookUI();
            }
        }
    }

    /**
     * 加载图书
     */
    private void updateBookUI() {
        if (mAdapter == null){
            mAdapter = new BookListAdapter(mBooks);
        }
        mAdapter.notifyDataSetChanged();
    }

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
    } @Override
      public void onStop() {
        ImageLoader.getInstance().stop();
        super.onStop();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_book_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_search:
                // 搜索有哪些图书馆

                return true;
            case R.id.action_scan:
                // 扫一扫

                return true;
            case R.id.action_display_style:
                // 显示风格样式选择

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class BookListAdapter extends RecyclerView.Adapter {
        private List<Book> data;

        public BookListAdapter(List<Book> mbooks) {
            this.data = mbooks;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_book_item_style_1, parent, false);

            BookViewHolder viewholder = new BookViewHolder(itemView, new BookViewHolder.IMyViewHolderClicks() {
                @Override
                public void onItemClicked(View caller, int position) {
                    Log.i(tag, "点击了Item, 位置position为：" + position);
                    showMessage("点击了Item, 位置position为：" + position, null, true);
                    // 获取position位置data
                    Book itemData = getData().get(position);
                    startBookActivity(itemData, R.id.fragment_library_bookdisplay);
                }

                @Override
                public void onOverflowClicked(ImageView callerImage, int position) {
                    Log.i(tag, "点击了Item的overflow, 位置position为：" + position);
                    showMessage("点击了Item的overflow, 位置position为：" + position, null, true);
                }
            });
            ViewGroup.LayoutParams params = viewholder.getBookPic().getLayoutParams();
            params.width = bookWidth;
            params.height = bookHeight;
            viewholder.getBookPic().setLayoutParams(params);

            return viewholder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            // 获取position位置的JoLibrary对象
            Book mBook = this.data.get(position);
            BookViewHolder viewHolder = (BookViewHolder) holder;

            viewHolder.getBookName().setText(mBook.getTitle());
            ImageLoader.getInstance().displayImage(mBook.getImage(), viewHolder.getBookPic(), options);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public List<Book> getData() {
            return data;
        }

    }

    private void startBookActivity(Book itemData, int fragment_library_bookdisplay) {

    }

    /**
     * 继承于{@link android.support.v7.widget.RecyclerView}
     */
    public static class BookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView overflow;
        TextView bookName;
        ImageView bookPic;


        public IMyViewHolderClicks mListener;

        public static interface IMyViewHolderClicks {
            public void onItemClicked(View caller, int position);

            public void onOverflowClicked(ImageView callerImage, int position);
        }

        public BookViewHolder(View itemView, IMyViewHolderClicks listener) {
            super(itemView);
            this.mListener = listener;
            bookName = (TextView) itemView.findViewById(R.id.book_title);
            bookPic = (ImageView) itemView.findViewById(R.id.book_pic);
            overflow = (ImageView) itemView.findViewById(R.id.btn_overflow);

            itemView.setOnClickListener(this);
            overflow.setOnClickListener(this);
        }

        public ImageView getOverflow() {
            return overflow;
        }

        public TextView getBookName() {
            return bookName;
        }

        public ImageView getBookPic() {
            return bookPic;
        }

        @Override
        public void onClick(View v) {
            if (v instanceof ImageButton) {
                mListener.onOverflowClicked((ImageView) v, getPosition());
            } else {
                mListener.onItemClicked(v, getPosition());
            }
        }
    }
}
