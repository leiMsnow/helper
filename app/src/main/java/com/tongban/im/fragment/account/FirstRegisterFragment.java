package com.tongban.im.fragment.account;


import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dd.CircularProgressButton;
import com.tongban.corelib.model.ApiErrorResult;
import com.tongban.corelib.utils.ToastUtil;
import com.tongban.im.R;
import com.tongban.im.api.AccountApi;
import com.tongban.im.common.VerifyTimerCount;
import com.tongban.im.fragment.base.BaseToolBarFragment;
import com.tongban.im.model.BaseEvent;

/**
 * 注册第一步
 */
public class FirstRegisterFragment extends BaseToolBarFragment
        implements TextWatcher, View.OnClickListener {

    private EditText etPhoneNum;
    private EditText etPwd;
    private EditText etVerifyCode;
    private Button btnGetSMSCode;
    private CircularProgressButton btnRegister;
    private String mPhoneNum, mPwd, mVerifyId, mVerifyCode;

    private VerifyTimerCount mTime;
    private BaseEvent.RegisterEvent regEvent;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_first_register;
    }

    @Override
    protected void initView() {
        etPhoneNum = (EditText) mView.findViewById(R.id.et_phone_num);
        etPwd = (EditText) mView.findViewById(R.id.et_pwd);
        etVerifyCode = (EditText) mView.findViewById(R.id.et_verify_code);
        btnGetSMSCode = (Button) mView.findViewById(R.id.btn_verify_code);
        btnRegister = (CircularProgressButton) mView.findViewById(R.id.btn_register);
    }

    @Override
    protected void initData() {
    }


    @Override
    protected void initListener() {
        etPhoneNum.addTextChangedListener(this);
        etPwd.addTextChangedListener(this);
        etVerifyCode.addTextChangedListener(this);
        btnGetSMSCode.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // 获取手机验证码
        if (v == btnGetSMSCode) {
            if (mPhoneNum.length() != 11) {
                ToastUtil.getInstance(mContext).showToast("请输入正确的手机号码");
            } else {
                AccountApi.getInstance().getSMSCode(mPhoneNum, this);
            }
        }
        //校验手机验证码
        else if (v == btnRegister) {
            if (regEvent != null) {
                btnRegister.setProgress(50);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AccountApi.getInstance().register(mPhoneNum, mPwd, mVerifyId,
                                mVerifyCode, FirstRegisterFragment.this);
                    }
                }, 2 * 1000);

            } else {
                //提示获取验证码
                ToastUtil.getInstance(mContext).showToast(R.string.get_verify_code);
            }
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
        mPhoneNum = etPhoneNum.getText().toString();
        mPwd = etPwd.getText().toString();
        mVerifyCode = etVerifyCode.getText().toString();
        if (!TextUtils.isEmpty(mPhoneNum)) {
            btnGetSMSCode.setEnabled(true);
        } else {
            btnGetSMSCode.setEnabled(false);
        }
        if (!TextUtils.isEmpty(mPhoneNum) && !TextUtils.isEmpty(mVerifyCode)
                && mPwd.length() > 5) {
            btnRegister.setEnabled(true);
        } else {
            btnRegister.setEnabled(false);
        }
    }


    public void onEventMainThread(ApiErrorResult obj) {
        if (obj.getApiName().equals(AccountApi.REGISTER)){
            btnRegister.setProgress(0);
        }
    }

    /**
     * 注册成功
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.RegisterEvent obj) {
        regEvent = obj;
        // 获取验证码成功
        if (regEvent.registerEnum == BaseEvent.RegisterEvent.RegisterEnum.SMS_CODE) {
            mVerifyId = obj.verify_id;
            //构造CountDownTimer对象
            mTime = new VerifyTimerCount(btnGetSMSCode);
            mTime.start();
            ToastUtil.getInstance(mContext).showToast(getString(R.string.verify_send_success));
        }
        // 注册成功
        else if (regEvent.registerEnum == BaseEvent.RegisterEvent.RegisterEnum.REGISTER) {
            AccountApi.getInstance().login(mPhoneNum, mPwd, this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTime != null) {
            mTime.cancel();
            mTime = null;
        }
    }
}
