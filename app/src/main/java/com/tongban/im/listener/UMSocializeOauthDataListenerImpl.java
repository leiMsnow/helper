package com.tongban.im.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tongban.corelib.base.api.ApiCallback;
import com.tongban.corelib.utils.LogUtil;
import com.tongban.im.api.AccountApi;
import com.tongban.im.common.TransferCenter;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.OtherRegister;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.listener.SocializeListeners;

import java.util.Map;

import de.greenrobot.event.EventBus;

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
                type = OtherRegister.WECHAT;
            }
            final String userInfoJson = JSON.toJSONString(info);
            LogUtil.d("getPlatformInfo-onComplete", userInfoJson);
            OtherRegister otherRegister = JSON.parseObject(userInfoJson,
                    new TypeReference<OtherRegister>() {
                    });
            AccountApi.getInstance().otherLogin(otherRegister.getOpenId(), type,
                    new ApiCallback() {
                        @Override
                        public void onStartApi() {

                        }

                        @Override
                        public void onComplete(Object obj) {
                            BaseEvent.UserLoginEvent userLoginEvent =
                                    AccountApi.getInstance().loginSuccess(obj, null);
                            EventBus.getDefault().post(userLoginEvent);
                            oauthBackListener.oauthSuccess();
                        }

                        @Override
                        public void onFailure(DisplayType displayType, Object errorObj) {
                            TransferCenter.getInstance().startRegister(userInfoJson, type, false);
                            oauthBackListener.oauthNewAccount();
                        }
                    });
        } else {
            LogUtil.d("getPlatformInfo-onComplete", "发生错误：" + status);
        }
    }
}
