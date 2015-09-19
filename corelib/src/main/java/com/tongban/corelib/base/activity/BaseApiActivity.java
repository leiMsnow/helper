package com.tongban.corelib.base.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.tongban.corelib.R;
import com.tongban.corelib.base.api.IApiCallback;
import com.tongban.corelib.base.api.RequestApiListener;
import com.tongban.corelib.model.ApiErrorResult;
import com.tongban.corelib.utils.ToastUtil;

import de.greenrobot.event.EventBus;

/**
 * 基础activity，处理通用功能：
 * 1.统一api回调方法
 */
public abstract class BaseApiActivity extends BaseTemplateActivity implements IApiCallback {

    protected View mEmptyView;
    protected RequestApiListener requestApiListener;

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
        showProgress();
    }

    @Override
    public void onComplete(Object obj) {
        EventBus.getDefault().post(obj);

        if (mEmptyView != null) {
            if (mEmptyView.getVisibility() == View.VISIBLE) {
                ObjectAnimator objectAnimator = ObjectAnimator
                        .ofFloat(mEmptyView, "alpha", 1.0f, 0.0f).setDuration(500);
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
     * 创建空数据布局
     */
    protected void createEmptyView() {

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
     * 显示进度条
     */
    public void showProgress() {
        createEmptyView();
        ProgressBar pb = (ProgressBar) mEmptyView.findViewById(R.id.pb_loading);
        pb.setVisibility(View.VISIBLE);
        mEmptyView.findViewById(com.tongban.corelib.R.id.iv_empty).setVisibility(View.GONE);
    }

    /**
     * 隐藏进度条
     */
    public void hideProgress() {
        createEmptyView();
        ProgressBar pb = (ProgressBar) mEmptyView.findViewById(R.id.pb_loading);
        pb.setVisibility(View.GONE);
    }


    public abstract void setEmptyView(ApiErrorResult result);
}
