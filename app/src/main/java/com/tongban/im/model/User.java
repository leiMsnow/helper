package com.tongban.im.model;

/**
 * 用户信息表
 * Created by zhangleilei on 15/7/3.
 */
public class User {

    // 用户id
    private String user_id;
    // 手机号码
    private String mobile_phone;
    // 用户免认证token
    private String freeauth_token;
    // 第三方账号类型：默认为0，1QQ 2微信 3淘宝 4微博 5当当 6京东 7美丽说
    private String thirdparty_access_type;
    // 昵称
    private String nick_name;
    // im绑定token串，用于聊天
    private String im_bind_token;
    // 账户状态：默认0，0已经注册未验证；1短信验证成功未绑定；2绑定IM成功并生效
    private String status;
    // 年龄
    private String age;
    // 性别
    private String sex;
    // 个性签名
    private String personal_sign;
    // 用户头像
    private String portrait_url;
    //用户标签
    private String tags;
    //
    private String declaration;


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getMobile_phone() {
        return mobile_phone;
    }

    public void setMobile_phone(String mobile_phone) {
        this.mobile_phone = mobile_phone;
    }

    public String getFreeauth_token() {
        return freeauth_token;
    }

    public void setFreeauth_token(String freeauth_token) {
        this.freeauth_token = freeauth_token;
    }

    public String getThirdparty_access_type() {
        return thirdparty_access_type;
    }

    public void setThirdparty_access_type(String thirdparty_access_type) {
        this.thirdparty_access_type = thirdparty_access_type;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getIm_bind_token() {
        return im_bind_token;
    }

    public void setIm_bind_token(String im_bind_token) {
        this.im_bind_token = im_bind_token;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPersonal_sign() {
        return personal_sign;
    }

    public void setPersonal_sign(String personal_sign) {
        this.personal_sign = personal_sign;
    }

    public String getPortrait_url() {
        return portrait_url;
    }

    public void setPortrait_url(String portrait_url) {
        this.portrait_url = portrait_url;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getDeclaration() {
        return declaration;
    }

    public void setDeclaration(String declaration) {
        this.declaration = declaration;
    }

}
