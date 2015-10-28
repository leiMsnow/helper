package com.tb.api.model;

/**
 * 标签
 * Created by Cheney on 15/9/10.
 */
public class Tag {
    // id
    private String tag_id;
    // 标签名字
    private String tag_name;
    // 标签配图的url
    private String tag_desc;
    // 标签分类
    private String tag_type;
    // 标签子分类
    private String tag_subtype;
    // 状态
    private String status;
    // 创建时间
    private String c_time;
    // 修改时间
    private String m_time;

    public String getTag_id() {
        return tag_id;
    }

    public void setTag_id(String tag_id) {
        this.tag_id = tag_id;
    }

    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

    public String getTag_desc() {
        return tag_desc;
    }

    public void setTag_desc(String tag_desc) {
        this.tag_desc = tag_desc;
    }

    public String getTag_type() {
        return tag_type;
    }

    public void setTag_type(String tag_type) {
        this.tag_type = tag_type;
    }

    public String getTag_subtype() {
        return tag_subtype;
    }

    public void setTag_subtype(String tag_subtype) {
        this.tag_subtype = tag_subtype;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getC_time() {
        return c_time;
    }

    public void setC_time(String c_time) {
        this.c_time = c_time;
    }

    public String getM_time() {
        return m_time;
    }

    public void setM_time(String m_time) {
        this.m_time = m_time;
    }
}
