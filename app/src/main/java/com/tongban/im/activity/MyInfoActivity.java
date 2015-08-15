package com.tongban.im.activity;


import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.fragment.FansFragment;
import com.tongban.im.fragment.FollowFragment;

public class MyInfoActivity extends BaseToolBarActivity {
    private FrameLayout flReplasedFragment;
    private FansFragment mFansFragment;
    private FollowFragment mFollowFragment;

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
        Intent intent = getIntent();
        String tag = intent.getStringExtra("Tag");
        if (tag.equals("Fans")) {
            setTitle("粉丝");
            mFansFragment = new FansFragment();
            transaction.replace(R.id.fl_container, mFansFragment);
            transaction.commit();
        } else if (tag.equals("Follow")) {
            setTitle("关注");
            mFollowFragment = new FollowFragment();
            transaction.replace(R.id.fl_container, mFollowFragment);
            transaction.commit();
        }
    }

    @Override
    protected void initListener() {

    }
}
