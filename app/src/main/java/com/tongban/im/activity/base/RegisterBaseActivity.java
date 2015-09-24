package com.tongban.im.activity.base;

import android.os.Bundle;
import android.view.MenuItem;

import com.tongban.im.R;
import com.tongban.im.fragment.account.EditUserFragment;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.user.User;

/**
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
        logout();
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
        if (isEditUser) {
            connectIM(true, false);
        } else {
            connectIM(user.getChild_info() == null);
        }
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
