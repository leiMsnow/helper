package com.tongban.im.activity;

import android.content.Intent;
import android.os.Bundle;

import com.baidu.location.BDLocation;
import com.tongban.corelib.model.ApiResult;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.im.R;
import com.tongban.im.RongCloudEvent;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.activity.user.LoginActivity;
import com.tongban.im.api.FileUploadApi;
import com.tongban.im.api.LocationApi;
import com.tongban.im.api.AccountApi;
import com.tongban.im.common.Consts;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.QiniuToken;
import com.tongban.im.model.User;
import com.tongban.im.utils.LocationUtils;

/**
 * 加载界面
 * * @author zhangleilei
 *
 * @createTime 2015/7/16
 */
public class LoadingActivity extends BaseToolBarActivity {


    private String mToken;

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
        mToken = SPUtils.get(mContext, Consts.FREEAUTH_TOKEN, "").toString();
        if (mToken.equals("")) {
            startActivity(new Intent(mContext, LoginActivity.class));
            finish();
        } else {
            AccountApi.getInstance().tokenLogin(mToken, LoadingActivity.this);
        }
        // 获取七牛token
        FileUploadApi.getInstance().fetchUploadToken(this);
    }

    public void onEventMainThread(User user) {
        RongCloudEvent.getInstance().connectIM(user.getIm_bind_token());
        startActivity(new Intent(mContext, MainActivity.class));
        finish();
    }

    /**
     * 获取七牛token成功的回调
     */
    public void onEventMainThread(QiniuToken qiniuToken) {
        SPUtils.put(mContext, Consts.QINIU_TOKEN, qiniuToken.getUpload_token());
    }

    public void onEventMainThread(ApiResult obj) {
        SPUtils.put(mContext, Consts.FREEAUTH_TOKEN, "");
        startActivity(new Intent(mContext, LoginActivity.class));
        finish();
    }

    public void onEventMainThread(BaseEvent.ConnectionErrorEvent obj) {
        RongCloudEvent.getInstance().
                connectIM(SPUtils.get(mContext, Consts.IM_BIND_TOKEN, "").toString());
        startActivity(new Intent(mContext, MainActivity.class));
        finish();

    }

}
