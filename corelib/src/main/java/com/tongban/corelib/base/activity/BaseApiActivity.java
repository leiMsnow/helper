package com.tongban.corelib.base.activity;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.android.volley.Request;
import com.tongban.corelib.base.api.ApiCallback;
import com.tongban.corelib.model.ApiFailedEvent;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 基础activity，处理通用功能：
 * 1.统一api回调方法
 */
public abstract class BaseApiActivity extends BaseTemplateActivity implements ApiCallback {

    private static List<Request> failedRequest = null;

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mDialog = new ProgressDialog(mContext);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销EventBus
        EventBus.getDefault().unregister(this);
        // 销毁Dialog
        mDialog = null;
        // 清空失败请求的队列
        if (failedRequest != null)
            failedRequest.clear();
    }

    public void onEventMainThread(Object obj) {

    }

    /**
     * 获取失败请求的队列
     * @return
     */
    public static List<Request> getFailedRequest() {
        if (failedRequest == null) {
            failedRequest = new ArrayList<>();
        }
        return failedRequest;
    }

    @Override
    public void onStartApi() {
        mDialog.show();
        mDialog.setMessage("请稍后...");

    }

    @Override
    public void onComplete(Object obj) {
        if (mDialog != null)
            mDialog.dismiss();
        EventBus.getDefault().post(obj);
    }

    @Override
    public void onFailure(DisplayType displayType, String errorMessage) {
        if (mDialog != null)
            mDialog.dismiss();
        EventBus.getDefault().post(new ApiFailedEvent(errorMessage));
    }


}
