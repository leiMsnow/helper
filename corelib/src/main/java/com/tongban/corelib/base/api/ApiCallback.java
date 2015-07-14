package com.tongban.corelib.base.api;

/**
 * api回调接口
 */
public interface ApiCallback {

    /**
     * Toast:toast提示
     * View:错误页展示
     */
    public enum DisplayType {
        Toast, View
    }

    /**
     * api启动做的操作,比如加载dialog
     */
    void onStartApi();

    /**
     * api调用成功回调
     *
     * @param obj 回调对象
     */
    void onComplete(Object obj);

    /**
     * api调用失败回调
     *
     * @param displayType  错误提示方式
     * @param errorMessage 错误信息
     */
    void onFailure(DisplayType displayType, String errorMessage);

}