package com.voice.tongban.model;


public class MoreResults {

    // 答案
    private Answer answer;
    /**
     * // 不同答案可能来自不同的服务
     * {@link ServiceType}
     */
    private String service;


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