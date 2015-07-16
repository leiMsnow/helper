package com.tongban.im.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tongban.corelib.model.ApiResult;
import com.tongban.corelib.utils.ToastUtil;
import com.tongban.im.R;
import com.tongban.im.RongCloudEvent;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.api.UserApi;
import com.tongban.im.model.ApiErrorCode;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.User;

/**
 * 注册
 */
public class RegisterActivity extends BaseToolBarActivity implements TextWatcher, View.OnClickListener {

    private EditText etUser;
    private EditText etPwd;
    private EditText etVerifyCode;
    private Button btnLogin;
    private String mUser, mPwd, mVerifyCode;

    BaseEvent.RegisterEvent registerEvent;

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
        btnLogin = (Button) findViewById(R.id.btn_login);
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
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        mUser = etUser.getText().toString();
        mPwd = etPwd.getText().toString();
        mVerifyCode = etVerifyCode.getText().toString();
    }

    public void onEventMainThread(BaseEvent.RegisterEvent reg) {
        // 获取验证码
        if (reg.getRegisterEnum() == BaseEvent.RegisterEvent.RegisterEnum.REG) {
            registerEvent = reg;
            UserApi.getInstance().fetch(reg.getUser_id(), mUser, "1", this);
        }
        // 获取验证码成功
        else if (reg.getRegisterEnum() == BaseEvent.RegisterEvent.RegisterEnum.FETCH) {
            registerEvent = reg;
            ToastUtil.getInstance(mContext).showToast(getString(R.string.verify_send_success));
        }
        // 注册成功
        else if (reg.getRegisterEnum() == BaseEvent.RegisterEvent.RegisterEnum.EXAM) {
            ToastUtil.getInstance(mContext).showToast(getResources().getString(R.string.register_success));
            UserApi.getInstance().tokenLogin(reg.getFreeauth_token(), this);
        }
    }

    public void onEventMainThread(User user) {
        ToastUtil.getInstance(mContext).showToast(getString(R.string.login_success));
        RongCloudEvent.getInstance().connectIM(user.getIm_bind_token());
        startActivity(new Intent(mContext, MainActivity.class));
        setResult(RESULT_OK);
        finish();
    }

    public void onEventMainThread(Object error) {
        if (error instanceof ApiResult) {
            ApiResult result = (ApiResult) error;
            if (result.getStatusCode() == ApiErrorCode.User.USER_REGISTERED) {
                UserApi.getInstance().fetch(result.getData().toString(), mUser, "1", this);
            }
        }else{
            ToastUtil.getInstance(mContext).showToast(error.toString());
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnLogin) {
            if (registerEvent != null) {
                //校验手机验证码
                if (registerEvent.getRegisterEnum() == BaseEvent.RegisterEvent.RegisterEnum.FETCH) {
                    UserApi.getInstance().exam(registerEvent.getVerify_id(), mVerifyCode, this);
                }
            }
            // 注册
            else {
                UserApi.getInstance().register(mUser, mUser, mPwd, this);
            }


        }
    }


}
