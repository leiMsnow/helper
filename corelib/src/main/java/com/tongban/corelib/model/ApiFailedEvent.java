package com.tongban.corelib.model;

/**
 * 接口失败event
 */
public class ApiFailedEvent {

    private String errorMessage;

    public ApiFailedEvent(String msg) {
        this.errorMessage = msg;
    }


    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}