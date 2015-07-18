package com.tongban.im.model;

/**
 * Created by fushudi on 2015/7/16.
 */
public class Chat {
    private String chatName;
    private String chatPersonNum;
    private String chatContext;

    public String getChatName() {
        return chatName;
    }

    public String getChatPersonNum() {
        return chatPersonNum;
    }

    public String getChatContext() {
        return chatContext;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public void setChatPersonNum(String chatPersonNum) {
        this.chatPersonNum = chatPersonNum;
    }

    public void setChatContext(String chatContext) {
        this.chatContext = chatContext;
    }
}
