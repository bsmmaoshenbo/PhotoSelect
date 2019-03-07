package com.dijkstra.photoselect;

import android.Manifest;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;

import com.xiaozhu.xzdz.application.XZApplication;
import com.xiaozhu.xzdz.bizBase.photo.photoselector.activity.PhotoAlbumActivity;
import com.xiaozhu.xzdz.bizBase.photo.photoselector.model.PhotoDetailInfo;
import com.xiaozhu.xzdz.bizBase.tools.EnvSetting;
import com.xiaozhu.xzdz.bizBase.tools.XZAlertDialog;
import com.xiaozhu.xzdz.newapp.permission.XZPermissionUtils;
import com.xiaozhu.xzdz.newapp.permission.listener.PermissionListener;
import com.xiaozhu.xzdz.newapp.utils.ToastUtils;
import com.xiaozhu.xzdz.support.mycontrol.globleDialog.XZDialogPage;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

/**
 * 图片选择操作dialog
 * Created by wsy on 2017/9/27.
 *
 * ===========特别注意===============
 * ===========特别注意===============
 * ===========特别注意===============
 *
 * 调用这个方法的Activity或者说所属的Activity
 * 请在AndroidManfest 加入以下属性
 * android:configChanges="keyboardHidden|orientation|screenSize"
 *
 * ===========特别注意=================
 * ===========特别注意=================
 * ===========特别注意=================
 *
 *
 */

public class PicPickUpUtil {

    /* 从相册选取照片 */
    private static final int REQUEST_CODE_ALBUM = 2001;
    /* 选择相机拍照 */
    private static final int REQUEST_CODE_CAMERA = 2002;

    /**
     * @param selectedList 已经勾选的照片
     * @param canPickNum   可选图片总张数（包含已选）
     * @param callBack     回调
     */
    public static void showPickUpDialog(final ArrayList<PhotoDetailInfo> selectedList, final int canPickNum, final PicPickCallBack callBack) {

        if (selectedList != null && selectedList.size() >= canPickNum) {
            ToastUtils.show(String.format(Locale.getDefault(), "最多可选择%d张", canPickNum));
            return;
        }

        ArrayList<String> strList = new ArrayList<>();
        strList.add("相册");
        strList.add("拍照");

        final XZDialogPage xzDialogPage = new XZDialogPage(XZApplication.getInstance());
        xzDialogPage
                .getListDialogBuilder()
                .setBtnStr("取消")
                .setItemStr(strList)
                .setOnItemClickListener(new XZAlertDialog.OnDialogItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, Dialog dialog, View view, int position, final long id) {
                        switch (position) {
                            case 0:
                                //相册
                                Intent intent = new Intent(view.getContext(), PhotoAlbumActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putInt("canPickNum", canPickNum);
                                if (selectedList != null && selectedList.size() > 0) {
                                    bundle.putSerializable("selected_picture", selectedList);
                                }
                                intent.putExtras(bundle);

                                xzDialogPage.startActivityForResult(intent, REQUEST_CODE_ALBUM, new XZDialogPage.OnActivityForResultListener() {
                                    @Override
                                    public void onActivityForResult(int requestCode, int resultCode, Intent data) {
                                        if (requestCode == REQUEST_CODE_ALBUM && resultCode == RESULT_OK) {//相册返回

                                            Serializable allPath = data.getSerializableExtra("all_path");
                                            Serializable removeList = data.getSerializableExtra("removeList");

                                            if (allPath != null) {
                                                ArrayList<PhotoDetailInfo> photoDetailInfoList = (ArrayList<PhotoDetailInfo>) allPath;
                                                if (removeList != null) {
                                                    //已删除的图片
                                                    ArrayList<PhotoDetailInfo> removePhotoList = (ArrayList<PhotoDetailInfo>) removeList;
                                                    if (removePhotoList.size() > 0) {
                                                        for (PhotoDetailInfo photoDetailInfo : removePhotoList) {
                                                            if (selectedList != null) {
                                                                Iterator<PhotoDetailInfo> infoIterator = selectedList.iterator();
                                                                while (infoIterator.hasNext()) {
                                                                    PhotoDetailInfo detailInfo = infoIterator.next();
                                                                    if (detailInfo.sdcardPath.equalsIgnoreCase(photoDetailInfo.sdcardPath)) {
                                                                        //减去已删除图片
                                                                        infoIterator.remove();
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                    // 已选择 + 去掉已删除 = 现在选择的所有图片
                                                    //所有图片 - 重合图片 = 真是现在已选择的图片
                                                    for (PhotoDetailInfo photoDetailInfo : photoDetailInfoList) {
                                                        if (selectedList != null) {
                                                            Iterator<PhotoDetailInfo> infoIterator = selectedList.iterator();
                                                            while (infoIterator.hasNext()) {
                                                                PhotoDetailInfo detailInfo = infoIterator.next();
                                                                if (detailInfo.sdcardPath.equalsIgnoreCase(photoDetailInfo.sdcardPath)) {
                                                                    infoIterator.remove();
                                                                }
                                                            }
                                                        }
                                                    }
                                                    if (selectedList != null && canPickNum > 1) {
                                                        photoDetailInfoList.addAll(selectedList);
                                                    }
                                                }

                                                if (callBack != null) {
                                                    callBack.onPickPicFromAlbum(photoDetailInfoList);
                                                }
                                            }
                                        }
                                    }
                                });
                                break;
                            case 1:
                                //拍照

                                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
                                Date date = new Date(System.currentTimeMillis());
                                String fileName = format.format(date);
                                File pathFile = new File(EnvSetting.getDICMPath());
                                if (!pathFile.exists() || !pathFile.isDirectory()) {
                                    pathFile.mkdir();
                                }
                                final File file = new File(EnvSetting.getDICMPath(), fileName + ".jpg");
                                final Uri uri = XZCropHelper.getUriForFile(file);
                                XZPermissionUtils.checkPermission(XZApplication.getInstance(), new String[]{Manifest.permission.CAMERA}, new PermissionListener() {
                                    @Override
                                    public void onSucceed(int requestCode, @NonNull String[] grantPermissions) {
                                        // 拍照
                                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                                        try {
                                            intent.putExtra("return-data", true);
                                            xzDialogPage.startActivityForResult(intent, REQUEST_CODE_CAMERA, new XZDialogPage.OnActivityForResultListener() {
                                                @Override
                                                public void onActivityForResult(int requestCode, int resultCode, Intent data) {
                                                    if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {
                                                        XZApplication.getInstance().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                                                        if (file.exists() && callBack != null) {
                                                            callBack.onTakePhotoResult(new PhotoDetailInfo(file.getAbsolutePath(), true));
                                                        }
                                                    }
                                                }
                                            });
                                        } catch (ActivityNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onFailed(int requestCode, @NonNull String[] deniedPermissions) {
                                        String funName = XZPermissionUtils.getPermissionGroupLabelStr(XZApplication.getInstance(), Manifest.permission.CAMERA);
                                        ToastUtils.show("权限请求失败，您将无法使用" + funName + "功能");
                                    }
                                });
                                break;
                        }
                    }
                })
                .show();
    }


    /**
     * @param callBack callBack
     */
    public static void showPickUpDialog(final PicPickCallBack callBack) {
        showPickUpDialog(null, PhotoAlbumActivity.NO_LIMIT_NUM, callBack);
    }

    public interface PicPickCallBack {
        void onPickPicFromAlbum(ArrayList<PhotoDetailInfo> pictures);

        void onTakePhotoResult(PhotoDetailInfo picture);
    }
}