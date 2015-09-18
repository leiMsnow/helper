package com.tongban.umeng.listener;

/**
 * 授权结果回调
 */
public interface UMSocializeOauthBackListener {

    /**
     * 授权成功
     *
     * @param userInfo 用户信息
     * @param type     用户类型{@link com.tongban.umeng.UMConstant}
     */
    void oauthSuccess(String userInfo, String type);

    /**
     * 授权失败
     */
    void oauthFailure();
}