package com.tongban.im.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tongban.corelib.utils.LogUtil;
import com.tongban.corelib.utils.ToastUtil;
import com.tongban.im.R;
import com.tongban.im.RongCloudEvent;
import com.tongban.im.activity.base.BaseToolBarActivity;

import io.rong.imkit.RongIM;
import io.rong.imkit.logic.MessageCounterLogic;
import io.rong.imlib.RongIMClient;

public class LoginActivity extends BaseToolBarActivity implements TextWatcher, View.OnClickListener {

    private EditText etUser;
    private EditText etPwd;
    private Button btnLogin;
    private String mUser, mPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getResources().getString(R.string.title_activity_login));
    }

    @Override
    protected void initView() {
        etUser = (EditText) findViewById(R.id.et_user);
        etPwd = (EditText) findViewById(R.id.et_pwd);
        btnLogin = (Button) findViewById(R.id.btn_login);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_login;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void initListener() {
        etUser.addTextChangedListener(this);
        etPwd.addTextChangedListener(this);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        mUser = etUser.getText().toString();
        mPwd = etPwd.getText().toString();
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public void onClick(View v) {
        if (v == btnLogin) {
            String connectToken = "FrtKZnEVxSFoTnvEWt5Xo6lOBUKc7eU7iEQmFmaXJUnvs0TnNd9FJFaNW8k0aL5J0HLaHm9d8b835HzOhS7ZK/GRlVIDev9G";
            if (etUser.getText().toString().equals("android01")) {
                connectToken = "Q5CE0ev/gZ3dqpYi0l9Kq6lOBUKc7eU7iEQmFmaXJUnvs0TnNd9FJNYnfETdxt64QDNjPeW16tBwFp0yA4yWxPGRlVIDev9G";
            }

            RongCloudEvent.getInstance().connectIM(connectToken);
            startActivity(new Intent(mContext,MainActivity.class));
            finish();
        }
    }


}
