package com.dijkstra.photo.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.dijkstra.photoselect.Constant;
import com.dijkstra.photoselect.R;
import com.dijkstra.photoselect.callback.TransitionCallback;
import com.dijkstra.photoselect.fragment.PhotoAlbumFragment;
import com.dijkstra.photoselect.fragment.PhotoSelectFragment;

/**
 * @Description: 相册activity
 * @Author: maoshenbo
 * @Date: 2018/4/10 17:41
 * @Version: 2.0
 */
public class PhotoAlbumActivity extends AppCompatActivity implements TransitionCallback {

    public static final int PICK_FROM_FILE = 113;//相册
    public static final int SELECT_PHOTO_PREVIEW = 2000;//图片预览
    public static final int NO_LIMIT_NUM = 9999;

    private FragmentManager mFragmentManager;
    private Bundle mBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            savedInstanceState.putParcelable("android:support:fragments", null);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_album);

        mFragmentManager = getSupportFragmentManager();

        loadStartParams();
        initFirstFragment();
    }

    private void loadStartParams() {
        Intent intent = getIntent();
        if (intent != null) {
            mBundle = intent.getExtras();
        }
    }

    private void initFirstFragment() {
        PhotoAlbumFragment photoAlbumFragment = new PhotoAlbumFragment();
        photoAlbumFragment.setTransitionCallback(this);
        photoAlbumFragment.setArguments(mBundle);
        addFragment(photoAlbumFragment);
    }

    private void initSecondFragment(Bundle bundle) {
        PhotoSelectFragment photoSelectFragment = new PhotoSelectFragment();
        photoSelectFragment.setArguments(bundle);
        addFragment(photoSelectFragment);
    }

    private void addFragment(Fragment fragment) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_right_left, R.anim.exit_right_left, R.anim.enter_left_right, R.anim.exit_left_right)
                .replace(R.id.fl_container, fragment)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        if (mFragmentManager.getBackStackEntryCount() == 1) {//fragment退栈
            finish();
            overridePendingTransition(R.anim.enter_left_right, R.anim.exit_left_right);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onTransitionCallback(Bundle bundle) {
        //fragment页面切换
        initSecondFragment(bundle);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Constant.mContextMap.remove("photoAlbum");
    }
}