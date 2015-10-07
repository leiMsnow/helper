package com.tongban.corelib.base.activity;

import android.os.Bundle;
import android.text.TextUtils;

import com.tongban.corelib.R;
import com.tongban.corelib.base.api.IApiCallback;
import com.tongban.corelib.model.ApiErrorResult;
import com.tongban.corelib.utils.ToastUtil;
import com.tongban.corelib.widget.view.BaseProgressDialog;

import de.greenrobot.event.EventBus;

/**
 * 基础activity，处理通用功能：
 * 1.统一api回调方法
 * 2.使用EventBus框架来完成类之间解耦
 */
public abstract class BaseApiActivity extends BaseTemplateActivity implements IApiCallback {

    private BaseProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销EventBus
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(Object obj) {

    }

    @Override
    public void onStartApi() {
        showProgress();
    }

    @Override
    public void onComplete(Object obj) {
        hideProgress();
        EventBus.getDefault().post(obj);
    }

    @Override
    public void onFailure(ApiErrorResult result) {
        hideProgress();
        EventBus.getDefault().post(result);
        String errorMsg = result.getErrorMessage();
        if (TextUtils.isEmpty(errorMsg) || errorMsg.contains("volley")) {
            errorMsg = getString(R.string.api_error);
        }
        // toast提示
        if (result.getDisplayType() == DisplayType.Toast) {
            ToastUtil.getInstance(mContext).showToast(errorMsg);
        }
        // 视图提示
        else if (result.getDisplayType() == DisplayType.View) {
            setEmptyView(result);
        }
        // 全部提示
        else if (result.getDisplayType() == DisplayType.ALL) {
            ToastUtil.getInstance(mContext).showToast(errorMsg);
            setEmptyView(result);
        }
    }


    /**
     * 显示进度条
     */
    protected void showProgress() {
        if (mDialog == null) {
            mDialog = new BaseProgressDialog(mContext);
        }
        if (!isFinishing())
            mDialog.show();
    }

    /**
     * 隐藏进度条
     */
    public void hideProgress() {
        if (mDialog != null && mDialog.isShowing() && (!isFinishing())) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    public abstract void setEmptyView(ApiErrorResult result);
}
