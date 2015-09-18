package com.tongban.im.activity.base;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tongban.corelib.base.activity.BaseApiActivity;
import com.tongban.im.R;
import com.tongban.im.common.Consts;

/**
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
        mToolbar = (Toolbar) findViewById(R.id.in_toolbar);
        if (mToolbar == null) {
            return;
        }
        mToolbar.setPopupTheme(R.style.ThemeOverlay_AppCompat_Light);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
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

}
