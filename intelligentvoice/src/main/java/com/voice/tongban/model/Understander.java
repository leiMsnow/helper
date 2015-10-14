package com.voice.tongban.model;

import java.util.List;

/**
 * 语义model类
 * Created by zhangleilei on 10/13/15.
 */
public class Understander {

    // 回调结果 0为成功
    private int rc;
    /**
     * 操作类型
     * {@link OperationType}
     */
    private String operation;
    /**
     * 使用的服务
     * {@link ServiceType}
     */
    private String service;
    // 单个答案
    private Answer answer;
    // 问题
    private String text;
    // 更多答案
    private List<MoreResults> moreResults;

    public void setRc(int rc) {
        this.rc = rc;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setService(String service) {
        this.service = service;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setMoreResults(List<MoreResults> moreResults) {
        this.moreResults = moreResults;
    }

    public int getRc() {
        return rc;
    }

    public String getOperation() {
        return operation;
    }

    public String getService() {
        return service;
    }

    public Answer getAnswer() {
        return answer;
    }

    public String getText() {
        return text;
    }

    public List<MoreResults> getMoreResults() {
        return moreResults;
    }

}
