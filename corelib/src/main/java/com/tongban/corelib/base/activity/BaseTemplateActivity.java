package com.tongban.corelib.base.activity;

import android.os.Bundle;

import com.tongban.corelib.model.ApiErrorResult;

import butterknife.ButterKnife;

/**
 * 基础activity，处理通用功能：
 * 1.统一的模板方法
 * 2.加入注解框架
 */
public abstract class BaseTemplateActivity extends BasisActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutRes());
        // // TODO: 9/29/15 未完待读
        ButterKnife.bind(this);
        initData();
    }

    /**
     * activity的布局文件
     *
     * @return R.layout.xxx
     */
    protected abstract int getLayoutRes();

    /**
     * 初始化数据；访问接口等
     */
    protected abstract void initData();

    protected abstract void setEmptyView(ApiErrorResult result);

}





