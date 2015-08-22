package com.tongban.im.activity.user;


import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.fragment.user.FansFragment;
import com.tongban.im.fragment.user.FocusFragment;

/**
 * 粉丝、关注界面
 * @author fushudi
 */
public class MyRelationshipActivity extends BaseToolBarActivity {
    private FrameLayout flReplasedFragment;
    private FansFragment mFansFragment;
    private FocusFragment mFocusFragment;

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
            mFocusFragment = new FocusFragment();
            transaction.replace(R.id.fl_container, mFocusFragment);
            transaction.commit();
        }
    }

    @Override
    protected void initListener() {

    }
}
