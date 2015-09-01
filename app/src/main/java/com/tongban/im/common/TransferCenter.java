package com.tongban.im.common;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.tongban.corelib.base.ActivityContainer;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.im.App;

import org.w3c.dom.Text;

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
                .appendPath(pathPrefix)
                .appendQueryParameter("keyword", keyword)
                .build();
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    /**
     * 打开用户中心界面
     *
     * @param visitorId
     */
    public void startUserCenter(String visitorId) {

        String pathPrefix = TransferPathPrefix.USER_CENTER;
        if (!startLogin())
            return;

        if (visitorId.equals(SPUtils.get(mContext, Consts.USER_ID, ""))) {
            pathPrefix = TransferPathPrefix.MY_CENTER;
        }
        Uri uri = Uri.parse(APP_SCHEME + mContext.getApplicationInfo().packageName).buildUpon()
                .appendPath(pathPrefix)
                .appendQueryParameter("visitorId", visitorId)
                .build();
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    /**
     * 打开话题详情
     *
     * @param topicId
     */
    public void startTopicDetails(String topicId) {
        String pathPrefix = TransferPathPrefix.TOPIC_DETAILS;

        Uri uri = Uri.parse(APP_SCHEME + mContext.getApplicationInfo().packageName).buildUpon()
                .appendPath(pathPrefix)
                .appendQueryParameter(Consts.KEY_TOPIC_ID, topicId)
                .build();
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    /**
     * 打开专题详情页
     *
     * @param themeId 专题id
     */
    public void startThemeDetails(String themeId) {
        String pathPrefix = TransferPathPrefix.THEME_DETAILS;
        Uri uri = Uri.parse(APP_SCHEME + mContext.getApplicationInfo().packageName).buildUpon()
                .appendPath(pathPrefix).appendQueryParameter(Consts.KEY_THEME_ID, themeId)
                .build();
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    /**
     * 打开图书单品页
     *
     * @param productBookId 图书id
     */
    public void startProductBook(String productBookId) {
        String pathPrefix = TransferPathPrefix.PRODUCT_BOOK;
        Uri uri = Uri.parse(APP_SCHEME + mContext.getApplicationInfo().packageName).buildUpon()
                .appendPath(pathPrefix).appendQueryParameter(Consts.KEY_PRODUCT_BOOK_ID, productBookId)
                .build();
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    /**
     * 打开圈子详情页
     *
     * @param groupId 圈子Id
     */
    public void startGroupInfo(String groupId, boolean isJoin) {
        String pathPrefix = TransferPathPrefix.GROUP_INFO;
        Uri uri = Uri.parse(APP_SCHEME + mContext.getApplicationInfo().packageName).buildUpon()
                .appendPath(pathPrefix).appendQueryParameter(Consts.KEY_GROUP_ID, groupId)
                .build();
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.putExtra(Consts.KEY_IS_JOIN, isJoin);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    /**
     * 是否已经登录，没有登录则跳转到登录界面
     *
     * @param isOpenMain 是否跳转到main界面，有的界面需要登录完成后返回
     * @return true 已经登录；false 未登录
     */
    public boolean startLogin(boolean isOpenMain) {
        if (SPUtils.get(mContext, Consts.USER_ID, "").toString().equals("")) {
            if (isOpenMain){
                ActivityContainer.getInstance().finishActivity();
            }
            Uri uri = Uri.parse(APP_SCHEME + mContext.getApplicationInfo().packageName).buildUpon()
                    .appendPath(TransferPathPrefix.LOGIN).build();
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.putExtra(Consts.KEY_IS_MAIN, isOpenMain);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
            return false;
        }
        return true;
    }

    public boolean startLogin() {
        return startLogin(false);
    }


}
