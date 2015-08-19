package com.tongban.corelib;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * 跳转中心
 * Created by zhangleilei on 8/19/15.
 */
public class TransferCenter {

    /**
     * 打开搜索圈子界面
     *
     * @param mContext
     * @param keyword  关键字,可以为空
     */
    public static void startGroupSearch(Context mContext, @Nullable String keyword) {
        Uri uri = Uri.parse("tongban://" + mContext.getApplicationInfo().packageName).buildUpon()
                .appendPath("search_group")
                .appendQueryParameter("keyword", keyword)
                .build();
        mContext.startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }
}
