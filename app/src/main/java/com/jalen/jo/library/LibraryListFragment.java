package com.jalen.jo.library;

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
import android.widget.TextView;

import com.jalen.jo.R;
import com.jalen.jo.beans.JoLibrary;
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
    private MyAdapter mAdapter;

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

        libraries = new ArrayList<JoLibrary>();
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
                Log.i(tag, "点击创建图书馆");
            }
        });

        // RecyclerView
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_library_join);
        mAdapter = new MyAdapter(libraries, R.layout.adapter_library_item);
        mRecyclerView.setAdapter(mAdapter);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // PtrFrameLayout
        mPtrFrame = (PtrClassicFrameLayout) rootView.findViewById(R.id.library_user_pullrefresh);
        mPtrFrame.setLastUpdateTimeRelateObject(this);
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

        // 联网获取最新数据
        JoLibrary library = new JoLibrary();
        library.setCounts("245");
        library.setLibraryBrief("fakisodhgfiashndifdklfhnjkasd");
        library.setLibraryId("0215");
        library.setLibraryManager("joyce");
        library.setLibraryManagerPicUrl("https://raw.githubusercontent.com/aosp-exchange-group/about/master/weixin-qrcode.jpg");
        library.setLibraryName("东软北京图书馆");
        library.setLibraryPic("https://raw.githubusercontent.com/aosp-exchange-group/about/master/weixin-qrcode.jpg");
        libraries.add(library);
        displayData(libraries);
/*
        DemoRequestData.getImageList(new RequestFinishHandler<JsonData>() {
            @Override
            public void onRequestFinish(final JsonData data) {
                displayData(data);
            }
        });
*/
    }

    /**
     * 显示数据
     * @param data
     */
    private void displayData(List<JoLibrary> data) {

        mEmptyView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);

        mAdapter.getDataList().clear();
//        mAdapter.getDataList().addAll(data.optJson("data").optJson("list").toArrayList());
        mAdapter.getDataList().addAll(data);
        mPtrFrame.refreshComplete();
        mAdapter.notifyDataSetChanged();
    }
/*
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
*/

/*
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
*/

/*
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
*/

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

    /**
     * {@link android.support.v7.widget.RecyclerView}的Adapter
     */
    private class MyAdapter extends RecyclerView.Adapter {
        private List<JoLibrary> libraries;
        private int resourceId;

        public MyAdapter(List<JoLibrary> libraries, int resourceId){
            this.libraries = libraries;
            this.resourceId = resourceId;
        }
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // Create a new view.
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(resourceId, parent, false);

            MyViewHolder viewholder = new MyViewHolder(v, new MyViewHolder.IMyViewHolderClicks(){
                @Override
                public void onItemClicked(View caller, int position) {
                    Log.i(tag, "点击了Item, 位置position为：" + position);
                }
                @Override
                public void onOverflowClicked(ImageView callerImage, int position) {
                    Log.i(tag, "点击了Item的overflow, 位置position为：" + position);
                }
            });
            return viewholder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Log.d(tag, "Element " + position + " set.");
            JoLibrary library = libraries.get(position);
            MyViewHolder myViewHolder = (MyViewHolder) holder;

            myViewHolder.getCreatorName().setText(library.getLibraryManager());
            ImageLoader.getInstance().displayImage(library.getLibraryManagerPicUrl(), myViewHolder.getCreatorPic());
            ImageLoader.getInstance().displayImage(library.getLibraryPic(), myViewHolder.getLibraryPic());
            myViewHolder.getLibraryName().setText(library.getLibraryName());
            myViewHolder.getLibraryBrief().setText(library.getLibraryBrief());
            myViewHolder.getLibraryCount().setText(library.getCounts());
            myViewHolder.getOverflow().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 点击overflow
                }
            });

        }

        @Override
        public int getItemCount() {
            return libraries.size();
        }

        public List<JoLibrary> getDataList(){
            return libraries;
        }
    }

    /**
     * 继承于{@link android.support.v7.widget.RecyclerView}
     */
    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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

        public MyViewHolder(View itemView, IMyViewHolderClicks listener) {
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
