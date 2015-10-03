package com.tongban.im.activity.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tongban.corelib.base.ActivityContainer;
import com.tongban.corelib.base.activity.BaseApiActivity;
import com.tongban.corelib.base.api.RequestApiListener;
import com.tongban.corelib.model.ApiErrorResult;
import com.tongban.corelib.utils.DensityUtils;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.im.R;
import com.tongban.im.common.Consts;
import com.tongban.im.common.TransferCenter;
import com.tongban.im.utils.EmptyViewUtils;

import butterknife.Bind;
import io.rong.imkit.RongIM;

/**
 * toolbar的基础类
 * 1 toolbar的综合封装
 * 2 错误页的统一处理类
 * Created by zhangleilei on 15/7/8.
 */
public abstract class BaseToolBarActivity extends BaseApiActivity implements RequestApiListener {

    @Nullable
    @Bind(R.id.in_toolbar)
    protected Toolbar mToolbar;

    protected View mEmptyParentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolbar();
    }


    protected void initToolbar() {
//        mToolbar = (Toolbar) findViewById(R.id.in_toolbar);
        if (mToolbar == null) {
            return;
        }
        //弹出菜单变成白色
        mToolbar.setPopupTheme(R.style.ThemeOverlay_AppCompat_Light);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    protected int getToolBarHeight() {
        if (mToolbar == null)
            return 0;
        return DensityUtils.dp2px(mContext, 56);
    }


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

    @Override
    public void setEmptyView(ApiErrorResult result) {
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
}
