package com.dijkstra.photo.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.dijkstra.common.BaseRecyclerViewAdapter;
import com.dijkstra.common.BaseRecyclerViewHolder;
import com.dijkstra.photoselect.R;
import com.dijkstra.photoselect.model.PhotoDetailInfo;

import java.io.File;

/**
 * @Description: 选择图片adapter
 * @Author: maoshenbo
 * @Date: 2018/6/29 20:52
 * @Version: 1.0
 */
public class PhotoSelectAdapter extends BaseRecyclerViewAdapter<PhotoDetailInfo> {

    @Override
    public int[] initLayouts() {
        return new int[]{R.layout.gallery_item};
    }

    @Override
    protected void bindData(BaseRecyclerViewHolder holder, int position, PhotoDetailInfo data) {
        ImageView imgQueue = holder.getView(R.id.imgQueue);
        ImageView ivUnselected = holder.getView(R.id.iv_unselected);
        RelativeLayout imgQueueMultiSelected = holder.getView(R.id.rl_cover);

        File file = new File(data.sdcardPath);
        if (file.exists() && file.isFile()) {
            Glide.with(holder.itemView.getContext()).load(data.sdcardPath).into(imgQueue);
        }
        if (data.isSeleted) {
            ivUnselected.setVisibility(View.GONE);
            imgQueueMultiSelected.setVisibility(View.VISIBLE);
        } else {
            ivUnselected.setVisibility(View.VISIBLE);
            imgQueueMultiSelected.setVisibility(View.GONE);
        }
    }
}