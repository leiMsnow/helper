package com.tongban.im.activity.user;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tongban.corelib.utils.ToastUtil;
import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.api.UserApi;
import com.tongban.im.model.BaseEvent;

/**
 * 重置密码界面
 *
 * @author fushudi
 * @createTime 2015/07/27
 */
public class PwdResetActivity extends BaseToolBarActivity implements View.OnClickListener, TextWatcher {
    private EditText etOldPwd, etNewPwd, etConfirmNewPwd, etUserId;
    private Button btnPwdReset;

    private String mOldPwd, mNewPwd, mConfirmNewPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.pwd_reset));
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_pass_reset;
    }

    @Override
    protected void initView() {
        etOldPwd = (EditText) findViewById(R.id.et_old_pwd);
        etNewPwd = (EditText) findViewById(R.id.et_new_pwd);
        etConfirmNewPwd = (EditText) findViewById(R.id.et_confirm_new_pwd);
        btnPwdReset = (Button) findViewById(R.id.btn_pwd_reset);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        btnPwdReset.setOnClickListener(this);
        etOldPwd.addTextChangedListener(this);
        etNewPwd.addTextChangedListener(this);
        etConfirmNewPwd.addTextChangedListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnPwdReset) {
            if (mOldPwd.length() >= 6) {
//                if (mNewPwd.length() == 0) {
//                    ToastUtil.getInstance(mContext).showToast(getResources().getString(R.string.new_pwd_empty));
//                } else if (mConfirmNewPwd.length() == 0) {
//                    ToastUtil.getInstance(mContext).showToast(getResources().getString(R.string.confirm_new_pwd_empty));
//                }
//                else {
                if (mOldPwd.equals(mNewPwd)) {
                    ToastUtil.getInstance(mContext).showToast(getResources().getString(R.string.old_pwd_equals_new_pwd));
                } else {
                    if (mNewPwd.length() >= 6) {
                        if (mNewPwd.equals(mConfirmNewPwd)) {
                            UserApi.getInstance().pwdReset(mOldPwd, mNewPwd, this);
                        } else {
                            ToastUtil.getInstance(mContext).showToast(getResources().getString(R.string.twice_pwd_same));
                        }
                    } else {
                        ToastUtil.getInstance(mContext).showToast(getResources().getString(R.string.pwd_least));
                    }
                }
//                }
            }
            else {
                if (mOldPwd.length() == 0) {
                    ToastUtil.getInstance(mContext).showToast(getString(R.string.old_pwd_empty));
                } else {
                    ToastUtil.getInstance(mContext).showToast(getString(R.string.pwd_least));
                }
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
        mOldPwd = etOldPwd.getText().toString().trim();
        mNewPwd = etNewPwd.getText().toString().trim();
        mConfirmNewPwd = etConfirmNewPwd.getText().toString().trim();
        if (mOldPwd.length() < 6 || mNewPwd.length() < 6 || mConfirmNewPwd.length() < 6) {
            btnPwdReset.setEnabled(false);
        } else {
            btnPwdReset.setEnabled(true);
        }
    }

    public void onEventMainThread(BaseEvent.PwdResetEvent result) {
        ToastUtil.getInstance(mContext).showToast(result.getResult());
        finish();
    }
}
