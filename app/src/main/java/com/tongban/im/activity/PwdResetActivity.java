package com.tongban.im.activity;

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
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public void onEventMainThread(BaseEvent.PwdResetEvent result) {
        ToastUtil.getInstance(mContext).showToast(result.getResult());
        finish();
    }
}
