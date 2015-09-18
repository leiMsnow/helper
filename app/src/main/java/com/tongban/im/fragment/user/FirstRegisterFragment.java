package com.tongban.im.fragment.user;


import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tongban.corelib.utils.ToastUtil;
import com.tongban.im.R;
import com.tongban.im.api.AccountApi;
import com.tongban.im.common.Consts;
import com.tongban.im.common.VerifyTimerCount;
import com.tongban.im.fragment.base.BaseToolBarFragment;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.OtherRegister;

/**
 * 注册第一步
 */
public class FirstRegisterFragment extends BaseToolBarFragment
        implements TextWatcher, View.OnClickListener {

    private EditText etPhoneNum;
    private EditText etPwd;
    private EditText etVerifyCode;
    private Button btnGetSMSCode;
    private CheckBox cbAgree;
    private Button btnRegister;
    private String mPhoneNum, mPwd, mVerifyId, mVerifyCode;

    private VerifyTimerCount mTime;
    private BaseEvent.RegisterEvent regEvent;

    private String mOtherInfo;
    private String mOtherType;
    private OtherRegister otherRegister;

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
        cbAgree = (CheckBox) mView.findViewById(R.id.cb_agree);
        btnRegister = (Button) mView.findViewById(R.id.btn_register);
    }

    @Override
    protected void initData() {
        if (getArguments() != null) {
            mOtherInfo = getArguments().getString(Consts.OTHER_REGISTER_INFO);
            if (!TextUtils.isEmpty(mOtherInfo)) {
                mOtherType = getArguments().getString(Consts.OTHER_REGISTER_TYPE);
                otherRegister = JSON.parseObject(mOtherInfo,
                        new TypeReference<OtherRegister>() {
                        });
                otherRegister.setType(mOtherType);
            }
        }
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
                if (!cbAgree.isChecked()) {
                    ToastUtil.getInstance(mContext).showToast("请阅读并同意用户协议");
                } else {
                    if (otherRegister == null) {
                        AccountApi.getInstance().register(mPhoneNum, mPwd, mVerifyId,
                                mVerifyCode, this);
                    }
                    // 第三方注册
                    else {
                        AccountApi.getInstance().checkPhone(mPhoneNum, this);
                    }
                }
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

    public void onEventMainThread(BaseEvent.CheckPhoneEvent obj) {
        AccountApi.getInstance().otherRegister(mPhoneNum, mPwd,
                otherRegister.getOpenId(), otherRegister.getType(),
                mVerifyId, mVerifyCode, this);
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
            mTime = new VerifyTimerCount(btnGetSMSCode);//构造CountDownTimer对象
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
