package com.jalen.jo.library;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jalen.jo.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

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
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        builder.cacheInMemory(true);    // 内存缓存
        builder.cacheOnDisk(true);      // 硬盘缓存
        builder.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2);
        builder.showImageForEmptyUri(R.drawable.pic_empty_uri);  // 暂无图片
        builder.showImageOnFail(R.drawable.pic_fail_uri);   // 图片下载失败
//        builder.showImageOnLoading(R.drawable.pic_loading_uri);  // 图片下载中
        ImageLoader.getInstance().displayImage(library.getLibraryPic(), viewHolder.getLibraryPic(), builder.build());
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
