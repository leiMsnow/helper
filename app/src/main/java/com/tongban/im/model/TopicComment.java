package com.tongban.im.model;

import android.content.Context;

import com.tongban.corelib.utils.DateUtils;

/**
 * 话题评论信息表
 * Created by fushudi on 2015/8/1.
 */
public class TopicComment {
    // 话题回复Id
    private String comment_id;
    // 创建时间
    private long c_time;
    // 回复的用户信息
    private User user_info;
    // 话题信息
    private Topic topic_info;
    // 回复内容
    private String comment_content;
    // 回复的回复Id
    private String replied_comment_id;

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getC_time(Context context) {

        return DateUtils.formatDateTime(c_time,context);
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

    public Topic getTopic_info() {
        return topic_info;
    }

    public void setTopic_info(Topic topic_info) {
        this.topic_info = topic_info;
    }

    public String getComment_content() {
        return comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    public String getReplied_comment_id() {
        return replied_comment_id;
    }

    public void setReplied_comment_id(String replied_comment_id) {
        this.replied_comment_id = replied_comment_id;
    }
}
