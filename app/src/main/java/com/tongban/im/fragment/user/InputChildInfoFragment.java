package com.tongban.im.fragment.user;


import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.tongban.corelib.base.fragment.BaseApiFragment;
import com.tongban.im.R;


public class InputChildInfoFragment extends BaseApiFragment implements TextWatcher {
    private EditText etChildNickName, etChildBirthday;

    private String mChildNickName, mChildBirthday;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_input_child_info;
    }

    @Override
    protected void initView() {
        etChildNickName = (EditText) mView.findViewById(R.id.et_input_nickname);
        etChildBirthday = (EditText) mView.findViewById(R.id.et_input_birthday);
    }

    @Override
    protected void initData() {
        if (TextUtils.isEmpty(mChildBirthday) || TextUtils.isEmpty(mChildNickName)) {

        }
    }

    @Override
    protected void initListener() {
        etChildBirthday.addTextChangedListener(this);
        etChildNickName.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        mChildNickName = etChildNickName.getText().toString().trim();
        mChildBirthday = etChildBirthday.getText().toString().trim();
    }
}
