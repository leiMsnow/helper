package com.tongban.im.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.im.R;
import com.tongban.im.activity.base.CameraResultActivity;
import com.tongban.im.api.FileUploadApi;
import com.tongban.im.common.Consts;
import com.tongban.im.fragment.user.FirstRegisterFragment;
import com.tongban.im.fragment.user.SecondRegisterFragment;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.OtherRegister;
import com.tongban.im.model.User;

/**
 * 注册
 * * @author zhangleilei
 *
 * @createTime 2015/7/16
 */
public class RegisterActivity extends CameraResultActivity {

    private User user;
    private boolean isSecond;

//    private String mOtherInfo;
//    private String mOtherType;
//    private OtherRegister otherRegister;

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
            isSecond = bundle.getBoolean(Consts.KEY_EDIT_USER, false);
            if (!isSecond) {
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
        if (!isSecond)
            startActivity(new Intent(mContext, LoginActivity.class));
        SPUtils.clear(mContext);
        finish();
    }

    public void onEventMainThread(BaseEvent.UserLoginEvent userEvent) {
        user = userEvent.user;
        openSecondFragment();
    }

    public void onEventMainThread(BaseEvent.EditUserEvent obj) {
        if (isSecond) {
            connectIM(true, false);
        } else {
            connectIM(user.getChild_info() == null);
        }
    }

    private void openSecondFragment() {
        mToolbar.setVisibility(View.GONE);
        SecondRegisterFragment secondRegisterFragment = new SecondRegisterFragment();
        secondRegisterFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container,
                secondRegisterFragment)
                .commit();
    }
}


