package com.tongban.im.fragment.account;


import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.dd.CircularProgressButton;
import com.tongban.corelib.model.ApiErrorResult;
import com.tongban.im.R;
import com.tongban.im.api.AccountApi;
import com.tongban.im.common.Consts;
import com.tongban.im.fragment.base.BaseToolBarFragment;
import com.tongban.im.model.user.OtherRegister;

/**
 * 第三方注册第一步
 */
public class OtherRegisterFragment extends BaseToolBarFragment
        implements TextWatcher, View.OnClickListener {

    private EditText etPhoneNum;
    private CircularProgressButton btnNext;

    private String mPhoneNum;
//    private String mOtherInfo;
//    private String mOtherType;
//    private OtherRegister otherRegister;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_other_register;
    }

    @Override
    protected void initView() {
        etPhoneNum = (EditText) mView.findViewById(R.id.et_phone_num);
        btnNext = (CircularProgressButton) mView.findViewById(R.id.btn_next);
    }

    @Override
    protected void initData() {
//        if (getArguments() != null) {
//            mOtherInfo = getArguments().getString(Consts.OTHER_REGISTER_INFO);
//            if (!TextUtils.isEmpty(mOtherInfo)) {
//                mOtherType = getArguments().getString(Consts.OTHER_REGISTER_TYPE);
//                otherRegister = JSON.parseObject(mOtherInfo,
//                        new TypeReference<OtherRegister>() {
//                        });
//                otherRegister.setType(mOtherType);
//            }
//        }
    }


    @Override
    protected void initListener() {
        etPhoneNum.addTextChangedListener(this);
        btnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //校验手机
        if (v == btnNext) {
            btnNext.setProgress(50);
            AccountApi.getInstance().checkPhone(mPhoneNum, this);
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
        if (!TextUtils.isEmpty(mPhoneNum)) {
            btnNext.setEnabled(true);
        } else {
            btnNext.setEnabled(false);
        }
    }


    public void onEventMainThread(ApiErrorResult obj) {
        if (obj.getApiName().equals(AccountApi.REGISTER)) {
            btnNext.setProgress(0);
        }
    }


}
