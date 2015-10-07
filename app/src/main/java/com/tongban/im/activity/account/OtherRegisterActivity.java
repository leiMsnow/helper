package com.tongban.im.activity.account;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tongban.im.R;
import com.tongban.im.activity.base.RegisterBaseActivity;
import com.tongban.im.common.Consts;
import com.tongban.im.fragment.account.OtherRegisterFragment;
import com.tongban.im.fragment.account.OtherVerifyCodeFragment;
import com.tongban.im.model.BaseEvent;

/**
 * 第三方注册
 * * @author zhangleilei
 *
 * @createTime 2015/7/16
 */
public class OtherRegisterActivity extends RegisterBaseActivity {

    private Fragment fragment;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_register;
    }

    @Override
    protected void initData() {
        fragment = new OtherRegisterFragment();
        fragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_container, fragment).commit();
    }

    public void onEventMainThread(BaseEvent.CheckPhoneEvent obj) {

        fragment = new OtherVerifyCodeFragment();

        Bundle bundle = getIntent().getExtras();
        bundle.putString(Consts.USER_ACCOUNT, obj.phone);
        fragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_container, fragment).commit();
    }

}