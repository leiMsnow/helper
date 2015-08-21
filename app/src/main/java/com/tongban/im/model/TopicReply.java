package com.tongban.im.model;

/**
 * 话题评论信息表
 * Created by fushudi on 2015/8/1.
 */
public class TopicReply {
    /**
     * 评论者头像
     */
    private String replyUserIcon;
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
    private String comment_content;
    /**
     * 评论时间
     */
    private String replyTime;
    /**
     * 评论数量
     */
    private String replyNum;
    private String portrait_url;

    public String getReplyUserIcon() {
        return replyUserIcon;
    }

    public void setReplyUserIcon(String replyUserIcon) {
        this.replyUserIcon = replyUserIcon;
    }

    public String getReplyNickName() {
        return replyNickName;
    }

    public String getReplySex() {
        return replySex;
    }

    public String getReplyAge() {
        return replyAge;
    }

    public String getComment_content() {
        return comment_content;
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

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    public void setReplyTime(String replyTime) {
        this.replyTime = replyTime;
    }

    public void setReplyNum(String replyNum) {
        this.replyNum = replyNum;
    }

    public String getPortrait_url() {
        return portrait_url;
    }

    public void setPortrait_url(String portrait_url) {
        this.portrait_url = portrait_url;
    }
}
