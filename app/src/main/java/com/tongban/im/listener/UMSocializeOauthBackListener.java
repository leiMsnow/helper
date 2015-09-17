package com.tongban.im.listener;

/**
 * 授权结果回调
 */
public interface UMSocializeOauthBackListener {

    /**
     * 授权成功
     */
    void oauthSuccess();

    /**
     * 授权失败
     */
    void oauthFailure();

    /**
     * 该账户为新授权，需要绑定
     */
    void oauthNewAccount();
}