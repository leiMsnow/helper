package com.tongban.im.model;

/**
 * 话题信息表
 * Created by fushudi on 2015/7/16.
 */
public class Topic {

    //话题内容为文本
    public final static int TEXT = 0;
    //话题内容为图片
    public final static int IMAGE = 1;
    //话题ID
    private String topic_id;
    // 话题标题
    private String topic_title;
    // 话题内容
    private String topic_content;
    //话题类型：0，用户话题；1：官方话题
    private String topic_type;
    //图片地址
    //回复数量
    private int amount;
    //内容类型（文字或者图片）
    private int contentType;
    //话题图片
    private ImageUrl[] topic_img_url;

    public ImageUrl[] getTopic_img_url() {
        return topic_img_url;
    }

    public void setTopic_img_url(ImageUrl[] topic_img_url) {
        this.topic_img_url = topic_img_url;
    }

    public String getTopic_type() {
        return topic_type;
    }

    public void setTopic_type(String topic_type) {
        this.topic_type = topic_type;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(String topic_id) {
        this.topic_id = topic_id;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public String getTopic_title() {
        return topic_title;
    }

    public String getTopic_content() {
        return topic_content;
    }

    public void setTopic_title(String topic_title) {
        this.topic_title = topic_title;
    }

    public void setTopic_content(String topic_content) {
        this.topic_content = topic_content;
    }

}
