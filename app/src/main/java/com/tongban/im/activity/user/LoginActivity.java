package com.tongban.im.activity.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import com.tongban.corelib.utils.SPUtils;
import com.tongban.corelib.utils.ToastUtil;
import com.tongban.corelib.widget.view.BaseDialog;
import com.tongban.im.R;
import com.tongban.im.RongCloudEvent;
import com.tongban.im.activity.MainActivity;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.api.AccountApi;
import com.tongban.im.common.Consts;
import com.tongban.im.common.TransferCenter;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.User;
import com.tongban.im.widget.view.ClearEditText;

import de.greenrobot.event.EventBus;

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
    //是否其他设备登录进入
    private boolean isOther = false;
    //是否需要跳转到main界面
    private boolean mIsOpenMain;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_login;
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
        setTitle(getResources().getString(R.string.login));
        mIsOpenMain = getIntent().getBooleanExtra(Consts.KEY_IS_MAIN, true);
        isOther = getIntent().getBooleanExtra(Consts.KEY_OTHER_CLIENT, false);
        if (isOther) {
            BaseDialog.Builder dialog = new BaseDialog.Builder(mContext);
            dialog.setMessage("您的账号已在别的设备登录");
            dialog.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                        }
                    });
            dialog.show();
        }
        mUser = SPUtils.get(mContext,SPUtils.VISIT_FILE, Consts.USER_ACCOUNT, "").toString();
        etUser.setText(mUser);
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
    public void onEventMainThread(BaseEvent.UserLoginEvent obj) {
        SPUtils.put(mContext,SPUtils.VISIT_FILE, Consts.USER_ACCOUNT, mUser);
        if (TextUtils.isEmpty(obj.user.getNick_name())) {
            Intent intent = new Intent(mContext, RegisterActivity.class);
            Bundle bundle = new Bundle();
            bundle.putBoolean(Consts.KEY_EDIT_USER, true);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        } else {
            connectIM(mIsOpenMain,obj.user.getChild_info() == null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
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
