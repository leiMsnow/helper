package com.tb.api.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.tb.api.model.topic.TopicType;
import com.tongban.corelib.base.ActivityContainer;
import com.tongban.corelib.base.BaseApplication;
import com.tongban.corelib.utils.Constants;
import com.tongban.corelib.utils.LogUtil;
import com.tongban.corelib.utils.SPUtils;

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
                    mApi = new TransferCenter(BaseApplication.getInstance());
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
     * 打开专题搜索结果界面
     *
     * @param isExpanded 是否展开搜索框 true展开；false不展开
     * @param keyword    搜索的关键字
     */
    public void startThemeSearchResult(boolean isExpanded, @Nullable String keyword) {
        Uri uri = Uri.parse(APP_SCHEME + mContext.getApplicationInfo().packageName).buildUpon()
                .appendPath(TransferPathPrefix.THEME_SEARCH_RESULT)
                .appendQueryParameter("keyword", keyword)
                .appendQueryParameter("isExpanded", isExpanded ? "true" : "false")
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
    public void startUserCenter(String visitorId, boolean isOpenMain) {

        String pathPrefix = TransferPathPrefix.USER_CENTER;
        if (!startLogin(isOpenMain, false))
            return;
        String center_tag = "visit_center";
        if (visitorId.equals(SPUtils.get(mContext, Constants.USER_ID, ""))) {
            center_tag = "my_center";
            pathPrefix = TransferPathPrefix.MY_CENTER;
        }
        Uri uri = Uri.parse(APP_SCHEME + mContext.getApplicationInfo().packageName).buildUpon()
                .appendPath(pathPrefix)
                .appendQueryParameter("visitorId", visitorId)
                .appendQueryParameter("center_tag", center_tag)
                .build();
        Log.d("TransferCenter", visitorId);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    public void startUserCenter(String visitorId) {
        startUserCenter(visitorId, false);
    }


    /**
     * 打开话题详情
     *
     * @param topicId
     * @param topicType 0 个人话题 1官方话题 {@link TopicType}
     */
    public void startTopicDetails(String topicId, String topicType) {
        String pathPrefix = TransferPathPrefix.TOPIC_DETAILS;

        if (topicType.equals(TopicType.EVALUATION)
                || topicType.equals(TopicType.THEME)) {
            pathPrefix = TransferPathPrefix.TOPIC_OFFICIAL;
        }

        Uri uri = Uri.parse(APP_SCHEME + mContext.getApplicationInfo().packageName).buildUpon()
                .appendPath(pathPrefix)
                .appendQueryParameter(ApiConstants.KEY_TOPIC_ID, topicId)
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
                .appendPath(pathPrefix)
                .appendQueryParameter(ApiConstants.KEY_THEME_ID, themeId)
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
                .appendPath(pathPrefix)
                .appendQueryParameter(ApiConstants.KEY_PRODUCT_BOOK_ID, productBookId)
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
                .appendPath(pathPrefix)
                .appendQueryParameter(ApiConstants.KEY_GROUP_ID, groupId)
                .build();
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.putExtra(ApiConstants.KEY_IS_JOIN, isJoin);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    /**
     * 是否已经登录，没有登录则跳转到登录界面
     *
     * @param isOpenMain    是否跳转到main界面，有的界面需要登录完成后返回
     * @param isOtherClient 是否其它设备登录
     * @return true 已经登录；false 未登录
     */
    public boolean startLogin(boolean isOpenMain, boolean isOtherClient) {
        if (SPUtils.get(mContext, Constants.USER_ID, "").toString().equals("")) {
            if (isOpenMain) {
                ActivityContainer.getInstance().finishActivity();
            }
            Uri uri = Uri.parse(APP_SCHEME + mContext.getApplicationInfo().packageName).buildUpon()
                    .appendPath(TransferPathPrefix.LOGIN).build();
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.putExtra(ApiConstants.KEY_IS_MAIN, isOpenMain);
            intent.putExtra(ApiConstants.KEY_OTHER_CLIENT, isOtherClient);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
            return false;
        }
        return true;
    }

    public boolean startLoginOtherClient() {
        return startLogin(true, true);
    }

    public boolean startLogin() {
        return startLogin(true, false);
    }

    /**
     * 打开注册界面
     *
     * @param editUser 编辑用户,该情况为用户注册成功后，昵称没有填写,所以需要进入编辑用户信息界面
     */
    public void startRegister(boolean editUser) {
        Uri uri = Uri.parse(APP_SCHEME + mContext.getApplicationInfo().packageName).buildUpon()
                .appendPath(TransferPathPrefix.REGISTER)
                .build();
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        Bundle bundle = new Bundle();
        bundle.putBoolean(ApiConstants.KEY_EDIT_USER, editUser);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);

    }

    public void startRegister() {
        startRegister(false);
    }

    /**
     * 第三方注册
     *
     * @param otherRegister
     * @param type
     */
    public void startOtherRegister(String otherRegister, String type) {
        Uri uri = Uri.parse(APP_SCHEME + mContext.getApplicationInfo().packageName).buildUpon()
                .appendPath(TransferPathPrefix.OTHER_REGISTER)
                .build();
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        Bundle bundle = new Bundle();
        bundle.putString(ApiConstants.OTHER_REGISTER_INFO, otherRegister);
        bundle.putString(ApiConstants.OTHER_REGISTER_TYPE, type);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    /**
     * 打开关注、粉丝界面
     *
     * @param tag    标记{@link ApiConstants TAG_FANS TAG_FOLLOW}
     * @param userId 用户Id
     */
    public void startRelationship(String tag, String userId) {

        if (!startLogin())
            return;

        Uri uri = Uri.parse(APP_SCHEME + mContext.getApplicationInfo().packageName).buildUpon()
                .appendPath(TransferPathPrefix.RELATIONSHIP)
                .appendQueryParameter(ApiConstants.KEY_TAG, tag)
                .appendQueryParameter(Constants.USER_ID, userId).build();
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);

    }


    /**
     * 打开我的圈子列表
     *
     * @param userId 用户Id
     */
    public void startMyGroupList(String userId) {

        if (!startLogin())
            return;

        Uri uri = Uri.parse(APP_SCHEME + mContext.getApplicationInfo().packageName).buildUpon()
                .appendPath(TransferPathPrefix.MY_GROUP_LIST)
                .appendQueryParameter(Constants.USER_ID, userId).build();
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);

    }

    /**
     * 点击图片链接跳转
     *
     * @param linkUrl
     */
    public void startLinkUrl(String linkUrl) {
        if (linkUrl == null || "".equals(linkUrl.trim())) {
            return;
        }
        String[] split = linkUrl.split("://");
        if (split.length == 2) {
            LogUtil.d("split", split[0] + "," + split[1]);
            if ("topic".equals(split[0])) {
                startTopicDetails(split[1], TopicType.THEME);
            } else if ("product".equals(split[0])) {
                startProductBook(split[1]);
            }
        }
    }
}
