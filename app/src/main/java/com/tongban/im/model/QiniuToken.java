package com.tongban.im.model;

/**
 * 获取七牛token的model
 * Created by Cheney on 15/8/17.
 */
public class QiniuToken {
    // 过期时间
    private int expireTime;
    // 上传token
    private String uploadToken;

    public int getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(int expireTime) {
        this.expireTime = expireTime;
    }

    public String getUploadToken() {
        return uploadToken;
    }

    public void setUploadToken(String uploadToken) {
        this.uploadToken = uploadToken;
    }
}
