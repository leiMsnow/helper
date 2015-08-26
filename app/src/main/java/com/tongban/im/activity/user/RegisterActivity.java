package com.tongban.im.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.tongban.corelib.utils.SPUtils;
import com.tongban.corelib.utils.ToastUtil;
import com.tongban.im.R;
import com.tongban.im.RongCloudEvent;
import com.tongban.im.activity.MainActivity;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.api.AccountApi;
import com.tongban.im.common.Consts;
import com.tongban.im.common.VerifyTimerCount;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.User;

/**
 * 注册
 * * @author zhangleilei
 *
 * @createTime 2015/7/16
 */
public class RegisterActivity extends BaseToolBarActivity implements TextWatcher, View.OnClickListener {

    private EditText etPhoneNum;
    private EditText etPwd;
    private EditText etVerifyCode;
    private TextView tvVerifyCode;
    private CheckBox cbAgree;
    private Button btnRegister;
    private String mPhoneNum, mPwd, mVerifyCode;

    private VerifyTimerCount mTime;
    private BaseEvent.RegisterEvent regEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getResources().getString(R.string.register));
    }

    @Override
    protected void initView() {
        etPhoneNum = (EditText) findViewById(R.id.et_phone_num);
        etPwd = (EditText) findViewById(R.id.et_pwd);
        etVerifyCode = (EditText) findViewById(R.id.et_verify_code);
        tvVerifyCode = (TextView) findViewById(R.id.tv_verify_code);
        cbAgree = (CheckBox) findViewById(R.id.cb_agree);
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
        etPhoneNum.addTextChangedListener(this);
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
        mPhoneNum = etPhoneNum.getText().toString();
        mPwd = etPwd.getText().toString();
        mVerifyCode = etVerifyCode.getText().toString();
        if (!TextUtils.isEmpty(mPhoneNum)) {
            tvVerifyCode.setEnabled(true);
        } else {
            tvVerifyCode.setEnabled(false);
        }
        if (!TextUtils.isEmpty(mPhoneNum) && !TextUtils.isEmpty(mVerifyCode)
                && mPwd.length() > 5) {
            btnRegister.setEnabled(true);
        } else {
            btnRegister.setEnabled(false);
        }
    }

    public void onEventMainThread(BaseEvent.RegisterEvent obj) {
        regEvent = obj;
        // 获取验证码成功
        if (regEvent.registerEnum == BaseEvent.RegisterEvent.RegisterEnum.SMS_CODE) {
            mTime = new VerifyTimerCount(tvVerifyCode);//构造CountDownTimer对象
            mTime.start();
            ToastUtil.getInstance(mContext).showToast(getString(R.string.verify_send_success));
        }
        // 注册成功，自动登录
        else if (regEvent.registerEnum == BaseEvent.RegisterEvent.RegisterEnum.REGISTER) {
            ToastUtil.getInstance(mContext).showToast(getResources().getString(R.string.register_success));
            AccountApi.getInstance().login(mPhoneNum, mPwd, this);
        }
    }

    public void onEventMainThread(String result) {
        ToastUtil.getInstance(mContext).showToast(result.toString());
    }


    public void onEventMainThread(User user) {
        SPUtils.put(mContext, Consts.USER_ACCOUNT, mPhoneNum);
        ToastUtil.getInstance(mContext).showToast(getString(R.string.login_success));
        startActivity(new Intent(mContext, MainActivity.class));
        RongCloudEvent.getInstance().connectIM(user.getIm_bind_token());
        finish();
    }

    @Override
    public void onClick(View v) {
        // 获取手机验证码
        if (v == tvVerifyCode) {
            if (mPhoneNum.length() != 11) {
                ToastUtil.getInstance(mContext).showToast("请输入正确的手机号码");
            } else {
                AccountApi.getInstance().getSMSCode(mPhoneNum, this);
            }
        }
        //校验手机验证码
        else if (v == btnRegister) {
            if (regEvent != null) {
                //校验手机验证码接口
                if (!cbAgree.isChecked()) {
                    ToastUtil.getInstance(mContext).showToast("请阅读并同意用户协议");
                } else {
                    AccountApi.getInstance().register(mPhoneNum, mPwd, regEvent.verify_id,
                            mVerifyCode, this);
                }
            } else {
                //提示获取验证码
                ToastUtil.getInstance(mContext).showToast(R.string.get_verify_code);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTime != null) {
            mTime.cancel();
            mTime = null;
        }
    }
}


