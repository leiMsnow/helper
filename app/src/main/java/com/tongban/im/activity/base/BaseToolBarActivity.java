package com.tongban.im.activity.base;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tongban.corelib.base.activity.BaseApiActivity;
import com.tongban.corelib.base.api.RequestApiListener;
import com.tongban.corelib.model.ApiErrorResult;
import com.tongban.corelib.utils.DensityUtils;
import com.tongban.corelib.utils.ScreenUtils;
import com.tongban.im.R;
import com.tongban.im.api.ProductApi;
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

    /**
     * 显示空数据
     *
     * @param result
     */
    @Override
    public void setEmptyView(ApiErrorResult result) {
        int resId = 0;
        if (result.getApiName().equals(ProductApi.FETCH_HOME_INFO)) {
            resId = R.mipmap.bg_empty_discover;
        }
        ImageView ivEmpty = (ImageView) mEmptyView.findViewById(com.tongban.corelib.R.id.iv_empty);
        if (resId == 0) {
            ivEmpty.setVisibility(View.GONE);
            return;
        }
        ivEmpty.setVisibility(View.VISIBLE);
        ivEmpty.setImageResource(resId);
        ivEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (requestApiListener != null) {
                    requestApiListener.onRequest();
                }
            }
        });
    }

}
