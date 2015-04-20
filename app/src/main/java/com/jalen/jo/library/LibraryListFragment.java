package com.jalen.jo.library;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.jalen.jo.R;
import com.jalen.jo.fragments.BaseFragment;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LibraryListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LibraryListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LibraryListFragment extends BaseFragment {
    private List<JoLibrary> libraries;

    private PtrClassicDefaultHeader mPtrClassicHeader;
    private CardView mEmptyView;
    private RecyclerView mRecyclerView;
    private PtrClassicFrameLayout mPtrFrame;
    private LibraryAdapter mAdapter;

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

        // 初始化图书馆列表
        libraries = new ArrayList<JoLibrary>();
        // 查询图书馆列表
        query();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_library_list, container, false);
        mEmptyView = (CardView) rootView.findViewById(R.id.empty_view);
        ImageView mCardNew = (ImageView) rootView.findViewById(R.id.emptycardview_new);
        mCardNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 启动创建图书馆页面
                showMessage(getText(R.string.onclick_library_create), null, true);
                Intent intentLibraryCreate = new Intent(getActivity(), LibraryCreateActivity.class);
                startActivity(intentLibraryCreate);
            }
        });

        // RecyclerView
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_library_join);
        // 给RecyclerView设置一个垂直的线性布局管理器
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new LibraryAdapter(libraries, R.layout.adapter_library_item_style_1);
        mRecyclerView.setAdapter(mAdapter);


        // PtrFrameLayout
        mPtrFrame = (PtrClassicFrameLayout) rootView.findViewById(R.id.library_user_pullrefresh);
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                updateData();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {

                // here check $mListView instead of $content
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mRecyclerView, header);
            }
        });

        return rootView;
    }

    /**
     * 更新数据
     * <br/>
     * 联网获取最新数据
     */
    protected void updateData() {
        displayData(libraries);
    }

    /**
     * 查询图书馆列表
     */
    private void query(){
        if (AVUser.getCurrentUser() != null){
            showDialog(getText(R.string.dialog_loading_query));
            AVQuery<AVObject> query = new AVQuery<AVObject>("Library");
            query.whereEqualTo("libraryManager", AVUser.getCurrentUser().getUsername());
            query.findInBackground(new FindCallback<AVObject>() {
                public void done(List<AVObject> avObjects, AVException e) {
                    if (e == null) {
                        // 先清空一下原来的数据
                        libraries.clear();
                        for (AVObject avObject : avObjects){
                            JoLibrary mLibrary = new JoLibrary();
                            mLibrary.setObjectId(avObject.getObjectId());
                            mLibrary.setCounts(avObject.getString("counts"));
                            mLibrary.setLibraryName(avObject.getString("libraryName"));
                            mLibrary.setLibraryBrief(avObject.getString("libraryBrief"));
                            mLibrary.setLibraryManager(avObject.getString("libraryManager"));
                            mLibrary.setLibraryPic(avObject.getString("libraryPic"));
                            mLibrary.setLibraryType(avObject.getString("libraryType"));

                            libraries.add(mLibrary);
                        }
                        showMessage(getText(R.string.toast_update_success), null, true);
                        updateData();
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
     * 显示数据
     * @param data
     */
    private void displayData(List<JoLibrary> data) {
        if(data.size() == 0){
            mEmptyView.setVisibility(View.VISIBLE);
            mPtrFrame.setVisibility(View.INVISIBLE);
        }else {
            mEmptyView.setVisibility(View.GONE);
            mPtrFrame.setVisibility(View.VISIBLE);
            mAdapter.getDataList().clear();
            mAdapter.getDataList().addAll(data);
            mPtrFrame.refreshComplete();
            mAdapter.notifyDataSetChanged();
        }

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    /**
     * {@link android.support.v7.widget.RecyclerView}的Adapter
     */
    private class LibraryAdapter extends RecyclerView.Adapter {
        private List<JoLibrary> data;
        private int resourceId;

        public LibraryAdapter(List<JoLibrary> libraries, int resourceId){
            this.data = new ArrayList<JoLibrary>();
            this.data.addAll(libraries);
            this.resourceId = resourceId;
        }
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // Create a new view.
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(resourceId, parent, false);

            LibraryViewHolder viewholder = new LibraryViewHolder(itemView, new LibraryViewHolder.IMyViewHolderClicks(){
                @Override
                public void onItemClicked(View caller, int position) {
                    Log.i(tag, "点击了Item, 位置position为：" + position);
                    showMessage("点击了Item, 位置position为：" + position, null, true);
                }
                @Override
                public void onOverflowClicked(ImageView callerImage, int position) {
                    Log.i(tag, "点击了Item的overflow, 位置position为：" + position);
                    showMessage("点击了Item的overflow, 位置position为：" + position, null, true);
                }
            });

            return viewholder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Log.d(tag, "Element " + position + " set.");
            // 获取position位置的JoLibrary对象
            JoLibrary library = this.data.get(position);
            LibraryViewHolder myViewHolder = (LibraryViewHolder) holder;

            myViewHolder.getCreatorName().setText(library.getLibraryManager());
            ImageLoader.getInstance().displayImage(library.getLibraryPic(), myViewHolder.getLibraryPic());
            myViewHolder.getLibraryName().setText(library.getLibraryName());
            myViewHolder.getLibraryBrief().setText(library.getLibraryBrief());
            myViewHolder.getLibraryCount().setText(library.getCounts());
/*            myViewHolder.getOverflow().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 点击overflow
                    showMessage(getText(R.string.onclick_overflow), null, true);
                }
            });*/

        }

        @Override
        public int getItemCount() {
            return this.data == null ? 0 : data.size();
        }

        public List<JoLibrary> getDataList(){
            return data;
        }
    }

    /**
     * 继承于{@link android.support.v7.widget.RecyclerView}
     */
    public static class LibraryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView libraryPic;
        ImageView creatorPic;
        ImageView overflow;
        TextView libraryName;
        TextView libraryBrief;
        TextView creatorName;
        TextView libraryCount;

        public IMyViewHolderClicks mListener;

        public static interface IMyViewHolderClicks {
            public void onItemClicked(View caller, int position);
            public void onOverflowClicked(ImageView callerImage, int position);
        }

        public LibraryViewHolder(View itemView, IMyViewHolderClicks listener) {
            super(itemView);
            this.mListener = listener;
            libraryBrief = (TextView) itemView.findViewById(R.id.library_brief);
            libraryCount = (TextView) itemView.findViewById(R.id.library_collection);
            libraryName = (TextView) itemView.findViewById(R.id.library_name);
            libraryPic = (ImageView) itemView.findViewById(R.id.library_pic);
            creatorName = (TextView) itemView.findViewById(R.id.library_creator);
            overflow = (ImageView) itemView.findViewById(R.id.iv_overflow);
            creatorPic = (ImageView) itemView.findViewById(R.id.library_creator_pic);

            itemView.setOnClickListener(this);
            overflow.setOnClickListener(this);
            libraryPic.setOnClickListener(this);

        }

        public ImageView getLibraryPic() {
            return libraryPic;
        }

        public ImageView getCreatorPic() {
            return creatorPic;
        }

        public ImageView getOverflow() {
            return overflow;
        }

        public TextView getLibraryName() {
            return libraryName;
        }

        public TextView getLibraryBrief() {
            return libraryBrief;
        }

        public TextView getCreatorName() {
            return creatorName;
        }

        public TextView getLibraryCount() {
            return libraryCount;
        }

        @Override
        public void onClick(View v) {
            if (v instanceof ImageView){
                // 点击了item
                mListener.onOverflowClicked((ImageView) v, getPosition());
            }else{
                // 点击了overflow
                mListener.onItemClicked(v, getPosition());
            }
        }

    }

}
