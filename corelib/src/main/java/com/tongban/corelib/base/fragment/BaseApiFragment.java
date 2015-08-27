package com.tongban.corelib.base.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.tongban.corelib.base.api.ApiCallback;
import com.tongban.corelib.base.api.RequestApiListener;
import com.tongban.corelib.model.ApiErrorResult;
import com.tongban.corelib.model.ApiListResult;
import com.tongban.corelib.model.ApiResult;
import com.tongban.corelib.utils.ToastUtil;

import de.greenrobot.event.EventBus;

/**
 * 基础fragment的api通用类
 */
public abstract class BaseApiFragment extends BaseUIFragment implements ApiCallback {

    private ProgressDialog mDialog;
    private View mEmptyView;
    private RequestApiListener requestListener;

    public void setRequestListener(RequestApiListener requestListener) {
        this.requestListener = requestListener;
    }

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

        if (mEmptyView != null) {
            if (mEmptyView.getVisibility() == View.VISIBLE) {
                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mEmptyView, "alpha", 1.0f, 0.0f).setDuration(500);
                objectAnimator.start();
                objectAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mEmptyView.setVisibility(View.GONE);
                    }
                });
            }
        }
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
        if (TextUtils.isEmpty(errorMsg) || errorMsg.contains("volley")) {
            errorMsg = "网络异常，请稍后重试";
        }
        if (displayType == DisplayType.Toast) {
            ToastUtil.getInstance(mContext).showToast(errorMsg);
        } else if (displayType == DisplayType.View) {
            createEmptyView(errorMsg);
        } else if (displayType == DisplayType.ALL) {
            ToastUtil.getInstance(mContext).showToast(errorMsg);
            createEmptyView(errorMsg);
        }
        ApiErrorResult errorResult = new ApiErrorResult();
        errorResult.setErrorMessage(errorMsg);
        EventBus.getDefault().post(errorResult);
    }

    public void onEventMainThread(Object obj) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        mDialog = null;
    }

    /**
     * 创建空数据布局
     *
     * @param msg 提示信息
     */
    protected void createEmptyView(final String msg) {

        mEmptyView = mView.findViewById(com.tongban.corelib.R.id.rl_empty_view);
        if (mEmptyView != null) {
            mEmptyView.setVisibility(View.VISIBLE);
            TextView tvMsg = (TextView) mEmptyView.findViewById(com.tongban.corelib.R.id.tv_empty_msg);
            tvMsg.setText(msg);
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mEmptyView, "alpha", 0.0f, 1.0f).setDuration(500);
            objectAnimator.start();
        } else {
            mEmptyView = LayoutInflater.from(mContext).inflate(com.tongban.corelib.R.layout.view_empty, null);
            TextView tvMsg = (TextView) mEmptyView.findViewById(com.tongban.corelib.R.id.tv_empty_msg);
            tvMsg.setText(msg);
            tvMsg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (requestListener != null) {
                        requestListener.onRequest();
                    }
                }
            });

            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mEmptyView, "alpha", 0.0f, 1.0f).setDuration(500);
            objectAnimator.start();
            ((ViewGroup) mView).addView(mEmptyView, layoutParams);
        }
    }
}
