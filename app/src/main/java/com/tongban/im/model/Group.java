package com.tongban.im.model;

import java.util.Date;

/**
 * 群组
 * author: chenenyu 15/7/14
 */
public class Group {
    /**
     * id
     */
    private String group_id;
    /**
     * 群组名称
     */
    private String group_name;
    /**
     * 群组介绍
     */
    private String declaration;
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
}
