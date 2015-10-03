package com.tongban.umeng.listener;

import android.content.Context;
import android.os.Bundle;

import com.tongban.corelib.utils.LogUtil;
import com.tongban.umeng.UMConstant;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.weixin.controller.UMWXHandler;

/**
 * 第三方授权获取授权的回调接口
 * Created by zhangleilei on 9/17/15.
 */
public class UMSocializeOauthListenerImpl implements
        SocializeListeners.UMAuthListener {

    private Context mContext;
    private UMSocialService mController;
    private UMSocializeOauthBackListener oauthBackListener;

    public UMSocializeOauthListenerImpl(Context context, UMSocializeOauthBackListener backListener) {
        this.mContext = context;
        this.mController = UMServiceFactory.getUMSocialService("com.umeng.login");
        this.oauthBackListener = backListener;
    }

    // 微信授权登录
    public void doWeCHatOauth() {
        addWeChatHandler();
        mController.doOauthVerify(mContext, SHARE_MEDIA.WEIXIN, this);
    }

    @Override
    public void onStart(SHARE_MEDIA share_media) {
        LogUtil.d("oauth-onStart" + share_media.toString());
    }

    @Override
    public void onComplete(Bundle bundle, SHARE_MEDIA share_media) {
        LogUtil.d("oauth-onComplete" + share_media.toString());
        oauthBackListener.oauthStart();
        //获取相关授权信息
        mController.getPlatformInfo(mContext, share_media
                , new UMSocializeOauthDataListenerImpl(share_media, oauthBackListener));
    }

    @Override
    public void onError(SocializeException e, SHARE_MEDIA share_media) {
        LogUtil.d("oauth-onError" + e.getMessage());
        oauthBackListener.oauthFailure("授权失败");
    }

    @Override
    public void onCancel(SHARE_MEDIA share_media) {
        LogUtil.d("oauth-onCancel" + share_media.toString());
        oauthBackListener.oauthFailure("已取消授权");
    }

    //添加微信平台
    private void addWeChatHandler() {
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(mContext,
                UMConstant.weChatAPPID,
                UMConstant.weChatAppSecret);
        wxHandler.addToSocialSDK();
        wxHandler.setRefreshTokenAvailable(false);
    }


}
