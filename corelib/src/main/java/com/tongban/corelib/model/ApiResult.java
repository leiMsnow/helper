package com.tongban.corelib.model;

/**
 * 非列表数据接口的model
 */
public class ApiResult<T> {

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
