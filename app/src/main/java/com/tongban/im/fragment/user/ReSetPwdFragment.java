package com.tongban.im.fragment.user;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tongban.corelib.base.fragment.BaseApiFragment;
import com.tongban.corelib.utils.ToastUtil;
import com.tongban.im.R;
import com.tongban.im.api.AccountApi;
import com.tongban.im.common.VerifyTimerCount;
import com.tongban.im.model.BaseEvent;

/**
 * 找回密码，第二步
 *
 * @author fushudi
 */
public class ReSetPwdFragment extends BaseApiFragment implements View.OnClickListener, TextWatcher {

    private TextView tvPhoneNum;
    private EditText etVerifyCode;
    private TextView tvVerifyCode;
    private EditText etSetPwd;
    private Button btnSubmit;

    private String mVerifyCode, mPwd;

    private VerifyTimerCount mTime;


    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_re_set_pwd;
    }

    @Override
    protected void initView() {
        tvPhoneNum = (TextView) mView.findViewById(R.id.tv_phone_num);
        etVerifyCode = (EditText) mView.findViewById(R.id.et_verify_code);
        tvVerifyCode = (TextView) mView.findViewById(R.id.tv_verify_code);
        etSetPwd = (EditText) mView.findViewById(R.id.et_set_pwd);
        btnSubmit = (Button) mView.findViewById(R.id.btn_submit);
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        String mInputPhone = bundle.getString("mInputPhone");
        tvPhoneNum.setText(mInputPhone);
    }

    @Override
    protected void initListener() {
        etVerifyCode.addTextChangedListener(this);
        tvVerifyCode.setOnClickListener(this);
        etSetPwd.addTextChangedListener(this);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnSubmit) {
            ToastUtil.getInstance(mContext).showToast(R.string.submit);
        } // 获取手机验证码
        else if (v == tvVerifyCode) {
            AccountApi.getInstance().getSMSCode(tvPhoneNum.getText().toString(), this);
        }
    }

    public void onEventMainThread(BaseEvent.RegisterEvent obj) {
        // 获取验证码成功
        if (obj.registerEnum == BaseEvent.RegisterEvent.RegisterEnum.SMS_CODE) {
            //构造CountDownTimer对象
            mTime = new VerifyTimerCount(tvVerifyCode);
            mTime.start();
            ToastUtil.getInstance(mContext).showToast(getString(R.string.verify_send_success));
        }
        // 找回成功，自动登录
        else if (obj.registerEnum == BaseEvent.RegisterEvent.RegisterEnum.VERIFY_CODE) {
            ToastUtil.getInstance(mContext).showToast(getResources().getString(R.string.pwd_reset_success));
            AccountApi.getInstance().login(tvPhoneNum.getText().toString(), mPwd, this);
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
