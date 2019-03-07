package com.dijkstra.photoselect;

import android.app.Application;

/**
 * @Description:
 * @Author: maoshenbo
 * @Date: 2019/3/7 下午5:45
 * @Version: 1.0
 */
public class MyApplication extends Application {

    private static Application mInstance = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static Application getInstance() {
        return mInstance;
    }
}
