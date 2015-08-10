package com.tongban.im.model;

/**
 * 话题评论信息表
 * Created by fushudi on 2015/8/1.
 */
public class TopicReply {

//    private String replyId;
    /**
     * 评论者昵称
     */
    private String replyNickName;
    /**
     * 评论者性别
     */
    private String replySex;
    /**
     * 评论者年龄
     */
    private String replyAge;
    /**
     * 评论内容
     */
    private String replyContent;
    /**
     * 评论时间
     */
    private String replyTime;
    /**
     * 评论数量
     */
    private String replyNum;

    public String getReplyNickName() {
        return replyNickName;
    }

    public String getReplySex() {
        return replySex;
    }

    public String getReplyAge() {
        return replyAge;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public String getReplyTime() {
        return replyTime;
    }

    public String getReplyNum() {
        return replyNum;
    }

    public void setReplyNickName(String replyNickName) {
        this.replyNickName = replyNickName;
    }

    public void setReplySex(String replySex) {
        this.replySex = replySex;
    }

    public void setReplyAge(String replyAge) {
        this.replyAge = replyAge;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public void setReplyTime(String replyTime) {
        this.replyTime = replyTime;
    }

    public void setReplyNum(String replyNum) {
        this.replyNum = replyNum;
    }
}
