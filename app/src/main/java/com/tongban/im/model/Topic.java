package com.tongban.im.model;

import java.util.List;

/**
 * 话题信息表
 * Created by fushudi on 2015/7/16.
 */
public class Topic {
    //话题内容为文本
    public final static int TEXT = 0;
    //话题内容为图片
    public final static int IMAGE = 1;
    // 话题名称
    private String topicName;
    // 话题评论数量
    private String topicReplyNum;
    //话题获得赞的数量
    private String topicPraiseNum;
    //话题生成时间
    private String topicTime;
    // 话题内容
    private String topicContent;
    //用户
    private User user;
    //内容类型（文字或者图片）
    private int contentType;
    //缩略图地址
    private List<String> smallUrl;
    //原图地址
    private List<String> bigUrl;

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public List<String> getSmallUrl() {
        return smallUrl;
    }

    public void setSmallUrl(List<String> smallUrl) {
        this.smallUrl = smallUrl;
    }

    public List<String> getBigUrl() {
        return bigUrl;
    }

    public void setBigUrl(List<String> bigUrl) {
        this.bigUrl = bigUrl;
    }

    public void setTopicPraiseNum(String topicPraiseNum) {
        this.topicPraiseNum = topicPraiseNum;
    }

    public void setTopicTime(String topicTime) {
        this.topicTime = topicTime;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTopicPraiseNum() {

        return topicPraiseNum;
    }

    public String getTopicTime() {
        return topicTime;
    }

    public User getUser() {
        return user;
    }

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
