package com.dijkstra.photo.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dijkstra.common.BaseRecyclerViewAdapter;
import com.dijkstra.common.CommonRecyclerView;
import com.dijkstra.photoselect.Constant;
import com.dijkstra.photoselect.DisplayUtil;
import com.dijkstra.photoselect.DividerGridItemDecoration;
import com.dijkstra.photoselect.R;
import com.dijkstra.photoselect.activity.PhotoAlbumActivity;
import com.dijkstra.photoselect.adapter.PhotoSelectAdapter;
import com.dijkstra.photoselect.model.PhotoDetailInfo;
import com.dijkstra.photoselect.model.PhotoSelectInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

/**
 * @Description: 图片选择
 * @Author: maoshenbo
 * @Date: 2018/8/16 15:04
 * @Version: 1.0
 */
public class PhotoSelectFragment extends Fragment implements View.OnClickListener {

    private CommonRecyclerView mRecycleViewImage;
    private ImageView mImgNoMedia;
    private TextView mBtnGalleryOk;
    private LinearLayout mActionBarWidgetBack;
    private TextView mActionBarWidgetBackText, mActionBarWidgetTitle, mActionBarWidgetMoreText;
    private TextView mCommentPhotosPreview;
    private RelativeLayout mRlPhotoContainer;
    private TextView mTvNoPhotos;

    private ArrayList<PhotoDetailInfo> mSelectedList;//已选择的图片
    private int mCanPickNum;//可选择图片数量,默认10张
    private PhotoSelectInfo mPhotoSelectInfo;//图片总量
    private int mPosition;//相册列表位置

