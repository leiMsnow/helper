package com.tongban.im.common;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.tongban.im.App;

/**
 * 跳转中心
 * Created by zhangleilei on 8/19/15.
 */
public class TransferCenter {

    private static TransferCenter mApi;
    private Context mContext;

    private TransferCenter(Context context) {
        this.mContext = context;
    }

    public static TransferCenter getInstance() {
        if (mApi == null) {
            synchronized (TransferCenter.class) {
                if (mApi == null) {
                    mApi = new TransferCenter(App.getInstance());
                }
            }
        }
        return mApi;
    }

    public static final String APP_SCHEME = "tongban://";

    /**
     * 打开搜索界面
     *
     * @param pathPrefix 跳转的前缀参考 {@link TransferPathPrefix}
     * @param keyword    关键字,可以为空
     */
    public void startSearch(String pathPrefix, @Nullable String keyword) {
        Uri uri = Uri.parse(APP_SCHEME + mContext.getApplicationInfo().packageName).buildUpon()
                .appendPath(pathPrefix).appendPath("search")
                .appendQueryParameter("keyword", keyword)
                .build();
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }
}
