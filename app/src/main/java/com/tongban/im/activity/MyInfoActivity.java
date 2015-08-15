package com.tongban.im.activity;


import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.fragment.FansFragment;

public class MyInfoActivity extends BaseToolBarActivity {
    private FrameLayout flReplasedFragment;
    private FansFragment mFansFragment;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_my_info;
    }

    @Override
    protected void initView() {
        flReplasedFragment = (FrameLayout) findViewById(R.id.fl_container);
    }

    @Override
    protected void initData() {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        mFansFragment = new FansFragment();
        transaction.replace(R.id.fl_container, mFansFragment);
        transaction.commit();
    }

    @Override
    protected void initListener() {

    }
}
