package com.dijkstra.photoselect.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dijkstra.photoselect.R;
import com.dijkstra.photoselect.model.PhotoSelectInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 相册列表页adapter
 * @Author: maoshenbo
 * @Date: 2017/7/24 22:39
 * @Version: 1.0
 */
public class PhotoAlbumAdapter extends RecyclerView.Adapter<PhotoAlbumAdapter.AlbumFolderViewHolder> {

    private List<PhotoSelectInfo> mPictureFolders = new ArrayList<>();
    private final Activity mContext;
    private OnItemClientListener onItemClientListener;

    public PhotoAlbumAdapter(Activity context) {
        mContext = context;
    }

    public void setData(List<PhotoSelectInfo> folders) {
        mPictureFolders.clear();
        mPictureFolders.addAll(folders);
    }

    @NonNull
    @Override
    public AlbumFolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycle_view_photo_album_item, parent, false);
        return new AlbumFolderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumFolderViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if (null != mPictureFolders.get(position)
                && null != mPictureFolders.get(position).getAllPictureList()
                && mPictureFolders.get(position).getAllPictureList().size() > 0) {
            Glide.with(holder.itemView.getContext()).load(mPictureFolders.get(position).getAllPictureList().get(0).sdcardPath).into(holder.tvThumbImage);
        }

        holder.tvPhotoAlbumName.setText(mPictureFolders.get(position).mFolderName);
        if (null != mPictureFolders.get(position)
                && null != mPictureFolders.get(position).getAllPictureList()) {
            holder.tvPhotoAlbumNum.setText(String.valueOf(mPictureFolders.get(position).getAllPictureList().size()));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClientListener != null) {
                    onItemClientListener.onItemClientListener(view, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mPictureFolders != null && mPictureFolders.size() > 0) ? mPictureFolders.size() : 0;
    }

    class AlbumFolderViewHolder extends RecyclerView.ViewHolder {

        private ImageView tvThumbImage;
        private TextView tvPhotoAlbumName, tvPhotoAlbumNum;

        AlbumFolderViewHolder(View itemView) {
            super(itemView);
            tvThumbImage = itemView.findViewById(R.id.iv_thumb_image);
            tvPhotoAlbumName = itemView.findViewById(R.id.tv_photo_album_name);
            tvPhotoAlbumNum = itemView.findViewById(R.id.tv_photo_album_num);
        }
    }

    public interface OnItemClientListener {
        void onItemClientListener(View itemView, int position);
    }

    public void setOnItemClientListener(OnItemClientListener listener) {
        this.onItemClientListener = listener;
    }
}
