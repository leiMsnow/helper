package com.tongban.im.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.tongban.corelib.base.api.ApiCallback;
import com.tongban.corelib.model.ApiResult;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.activity.user.ChildInfoActivity;
import com.tongban.im.activity.user.LoginActivity;
import com.tongban.im.api.AccountApi;
import com.tongban.im.api.FileUploadApi;
import com.tongban.im.common.Consts;
import com.tongban.im.model.User;
import com.tongban.im.utils.LocationUtils;

/**
 * 加载界面
 * * @author zhangleilei
 *
 * @createTime 2015/7/16
 */
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
        if (TextUtils.isEmpty(SPUtils.get(mContext, Consts.ADDRESS, "").toString())) {
            LocationUtils.get(mContext).start();
        }

        if ((boolean) SPUtils.get(mContext, Consts.FIRST_SET_CHILD_INFO, true)) {
            startActivity(new Intent(mContext, ChildInfoActivity.class));
            finish();
        } else {
            String freeAuthToken = SPUtils.get(mContext, Consts.FREEAUTH_TOKEN, "").toString();
            if (freeAuthToken.equals("")) {
                connectIM();
            } else {
                AccountApi.getInstance().tokenLogin(freeAuthToken, new ApiCallback() {
                    @Override
                    public void onStartApi() {

                    }

                    @Override
                    public void onComplete(Object obj) {
                        if (obj instanceof ApiResult) {
                            SPUtils.put(mContext, Consts.FREEAUTH_TOKEN, "");
                            startActivity(new Intent(mContext, LoginActivity.class));
                            finish();
                        } else if (obj instanceof User) {
                            User user = (User) obj;
                            connectIM(user.getUser_id(), user.getChild_info() == null);
                        }
                    }

                    @Override
                    public void onFailure(DisplayType displayType, Object errorObj) {
                        connectIM();
                    }
                });
            }
        }
        // 获取七牛token
        FileUploadApi.getInstance().fetchUploadToken();
    }

}
