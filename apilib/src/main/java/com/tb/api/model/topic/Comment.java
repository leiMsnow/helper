package com.tb.api.model.topic;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tb.api.model.user.User;
import com.tongban.corelib.utils.DateUtils;

/**
 * 话题评论信息表
 * Created by fushudi on 2015/8/1.
 */
public class Comment {
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

    private CommentContent commentContent;
    // 回复评论的Id
    private String replied_comment_id;
    // 回复评论的用户Id
    private String replied_user_id;
    // 回复评论的用户昵称
    private String replied_nick_name;

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


    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    public CommentContent getCommentContent() {
        if (commentContent != null)
            return commentContent;
        if (!TextUtils.isEmpty(comment_content)) {
            try {
                commentContent = JSON.parseObject(comment_content,
                        new TypeReference<CommentContent>() {
                        });
            } catch (Exception e) {

            }
        }
        return commentContent;
    }

    public String getReplied_comment_id() {
        return replied_comment_id;
    }

    public void setReplied_comment_id(String replied_comment_id) {
        this.replied_comment_id = replied_comment_id;
    }
}
