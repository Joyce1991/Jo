package com.jalen.jo.library;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jalen.jo.R;

/**
 * 图书馆列表item的View Holder<br/>
 * 继承于{@link android.support.v7.widget.RecyclerView}<br/>
 * Created by jh on 2015/4/23.
 */
public class LibraryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    ImageView libraryPic;
    ImageView creatorPic;
    ImageView overflow;
    TextView libraryName;
    TextView libraryBrief;
    TextView creatorName;
    TextView libraryCount;

    public IViewHolderClicksListener mListener;

    public LibraryViewHolder(View itemView, @Nullable IViewHolderClicksListener listener) {
        super(itemView);
        this.mListener = listener;
        libraryBrief = (TextView) itemView.findViewById(R.id.library_brief);
        libraryCount = (TextView) itemView.findViewById(R.id.library_collection);
        libraryName = (TextView) itemView.findViewById(R.id.library_name);
        libraryPic = (ImageView) itemView.findViewById(R.id.library_pic);
        creatorName = (TextView) itemView.findViewById(R.id.library_creator);
        overflow = (ImageView) itemView.findViewById(R.id.iv_overflow);
        creatorPic = (ImageView) itemView.findViewById(R.id.library_creator_pic);

        itemView.setOnClickListener(this);  // Item-Click
        overflow.setOnClickListener(this);  // Overflow-Click
        libraryPic.setOnClickListener(this);// Pic-Click

    }

    @Override
    public void onClick(View v) {
        // 假如没有设置监听则返回
        if (mListener != null){
            mListener.onItemClicked(v, getPosition());
        }

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
}