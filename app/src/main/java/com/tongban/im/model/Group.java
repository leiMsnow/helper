package com.tongban.im.model;

import java.util.List;

/**
 * 群组
 * author: chenenyu 15/7/14
 */
public class Group {
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
    // 群主的用户ID
    private String group_owner_id;
    // 群主信息
    private User owner_info;
    //星座
    private String constellation;
    //距离
    private String distance;
    //年龄
    private String age;
    //是否已经加入
    private boolean isAllowAdd;

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
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

    public String getGroup_owner_id() {
        return group_owner_id;
    }

    public void setGroup_owner_id(String group_owner_id) {
        this.group_owner_id = group_owner_id;
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

    public String getDeclaration() {
        return declaration;
    }

    public void setDeclaration(String declaration) {
        this.declaration = declaration;
    }

    public int getGroup_type() {
        return group_type;
    }

    public void setGroup_type(int group_type) {
        this.group_type = group_type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public User getOwner_info() {
        return owner_info;
    }

    public void setOwner_info(User owner_info) {
        this.owner_info = owner_info;
    }

    public boolean is_joined() {
        return isAllowAdd;
    }

    public void setAllowAdd(boolean allowAdd) {
        this.isAllowAdd = allowAdd;
    }
}
