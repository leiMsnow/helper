package com.tongban.im.model;

import java.io.Serializable;

/**
 * 第三方登录授权需要的信息
 * Created by zhangleilei on 9/16/15.
 */
public class OtherRegister implements Serializable {


    public static final String WECHAT = "weChat";

    private String openId;
    private String nickName;
    private String headimgurl;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }
}
