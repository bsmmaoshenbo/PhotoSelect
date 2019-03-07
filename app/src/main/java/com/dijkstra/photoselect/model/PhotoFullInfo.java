package com.dijkstra.photoselect.model;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * @Description: 图片信息
 * @Author: maoshenbo
 * @Date: 2019/3/7 下午4:09
 * @Version: 1.0
 */
public class PhotoFullInfo implements Serializable {

    public String bigImgUrl = "";//图片地址imagefull
    public String imgIntro = "";//图片简介
    public boolean isReality = false;//是否是实拍
    public boolean isSelected = true;//是否被选中（即当前显示）
    public String shiPaiDate = "";//创建时间

    /**
     * 图片类型
     */
    private String imageType;

    private float widthHeightRate;

    public String getImageDescribe() {
        return TextUtils.isEmpty(imgIntro) ? "" : imgIntro;
    }


    public String getPhotoDescribe() {
        return getImageDescribe();
    }

    public Object getPhotoLoadUrl() {
        return bigImgUrl;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getImageCategory() {
        return getImageType();
    }

    public float getWidthHeightRate() {
        return widthHeightRate;
    }

    public void setWidthHeightRate(float widthHeightRate) {
        this.widthHeightRate = widthHeightRate;
    }
}
