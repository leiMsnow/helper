package com.tongban.im.fragment.account;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tb.api.AccountApi;
import com.tb.api.model.BaseEvent;
import com.tongban.corelib.utils.ToastUtil;
import com.tongban.corelib.widget.view.ClearEditText;
import com.tongban.im.R;
import com.tongban.im.common.VerifyTimerCount;
import com.tongban.im.fragment.base.AppBaseFragment;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * 找回密码，第二步
 *
 * @author fushudi
 */
public class ReSetPwdFragment extends AppBaseFragment {

    @Bind(R.id.tv_phone_num)
    TextView tvPhoneNum;
    @Bind(R.id.et_verify_code)
    ClearEditText etVerifyCode;
    @Bind(R.id.btn_verify_code)
    Button btnVerifyCode;
    @Bind(R.id.et_set_pwd)
    ClearEditText etSetPwd;
    @Bind(R.id.btn_submit)
    Button btnSubmit;

    private String mVerifyCode, mPwd, mInputPhone;

    private VerifyTimerCount mTime;
    private BaseEvent.RegisterEvent regEvent;


    public static ReSetPwdFragment getInstance(String mInputPhone) {
        ReSetPwdFragment mResetPwdFragment = new ReSetPwdFragment();
        Bundle bundle = new Bundle();
        bundle.putString("mInputPhone", mInputPhone);
        mResetPwdFragment.setArguments(bundle);
        return mResetPwdFragment;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_re_set_pwd;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        mInputPhone = bundle.getString("mInputPhone");
        tvPhoneNum.setText(mInputPhone);
    }

    @OnClick({R.id.btn_submit, R.id.btn_verify_code})
    public void onClick(View v) {
        if (v == btnSubmit) {
            if (regEvent == null) {
                ToastUtil.getInstance(mContext).showToast("请获取验证码");
                return;
            }
            AccountApi.getInstance().pwdReset(mVerifyCode, regEvent.verify_id, mInputPhone, mPwd, this);
            ToastUtil.getInstance(mContext).showToast("重置密码成功");
            getActivity().finish();

        } // 获取手机验证码
        else if (v == btnVerifyCode) {
            AccountApi.getInstance().getSMSCode(tvPhoneNum.getText().toString(), this);
        }
    }

    public void onEventMainThread(BaseEvent.RegisterEvent obj) {
        regEvent = obj;
        // 获取验证码成功
        if (obj.registerEnum == BaseEvent.RegisterEvent.RegisterEnum.SMS_CODE) {
            //构造CountDownTimer对象
            mTime = new VerifyTimerCount(btnVerifyCode);
            mTime.start();
            ToastUtil.getInstance(mContext).showToast(getString(R.string.verify_send_success));
        }
        // 找回成功，自动登录
        else if (obj.registerEnum == BaseEvent.RegisterEvent.RegisterEnum.VERIFY_CODE) {
            ToastUtil.getInstance(mContext).showToast(getResources().getString(R.string.pwd_reset_success));
            AccountApi.getInstance().login(tvPhoneNum.getText().toString(), mPwd, this);
        }
    }

    /**
     * 密码重置成功Event
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.PwdResetEvent obj) {
        AccountApi.getInstance().login(mInputPhone, mPwd, this);
    }

    @OnTextChanged({R.id.et_verify_code,R.id.et_set_pwd})
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @OnTextChanged({R.id.et_verify_code,R.id.et_set_pwd})
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @OnTextChanged({R.id.et_verify_code,R.id.et_set_pwd})
    public void afterTextChanged(Editable s) {
        mVerifyCode = etVerifyCode.getText().toString().trim();
        mPwd = etSetPwd.getText().toString().trim();
        if (!TextUtils.isEmpty(mVerifyCode) && !TextUtils.isEmpty(mPwd)) {
            btnSubmit.setEnabled(true);
        } else {
            btnSubmit.setEnabled(false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mTime != null) {
            mTime.cancel();
            mTime = null;
        }
    }

}
