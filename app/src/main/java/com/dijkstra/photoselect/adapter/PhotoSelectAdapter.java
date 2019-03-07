package com.dijkstra.photoselect.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.xiaozhu.xzdz.R;
import com.xiaozhu.xzdz.bizBase.photo.photoselector.model.PhotoDetailInfo;
import com.xiaozhu.xzdz.newapp.network.ImageLoader;
import com.xiaozhu.xzdz.newapp.recycler.ZRecyclerView;

import java.io.File;

/**
 * @Description: 选择图片adapter
 * @Author: maoshenbo
 * @Date: 2018/6/29 20:52
 * @Version: 1.0
 */
public class PhotoSelectAdapter extends ZRecyclerView.InitListener {
    private Context mContext;
    private ZRecyclerView mRecyclerView;

    public PhotoSelectAdapter(Context context, ZRecyclerView recyclerView) {
        mContext = context;
        mRecyclerView = recyclerView;
    }

    @Override
    public View getItemView(View parent, int viewType) {
        return LayoutInflater.from(mContext).inflate(R.layout.gallery_item, (ViewGroup) parent, false);
    }

    @Override
    public void bindData(ZRecyclerView.ZViewHolder viewHolder, int position) {
        PhotoDetailInfo itemData = mRecyclerView.getItemByPosition(position);

        ImageView imgQueue = (ImageView) viewHolder.getItemSubView(R.id.imgQueue);
        ImageView ivUnselected = (ImageView) viewHolder.getItemSubView(R.id.iv_unselected);
        RelativeLayout imgQueueMultiSelected = (RelativeLayout) viewHolder.getItemSubView(R.id.rl_cover);

        File file = new File(itemData.sdcardPath);
        if (file.exists() && file.isFile()) {
            ImageLoader.getInstance().load(itemData.sdcardPath, null, true, true).into(imgQueue);
        }
        if (itemData.isSeleted) {
            ivUnselected.setVisibility(View.GONE);
            imgQueueMultiSelected.setVisibility(View.VISIBLE);
        } else {
            ivUnselected.setVisibility(View.VISIBLE);
            imgQueueMultiSelected.setVisibility(View.GONE);
        }
    }
}