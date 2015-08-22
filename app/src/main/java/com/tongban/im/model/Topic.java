package com.tongban.im.model;

import android.content.Context;

import com.tongban.corelib.utils.DateUtils;

import java.util.List;

/**
 * 话题信息表
 * Created by fushudi on 2015/7/16.
 */
public class Topic {

    // 话题内容为文本
    public final static int TEXT = 0;
    // 话题内容为图片
    public final static int IMAGE = 1;


    // 话题ID
    private String topic_id;
    // 话题标题
    private String topic_title;
    // 话题内容
    private String topic_content;
    // 话题标签
    private String topic_tags;
    // 话题类型：0，用户话题；1：官方话题
    private String topic_type;
    // 用户回复数量
    private int comment_amount;
    // 用户收藏数量
    private int collect_amount;
    // 话题创建时间
    private long c_time;
    // 内容类型（文字或者图片）
    private int contentType;
    //用户信息
    private User user_info;
    //话题评论
    private List<TopicComment> topicReplyList;
    // 话题图片
    private List<ImageUrl> topic_img_url;

    public List<TopicComment> getTopicReplyList() {
        return topicReplyList;
    }

    public void setTopicReplyList(List<TopicComment> topicReplyList) {
        this.topicReplyList = topicReplyList;
    }

    public List<ImageUrl> getTopic_img_url() {
        return topic_img_url;
    }

    public void setTopic_img_url(List<ImageUrl> topic_img_url) {
        this.topic_img_url = topic_img_url;
    }

    public String getTopic_type() {
        return topic_type;
    }

    public void setTopic_type(String topic_type) {
        this.topic_type = topic_type;
    }

    public int getComment_amount() {
        return comment_amount;
    }

    public void setComment_amount(int comment_amount) {
        this.comment_amount = comment_amount;
    }

    public String getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(String topic_id) {
        this.topic_id = topic_id;
    }

    public int getContentType() {
        if (topic_img_url.size() > 0) {
            return contentType = IMAGE;
        } else {
            return contentType = TEXT;
        }
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

    public String getTopic_tags() {
        return topic_tags;
    }

    public void setTopic_tags(String topic_tags) {
        this.topic_tags = topic_tags;
    }

    public int getCollect_amount() {
        return collect_amount;
    }

    public void setCollect_amount(int collect_amount) {
        this.collect_amount = collect_amount;
    }

    public String getC_time(Context context) {
        return DateUtils.formatDateTime(c_time, context);
    }

    public void setC_time(long c_time) {
        this.c_time = c_time;
    }

    public User getUser_info() {
        return user_info;
    }

    public void setUser_info(User user_info) {
        this.user_info = user_info;
    }

}
