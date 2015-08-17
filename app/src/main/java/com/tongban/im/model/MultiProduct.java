package com.tongban.im.model;

import java.util.List;

/**
 * 专题的model
 * Created by chenenyu on 15/8/17.
 */
public class MultiProduct {
    private int id;
    // 标题
    private String title;
    // 专题头图
    private String headUrl;
    // 专题标签
    private String tag;
    // 专题描述
    private String desc;
    // 发布人头像
    private String user_portrait;
    // 发布人用户名
    private String user_name;
    // 发布人标签
    private String user_tag;
    // 发布时间
    private String create_time;
    // 单品列表
    private List<Product> product_list;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUser_portrait() {
        return user_portrait;
    }

    public void setUser_portrait(String user_portrait) {
        this.user_portrait = user_portrait;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_tag() {
        return user_tag;
    }

    public void setUser_tag(String user_tag) {
        this.user_tag = user_tag;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public List<Product> getProduct_list() {
        return product_list;
    }

    public void setProduct_list(List<Product> product_list) {
        this.product_list = product_list;
    }

}
