package com.tongban.im.model;

import java.util.Date;
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
    private String group_avatar;
    /**
     * 是否需要验证 0:无需验证；1需要验证
     */
    private String verify_user;
    /**
     * 群主的用户ID
     */
    private String user_id;
    /**
     * 注册时间
     */
    private Date register_time;
    /**
     * 状态 0:表示无效 1:表示有效
     */
    private String status;
    /**
     * 群主信息
     */
    private User owner_info;
    /**
     * 群成员信息
     */
    private List<User> members;

    //标签
    private String tags;

   private boolean is_joined;

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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Date getRegister_time() {
        return register_time;
    }

    public void setRegister_time(Date register_time) {
        this.register_time = register_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
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

    public String getGroup_avatar() {
        return group_avatar;
    }

    public void setGroup_avatar(String group_avatar) {
        this.group_avatar = group_avatar;
    }

    public String getVerify_user() {
        return verify_user;
    }

    public void setVerify_user(String verify_user) {
        this.verify_user = verify_user;
    }

    public User getOwner_info() {
        return owner_info;
    }

    public void setOwner_info(User owner_info) {
        this.owner_info = owner_info;
    }

    public boolean is_joined() {
        return is_joined;
    }

    public void setIs_joined(boolean is_joined) {
        this.is_joined = is_joined;
    }
}
