package com.tongban.im.model;


import java.util.List;

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
    private List<ImageUrl> theme_img_url;
    // 专题标签
    private String theme_tags;
    // 专题描述
    private String theme_content;
    // 状态
    private String status;
    // 发布人id
    private String user_id;
    // 收藏状态
    private boolean collect_status;
    // 发布时间
    private long c_time;
    // 修改时间
    private long m_time;
    //收藏数量
    private String collect_amount;

    public String getCollect_amount() {
        return collect_amount;
    }

    public void setCollect_amount(String collect_amount) {
        this.collect_amount = collect_amount;
    }

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

    public List<ImageUrl> getTheme_img_url() {
        return theme_img_url;
    }

    public void setTheme_img_url(List<ImageUrl> theme_img_url) {
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

    public boolean isCollect_status() {
        return collect_status;
    }

    public void setCollect_status(boolean collect_status) {
        this.collect_status = collect_status;
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
