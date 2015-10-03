package com.tongban.im.fragment.base;

import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tongban.corelib.base.api.IApiCallback;
import com.tongban.corelib.base.api.RequestApiListener;
import com.tongban.corelib.base.fragment.BaseApiFragment;
import com.tongban.corelib.model.ApiErrorResult;
import com.tongban.corelib.utils.DensityUtils;
import com.tongban.im.R;
import com.tongban.im.common.Consts;
import com.tongban.im.utils.EmptyViewUtils;

import butterknife.Bind;

/**
 * 基础fragment的api通用类
 * 目前都复用activity中的处理方式
 */
public abstract class BaseToolBarFragment extends BaseApiFragment implements IApiCallback,
        RequestApiListener {

    @Nullable
    @Bind(R.id.rl_toolbar)
    protected View mToolbar;

    private View mEmptyParentView;

    /**
     * 设置用户头像信息
     *
     * @param uri  网络地址
     * @param view imageView控件
     */
    public void setUserPortrait(String uri, ImageView view) {
        Glide.with(BaseToolBarFragment.this)
                .load(uri)
                .error(Consts.getUserDefaultPortrait())
                .into(view);
    }

    protected int getToolbarHeight() {
        int height = 0;
        if (mToolbar != null && mToolbar.getVisibility() != View.GONE) {
            height = DensityUtils.dp2px(mContext, 56);
        }
        return height;
    }

    @Override
    public void setEmptyView(ApiErrorResult result) {

        mEmptyParentView = mView.findViewById(R.id.rl_empty_view_parent);
        if (mEmptyParentView == null) {
            mEmptyParentView = EmptyViewUtils.createEmptyView(mContext);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ((ViewGroup) mView).addView(mEmptyParentView, layoutParams);
        } else {
            EmptyViewUtils.setAlphaEmptyView(mEmptyParentView);
        }
        EmptyViewUtils.showEmptyView(result, mEmptyParentView, getToolbarHeight(), this);

    }

    @Override
    public void onRequest() {

    }

    @Override
    public void onStartApi() {
        hideEmptyView();
        super.onStartApi();
    }

    @Override
    public void onComplete(Object obj) {
        super.onComplete(obj);
    }

    /**
     * 隐藏空数据布局
     */
    protected void hideEmptyView() {
        EmptyViewUtils.hideEmptyView(mEmptyParentView);
    }
}
