package com.tongban.im.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import com.tongban.corelib.utils.SPUtils;
import com.tongban.corelib.utils.ToastUtil;
import com.tongban.im.R;
import com.tongban.im.RongCloudEvent;
import com.tongban.im.activity.MainActivity;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.api.AccountApi;
import com.tongban.im.common.Consts;
import com.tongban.im.model.User;
import com.tongban.im.widget.view.ClearEditText;

/**
 * 登录
 *
 * @author zhangleilei
 * @createTime 2015/7/16
 */
public class LoginActivity extends BaseToolBarActivity implements TextWatcher, View.OnClickListener {

    private ClearEditText etUser;
    private ClearEditText etPwd;
    private TextView tvFindPwd;
    private TextView tvRegister;
    private Button btnLogin;

    private String mUser, mPwd;
    //是否需要跳转到main界面
    private boolean mIsOpenMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getResources().getString(R.string.login));

    }

    @Override
    protected void initView() {
        etUser = (ClearEditText) findViewById(R.id.et_phone_num);
        etPwd = (ClearEditText) findViewById(R.id.et_pwd);
        tvFindPwd = (TextView) findViewById(R.id.tv_forget_pwd);
        tvRegister = (TextView) findViewById(R.id.tv_new_user_register);
        btnLogin = (Button) findViewById(R.id.btn_login);
    }

    @Override
    protected void initData() {
        mIsOpenMain = getIntent().getBooleanExtra(Consts.KEY_IS_MAIN, true);
        mUser = SPUtils.get(mContext, Consts.USER_ACCOUNT, "").toString();
        etUser.setText(mUser);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_login;
    }

    @Override
    protected void initListener() {
        etUser.addTextChangedListener(this);
        etPwd.addTextChangedListener(this);
        btnLogin.setOnClickListener(this);
        tvFindPwd.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        etPwd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    onClick(btnLogin);
                }
                return false;
            }
        });
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
        if (mUser.length() == 0 || mPwd.length() < 6) {
            btnLogin.setEnabled(false);
        } else {
            btnLogin.setEnabled(true);
        }
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    //登录成功
    public void onEventMainThread(User user) {
        SPUtils.put(mContext, Consts.USER_ACCOUNT, mUser);
        connectIM(user.getUser_id(), user.getChild_info() == null);
    }

    @Override
    public void onClick(View v) {
        if (v == btnLogin) {
            AccountApi.getInstance().login(mUser, mPwd, this);
        } else if (v == tvRegister) {
            startActivity(new Intent(mContext, RegisterActivity.class));
        } else if (v == tvFindPwd) {
            startActivity(new Intent(mContext, FindPwdActivity.class));
        }
    }


}
