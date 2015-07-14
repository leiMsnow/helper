package com.tongban.im.model;

/**
 * Created by 接口请求结果对象 on 15/7/3.
 */
public class ApiResult<T extends Object> {

    protected int statusCode;
    protected String statusDesc;
    /**
     * 泛型，根据的接口返回不同对象
     */
    protected T data;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }


    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
