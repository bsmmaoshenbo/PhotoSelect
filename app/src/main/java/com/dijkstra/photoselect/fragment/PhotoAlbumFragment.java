package com.dijkstra.photoselect.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dijkstra.photoselect.Constant;
import com.dijkstra.photoselect.R;
import com.dijkstra.photoselect.activity.PhotoAlbumActivity;
import com.dijkstra.photoselect.adapter.PhotoAlbumAdapter;
import com.dijkstra.photoselect.callback.TransitionCallback;
import com.dijkstra.photoselect.manager.AlbumScannerManager;
import com.dijkstra.photoselect.manager.PhotoAlbumItemDecoration;
import com.dijkstra.photoselect.model.PhotoDetailInfo;
import com.dijkstra.photoselect.model.PhotoSelectInfo;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class PhotoAlbumFragment extends Fragment implements View.OnClickListener {

    private RecyclerView mRecycleViewPhotoAlbum;
    private ArrayList<PhotoSelectInfo> mPictureFolders;
    private TextView mActionBarWidgetTitle, mActionBarWidgetMoreText;
    private ArrayList<PhotoDetailInfo> mSelectedList;
    private ArrayList<PhotoDetailInfo> mNewSelectedList;
    private int mCanPickNum;//可选择图片数量,默认10张
    private TransitionCallback mTransitionCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.getSerializable("selected_picture") != null) {
                mSelectedList = (ArrayList<PhotoDetailInfo>) bundle.getSerializable("selected_picture");
            }
            mCanPickNum = bundle.getInt("canPickNum", -1);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_album, container, false);
        initUI(view);
        refreshUIEx();
        return view;
    }

    private void initUI(View view) {
        view.findViewById(R.id.actionbarwidget_back).setVisibility(View.GONE);
        mActionBarWidgetTitle = view.findViewById(R.id.actionbarwidget_title);
        mActionBarWidgetMoreText = view.findViewById(R.id.actionbarwidget_more_text);
        mRecycleViewPhotoAlbum = view.findViewById(R.id.recycle_view_photo_album);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        mRecycleViewPhotoAlbum.setLayoutManager(linearLayoutManager);
        if (getActivity() != null) {
            mRecycleViewPhotoAlbum.addItemDecoration(new PhotoAlbumItemDecoration(dp2px(getActivity(), 10)));
        }
        mActionBarWidgetMoreText.setOnClickListener(this);
    }

    private void refreshUIEx() {
        mActionBarWidgetTitle.setText("相册");
        mActionBarWidgetMoreText.setText("取消");

//        XZPermissionUtils.checkPermission(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionListener() {
//
//            @Override
//            public void onSucceed(int requestCode, @NonNull String[] grantPermissions) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (getActivity() == null) return;
//                        mPictureFolders = AlbumScannerManager.getInstance().getPhotoAlbum(getActivity());
//                        if (getActivity() == null) return;
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                renderUI(mPictureFolders);
//                            }
//                        });
//                    }
//                }).start();
//            }
//
//            @Override
//            public void onFailed(int requestCode, @NonNull String[] deniedPermissions) {
//                if (getActivity() != null) {
//                    String funName = XZPermissionUtils.getPermissionGroupLabelStr(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
//                    Toast.makeText(getActivity(), "权限请求失败，您将无法使用" + funName + "功能", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
    }

    /**
     * 取到相册，更新UI
     *
     * @param mPictureFolders 相册列表
     */
    private void renderUI(final ArrayList<PhotoSelectInfo> mPictureFolders) {
        PhotoAlbumAdapter photoAlbumAdapter = new PhotoAlbumAdapter(getActivity());
        photoAlbumAdapter.setData(mPictureFolders);
        mRecycleViewPhotoAlbum.setAdapter(photoAlbumAdapter);
        photoAlbumAdapter.setOnItemClientListener(new PhotoAlbumAdapter.OnItemClientListener() {
            @Override
            public void onItemClientListener(View itemView, int position) {
                Bundle bundle = new Bundle();
                bundle.putInt("position", position);

                int canPickNum = 0;
                ArrayList<PhotoDetailInfo> newList = new ArrayList<>();
                if (mSelectedList != null
                        && mSelectedList.size() > 0
                        && mPictureFolders.get(position) != null
                        && mPictureFolders.get(position).getAllPictureList() != null) {
                    for (PhotoDetailInfo photoDetailInfo : mPictureFolders.get(position).getAllPictureList()) {
                        for (PhotoDetailInfo detailInfo : mSelectedList) {
                            if (!TextUtils.isEmpty(photoDetailInfo.sdcardPath)
                                    && !TextUtils.isEmpty(detailInfo.sdcardPath)
                                    && photoDetailInfo.sdcardPath.equals(detailInfo.sdcardPath)) {
                                canPickNum += 1;
                                newList.add(detailInfo);
                            }
                        }
                    }
                }
                //当前potision条目中已选择的图片
                bundle.putSerializable("selected_picture", newList);
                bundle.putInt("canPickNum", mSelectedList != null ? (mCanPickNum - (mSelectedList.size() - canPickNum)) : mCanPickNum);
                Constant.mContextMap.put("photoAlbum", mPictureFolders.get(position));
                if (mTransitionCallback != null) {
                    mTransitionCallback.onTransitionCallback(bundle);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.actionbarwidget_more_text) {//取消按钮
            if (getActivity() != null) {
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.enter_left_right, R.anim.exit_left_right);
            }

        }
    }

    public void setTransitionCallback(TransitionCallback callback) {
        mTransitionCallback = callback;
    }

    private ArrayList<PhotoDetailInfo> reLoadData(Intent data) {
        if (data != null) {
            mNewSelectedList = new ArrayList<>();
            int position = data.getIntExtra("position", -1);
            //当没有在全部相册里选择的时候取到其他相册的已选照片
            if (position > 1 && mPictureFolders != null && mPictureFolders.size() > 0) {
                for (int i = 1; i < mPictureFolders.size(); i++) {
                    if (i != position && mPictureFolders.get(i).getAllPictureList() != null && mPictureFolders.get(i).getAllPictureList().size() > 0) {
                        for (int n = 0; n < mPictureFolders.get(i).getAllPictureList().size(); n++) {
                            if (mSelectedList != null && mSelectedList.size() > 0) {
                                for (int j = 0; j < mSelectedList.size(); j++) {
                                    if (mPictureFolders.get(i).getAllPictureList().get(n).sdcardPath.equals(mSelectedList.get(j).sdcardPath)) {
                                        mNewSelectedList.add(mSelectedList.get(j));
                                    }
                                }
                            }
                        }
                    }
                }
            }
            //获取全部选择返回的照片
            if (data.getExtras() != null && data.getExtras().get("all_path") != null) {
                ArrayList<PhotoDetailInfo> rPhotoDetailInfo = (ArrayList<PhotoDetailInfo>) data.getExtras().get("all_path");
                mNewSelectedList.addAll(rPhotoDetailInfo);
            }
        }
        return mNewSelectedList;
    }

    public static int dp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PhotoAlbumActivity.PICK_FROM_FILE://相册列表返回
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        mNewSelectedList = reLoadData(data);
                        Intent newData = new Intent();
                        newData.putExtra("all_path", mNewSelectedList);
                        if (getActivity() != null) {
                            getActivity().setResult(RESULT_OK, newData);
                            getActivity().finish();
                            getActivity().overridePendingTransition(R.anim.enter_left_right, R.anim.exit_left_right);
                        }
                    }
                    break;
                }
        }
    }
}
