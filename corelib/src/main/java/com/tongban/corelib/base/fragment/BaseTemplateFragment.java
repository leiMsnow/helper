package com.tongban.corelib.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tongban.corelib.model.ApiErrorResult;

import butterknife.ButterKnife;

/**
 * 通用模板类
 * initData 数据统一处理
 * Created by zhangleilei on 15/7/7 18:54.
 */
public abstract class BaseTemplateFragment extends BaseFragment {

    protected View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(getLayoutRes(), container, false);
        // TODO: 10/3/15 加入注解
        ButterKnife.bind(this, mView);
        initData();
        return mView;
    }

    /**
     * 获取Fragment的布局文件
     *
     * @return Layout
     */
    protected abstract int getLayoutRes();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    public abstract void setEmptyView(ApiErrorResult result);


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
