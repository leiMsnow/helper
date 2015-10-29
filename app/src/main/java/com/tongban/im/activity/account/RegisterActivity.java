package com.tongban.im.activity.account;

import android.os.Bundle;
import android.view.View;

import com.tb.api.FileUploadApi;
import com.tb.api.utils.ApiConstants;
import com.tongban.corelib.utils.Constants;
import com.tongban.im.R;
import com.tongban.im.activity.base.RegisterBaseActivity;
import com.tongban.im.common.Consts;
import com.tongban.im.fragment.account.FirstRegisterFragment;

/**
 * 注册
 * * @author zhangleilei
 *
 * @createTime 2015/7/16
 */
public class RegisterActivity extends RegisterBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FileUploadApi.getInstance().fetchUploadToken();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_register;
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        if (isEditUser) {
            if (mToolbar != null)
                mToolbar.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initData() {
        if (getIntent().getExtras() != null) {
            bundle = getIntent().getExtras();
            isEditUser = bundle.getBoolean(ApiConstants.KEY_EDIT_USER, false);
            if (!isEditUser) {
                FirstRegisterFragment registerFragment = new FirstRegisterFragment();
                registerFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fl_container,
                                registerFragment)
                        .commit();
            } else {
                openSecondFragment();
            }
        }
    }

}


