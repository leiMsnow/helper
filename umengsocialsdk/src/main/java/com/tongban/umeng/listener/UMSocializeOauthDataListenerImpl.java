package com.tongban.umeng.listener;

import com.alibaba.fastjson.JSON;
import com.tongban.corelib.utils.LogUtil;
import com.tongban.umeng.UMConstant;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.listener.SocializeListeners;

import java.util.Map;

/**
 * 第三方授权获取数据的回调接口
 * Created by zhangleilei on 9/17/15.
 */
public class UMSocializeOauthDataListenerImpl implements SocializeListeners.UMDataListener {

    private UMSocializeOauthBackListener oauthBackListener;
    private SHARE_MEDIA shareMedia;
    private String type;

    public UMSocializeOauthDataListenerImpl(SHARE_MEDIA shareMedia,
                                            UMSocializeOauthBackListener oauthBackListener) {
        this.shareMedia = shareMedia;
        this.oauthBackListener = oauthBackListener;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onComplete(int status, Map<String, Object> info) {
        if (status == 200 && info != null) {
            if (shareMedia == SHARE_MEDIA.WEIXIN) {
                type = UMConstant.WECHAT;
            }
            final String userInfoJson = JSON.toJSONString(info);
            LogUtil.d("getPlatformInfo-onComplete", userInfoJson);
            oauthBackListener.oauthSuccess(userInfoJson, type);
        } else {
            LogUtil.d("getPlatformInfo-onComplete", "发生错误：" + status);
        }
    }
}
