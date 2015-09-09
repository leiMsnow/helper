package com.tongban.im.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.tongban.corelib.utils.KeyBoardUtils;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.im.R;
import com.tongban.im.activity.base.CameraResultActivity;
import com.tongban.im.api.UserCenterApi;
import com.tongban.im.common.Consts;
import com.tongban.im.fragment.user.FirstRegisterFragment;
import com.tongban.im.fragment.user.SecondRegisterFragment;
import com.tongban.im.model.AddChildInfo;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * 注册
 * * @author zhangleilei
 *
 * @createTime 2015/7/16
 */
public class RegisterActivity extends CameraResultActivity
        implements FirstRegisterFragment.INextListener {

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        setTitle(getString(R.string.register));
        getSupportFragmentManager().beginTransaction().add(R.id.fl_container,
                new FirstRegisterFragment())
                .commit();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_register;
    }

    @Override
    protected void initListener() {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            KeyBoardUtils.hideSoftKeyboard(this);
            mToolbar.setVisibility(View.VISIBLE);
        } else {
            startActivity(new Intent(mContext, LoginActivity.class));
            finish();
        }
    }

    public void onEventMainThread(User user) {
        connectIM(user.getUser_id(), user.getChild_info() == null, true);
    }

    @Override
    public void next(String phone, String pwd, String verifyId, String verifyCode) {
        KeyBoardUtils.hideSoftKeyboard(this);
        mToolbar.setVisibility(View.GONE);
        SecondRegisterFragment secondRegister = new SecondRegisterFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Consts.KEY_PHONE, phone);
        bundle.putString(Consts.KEY_PWD, pwd);
        bundle.putString(Consts.KEY_VERIFY_ID, verifyId);
        bundle.putString(Consts.KEY_VERIFY_CODE, verifyCode);
        secondRegister.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, secondRegister)
                .addToBackStack(null).commit();
    }


}


