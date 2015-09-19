package com.tongban.corelib.base.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.tongban.corelib.R;
import com.tongban.corelib.base.activity.BaseApiActivity;
import com.tongban.corelib.base.api.IApiCallback;
import com.tongban.corelib.base.api.RequestApiListener;
import com.tongban.corelib.model.ApiErrorResult;

import de.greenrobot.event.EventBus;

/**
 * 基础fragment的api通用类
 * 目前都复用activity中的处理方式
 */
public abstract class BaseApiFragment extends BaseTemplateFragment implements IApiCallback {

    private BaseApiActivity mBaseApiActivity;

    protected View mEmptyView;
    protected RequestApiListener requestApiListener;

    public void setRequestApiListener(RequestApiListener requestApiListener) {
        this.requestApiListener = requestApiListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        if (getActivity() instanceof BaseApiActivity)
            mBaseApiActivity = (BaseApiActivity) getActivity();
    }

    @Override
    public void onStartApi() {
        hidEmptyView();
        if (mBaseApiActivity != null)
            mBaseApiActivity.onStartApi();
    }

    @Override
    public void onComplete(Object obj) {
        if (mBaseApiActivity != null)
            mBaseApiActivity.onComplete(obj);

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
        if (result.getDisplayType() == DisplayType.View ||
                result.getDisplayType() == DisplayType.ALL) {
            if (createEmptyView())
                setEmptyView(result);
            result.setDisplayType(DisplayType.None);
        }
        if (mBaseApiActivity != null)
            mBaseApiActivity.onFailure(result);
    }

    public void onEventMainThread(Object obj) {

    }

    public abstract void setEmptyView(ApiErrorResult result);

    /**
     * 隐藏空数据
     */
    protected void hidEmptyView() {
        createEmptyView();
        mEmptyView.findViewById(R.id.iv_empty).setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 创建空数据布局
     */
    protected boolean createEmptyView() {
        if (mView == null) {
            return false;
        }
        mEmptyView = mView.findViewById(com.tongban.corelib.R.id.rl_empty_view);
        if (mEmptyView != null) {
            mEmptyView.setVisibility(View.VISIBLE);
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mEmptyView, "alpha", 0.0f, 1.0f)
                    .setDuration(500);
            objectAnimator.start();
        } else {

            mEmptyView = LayoutInflater.from(mContext)
                    .inflate(com.tongban.corelib.R.layout.view_empty, null);

            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mEmptyView, "alpha", 0.0f, 1.0f)
                    .setDuration(500);
            objectAnimator.start();
            ((ViewGroup) mView).addView(mEmptyView, layoutParams);
        }
        return true;
    }
}
