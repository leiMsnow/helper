package com.tongban.im.activity.user;

import android.widget.FrameLayout;

import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.fragment.user.InputPhoneFragment;
import com.tongban.im.fragment.user.ReSetPwdFragment;

/**
 * 找回密码界面
 *
 * @author fushudi
 */
public class FindPwdActivity extends BaseToolBarActivity {
    private FrameLayout flReplasedFragment;

    private InputPhoneFragment mInputPhoneFragment;
    private ReSetPwdFragment mResetPwdFragment;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_find_pwd;
    }

    @Override
    protected void initView() {
        setTitle(R.string.find_pwd);
        flReplasedFragment = (FrameLayout) findViewById(R.id.fl_container);
    }

    @Override
    protected void initData() {
        mInputPhoneFragment = new InputPhoneFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, mInputPhoneFragment).commit();
    }

    @Override
    protected void initListener() {

    }
}
