package com.tongban.corelib.model;

import com.tongban.corelib.base.api.IApiCallback;

/**
 * 接口返回错误model
 */
public class ApiErrorResult {

    // 接口名称
    private String apiName;
    // 错误码
    private int errorCode = -1;
    // 错误提示
    private String errorMessage;
    // 提示方式
    private IApiCallback.DisplayType displayType;

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public void setDisplayType(IApiCallback.DisplayType displayType) {
        this.displayType = displayType;
    }

    public String getApiName() {
        return apiName;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public IApiCallback.DisplayType getDisplayType() {
        return displayType;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
