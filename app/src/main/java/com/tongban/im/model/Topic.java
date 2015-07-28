package com.tongban.im.model;

/**
 * 话题信息表
 * Created by fushudi on 2015/7/16.
 */
public class Topic {
    // 话题名称
    private String topicName;
    // 话题回应人数
    private String topicReplyNum;
    // 话题内容
    private String topicContent;

    public String getTopicName() {
        return topicName;
    }

    public String getTopicReplyNum() {
        return topicReplyNum;
    }

    public String getTopicContent() {
        return topicContent;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public void setTopicReplyNum(String topicReplyNum) {
        this.topicReplyNum = topicReplyNum;
    }

    public void setTopicContent(String topicContent) {
        this.topicContent = topicContent;
    }
}
