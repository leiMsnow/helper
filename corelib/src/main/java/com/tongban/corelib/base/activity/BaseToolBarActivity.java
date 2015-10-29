package com.tongban.corelib.base.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.tongban.corelib.R;
import com.tongban.corelib.utils.Constants;
import com.tongban.corelib.utils.DensityUtils;
import com.tongban.corelib.utils.SPUtils;

/**
 * toolbar的基础类
 * 1 toolbar的综合封装
 * 2 错误页的统一处理类
 * Created by zhangleilei on 15/7/8.
 */
public abstract class BaseToolBarActivity extends BaseApiActivity {

    protected Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolbar();
    }

    protected void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        if (mToolbar == null) {
            return;
        }
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

    protected String getUserId() {
        return SPUtils.get(mContext, Constants.USER_ID, "").toString();
    }

}