    private PhotoSelectAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.getSerializable("selected_picture") != null) {
                mSelectedList = (ArrayList<PhotoDetailInfo>) bundle.getSerializable("selected_picture");
            } else {
                mSelectedList = new ArrayList<>();
            }
            mCanPickNum = bundle.getInt("canPickNum", -1);
            mPosition = bundle.getInt("position", -1);
        }
        mPhotoSelectInfo = (PhotoSelectInfo) Constant.mContextMap.get("photoAlbum");
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_select_gallery, container, false);
        initUI(view);
        initUIData();
        initListener();
        return view;
    }

    private void initUI(View v) {
        mActionBarWidgetBack = v.findViewById(R.id.actionbarwidget_back);
        mActionBarWidgetBackText = v.findViewById(R.id.actionbarwidget_back_text);
        mActionBarWidgetTitle = v.findViewById(R.id.actionbarwidget_title);
        mActionBarWidgetMoreText = v.findViewById(R.id.actionbarwidget_more_text);
        v.findViewById(R.id.actionbarwidget_bottom_line).setVisibility(View.GONE);

        mRlPhotoContainer = v.findViewById(R.id.rl_photo_container);
        mTvNoPhotos = v.findViewById(R.id.tv_no_photos);

        mRecycleViewImage = v.findViewById(R.id.recycle_view_image);
        mImgNoMedia = v.findViewById(R.id.imgNoMedia);
        mBtnGalleryOk = v.findViewById(R.id.comment_photos_sure);
        mCommentPhotosPreview = v.findViewById(R.id.comment_photos_preview);
    }

    private void initUIData() {
        mActionBarWidgetTitle.setText("照片");
        mActionBarWidgetBackText.setVisibility(View.VISIBLE);
        mActionBarWidgetBackText.setText("返回相册");
        mActionBarWidgetMoreText.setText("取消");

        mRecycleViewImage.setNestedScrollingEnabled(false);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 4);
        mRecycleViewImage.setLayoutManager(manager);
        if (getActivity() != null) {
            mRecycleViewImage.addItemDecoration(new DividerGridItemDecoration(DisplayUtil.dp2px(getActivity(), 3), DisplayUtil.dp2px(getActivity(), 3), ContextCompat.getColor(getActivity(), R.color.ffffff)));
        }
        mAdapter = new PhotoSelectAdapter();
        mRecycleViewImage.setOnNestedScrollingEnabled(false)
                .noSpring()
                .setListType(CommonRecyclerView.LIST_TYPE_NORMAL)
                .setOrientation(CommonRecyclerView.VERTICAL)
                .setAdapter(mAdapter);

        if (mPhotoSelectInfo == null || mPhotoSelectInfo.getAllPictureList() == null || mPhotoSelectInfo.getAllPictureList().size() == 0) {
            mRlPhotoContainer.setVisibility(View.GONE);
            mTvNoPhotos.setVisibility(View.VISIBLE);
            mImgNoMedia.setVisibility(View.VISIBLE);
        } else {
            if (getActivity() != null) {
                mBtnGalleryOk.setEnabled(false);
                if (mSelectedList != null) {
                    mPhotoSelectInfo.modifyAllPictureList4Selected(mSelectedList);
                }
                previewAndSure(mPhotoSelectInfo);
                mAdapter.setData(mPhotoSelectInfo.getAllPictureList());
                mImgNoMedia.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 底部按钮状态改变
     *
     * @param info 已选择图片数据
     */
    public void previewAndSure(PhotoSelectInfo info) {
        int selectedNum = 0;
        if (info.getSelectdPictureList() != null) {
            selectedNum = info.getSelectdPictureList().size();
        }
        if (selectedNum == 0) {
            mBtnGalleryOk.setEnabled(false);
            mBtnGalleryOk.setText("上传");
            mCommentPhotosPreview.setEnabled(false);
            if (getActivity() != null && isAdded()) {
                mCommentPhotosPreview.setTextColor(Color.parseColor("#bdbdbd"));
            }
            mBtnGalleryOk.setBackgroundResource(R.drawable.picture_select_gray);
        } else {
            if (mCanPickNum == PhotoAlbumActivity.NO_LIMIT_NUM) {
                mBtnGalleryOk.setText(String.format(Locale.getDefault(), "上传%d", selectedNum));
            } else {
                mBtnGalleryOk.setText(String.format(Locale.getDefault(), "上传(%d/%d)", selectedNum, mCanPickNum));
            }
            mBtnGalleryOk.setEnabled(true);
            mCommentPhotosPreview.setEnabled(true);
            if (getActivity() != null && isAdded()) {
                mCommentPhotosPreview.setTextColor(Color.parseColor("#ff4081"));
            }
            mBtnGalleryOk.setBackgroundResource(R.drawable.picture_select_red);
        }
    }

    private void initListener() {
        mActionBarWidgetBack.setOnClickListener(this);
        mActionBarWidgetMoreText.setOnClickListener(this);
        mAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                //条目点击事件

                int selectedSize = 0;
                if (mPhotoSelectInfo.getSelectdPictureList() != null) {
                    selectedSize = mPhotoSelectInfo.getSelectdPictureList().size();
                }
                PhotoDetailInfo detailInfo = mPhotoSelectInfo.getAllPictureList().get(position);
                if (mCanPickNum == PhotoAlbumActivity.NO_LIMIT_NUM
                        || selectedSize < mCanPickNum
                        || detailInfo.isSeleted) {
                    detailInfo.isSeleted = !detailInfo.isSeleted;
                    mAdapter.notifyItemChanged(position);
                } else {
                    Toast.makeText(getActivity(), "最多能选" + mCanPickNum + "张图片", Toast.LENGTH_SHORT).show();
                }
                previewAndSure(mPhotoSelectInfo);
            }
        });
        mBtnGalleryOk.setOnClickListener(this);
        mCommentPhotosPreview.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.actionbarwidget_back) {//返回按钮
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }

        } else if (i == R.id.actionbarwidget_more_text) {//取消按钮
            if (getActivity() != null) {
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.enter_left_right, R.anim.exit_left_right);
            }

        } else if (i == R.id.comment_photos_sure) {//上传
            PhotoSelectInfo dto = mPhotoSelectInfo;
            if (dto.getSelectdPictureList().size() > 0) {

                // 当前条目中传进来的已选择的图片 - 新选择图片集合 = 已删除图片集合
                for (PhotoDetailInfo detailInfo : dto.getSelectdPictureList()) {
                    if (mSelectedList != null) {
                        Iterator<PhotoDetailInfo> infoIterator = mSelectedList.iterator();
                        while (infoIterator.hasNext()) {
                            PhotoDetailInfo photoDetailInfo = infoIterator.next();
                            if (photoDetailInfo.sdcardPath.equalsIgnoreCase(detailInfo.sdcardPath)) {
                                infoIterator.remove();
                            }
                        }
                    }
                }

                Intent data = new Intent();
                data.putExtra("all_path", dto.getSelectdPictureList());
                data.putExtra("position", mPosition);
                data.putExtra("removeList", mSelectedList);
                if (getActivity() != null) {
                    getActivity().setResult(RESULT_OK, data);
                    getActivity().finish();
                    getActivity().overridePendingTransition(R.anim.enter_left_right, R.anim.exit_left_right);
                }
            } else {
                Toast.makeText(getActivity(), "请先选择图片", Toast.LENGTH_SHORT).show();
            }

        } else if (i == R.id.comment_photos_preview) {//预览
            if (mPhotoSelectInfo.getSelectdPictureList().size() > 0) {
                mPhotoSelectInfo.setPreviewPicList(mPhotoSelectInfo.getSelectdPictureList());
//                PhotoPreviewUtil.start()
//                        .setDataList(mPhotoSelectInfo.getPreviewPicList())
//                        .isZoom(true)
//                        .isCanRemove(true)
//                        .isSingleBack(false)
//                        .setFirstShowPosition(0)
//                        .setOnFinishListener(PhotoAlbumActivity.SELECT_PHOTO_PREVIEW, new OnActivityFinishResultListener() {
//
//                            @Override
//                            public void onActivityForPhotoResult(int requestCode, int resultCode, Intent data) {
//                                if (requestCode == PhotoAlbumActivity.SELECT_PHOTO_PREVIEW) {
//                                    ArrayList<ImageInfoFullScreenDto> previewList = (ArrayList<ImageInfoFullScreenDto>) data.getSerializableExtra(PhotoPreviewUtil.KEY_LIST);
//                                    if (previewList != null) {
//                                        mPhotoSelectInfo.setAllPictureList(mPhotoSelectInfo.getAllPictureList());
//                                        mPhotoSelectInfo.modifyAllPictureList4Preview(previewList);
//                                        //刷新UI
//                                        mRecycleViewImage.refresh(mPhotoSelectInfo.getAllPictureList(), false);
//                                    }
//                                }
//                                previewAndSure(mPhotoSelectInfo);
//                            }
//                        })
//                        .show(getActivity());
            } else {
                Toast.makeText(getActivity(), "请先选择图片", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}