package com.tongban.im.activity.base;

import android.view.View;

import com.tongban.im.R;

/**
 * 通用的用户中心父类
 * Created by zhangleilei on 2015/09/01.
 */
public class UserBaseActivity extends AppBaseActivity implements View.OnClickListener {


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_personal_center;
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {

    }
}
