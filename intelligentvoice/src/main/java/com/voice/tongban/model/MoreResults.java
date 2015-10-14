package com.voice.tongban.model;

public class MoreResults {

    // 答案
    private Answer answer;
    /**
     * // 不同答案可能来自不同的服务
     * {@link ServiceType}
     */
    private String service;

    // 是否是第一条数据
    private boolean isFirst = false;

    public boolean isFirst() {
        return isFirst;
    }

    public void setIsFirst(boolean isFirst) {
        this.isFirst = isFirst;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Answer getAnswer() {
        return answer;
    }

    public String getService() {
        return service;
    }

}