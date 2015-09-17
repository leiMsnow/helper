package com.tongban.corelib.base.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
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
 * 基础activity，处理通用功能：
 * 1.统一api回调方法
 */
public abstract class BaseApiActivity extends BaseTemplateActivity implements ApiCallback {

    private View mEmptyView;

    private RequestApiListener requestApiListener;

    public void setRequestApiListener(RequestApiListener requestApiListener) {
        this.requestApiListener = requestApiListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
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
        showEmptyText("", true);
    }

    @Override
    public void onComplete(Object obj) {
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
    public void onFailure(final DisplayType displayType, final Object errorObj) {
        String errorMsg = "";

        if (errorObj instanceof ApiResult) {
            errorMsg = ((ApiResult) errorObj).getStatusDesc();
        } else if (errorObj instanceof ApiListResult) {
            errorMsg = ((ApiListResult) errorObj).getStatusDesc();
        } else if (errorObj instanceof String) {
            errorMsg = errorObj.toString();
        }

        if (TextUtils.isEmpty(errorMsg) || errorMsg.contains("volley")) {
            errorMsg = getString(R.string.api_error);
        }
        if (displayType == DisplayType.Toast) {
            ToastUtil.getInstance(mContext).showToast(errorMsg);
            showEmptyText("", false);
        } else if (displayType == DisplayType.View) {
            showEmptyText(errorMsg, false);
        } else if (displayType == DisplayType.ALL) {
            ToastUtil.getInstance(mContext).showToast(getString(R.string.api_error));
            showEmptyText(errorMsg, false);
        } else {
            showEmptyText("", false);
        }

        ApiErrorResult errorResult = new ApiErrorResult();
        errorResult.setErrorMessage(errorMsg);
        EventBus.getDefault().post(errorResult);
    }

    /**
     * 创建空数据布局
     */
    private void createEmptyView() {

        mEmptyView = this.findViewById(R.id.rl_empty_view);
        if (mEmptyView != null) {
            mEmptyView.setVisibility(View.VISIBLE);
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mEmptyView, "alpha", 0.0f, 1.0f)
                    .setDuration(500);
            objectAnimator.start();
        } else {
            mEmptyView = LayoutInflater.from(mContext).inflate(R.layout.view_empty, null);

            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mEmptyView, "alpha", 0.0f, 1.0f)
                    .setDuration(500);
            objectAnimator.start();
            this.addContentView(mEmptyView, layoutParams);
        }
    }

    /**
     * 显示空数据
     *
     * @param message
     */
    public void showEmptyText(String message, boolean isLoading) {
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
                    if (requestApiListener != null) {
                        requestApiListener.onRequest();
                    }
                }
            });
        }
    }
}
