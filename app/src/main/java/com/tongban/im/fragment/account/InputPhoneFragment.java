package com.tongban.im.fragment.account;


import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tongban.im.R;
import com.tongban.im.fragment.base.BaseToolBarFragment;

/**
 * 找回密码，第一步
 *
 * @author fushudi
 */
public class InputPhoneFragment extends BaseToolBarFragment implements View.OnClickListener, TextWatcher {
    private EditText etInputPhone;
    private Button btnSubmit;

    private ReSetPwdFragment mResetPwdFragment;

    private String mInputPhone;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_input_phone;
    }

    @Override
    protected void initView() {
        etInputPhone = (EditText) mView.findViewById(R.id.et_input_phone);
        btnSubmit = (Button) mView.findViewById(R.id.btn_submit);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        etInputPhone.addTextChangedListener(this);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnSubmit) {
            mResetPwdFragment = new ReSetPwdFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            Bundle bundle = new Bundle();
            bundle.putString("mInputPhone", mInputPhone);
            mResetPwdFragment.setArguments(bundle);
            transaction.replace(R.id.fl_container, mResetPwdFragment);
            transaction.addToBackStack(null);
            transaction.commit();
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
        mInputPhone = etInputPhone.getText().toString().trim();
        if (mInputPhone.length() == 11) {
            btnSubmit.setEnabled(true);
        } else {
            btnSubmit.setEnabled(false);
        }
    }
}
