package com.tongban.im.fragment.account;


import android.text.Editable;
import android.text.TextUtils;
import android.view.View;

import com.dd.CircularProgressButton;
import com.tongban.corelib.model.ApiErrorResult;
import com.tongban.corelib.widget.view.ClearEditText;
import com.tongban.im.R;
import com.tongban.im.api.AccountApi;
import com.tongban.im.fragment.base.BaseToolBarFragment;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * 第三方注册第一步
 */
public class OtherRegisterFragment extends BaseToolBarFragment {

    @Bind(R.id.et_phone_num)
    ClearEditText etPhoneNum;
    @Bind(R.id.btn_next)
    CircularProgressButton btnNext;

    private String mPhoneNum;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_other_register;
    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.btn_next)
    public void onClick(View v) {
        //校验手机
        if (v == btnNext) {
            btnNext.setProgress(50);
            AccountApi.getInstance().checkPhone(mPhoneNum, this);
        }
    }

    @OnTextChanged(R.id.et_phone_num)
    public void afterTextChanged(Editable s) {
        mPhoneNum = etPhoneNum.getText().toString();
        if (!TextUtils.isEmpty(mPhoneNum)) {
            btnNext.setEnabled(true);
        } else {
            btnNext.setEnabled(false);
        }
    }

    public void onEventMainThread(ApiErrorResult obj) {
        if (obj.getApiName().equals(AccountApi.REGISTER)) {
            btnNext.setProgress(0);
        }
    }
}
