package com.jalen.jo.library;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.jalen.jo.R;
import com.jalen.jo.fragments.BaseFragment;
import com.jalen.jo.scan.CaptureActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;


/**
 * 图书馆列表显示Fragment
 *
 * 自动分页加载 http://www.jianshu.com/p/4feb0c16d1b5
 */
public class LibraryListFragment extends BaseFragment implements IViewHolderClicksListener {
    private static final int PAGE_MAX = 5;  // 分页查询 --> 也大小

    private int page = 0;   // 分页查询 --> 页号码
    private boolean hasMore = true; // 标识是否还有更多数据
    private List<JoLibrary> mLibraries;

    private CardView mEmptyView;
    private RecyclerView mRecyclerView;
    private PtrClassicFrameLayout mPtrFrame;
    private LibraryAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

//    private OnFragmentInteractionListener mListener;

    public static LibraryListFragment newInstance(CharSequence mTitle, int mIndicatorColor, int mDividerColor) {
        LibraryListFragment fragment = new LibraryListFragment();

        return fragment;
    }

    public LibraryListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        // 设置actionbar标题
        setActionBarTitle(R.string.title_fragment_library_list);
        // 初始化图书馆列表
        mLibraries = new ArrayList<JoLibrary>();
        // 查询图书馆列表
        onRefresh();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_library_list, container, false);

        /**Empty View 设置**/
        mEmptyView = (CardView) rootView.findViewById(R.id.empty_view);
        ImageView mCardNew = (ImageView) rootView.findViewById(R.id.emptycardview_new);
        mCardNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLibraryCreate();
            }
        });

        /**RecyclerView 设置**/
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_library_join);
        // RecyclerView设置LayoutManager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // RecyclerView设置ItemDecoration
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.divider_recyclerview)));
        // RecyclerView设置Adapter
        mAdapter = new LibraryAdapter(R.layout.adapter_library_item_style_1, mLibraries);
        mAdapter.setItenClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = (mLayoutManager).findLastVisibleItemPosition();
                int totalItemCount = mLayoutManager.getItemCount();
                //lastVisibleItem >= totalItemCount - 1 表示剩下4个item自动加载，各位自由选择s
                // dy>0 表示向下滑动
                if (lastVisibleItem >= totalItemCount - 1 && dy > 0) {
                    /*if(isLoadingMore){
                        Log.d(TAG,"ignore manually update!");
                    } else{
                        loadPage();//这里多线程也要手动控制isLoadingMore
                        isLoadingMore = false;
                    }*/
                    if (hasMore){
                        onLoadMore();
                    }
                }
            }
        });


        /**PtrFrameLayout设置**/
        mPtrFrame = (PtrClassicFrameLayout) rootView.findViewById(R.id.library_user_pullrefresh);
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                onRefresh();
            }
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                // here check $mListView instead of $content
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mRecyclerView, header);
            }

        });

        /**初始化视图可见性**/
        mPtrFrame.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);

        return rootView;
    }

    /**
     * 下拉刷新数据
     */
    private void onRefresh() {
        page = 0;
        hasMore = true;
        if (AVUser.getCurrentUser() != null){
            showDialog(getText(R.string.dialog_loading_query));
            AVQuery<AVObject> query = new AVQuery<AVObject>("Library");
            query.setLimit(PAGE_MAX);
            query.setSkip(page*PAGE_MAX);
            // 根据score字段升序显示数据
            query.orderByAscending("updateAt");
            query.addAscendingOrder("updateAt");
            // 根据score字段降序显示数据
//            query.orderByDescending("updateAt");
            query.whereEqualTo("libraryManager", AVUser.getCurrentUser().getUsername());
            query.findInBackground(new FindCallback<AVObject>() {
                public void done(List<AVObject> avObjects, AVException e) {
                    if (e == null) {
                        page++;
                        // 1.清空数据源
                        mLibraries.clear();
                        // 2.获取最新的数据源
                        for (AVObject avObject : avObjects){
                            JoLibrary mLibrary = new JoLibrary();
                            mLibrary.setObjectId(avObject.getObjectId());
                            mLibrary.setCounts(avObject.getString("counts"));
                            mLibrary.setLibraryName(avObject.getString("libraryName"));
                            mLibrary.setLibraryBrief(avObject.getString("libraryBrief"));
                            mLibrary.setLibraryManager(avObject.getString("libraryManager"));
                            mLibrary.setLibraryPic(avObject.getString("libraryPic"));
                            mLibrary.setLibraryType(avObject.getString("libraryType"));
                            mLibraries.add(mLibrary);
                        }
                        showMessage(getText(R.string.toast_update_success), null, true);
                        // 3.绑定最新的数据源
                        mAdapter = new LibraryAdapter(R.layout.adapter_library_item_style_1, mLibraries);
                        mRecyclerView.setAdapter(mAdapter);
                        onLoad();
                    } else {
                        showMessage(getText(R.string.toast_update_failed), e, true);
                    }
                    // 关闭对话框
                    dismissDialog();
                }
            });
        }

    }

    private void onLoad() {
        if(mLibraries.size() == 0){
            mEmptyView.setVisibility(View.VISIBLE);
            mPtrFrame.setVisibility(View.INVISIBLE);
        }else {
            mEmptyView.setVisibility(View.GONE);
            mPtrFrame.setVisibility(View.VISIBLE);
            mPtrFrame.refreshComplete();
        }
    }

    /**
     * 加载更多
     */
    private void onLoadMore(){
        if (AVUser.getCurrentUser() != null && hasMore){
            showDialog(getText(R.string.dialog_loading_query));
            AVQuery<AVObject> query = new AVQuery<AVObject>("Library");
            query.setLimit(PAGE_MAX);
            query.setSkip(page*PAGE_MAX);
            // 根据score字段升序显示数据
            query.orderByAscending("updateAt");
            query.addAscendingOrder("updateAt");
            // 根据score字段降序显示数据
//            query.orderByDescending("updateAt");
            query.whereEqualTo("libraryManager", AVUser.getCurrentUser().getUsername());
            query.findInBackground(new FindCallback<AVObject>() {
                public void done(List<AVObject> avObjects, AVException e) {
                    if (e == null) {
                        page++;
                        int preSize = mLibraries.size();    // 之前数据源中数据量
                        if (avObjects.size() > 0) {
                            // 1.获取更多的数据源
                            for (AVObject avObject : avObjects) {
                                JoLibrary mLibrary = new JoLibrary();
                                mLibrary.setObjectId(avObject.getObjectId());
                                mLibrary.setCounts(avObject.getString("counts"));
                                mLibrary.setLibraryName(avObject.getString("libraryName"));
                                mLibrary.setLibraryBrief(avObject.getString("libraryBrief"));
                                mLibrary.setLibraryManager(avObject.getString("libraryManager"));
                                mLibrary.setLibraryPic(avObject.getString("libraryPic"));
                                mLibrary.setLibraryType(avObject.getString("libraryType"));
                                mLibraries.add(mLibrary);
                                onLoad();
                            }
                            showMessage(getText(R.string.toast_update_success), null, true);
                            // 2.调用RecyclerView.Adapter的notify*()方法通知RecyclerView更新UI
                            mAdapter.notifyItemRangeInserted(mLibraries.size(), mLibraries.size() - preSize);
                        }else{
                            hasMore = false;
                        }
                    } else {
                        showMessage(getText(R.string.toast_update_failed), e, true);
                    }
                    // 关闭对话框
                    dismissDialog();
                }
            });
        }

    }


    /**
     * 启动图书馆创建流程
     */
    private void startLibraryCreate() {
        // 启动创建图书馆页面
        showMessage(getText(R.string.onclick_library_create), null, true);
        Intent intentLibraryCreate = new Intent(getActivity(), LibraryCreateActivity.class);
        startActivity(intentLibraryCreate);
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
                startLibraryCreate();
                return true;
            case R.id.action_scan:
                Intent captureIntent = new Intent(getActivity(), CaptureActivity.class);
                startActivity(captureIntent);
                return true;
            case R.id.action_search:
                showMessage("点击了搜索页", null, true);
                return true;
            case R.id.action_settings:
                showMessage("点击了设置", null, true);
                return true;
            case R.id.action_display_style:
                showMessage("点击了样式", null, true);
                return true;
            case R.id.action_order_style:
                showMessage("排序样式", null, true);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {
        ImageLoader.getInstance().stop();
        super.onStop();
    }



    @Override
    public void onItemClicked(View caller, int position) {
        switch (caller.getId()){
            case R.id.iv_overflow:
                Log.i(tag, "点击了Item的overflow, 位置position为：" + position);
                showMessage("点击了Item的overflow, 位置position为：" + position, null, true);
                break;
            case R.id.library_pic:
                Log.i(tag, "点击了Item的ImageView, 位置position为：" + position);
                showMessage("点击了Item的ImageView, 位置position为：" + position, null, true);
                break;
            default:
                Log.i(tag, "点击了Item, 位置position为：" + position);
                showMessage("点击了Item, 位置position为：" + position, null, true);
                // 获取position位置data
                JoLibrary itemData = mLibraries.get(position);
                startLibraryActivity(itemData, R.id.fragment_library_bookdisplay);
                break;
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    /**
     * 启动LibraryActivity
     * @param itemData  图书馆ID
     * @param fragmentId    用来处理该事件的fragment
     */
    private void startLibraryActivity(JoLibrary itemData, int fragmentId) {
        Intent intentLibrary = new Intent(getActivity(), LibraryActivity.class);
        intentLibrary.putExtra(LibraryActivity.EXTRA_FRAGMENT_ID, fragmentId);
        intentLibrary.putExtra(LibraryActivity.EXTRA_LIBRARY_ID, itemData.getObjectId());
        startActivity(intentLibrary);
    }
}
