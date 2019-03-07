package com.dijkstra.photoselect.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 选择图片界面
 *
 * @author masohenbo
 */
public class PhotoSelectInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 目录Id
     */
    public int mId;
    /**
     * 目录名称
     */
    public String mFolderName;

    /**
     * 所有图片（路径，是否选中）的集合
     */
    private ArrayList<PhotoDetailInfo> mAllPictureList = new ArrayList<>();
    /**
     * 所有选中图片（路径，是否选中）的集合
     */
    private ArrayList<PhotoDetailInfo> mSelectedPictureList = new ArrayList<>();
    /**
     * 所有选中图片（路径，是否选中）预览的集合
     */
    private ArrayList<PhotoFullInfo> mPreviewPicList;

    public PhotoSelectInfo() {
        super();
    }

    /**
     * 获得要进行预览的图片集合（跳入图片预览界面用）
     *
     * @return 预览图片
     */
    public ArrayList<PhotoFullInfo> getPreviewPicList() {
        if (mPreviewPicList == null) {
            mPreviewPicList = new ArrayList<>();
        }
        return mPreviewPicList;
    }

    /**
     * 设置要进行预览的图片集合（跳入图片预览界面用）
     *
     * @param selectedPictureList 预览图片
     */
    public void setPreviewPicList(ArrayList<PhotoDetailInfo> selectedPictureList) {
        if (mPreviewPicList == null) {
            mPreviewPicList = new ArrayList<>();
        } else {
            mPreviewPicList.clear();
        }
        for (PhotoDetailInfo photoDetailInfo : selectedPictureList) {
            PhotoFullInfo photoFullInfo = new PhotoFullInfo();
            photoFullInfo.bigImgUrl = photoDetailInfo.sdcardPath;
            mPreviewPicList.add(photoFullInfo);
        }
    }

    /**
     * 获得所有图片的集合
     *
     * @return 所有图片
     */
    public ArrayList<PhotoDetailInfo> getAllPictureList() {
        return mAllPictureList;
    }

    /**
     * 对比传入的集合，修改图片是否选中
     *
     * @param pictureList 传入的图片集合
     */
    public void modifyAllPictureList4Selected(ArrayList<PhotoDetailInfo> pictureList) {
        for (PhotoDetailInfo mPic : mAllPictureList) {
            for (PhotoDetailInfo photoDetailInfo : pictureList) {
                if (mPic.sdcardPath.equals(photoDetailInfo.sdcardPath)) {
                    mPic.isSeleted = true;
                }
            }
        }
    }

    /**
     * 对比传入的集合，修改图片是否选中
     *
     * @param previewList 传入的图片集合
     */
    public void modifyAllPictureList4Preview(ArrayList<PhotoFullInfo> previewList) {
        for (PhotoDetailInfo mPic : this.mAllPictureList) {
            mPic.isSeleted = false;
            for (PhotoFullInfo pre : previewList) {
                if (mPic.sdcardPath.equals(pre.bigImgUrl)) {
                    mPic.isSeleted = true;
                }
            }
        }
    }

    /**
     * 设置所有图片（路径，是否选择）的集合
     *
     * @param pictureList 设置选择照片
     */
    public void setAllPictureList(ArrayList<PhotoDetailInfo> pictureList) {
        this.mAllPictureList = pictureList;
    }

    /**
     * 获得选中的图片集合
     *
     * @return 已选择图片list
     */
    public ArrayList<PhotoDetailInfo> getSelectdPictureList() {
        mSelectedPictureList.clear();
        for (PhotoDetailInfo photoDetailInfo : getAllPictureList()) {
            if (photoDetailInfo.isSeleted) {
                mSelectedPictureList.add(photoDetailInfo);
            }
        }
        return mSelectedPictureList;
    }

    public void addPicture(PhotoDetailInfo picture) {
        mAllPictureList.add(picture);
    }
}
