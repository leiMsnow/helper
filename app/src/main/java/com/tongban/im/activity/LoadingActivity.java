package com.tongban.im.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.tb.api.AccountApi;
import com.tb.api.FileUploadApi;
import com.tb.api.model.BaseEvent;
import com.tb.api.utils.TransferCenter;
import com.tongban.corelib.model.ApiErrorResult;
import com.tongban.corelib.utils.Constants;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.im.R;
import com.tongban.im.activity.base.AccountBaseActivity;
import com.tongban.im.utils.LocationUtils;

import de.greenrobot.event.EventBus;

/**
 * 加载界面
 * * @author zhangleilei
 *
 * @createTime 2015/7/16
 */
public class LoadingActivity extends AccountBaseActivity {

    private int tryCount = 0;
    private String freeAuthToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSwipeBackEnable(false);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_loading;
    }

    @Override
    protected void initData() {
        // 获取七牛token
        FileUploadApi.getInstance().fetchUploadToken();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (TextUtils.isEmpty(SPUtils.get(mContext, Constants.ADDRESS, "").toString())) {
                    LocationUtils.get(mContext).start();
                }
                freeAuthToken = SPUtils.get(mContext, Constants.FREEAUTH_TOKEN, "").toString();
                if (freeAuthToken.equals("")) {
                    connectIM();
                    finish();
                } else {
                    AccountApi.getInstance().tokenLogin(freeAuthToken, LoadingActivity.this);
                }
            }
        }, 2 * 1000);
    }

    //登录成功
    public void onEventMainThread(BaseEvent.UserLoginEvent obj) {
        if (TextUtils.isEmpty(obj.user.getNick_name())) {
            TransferCenter.getInstance().startRegister(true);
            finish();
        } else {
            connectIM(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    //登录失败，重试3次
    public void onEventMainThread(ApiErrorResult obj) {
        if (freeAuthToken != null) {
            tryCount++;
            if (tryCount < 3) {
                AccountApi.getInstance().tokenLogin(freeAuthToken, this);
                FileUploadApi.getInstance().fetchUploadToken();
            } else {
                connectIM();
            }
        }
    }
}
