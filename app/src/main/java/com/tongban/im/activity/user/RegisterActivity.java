package com.tongban.im.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.tongban.corelib.utils.KeyBoardUtils;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.im.R;
import com.tongban.im.activity.base.CameraResultActivity;
import com.tongban.im.api.AccountApi;
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
public class RegisterActivity extends CameraResultActivity {

    private User user;

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
        startActivity(new Intent(mContext, LoginActivity.class));
        finish();
    }

    public void onEventMainThread(BaseEvent.UserLoginEvent userEvent) {
        user = userEvent.user;
        mToolbar.setVisibility(View.GONE);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container,
                new SecondRegisterFragment())
                .commit();
    }

    public void onEventMainThread(BaseEvent.EditUserEvent obj) {
        connectIM(user.getChild_info() == null);
    }
}


