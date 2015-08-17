package com.tongban.im.model;

/**
 * 获取七牛token的model
 * Created by Cheney on 15/8/17.
 */
public class QiniuToken {
    // 过期时间
    private int expire_time;
    // 上传token
    private String upload_token;

    public int getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(int expire_time) {
        this.expire_time = expire_time;
    }

    public String getUpload_token() {
        return upload_token;
    }

    public void setUpload_token(String upload_token) {
        this.upload_token = upload_token;
    }
}
