package com.tongban.im.model;

import java.util.List;

/**
 * 用户信息model
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
    // im绑定token串，用于聊天
    private String im_bind_token;
    // 昵称
    private String nick_name;
    //出生日期
    private String birthday;
    // 年龄
    private String age;
    // 性别
    private String sex;
    // 账户状态：默认0，0已经注册未验证；1短信验证成功未绑定；2绑定IM成功并生效
    private String status;
    // 个性签名
    private String personal_sign;
    // 用户头像
    private ImageUrl portrait_url;
    //用户标签
    private String tags;
    //个人描述
    private String declaration;
    //用户地址
    private String address;
    //加入的群数量
    private int joined_group_amount;
    //粉丝数量
    private int fans_amount;
    //关注数量
    private int focused_amount;
    //地址类型
    private String address_type;
    //孩子信息
    private List<Child> child_info;


    public ImageUrl getPortrait_url() {
        return portrait_url;
    }

    public void setPortrait_url(ImageUrl portrait_url) {
        this.portrait_url = portrait_url;
    }

    public List<Child> getChild_info() {
        return child_info;
    }

    public void setChild_info(List<Child> child_info) {
        this.child_info = child_info;
    }

    public int getJoined_group_amount() {
        return joined_group_amount;
    }

    public void setJoined_group_amount(int joined_group_amount) {
        this.joined_group_amount = joined_group_amount;
    }

    public int getFans_amount() {
        return fans_amount;
    }

    public void setFans_amount(int fans_amount) {
        this.fans_amount = fans_amount;
    }

    public int getFocused_amount() {
        return focused_amount;
    }

    public void setFocused_amount(int focused_amount) {
        this.focused_amount = focused_amount;
    }

    public String getAddress_type() {
        return address_type;
    }

    public void setAddress_type(String address_type) {
        this.address_type = address_type;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

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
