package com.tongban.corelib.base.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tongban.corelib.base.api.ApiCallback;
import com.tongban.corelib.model.ApiListResult;
import com.tongban.corelib.model.ApiResult;
import com.tongban.corelib.utils.ToastUtil;

import de.greenrobot.event.EventBus;

public abstract class BaseApiFragment extends BaseUIFragment implements ApiCallback {

    private ProgressDialog mDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mDialog = new ProgressDialog(mContext);
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
    public void onFailure(DisplayType displayType, Object errorObj) {
        if (mDialog != null)
            mDialog.dismiss();
        String errorMsg = "网络异常，请稍后重试";

        if (errorObj instanceof ApiResult) {
            errorMsg = ((ApiResult) errorObj).getStatusDesc();
        } else if (errorObj instanceof ApiListResult) {
            errorMsg = ((ApiListResult) errorObj).getStatusDesc();
        } else if (errorObj instanceof String) {
            errorMsg = errorObj.toString();
        }
        ToastUtil.getInstance(mContext).showToast(errorMsg);
    }

    public void onEventMainThread(Object obj) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        mDialog = null;
    }

}
