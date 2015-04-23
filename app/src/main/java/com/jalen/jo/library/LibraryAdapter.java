package com.jalen.jo.library;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * {@link android.support.v7.widget.RecyclerView}的Adapter
 * Created by jh on 2015/4/23.
 */
public class LibraryAdapter extends RecyclerView.Adapter {
    private List<JoLibrary> data;
    private int resourceId;
    private IViewHolderClicksListener mItemClickListener;

    public LibraryAdapter(int resourceId, List<JoLibrary> data){
        this.data = data;
        this.resourceId = resourceId;
    }
    public void setItenClickListener(IViewHolderClicksListener mItemClickListener){
        this.mItemClickListener = mItemClickListener;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new view.
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(resourceId, parent, false);
        return new LibraryViewHolder(itemView, mItemClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // 获取position位置的JoLibrary对象
        JoLibrary library = data.get(position);
        LibraryViewHolder viewHolder = (LibraryViewHolder) holder;

        viewHolder.getCreatorName().setText(library.getLibraryManager());
        ImageLoader.getInstance().displayImage(library.getLibraryPic(), viewHolder.getLibraryPic());
        viewHolder.getLibraryName().setText(library.getLibraryName());
        viewHolder.getLibraryBrief().setText(library.getLibraryBrief());
        viewHolder.getLibraryCount().setText(library.getCounts());
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public List<JoLibrary> getDataList(){
        return data;
    }
}
