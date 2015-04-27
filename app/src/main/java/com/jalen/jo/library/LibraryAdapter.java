package com.jalen.jo.library;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jalen.jo.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;
import com.nostra13.universalimageloader.utils.ImageSizeUtils;

import java.util.List;

/**
 * {@link android.support.v7.widget.RecyclerView}的Adapter
 * Created by jh on 2015/4/23.
 */
public class LibraryAdapter extends RecyclerView.Adapter {
    private List<JoLibrary> data;
    private int resourceId;
    private DisplayImageOptions options;
    private IViewHolderClicksListener mItemClickListener;

    public LibraryAdapter(int resourceId, List<JoLibrary> data){
        this.data = data;
        this.resourceId = resourceId;

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
                int target = width < height ? width : height;
                int targetX = (width - target) / 2;
                int targetY = (height - target) / 2;
                bitmap = Bitmap.createBitmap(bitmap, targetX, targetY, target, target);
                /** 2.图片缩放 **/
                return bitmap;
            }
        }); // 加载到内存后，显示到内存前
        options = builder.build();
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
        ImageLoader.getInstance().displayImage(library.getLibraryPic(), viewHolder.getLibraryPic(), options);
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
