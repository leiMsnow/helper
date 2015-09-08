package com.tongban.im.activity.user;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;

import com.tongban.corelib.utils.KeyBoardUtils;
import com.tongban.im.R;
import com.tongban.im.activity.base.CameraResultActivity;
import com.tongban.im.fragment.user.FirstRegisterFragment;
import com.tongban.im.fragment.user.SecondRegisterFragment;
import com.tongban.im.model.User;

/**
 * 注册
 * * @author zhangleilei
 *
 * @createTime 2015/7/16
 */
public class RegisterActivity extends CameraResultActivity implements FirstRegisterFragment.INextListener {

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        setTitle(getString(R.string.register));
        getSupportFragmentManager().beginTransaction().add(R.id.fl_container, new FirstRegisterFragment())
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

    public void onEventMainThread(User user) {
        connectIM(user, true);
    }

    @Override
    public void next() {
        KeyBoardUtils.hideSoftKeyboard(this);
        mToolbar.setVisibility(View.GONE);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, new SecondRegisterFragment())
                .commit();
    }
}


