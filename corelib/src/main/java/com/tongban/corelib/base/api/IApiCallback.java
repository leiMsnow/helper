package com.tongban.corelib.base.api;

import com.tongban.corelib.model.ApiErrorResult;

/**
 * api回调接口
 */
public interface IApiCallback {

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
     * @param result 回调失败对象
     */
    void onFailure(ApiErrorResult result);

}