package com.tongban.corelib.model;

/**
 * 接口返回错误model
 */
public class ApiErrorResult {

    public ApiErrorResult(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    private String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
