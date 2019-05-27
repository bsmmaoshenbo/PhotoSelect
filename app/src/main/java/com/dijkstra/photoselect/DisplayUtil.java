package com.dijkstra.photoselect;

import android.content.Context;

/**
 * @Description:
 * @Author: maoshenbo
 * @Date: 2019/5/27 2:37 PM
 * @Version: 1.0
 */
public class DisplayUtil {
    /**
     * 根据手机分辨率将 sp 转为 px
     *
     * @param context
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 根据手机分辨率将 dp 转为 px
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
