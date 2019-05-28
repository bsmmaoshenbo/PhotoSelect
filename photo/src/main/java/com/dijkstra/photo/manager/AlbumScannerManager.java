/*
 * AUTHOR：Yan Zhenjie
 *
 * DESCRIPTION：create the File, and add the content.
 *
 * Copyright © ZhiMore. All Rights Reserved
 *
 */
package com.dijkstra.photo.manager;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.dijkstra.photoselect.model.PhotoDetailInfo;
import com.dijkstra.photoselect.model.PhotoSelectInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 相册扫描类
 * on 2017-7-20 19:13
 * v1.0
 */
public class AlbumScannerManager {

    private static AlbumScannerManager instance;

    public static AlbumScannerManager getInstance() {
        if (instance == null)
            synchronized (AlbumScannerManager.class) {
                if (instance == null)
                    instance = new AlbumScannerManager();
            }
        return instance;
    }

    /**
     * 设置获取图片的属性
     */
    private static final String[] STORE_IMAGES = {
            /*
             * 图片ID。
             */
            MediaStore.Images.Media._ID,
            /*
             * 图片完整路径。
             */
            MediaStore.Images.Media.DATA,
            /*
             * 文件名称。
             */
            MediaStore.Images.Media.DISPLAY_NAME,
            /*
             * 被添加到库中的时间。
             */
            MediaStore.Images.Media.DATE_ADDED,
            /*
             * 目录ID。
             */
            MediaStore.Images.Media.BUCKET_ID,
            /*
             * 所在文件夹名称。
             */
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME
    };

    /**
     * 获取文件夹列表。
     *
     * @param context {@link Context}.
     * @return {@code List<AlbumFolder>}.
     */
    public ArrayList<PhotoSelectInfo> getPhotoAlbum(Context context) {
        ArrayList<PhotoSelectInfo> albumFolders = new ArrayList<>();
        Map<String, PhotoSelectInfo> albumFolderMap = new HashMap<>();

        PhotoSelectInfo allImageAlbumFolder = new PhotoSelectInfo();
        allImageAlbumFolder.mFolderName = "所有图片";

        Cursor cursor = null;
        try {
            cursor = MediaStore.Images.Media.query(context.getContentResolver(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI, STORE_IMAGES);

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    int imageId = cursor.getInt(0);
                    String imagePath = cursor.getString(1);
                    String imageName = cursor.getString(2);
                    long addTime = cursor.getLong(3);

                    int bucketId = cursor.getInt(4);
                    String bucketName = cursor.getString(5);

                    PhotoDetailInfo albumImage = new PhotoDetailInfo();
                    albumImage.mId = imageId;
                    albumImage.sdcardPath = imagePath;
                    albumImage.mImageName = imageName;
                    albumImage.mAddTime = addTime;

                    allImageAlbumFolder.addPicture(albumImage);//所有的原始相册

                    PhotoSelectInfo albumFolder = albumFolderMap.get(bucketName);//生成新的包含所有照片的新相册
                    if (albumFolder != null) {
                        albumFolder.addPicture(albumImage);
                    } else {
                        albumFolder = new PhotoSelectInfo();
                        albumFolder.mId = bucketId;
                        albumFolder.mFolderName = bucketName;
                        albumFolder.addPicture(albumImage);

                        albumFolderMap.put(bucketName, albumFolder);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        Collections.sort(allImageAlbumFolder.getAllPictureList());
        albumFolders.add(allImageAlbumFolder);//添加原始相册

        for (Map.Entry<String, PhotoSelectInfo> folderEntry : albumFolderMap.entrySet()) {
            PhotoSelectInfo albumFolder = folderEntry.getValue();
            Collections.sort(albumFolder.getAllPictureList());
            albumFolders.add(albumFolder);//添加新相册
        }

        return albumFolders;
    }

}
