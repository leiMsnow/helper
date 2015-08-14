package com.tongban.corelib.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Cheney on 15/7/7 18:54.
 */
public abstract class BaseUIFragment extends BaseFragment {

    protected View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(getLayoutRes(), container, false);
            initView();
            initData();
            initListener();
        }
        return mView;
    }

    /**
     * 获取Fragment的布局文件
     *
     * @return Layout
     */
    protected abstract int getLayoutRes();

    /**
     * 初始化界面View
     */
    protected abstract void initView();
    /**
     * 初始化数据
     */
    protected abstract void initData();
    /**
     * 初始化监听器
     */
    protected abstract void initListener();

    @Override
    public void onDestroyView() {
        ((ViewGroup) mView.getParent()).removeView(mView);
        super.onDestroyView();
    }
}
