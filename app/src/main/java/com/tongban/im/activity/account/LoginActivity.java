package com.tongban.im.activity.account;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.dd.CircularProgressButton;
import com.tongban.corelib.model.ApiErrorResult;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.corelib.utils.ToastUtil;
import com.tongban.corelib.widget.view.BaseDialog;
import com.tongban.im.R;
import com.tongban.im.activity.MainActivity;
import com.tongban.im.activity.base.AccountBaseActivity;
import com.tongban.im.api.AccountApi;
import com.tongban.im.api.base.BaseApi;
import com.tongban.im.common.Consts;
import com.tongban.im.common.TransferCenter;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.user.OtherRegister;
import com.tongban.umeng.listener.UMSocializeOauthBackListener;
import com.tongban.umeng.listener.UMSocializeOauthListenerImpl;

import de.greenrobot.event.EventBus;

/**
 * 登录
 *
 * @author zhangleilei
 * @createTime 2015/7/16
 */
public class LoginActivity extends AccountBaseActivity implements TextWatcher, View.OnClickListener
        , UMSocializeOauthBackListener {

    private EditText etUser;
    private EditText etPwd;
    private TextView tvFindPwd;
    private TextView tvRegister;
    private CircularProgressButton btnLogin;

    private String mUser, mPwd;
    //是否其他设备登录进入
    private boolean isOther = false;
    //是否需要跳转到main界面
    private boolean mIsOpenMain;

    private UMSocializeOauthListenerImpl authListener;
    private String userInfoJson;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authListener = new UMSocializeOauthListenerImpl(mContext, this);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        etUser = (EditText) findViewById(R.id.et_phone_num);
        etPwd = (EditText) findViewById(R.id.et_pwd);
        tvFindPwd = (TextView) findViewById(R.id.tv_forget_pwd);
        tvRegister = (TextView) findViewById(R.id.tv_new_user_register);
        btnLogin = (CircularProgressButton) findViewById(R.id.btn_login);

        btnLogin.setIndeterminateProgressMode(true);

    }

    @Override
    protected void initData() {
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
        mUser = SPUtils.get(mContext, SPUtils.VISIT_FILE, Consts.USER_ACCOUNT, "").toString();
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
            btnLogin.setProgress(50);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    AccountApi.getInstance().login(mUser, mPwd, LoginActivity.this);
                }
            }, 1 * 1000);
        } else if (v == tvRegister) {
            TransferCenter.getInstance().startRegister();
            finish();
        } else if (v == tvFindPwd) {
            startActivity(new Intent(mContext, FindPwdActivity.class));
        } else {
            showProgress();
            switch (v.getId()) {
                case R.id.iv_wechat_login:
                    authListener.doWeCHatOauth();
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(mContext, MainActivity.class));
        finish();
    }

    /**
     * 登录成功
     */
    public void onEventMainThread(BaseEvent.UserLoginEvent obj) {
        hideProgress();
        SPUtils.put(mContext, SPUtils.VISIT_FILE, Consts.USER_ACCOUNT, mUser);
        if (TextUtils.isEmpty(obj.user.getNick_name())) {
            TransferCenter.getInstance().startRegister(true);
            finish();
        } else {
            connectIM(mIsOpenMain, obj.user.getChild_info() == null);
        }
    }

    /**
     * 接口出错
     *
     * @param obj
     */
    public void onEventMainThread(ApiErrorResult obj) {
        // 登录失败
        if (obj.getApiName().equals(AccountApi.LOGIN)) {
            btnLogin.setProgress(0);
        }
        // 授权登录失败
        else if (obj.getApiName().equals(AccountApi.OTHER_LOGIN)) {
            hideProgress();
            TransferCenter.getInstance().startOtherRegister(userInfoJson, type);
            finish();
        }
    }

    @Override
    public void oauthSuccess(final String userInfoJson, final String type) {
        this.userInfoJson = userInfoJson;
        this.type = type;
        OtherRegister otherRegister = JSON.parseObject(userInfoJson,
                new TypeReference<OtherRegister>() {
                });

        AccountApi.getInstance().otherLogin(otherRegister.getOpenId(), type, this);
    }


    @Override
    public void oauthFailure() {
        ToastUtil.getInstance(mContext).showToast("授权登录失败");
        hideProgress();
    }


    private int clickCount = 0;
    private long firstTime = 0;
    private long timeConsuming = 0;


    // 设置服务器地址 test
    public void setApiUrl(View v) {
        if (clickCount == 0) {
            firstTime = System.currentTimeMillis();
            clickCount++;
        } else if (clickCount == 1) {
            timeConsuming = System.currentTimeMillis();
            clickCount++;
        } else if (clickCount == 2) {
            if (timeConsuming - firstTime < 800) {
                final BaseDialog.Builder dialog = new BaseDialog.Builder(mContext);
                dialog.setPositiveButton("修改",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                BaseApi.getInstance().setHostUrl(mContext, dialog.getEditText());
                            }
                        });
                dialog.setNegativeButton("关闭",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        });

                dialog.setIsEditMode(true);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                dialog.setEditText(BaseApi.getInstance().getHostUrl());
                clickCount = 0;
                timeConsuming = 0;
                firstTime = 0;
            }
        }
    }
}
