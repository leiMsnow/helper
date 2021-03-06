package com.tongban.im.fragment.account;


import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.dd.CircularProgressButton;
import com.tb.api.AccountApi;
import com.tb.api.model.BaseEvent;
import com.tongban.corelib.model.ApiErrorResult;
import com.tongban.corelib.utils.ToastUtil;
import com.tongban.corelib.widget.view.ClearEditText;
import com.tongban.im.R;
import com.tongban.im.common.VerifyTimerCount;
import com.tongban.im.fragment.base.AppBaseFragment;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * 注册第一步
 */
public class FirstRegisterFragment extends AppBaseFragment {

    @Bind(R.id.et_phone_num)
    ClearEditText etPhoneNum;
    @Bind(R.id.et_pwd)
    ClearEditText etPwd;
    @Bind(R.id.et_verify_code)
    ClearEditText etVerifyCode;
    @Bind(R.id.btn_verify_code)
    Button btnVerifyCode;
    @Bind(R.id.btn_register)
    CircularProgressButton btnRegister;

    private String mPhoneNum, mPwd, mVerifyId, mVerifyCode;

    private VerifyTimerCount mTime;
    private BaseEvent.RegisterEvent regEvent;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_first_register;
    }

    @Override
    protected void initData() {
        btnRegister.setIndeterminateProgressMode(true);
    }

    @OnClick({R.id.btn_verify_code, R.id.btn_register})
    public void onClick(View v) {
        // 获取手机验证码
        if (v == btnVerifyCode) {
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
                btnRegister.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AccountApi.getInstance().register(mPhoneNum, mPwd, mVerifyId,
                                mVerifyCode, FirstRegisterFragment.this);
                    }
                }, 2 * 1000);

            } else {
                //提示获取验证码
                ToastUtil.getInstance(mContext).showToast("请获取验证码");
            }
        }
    }

    @OnTextChanged({R.id.et_phone_num,R.id.et_pwd,R.id.et_verify_code})
    public void afterTextChanged(Editable s) {
        mPhoneNum = etPhoneNum.getText().toString();
        mPwd = etPwd.getText().toString();
        mVerifyCode = etVerifyCode.getText().toString();
        if (!TextUtils.isEmpty(mPhoneNum)) {
            btnVerifyCode.setEnabled(true);
        } else {
            btnVerifyCode.setEnabled(false);
        }
        if (!TextUtils.isEmpty(mPhoneNum) && !TextUtils.isEmpty(mVerifyCode)
                && mPwd.length() > 5) {
            btnRegister.setEnabled(true);
        } else {
            btnRegister.setEnabled(false);
        }
    }


    public void onEventMainThread(ApiErrorResult obj) {
        if (obj.getApiName().equals(AccountApi.REGISTER)) {
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
            mTime = new VerifyTimerCount(btnVerifyCode);
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
