package com.tongban.im.fragment.account;


import android.text.Editable;
import android.view.View;
import android.widget.Button;

import com.tongban.corelib.widget.view.ClearEditText;
import com.tongban.im.R;
import com.tongban.im.fragment.base.AppBaseFragment;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * 找回密码，第一步
 *
 * @author fushudi
 */
public class InputPhoneFragment extends AppBaseFragment {

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

    @OnTextChanged(R.id.et_input_phone)
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @OnTextChanged(R.id.et_input_phone)
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @OnTextChanged(R.id.et_input_phone)
    public void afterTextChanged(Editable s) {
        mInputPhone = etInputPhone.getText().toString().trim();
        if (mInputPhone.length() == 11) {
            btnSubmit.setEnabled(true);
        } else {
            btnSubmit.setEnabled(false);
        }
    }
}
