package com.tongban.im.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.tongban.im.R;
import com.tongban.im.activity.base.CameraResultActivity;
import com.tongban.im.api.FileUploadApi;
import com.tongban.im.common.Consts;
import com.tongban.im.fragment.user.FirstRegisterFragment;
import com.tongban.im.fragment.user.EditUserFragment;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.User;

/**
 * 注册
 * * @author zhangleilei
 *
 * @createTime 2015/7/16
 */
public class RegisterActivity extends CameraResultActivity {

    private User user;
    //是否打开编辑资料界面
    private boolean isEditUser;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FileUploadApi.getInstance().fetchUploadToken();
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        if (getIntent().getExtras() != null) {
            bundle = getIntent().getExtras();
            isEditUser = bundle.getBoolean(Consts.KEY_EDIT_USER, false);
            if (!isEditUser) {
                FirstRegisterFragment registerFragment = new FirstRegisterFragment();
                registerFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_container,
                        registerFragment)
                        .commit();
            } else {
                openSecondFragment();
            }
        }
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
        if (!isEditUser) {
            startActivity(new Intent(mContext, LoginActivity.class));
        } else {
            connectIM(true, true);
        }
        finish();
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

    private void openSecondFragment() {
        if (mToolbar != null)
            mToolbar.setVisibility(View.GONE);
        isEditUser = true;
        EditUserFragment secondRegisterFragment = new EditUserFragment();
        secondRegisterFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container,
                secondRegisterFragment)
                .commit();
    }
}


