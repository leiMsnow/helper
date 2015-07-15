package com.tongban.im.activity;

import android.content.Intent;
import android.os.Bundle;

import com.tongban.corelib.model.ApiFailedEvent;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.corelib.utils.ToastUtil;
import com.tongban.im.R;
import com.tongban.im.RongCloudEvent;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.api.UserApi;
import com.tongban.im.common.Consts;
import com.tongban.im.model.User;

public class LoadingActivity extends BaseToolBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_loading;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String token = SPUtils.get(mContext, Consts.FREEAUTH_TOKEN, "").toString();
                if (token.equals("")) {
                    startActivity(new Intent(mContext, LoginActivity.class));
                    finish();
                } else {
                    UserApi.getInstance().tokenLogin(token, LoadingActivity.this);
                }
            }
        }).start();
    }

    public void onEventMainThread(User user) {
        ToastUtil.getInstance(mContext).showToast("自动登录成功");
        RongCloudEvent.getInstance().connectIM(user.getIm_bind_token());
        startActivity(new Intent(mContext, MainActivity.class));
        finish();
    }

    public void onEventMainThread(ApiFailedEvent obj) {
        startActivity(new Intent(mContext, LoginActivity.class));
        finish();
    }
}
