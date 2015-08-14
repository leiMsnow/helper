package com.tongban.im.model;

/**
 * 官方话题详情
 * Created by fushudi on 2015/8/11.
 */
public class OfficialTopic {
    //官方话题内容
    public final static int CONTENT = 0;
    //官方话题回复
    public final static int REPLY = 1;
    //官方话题回复数量栏
    public final static int REPLY_NUM = 2;
    //官方话题内容类型
    private int contentType;
    //产品
    private Product product;
    //话题评论
    private TopicReply topicReply;
    //话题
    private Topic topic;

    public TopicReply getTopicReply() {
        return topicReply;
    }

    public void setTopicReply(TopicReply topicReply) {
        this.topicReply = topicReply;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }
}
