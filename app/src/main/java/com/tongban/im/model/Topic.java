package com.tongban.im.model;

/**
 * 话题信息表
 * Created by fushudi on 2015/7/16.
 */
public class Topic {
    private String topicName;
    private String topicPersonNum;
    private String topicContext;

    public String getTopicName() {
        return topicName;
    }

    public String getTopicPersonNum() {
        return topicPersonNum;
    }

    public String getTopicContext() {
        return topicContext;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public void setTopicPersonNum(String topicPersonNum) {
        this.topicPersonNum = topicPersonNum;
    }

    public void setTopicContext(String topicContext) {
        this.topicContext = topicContext;
    }
}
