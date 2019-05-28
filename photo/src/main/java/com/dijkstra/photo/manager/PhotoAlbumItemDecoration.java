package com.dijkstra.photo.manager;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @Description: 间隔线
 * @Author: maoshenbo
 * @Date: 2019/3/7 下午4:30
 * @Version: 1.0
 */
public class PhotoAlbumItemDecoration extends RecyclerView.ItemDecoration {

    private int mDividerHeight = 3;

    public PhotoAlbumItemDecoration() {
    }

    public PhotoAlbumItemDecoration(int dividerHeight) {
        this.mDividerHeight = dividerHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(0, 0, 0, mDividerHeight);
    }
}
