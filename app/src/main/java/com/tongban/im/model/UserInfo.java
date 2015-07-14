package com.tongban.im.model;

/**
 * Created by dangdang on 15/7/3.
 */
public class UserInfo {

    private String im_app_key;
    private String im_app_secret;
    private String im_bind_token;

    public String getIm_app_key() {
        return im_app_key;
    }

    public void setIm_app_key(String im_app_key) {
        this.im_app_key = im_app_key;
    }

    public String getIm_app_secret() {
        return im_app_secret;
    }

    public void setIm_app_secret(String im_app_secret) {
        this.im_app_secret = im_app_secret;
    }

    public String getIm_bind_token() {
        return im_bind_token;
    }

    public void setIm_bind_token(String im_bind_token) {
        this.im_bind_token = im_bind_token;
    }


}
