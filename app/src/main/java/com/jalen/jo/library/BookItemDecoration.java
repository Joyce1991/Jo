package com.jalen.jo.library;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jalen.jo.utils.DisplayUtil;

/**
 * Created by jalenzhang on 2015/4/27.
 */
public class BookItemDecoration extends RecyclerView.ItemDecoration {
    private Context context;
    public BookItemDecoration(Context context){
        this.context = context;
    }
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = DisplayUtil.Dp2Px(context, 4);
        outRect.top = DisplayUtil.Dp2Px(context, 4);
        outRect.left = DisplayUtil.Dp2Px(context, 4);
        outRect.right = DisplayUtil.Dp2Px(context, 4);
    }
}
