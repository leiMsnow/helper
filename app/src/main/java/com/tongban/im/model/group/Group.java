package com.tongban.im.model.group;

import android.text.TextUtils;

import com.tongban.im.model.ImageUrl;
import com.tongban.im.model.user.User;

import java.io.Serializable;

/**
 * 群组
 * author: chenenyu 15/7/14
 */
public class Group implements Serializable {
    //群Id
    private String group_id;
    //群名称
    private String group_name;
    //群类型
    private int group_type;
    //地址Id
    private String address_id;
    //地址
    private String address;
    // 群组介绍
    private String declaration;
    //经度
    private double longitude;
    //纬度
    private double latitude;
    //群头像
    private ImageUrl group_avatar;
    //群主信息
    private User user_info;
    //星座
    private String constellation;
    //距离
    private String distance;
    //是否可以加入;true 可以加入；false 已经属于这个圈子
    private boolean allow_add = true;
    //成员数量
    private String member_amount;
    //年龄
    private String age;
    //最后一级地址
    private String lastAddress;
    //是否需要验证
    private boolean verify_user;

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getLastAddress() {
        if (!TextUtils.isEmpty(address)) {
            lastAddress = address;
            if (lastAddress.lastIndexOf(",") != -1) {
                lastAddress = lastAddress.substring(lastAddress.lastIndexOf(",") + 1);
                return lastAddress;
            }
        }
        return "";
    }

    public boolean isVerify_user() {
        return verify_user;
    }

    public void setVerify_user(boolean verify_user) {
        this.verify_user = verify_user;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public int getGroup_type() {
        return group_type;
    }

    public String getGroupType() {
        switch (group_type) {
            case GroupType.ACTIVITY:
                return "活动圈";
            case GroupType.AGE:
                return "同龄圈";
            case GroupType.CITY:
                return "同城圈";
            case GroupType.CLASSMATE:
                return "同学圈";
            case GroupType.LIFE:
                return "生活圈";
        }
        return "";
    }

    public void setGroup_type(int group_type) {
        this.group_type = group_type;
    }

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDeclaration() {
        return declaration;
    }

    public void setDeclaration(String declaration) {
        this.declaration = declaration;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public ImageUrl getGroup_avatar() {
        return group_avatar;
    }

    public void setGroup_avatar(ImageUrl group_avatar) {
        this.group_avatar = group_avatar;
    }

    public User getUser_info() {
        return user_info;
    }

    public void setUser_info(User user_info) {
        this.user_info = user_info;
    }

    public String getConstellation() {
        return constellation;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public boolean isAllow_add() {
        return allow_add;
    }

    public void setAllow_add(boolean allow_add) {
        this.allow_add = allow_add;
    }

    public String getMember_amount() {
        return member_amount;
    }

    public void setMember_amount(String member_amount) {
        this.member_amount = member_amount;
    }
}
