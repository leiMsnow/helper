package com.tongban.im.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tongban.corelib.model.ApiResult;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.corelib.utils.ToastUtil;
import com.tongban.im.R;
import com.tongban.im.RongCloudEvent;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.api.UserApi;
import com.tongban.im.common.Consts;
import com.tongban.im.model.ApiErrorCode;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.User;

/**
 * 注册
 * * @author zhangleilei
 *
 * @createTime 2015/7/16
 */
public class RegisterActivity extends BaseToolBarActivity implements TextWatcher, View.OnClickListener {

    private EditText etUser;
    private EditText etPwd;
    private EditText etVerifyCode;
    private TextView tvVerifyCode;
    private Button btnRegister;
    private String mUser, mPwd, mVerifyCode;

    BaseEvent.RegisterEvent regEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getResources().getString(R.string.register));
    }

    @Override
    protected void initView() {
        etUser = (EditText) findViewById(R.id.et_user);
        etPwd = (EditText) findViewById(R.id.et_pwd);
        etVerifyCode = (EditText) findViewById(R.id.et_verify_code);
        tvVerifyCode = (TextView) findViewById(R.id.tv_verify_code);
        btnRegister = (Button) findViewById(R.id.btn_register);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_register;
    }

    @Override
    protected void initListener() {
        etUser.addTextChangedListener(this);
        etPwd.addTextChangedListener(this);
        etVerifyCode.addTextChangedListener(this);
        tvVerifyCode.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

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

    @Override
    public void afterTextChanged(Editable s) {
        mUser = etUser.getText().toString();
        mPwd = etPwd.getText().toString();
        mVerifyCode = etVerifyCode.getText().toString();
    }

    public void onEventMainThread(BaseEvent.RegisterEvent obj) {
        regEvent = obj;
        // 获取验证码
        if (regEvent.getRegisterEnum() == BaseEvent.RegisterEvent.RegisterEnum.REG) {
            UserApi.getInstance().fetch(regEvent.getUser_id(), mUser, "1", this);
        }
        // 获取验证码成功
        else if (regEvent.getRegisterEnum() == BaseEvent.RegisterEvent.RegisterEnum.FETCH) {
            ToastUtil.getInstance(mContext).showToast(getString(R.string.verify_send_success));
        }
        // 注册成功
        else if (regEvent.getRegisterEnum() == BaseEvent.RegisterEvent.RegisterEnum.EXAM) {
            ToastUtil.getInstance(mContext).showToast(getResources().getString(R.string.register_success));
            UserApi.getInstance().tokenLogin(regEvent.getFreeauth_token(), this);
        }

    }

    public void onEventMainThread(String result) {
        ToastUtil.getInstance(mContext).showToast(result.toString());
    }

    public void onEventMainThread(ApiResult result) {
        if (result.getStatusCode() == ApiErrorCode.User.USER_REGISTERED) {
            UserApi.getInstance().fetch(result.getData().toString(), mUser, "1", this);
        }
    }

    public void onEventMainThread(User user) {
        SPUtils.put(mContext, Consts.USER_ACCOUNT, mUser);
        ToastUtil.getInstance(mContext).showToast(getString(R.string.login_success));
        startActivity(new Intent(mContext, MainActivity.class));
        RongCloudEvent.getInstance().connectIM(user.getIm_bind_token());
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v == tvVerifyCode) {
            // 注册
            UserApi.getInstance().register(mUser, mUser, mPwd, this);
        } else if (v == btnRegister) {
            //校验手机验证码
            if (regEvent != null &&
                    regEvent.getRegisterEnum() == BaseEvent.RegisterEvent.RegisterEnum.FETCH) {
                UserApi.getInstance().exam(regEvent.getVerify_id(), mVerifyCode, this);
            }
        }
    }


}
