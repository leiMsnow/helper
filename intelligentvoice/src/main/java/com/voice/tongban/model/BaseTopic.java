package com.voice.tongban.model;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tongban.corelib.utils.DateUtils;

/**
 * Created by zhangleilei on 10/23/15.
 */
public class BaseTopic {
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
    //收藏状态
    private boolean collect_status;

    private BaseTopicContent topicContent;

    public boolean isCollect_status() {
        return collect_status;
    }

    public void setCollect_status(boolean collect_status) {
        this.collect_status = collect_status;
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

    public BaseTopicContent getTopicContent() {
        if (!TextUtils.isEmpty(topic_content)) {
            try {
                topicContent = JSON.parseObject(topic_content,
                        new TypeReference<BaseTopicContent>() {
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
}
