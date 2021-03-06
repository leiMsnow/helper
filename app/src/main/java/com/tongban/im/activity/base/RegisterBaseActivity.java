package com.tongban.im.activity.base;

import android.os.Bundle;
import android.view.MenuItem;

import com.tb.api.model.BaseEvent;
import com.tb.api.model.user.User;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.im.R;
import com.tongban.im.fragment.account.EditUserFragment;

import io.rong.imkit.RongIM;

/**
 * 注册基础类
 * 1.普通注册
 * 2.第三方注册
 * Created by zhangleilei on 9/24/15.
 */
public abstract class RegisterBaseActivity extends AccountBaseActivity {


    protected User user;
    //是否打开编辑资料界面
    protected boolean isEditUser;
    protected Bundle bundle;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (isEditUser) {
            if (RongIM.getInstance() != null
                    && RongIM.getInstance().getRongIMClient() != null)
                RongIM.getInstance().logout();
            SPUtils.clear(mContext);
        }
        super.onBackPressed();
    }

    /**
     * 登录成功回调
     *
     * @param userEvent
     */
    public void onEventMainThread(BaseEvent.UserLoginEvent userEvent) {
        user = userEvent.user;
        openSecondFragment();
    }

    /**
     * 修改资料回调
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.EditUserEvent obj) {
        connectIM();
        finish();
    }

    protected void openSecondFragment() {
        isEditUser = true;
        EditUserFragment secondRegisterFragment = new EditUserFragment();
        secondRegisterFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container,
                secondRegisterFragment)
                .commit();
    }
}
