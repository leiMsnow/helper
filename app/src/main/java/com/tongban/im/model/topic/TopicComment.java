package com.tongban.im.model.topic;

import android.content.Context;

import com.tongban.corelib.utils.DateUtils;
import com.tongban.im.model.ImageUrl;
import com.tongban.im.model.user.User;

import java.util.List;

/**
 * 话题评论信息表
 * Created by fushudi on 2015/8/1.
 */
public class TopicComment {
    // 话题评论Id
    private String comment_id;
    // 评论创建时间
    private long c_time;
    // 发评论的用户信息
    private User user_info;
    // 回复的话题信息
    private Topic topic_info;
    // 评论/回复内容
    private String comment_content;
    // 回复评论的Id
    private String replied_comment_id;
    // 回复评论的用户Id
    private String replied_user_id;
    // 回复评论的用户昵称
    private String replied_nick_name;

    private List<ImageUrl> comment_img_url;

    public List<ImageUrl> getComment_img_url() {
        return comment_img_url;
    }

    public void setComment_img_url(List<ImageUrl> comment_img_url) {
        this.comment_img_url = comment_img_url;
    }

    public String getReplied_user_id() {
        return replied_user_id;
    }

    public void setReplied_user_id(String replied_user_id) {
        this.replied_user_id = replied_user_id;
    }

    public String getReplied_nick_name() {
        return replied_nick_name;
    }

    public void setReplied_nick_name(String replied_nick_name) {
        this.replied_nick_name = replied_nick_name;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
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
