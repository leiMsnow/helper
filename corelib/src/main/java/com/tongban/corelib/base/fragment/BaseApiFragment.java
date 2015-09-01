package com.tongban.corelib.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tongban.corelib.base.activity.BaseApiActivity;
import com.tongban.corelib.base.api.ApiCallback;
import com.tongban.corelib.base.api.RequestApiListener;

import de.greenrobot.event.EventBus;

/**
 * 基础fragment的api通用类
 * 目前都复用activity中的处理方式
 */
public abstract class BaseApiFragment extends BaseUIFragment implements ApiCallback {

    private BaseApiActivity mBaseApiActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        if (getActivity() instanceof BaseApiActivity)
            mBaseApiActivity = (BaseApiActivity) getActivity();
    }

    @Override
    public void onStartApi() {
        if (mBaseApiActivity != null)
            mBaseApiActivity.onStartApi();
    }

    @Override
    public void onComplete(Object obj) {
        if (mBaseApiActivity != null)
            mBaseApiActivity.onComplete(obj);
    }

    @Override
    public void onFailure(final DisplayType displayType, final Object errorObj) {
        if (mBaseApiActivity != null)
            mBaseApiActivity.onFailure(displayType, errorObj);
    }

    public void onEventMainThread(Object obj) {

    }

    /**
     * 显示空数据/加载条
     *
     * @param message   显示空数据的提示信息
     * @param isLoading 是否显示加载条
     */
    protected void showEmptyText(String message, boolean isLoading) {
        if (mBaseApiActivity != null)
            mBaseApiActivity.showEmptyText(message, isLoading);
    }

    /**
     * 重新请求接口监听
     *
     * @param requestListener
     */
    protected void setRequestListener(RequestApiListener requestListener) {
        if (mBaseApiActivity != null)
            mBaseApiActivity.setRequestApiListener(requestListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
