package com.tongban.im.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tongban.corelib.utils.ToastUtil;
import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.api.UserApi;

public class PassResetActivity extends BaseToolBarActivity implements View.OnClickListener, TextWatcher {
    private EditText etOldPass, etNewPass, etConfirmNewPass, etUserId;
    private Button btnPassReset;

    private String mOldPass, mNewPass, mConfirmNewPass;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_pass_reset;
    }

    @Override
    protected void initView() {
        etOldPass = (EditText) findViewById(R.id.et_old_pwd);
        etNewPass = (EditText) findViewById(R.id.et_new_pwd);
        etConfirmNewPass = (EditText) findViewById(R.id.et_confirm_new_pwd);
        btnPassReset = (Button) findViewById(R.id.btn_pass_reset);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        btnPassReset.setOnClickListener(this);
        etOldPass.addTextChangedListener(this);
        etNewPass.addTextChangedListener(this);
        etConfirmNewPass.addTextChangedListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnPassReset) {
            UserApi.getInstance().passReset(mOldPass, mNewPass, mConfirmNewPass, this);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        mOldPass = etOldPass.getText().toString().trim();
        mNewPass = etNewPass.getText().toString().trim();
        mConfirmNewPass = etConfirmNewPass.getText().toString().trim();
    }

    @Override
    public void onEventMainThread(Object obj) {
        ToastUtil.getInstance(mContext).showToast("重置密码成功");
        finish();
    }
}
