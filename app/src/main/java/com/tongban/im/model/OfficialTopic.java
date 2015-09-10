package com.tongban.im.model;

/**
 * 官方话题详情
 * Created by fushudi on 2015/8/11.
 */
public class OfficialTopic {
    //官方话题内容
    public final static int PRODUCT = 0;
    //官方话题回复
    public final static int REPLY = 1;
    //官方话题回复数量栏
    public final static int REPLY_NUM = 2;
    //官方话题内容类型
    private int itemType;
    //产品
    private ProductBook product;
    //话题评论
    private TopicComment topicReply;
    //话题
    private Topic topic;

    public TopicComment getTopicReply() {
        return topicReply;
    }

    public void setTopicReply(TopicComment topicReply) {
        this.topicReply = topicReply;
    }

    public ProductBook getProduct() {
        return product;
    }

    public void setProduct(ProductBook product) {
        this.product = product;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }
}
