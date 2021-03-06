package com.tb.api.model.topic;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tb.api.model.user.User;
import com.tongban.corelib.utils.DateUtils;

/**
 * 话题信息表
 * Created by fushudi on 2015/7/16.
 */
public class Topic {

    // 话题内容为文本
    public final static int TEXT = 0;
    // 话题内容为图片
    public final static int IMAGE = 1;
    // 我的话题 - 我收藏话题列表
    public final static int MY_COLLECT_TOPIC_LIST = 0;
    // 我的话题 - 我发起的话题
    public final static int MY_SEND_TOPIC_LIST = 1;
    // 话题ID
    private String topic_id;
    // 话题标题
    private String topic_title;
    // 话题内容
    private String topic_content;
    // 话题标签
    private String topic_tags;
    /**
     * 话题类型：
     * {@Link TopicType}
     */
    private String topic_type;
    // 用户回复数量
    private String comment_amount;
    // 用户收藏数量
    private String collect_amount;
    // 话题创建时间
    private long c_time;
    //话题回复时间
    private long m_time;
    //用户信息
    private User user_info;
    //话题评论
    private Comment topicComment;
    //收藏状态
    private boolean collect_status;

    private TopicContent topicContent;

    public boolean isCollect_status() {
        return collect_status;
    }

    public void setCollect_status(boolean collect_status) {
        this.collect_status = collect_status;
    }

    public Comment getTopicComment() {
        return topicComment;
    }

    public void setTopicComment(Comment topicComment) {
        this.topicComment = topicComment;
    }

    public void setM_time(long m_time) {
        this.m_time = m_time;
    }

    public String getTopic_type() {
        return topic_type;
    }

    public void setTopic_type(String topic_type) {
        this.topic_type = topic_type;
    }

    public String getComment_amount() {
        if (TextUtils.isEmpty(comment_amount))
            return "0";
        return String.valueOf(comment_amount);
    }

    public void setComment_amount(String comment_amount) {
        this.comment_amount = comment_amount;
    }

    public String getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(String topic_id) {
        this.topic_id = topic_id;
    }

    /**
     * 内容类型（文字或者图片）
     */
    public int getContentType() {

        if (topicContent.getTopic_img_url() != null) {
            if (topicContent.getTopic_img_url().size() > 0) {
                return IMAGE;
            } else {
                return TEXT;
            }
        }
        return TEXT;
    }

    public String getTopic_title() {
        return topic_title;
    }


    public void setTopic_title(String topic_title) {
        this.topic_title = topic_title;
    }

    /**
     * 不要使用该属性，使用getTopicContent()
     *
     * @return
     */
    public String getTopic_content() {

        return topic_content;
    }

    public TopicContent getTopicContent() {
        if (topicContent != null)
            return topicContent;
        if (!TextUtils.isEmpty(topic_content)) {
            try {
                topicContent = JSON.parseObject(topic_content,
                        new TypeReference<TopicContent>() {
                        });

            } catch (Exception e) {

            }
        }
        return topicContent;
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

    public String getCollect_amount() {
        if (TextUtils.isEmpty(collect_amount))
            return "0";
        return collect_amount;
    }

    public void setCollect_amount(String collect_amount) {
        this.collect_amount = collect_amount;
    }

    public String getC_time(Context context) {
        return DateUtils.formatDateTime(c_time, context);
    }

    public String getM_time(Context context) {
        return DateUtils.formatDateTime(m_time, context);
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
