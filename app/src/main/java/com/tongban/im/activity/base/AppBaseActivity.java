package com.tongban.im.activity.base;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tongban.corelib.base.ActivityContainer;
import com.tongban.corelib.base.activity.BaseToolBarActivity;
import com.tongban.corelib.base.api.RequestApiListener;
import com.tongban.corelib.model.ApiErrorResult;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.im.R;
import com.tongban.im.common.Consts;
import com.tongban.im.common.TransferCenter;
import com.tongban.im.utils.EmptyViewUtils;

import io.rong.imkit.RongIM;

/**
 * toolbar的基础类
 * 1 错误页的统一处理类
 * Created by zhangleilei on 15/7/8.
 */
public abstract class AppBaseActivity extends BaseToolBarActivity implements RequestApiListener {

    protected View mEmptyParentView;

    /**
     * 设置用户头像信息
     *
     * @param uri  网络地址
     * @param view imageView控件
     */
    public void setUserPortrait(String uri, ImageView view) {
        Glide.with(this).load(uri).error(Consts.getUserDefaultPortrait()).into(view);
    }

    @Override
    protected void setEmptyView(ApiErrorResult result) {
        mEmptyParentView = findViewById(R.id.rl_empty_view_parent);
        if (mEmptyParentView == null) {
            mEmptyParentView = EmptyViewUtils.createEmptyView(mContext);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            this.addContentView(mEmptyParentView, layoutParams);
        } else {
            EmptyViewUtils.setAlphaEmptyView(mEmptyParentView);
        }
        EmptyViewUtils.showEmptyView(result, mEmptyParentView, getToolBarHeight(), this);
    }

    /**
     * 隐藏空数据布局
     */
    protected void hideEmptyView() {
        EmptyViewUtils.hideEmptyView(mEmptyParentView);
    }

    /**
     * 注销
     */
    protected void logout() {
        if (RongIM.getInstance() != null
                && RongIM.getInstance().getRongIMClient() != null)
            RongIM.getInstance().logout();

        SPUtils.clear(mContext);
        ActivityContainer.getInstance().finishActivity();
        TransferCenter.getInstance().startLogin(true, false);
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

    @Override
    public void onRequest() {

    }
}
