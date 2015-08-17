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


public class ReSetPwdFragment extends BaseApiFragment implements View.OnClickListener, TextWatcher {

    private TextView tvPhoneNum;
    private EditText etVerifyCode;
    private EditText etSetPwd;
    private Button btnSubmit;

    private String mVerifyCode, mPwd;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_re_set_pwd;
    }

    @Override
    protected void initView() {
        tvPhoneNum = (TextView) mView.findViewById(R.id.tv_phone_num);
        etVerifyCode = (EditText) mView.findViewById(R.id.et_verify_code);
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
        etSetPwd.addTextChangedListener(this);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnSubmit) {
            ToastUtil.getInstance(mContext).showToast(R.string.submit);
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
}
