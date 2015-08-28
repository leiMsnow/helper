package com.tongban.corelib.base.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tongban.corelib.R;
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

    private View mEmptyView;
    private RequestApiListener requestListener;


    public void setRequestListener(RequestApiListener requestListener) {
        this.requestListener = requestListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStartApi() {
        showEmptyText("", true);
    }

    @Override
    public void onComplete(Object obj) {
        EventBus.getDefault().post(obj);

        if (mEmptyView != null) {
            if (mEmptyView.getVisibility() == View.VISIBLE) {
                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mEmptyView, "alpha", 1.0f, 0.0f)
                        .setDuration(500);
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
    public void onFailure(final DisplayType displayType, final Object errorObj) {

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
            ToastUtil.getInstance(mContext).showToast("网络异常，请稍后重试");
        } else if (displayType == DisplayType.View) {
            showEmptyText(errorMsg, false);
        } else if (displayType == DisplayType.ALL) {
            ToastUtil.getInstance(mContext).showToast("网络异常，请稍后重试");
            showEmptyText(errorMsg, false);
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
    }

    /**
     * 创建空数据布局
     */
    private void createEmptyView() {

        mEmptyView = mView.findViewById(com.tongban.corelib.R.id.rl_empty_view);
        if (mEmptyView != null) {
            mEmptyView.setVisibility(View.VISIBLE);
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mEmptyView, "alpha", 0.0f, 1.0f)
                    .setDuration(500);
            objectAnimator.start();
        } else {
            mEmptyView = LayoutInflater.from(mContext).
                    inflate(com.tongban.corelib.R.layout.view_empty, null);

            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mEmptyView, "alpha", 0.0f, 1.0f)
                    .setDuration(500);
            objectAnimator.start();
            ((ViewGroup) mView).addView(mEmptyView, layoutParams);
        }
    }

    /**
     * 显示空数据
     *
     * @param message
     */
    protected void showEmptyText(String message, boolean isLoading) {
        createEmptyView();
        TextView tvMsg = (TextView) mEmptyView.findViewById(com.tongban.corelib.R.id.tv_empty_msg);
        ProgressBar pb = (ProgressBar) mEmptyView.findViewById(R.id.pb_loading);
        if (isLoading) {
            pb.setVisibility(View.VISIBLE);
            tvMsg.setVisibility(View.GONE);
        } else {
            tvMsg.setText(message);
            pb.setVisibility(View.GONE);
            tvMsg.setVisibility(View.VISIBLE);
            tvMsg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (requestListener != null) {
                        requestListener.onRequest();
                    }
                }
            });
        }
    }
}
