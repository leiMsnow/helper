package com.tongban.im.fragment.account;


import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import com.tongban.corelib.widget.view.ClearEditText;
import com.tongban.im.R;
import com.tongban.im.fragment.base.BaseToolBarFragment;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 找回密码，第一步
 *
 * @author fushudi
 */
public class InputPhoneFragment extends BaseToolBarFragment implements
        TextWatcher {

    @Bind(R.id.et_input_phone)
    ClearEditText etInputPhone;
    @Bind(R.id.btn_submit)
    Button btnSubmit;

    private String mInputPhone;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_input_phone;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        etInputPhone.addTextChangedListener(this);
    }

    @OnClick(R.id.btn_submit)
    public void onClick(View v) {
        if (v == btnSubmit) {
            ReSetPwdFragment mResetPwdFragment = ReSetPwdFragment.getInstance(mInputPhone);

            getFragmentManager().beginTransaction()
                    .replace(R.id.fl_container, mResetPwdFragment)
                    .addToBackStack(null)
                    .commit();
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
