package com.tongban.im.model;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * 第三方登录授权需要的信息
 * Created by zhangleilei on 9/16/15.
 */
public class OtherRegister implements Serializable {


    public static final String DANG = "1";
    public static final String WECHAT = "2";
    public static final String QQ = "3";
    public static final String SINA = "4";

    private String openId;
    private String nickName;
    private String headimgurl;

    private String type;

    private ImageUrl urls;

    public ImageUrl getUrls() {
        if (!TextUtils.isEmpty(type) && !TextUtils.isEmpty(headimgurl)) {
            if (type.equals(WECHAT)) {
                urls = new ImageUrl();
                String min = headimgurl.substring(0, headimgurl.length() - 2) + "64";
                String mid = headimgurl.substring(0, headimgurl.length() - 2) + "132";
                urls.setMin(min);
                urls.setMin(mid);
                urls.setMin(headimgurl);
            }
        }
        return urls;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
