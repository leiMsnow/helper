package com.tongban.im.model;


/**
 * 专题的model
 * Created by chenenyu on 15/8/17.
 */
public class MultiProduct {
    // 专题id
    private String theme_id;
    // 标题
    private String theme_title;
    // 专题图片
    private String[] theme_img_url;
    // 专题标签
    private String theme_tags;
    // 专题描述
    private String theme_content;
    // 状态
    private String status;
    // 发布人id
    private String user_id;
    // 发布时间
    private long c_time;
    // 修改时间
    private long m_time;

    public String getTheme_id() {
        return theme_id;
    }

    public void setTheme_id(String theme_id) {
        this.theme_id = theme_id;
    }

    public String getTheme_title() {
        return theme_title;
    }

    public void setTheme_title(String theme_title) {
        this.theme_title = theme_title;
    }

    public String[] getTheme_img_url() {
        return theme_img_url;
    }

    public void setTheme_img_url(String[] theme_img_url) {
        this.theme_img_url = theme_img_url;
    }

    public String getTheme_tags() {
        return theme_tags;
    }

    public void setTheme_tags(String theme_tags) {
        this.theme_tags = theme_tags;
    }

    public String getTheme_content() {
        return theme_content;
    }

    public void setTheme_content(String theme_content) {
        this.theme_content = theme_content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public long getC_time() {
        return c_time;
    }

    public void setC_time(long c_time) {
        this.c_time = c_time;
    }

    public long getM_time() {
        return m_time;
    }

    public void setM_time(long m_time) {
        this.m_time = m_time;
    }
}
