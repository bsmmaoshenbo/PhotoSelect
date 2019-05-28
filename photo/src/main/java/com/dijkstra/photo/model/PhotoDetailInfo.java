package com.dijkstra.photo.model;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * 图片路径类（是否选择）
 *
 * @author shiguotao
 */
public class PhotoDetailInfo implements Serializable, Comparable<PhotoDetailInfo> {

    /**
     * 图片Id
     */
    public int mId;
    /**
     * 图片的名字
     */
    public String mImageName;
    /**
     * 图片的添加时间
     */
    public long mAddTime;

    /**
     * 图片的地址
     */
    public String sdcardPath;
    /**
     * 是否选中
     */
    public boolean isSeleted = false;
    /**
     * 是否可选（尺寸以及文件大小）
     */
    public boolean isSeletAble = true;

    /**
     * 是否是网络图片
     */
    public boolean isOnlinePic = false;
    /**
     * 网络缩略图地址
     */
    public String onlineSmallUrl = "";
    /**
     * 网络大图地址
     */
    public String onlineBigUrl = "";
    /**
     * 网络原图
     */
    public String url = "";

    public int width;

    public int height;
    /**
     * 图片宽度比高度的比值
     */
    public float widthHeightRatio = 0;

    /**
     * 上传状态 - 上传中
     */
    public static final int STATUS_UPLOAD_PROCESSING = 1;
    /**
     * 上传状态 - 上传成功
     */
    public static final int STATUS_UPLOAD_SUCCESS = 2;
    /**
     * 上传状态 - 上传失败
     */
    public static final int STATUS_UPLOAD_FAIL = 3;
    public int status = -1;
    public int uploadProcess = 0;


    public PhotoDetailInfo() {
        super();
    }

    public PhotoDetailInfo(String sdcardPath, boolean isSeleted) {
        super();
        this.sdcardPath = sdcardPath;
        this.isSeleted = isSeleted;
    }

    public void setStatus(int status) {
        if (this.status != status) {
            this.status = status;
        }
    }

    @Override
    public int compareTo(@NonNull PhotoDetailInfo o) {
        long time = o.mAddTime - mAddTime;
        if (time > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        } else if (time < -Integer.MAX_VALUE) {
            return -Integer.MAX_VALUE;
        }
        return (int) time;
    }
}
