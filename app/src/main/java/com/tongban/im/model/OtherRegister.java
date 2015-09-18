package com.tongban.im.model;

import android.text.TextUtils;

import com.tongban.umeng.UMConstant;

import java.io.Serializable;

/**
 * 第三方登录授权需要的信息
 * Created by zhangleilei on 9/16/15.
 */
public class OtherRegister implements Serializable {


    private String openId;
    private String nickName;
    private String headimgurl;

    private String type;

    private ImageUrl urls;

    public ImageUrl getUrls() {
        if (!TextUtils.isEmpty(type) && !TextUtils.isEmpty(headimgurl)) {
            if (type.equals(UMConstant.WECHAT)) {
                urls = new ImageUrl();
                String subUrl = headimgurl.substring(0, headimgurl.length() - 1);
                String min = subUrl + "64";
                String mid = subUrl + "132";
                String max = subUrl + "0";
                urls.setMin(min);
                urls.setMid(mid);
                urls.setMax(max);
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
