package com.voice.tongban.model;

import com.tb.api.model.topic.Topic;

/**
 * Created by zhangleilei on 10/14/15.
 */
public class FinalResult {

    // 用户提问
    public static final int USER_QUESTION = 0;
    // 没有答案
    public static final int ANSWER_ERROR = 1;
    // 文本类型
    public static final int ANSWER_TEXT = 2;
    // 话题类型
    public static final int ANSWER_TOPIC = 3;

    private int finalType;

    private String question;

    private String errorInfo;

    private MoreResults moreResults;

    private Topic topic;

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public int getFinalType() {
        return finalType;
    }

    public void setFinalType(int finalType) {
        this.finalType = finalType;
    }

    public MoreResults getMoreResults() {
        return moreResults;
    }

    public void setMoreResults(MoreResults moreResults) {
        this.moreResults = moreResults;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }


}
