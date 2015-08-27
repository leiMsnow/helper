package com.tongban.corelib.base.api;

/**
 * api回调接口
 */
public interface ApiCallback {

    /**
     * Toast:toast提示
     * View:错误页展示
     */
    enum DisplayType {
        Toast, View, None, ALL
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
     * @param displayType 错误提示方式
     * @param errorObj    错误信息
     */
    void onFailure(DisplayType displayType, Object errorObj);

}