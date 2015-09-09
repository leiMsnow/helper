package com.tongban.im.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.tongban.corelib.model.ApiErrorResult;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.activity.user.ChildInfoActivity;
import com.tongban.im.api.AccountApi;
import com.tongban.im.api.FileUploadApi;
import com.tongban.im.common.Consts;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.utils.LocationUtils;

import java.util.Random;

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
        //第一次启动APP，进入设置宝宝界面
        if ((boolean) SPUtils.get(mContext,SPUtils.VISIT_FILE, Consts.FIRST_SET_CHILD_INFO, true)) {
            //为用户随机生成一个头像
            randomPortrait();
            startActivity(new Intent(mContext, ChildInfoActivity.class));
            finish();
        } else {
            String freeAuthToken = SPUtils.get(mContext, Consts.FREEAUTH_TOKEN, "").toString();
            if (freeAuthToken.equals("")) {
                connectIM();
            } else {
                AccountApi.getInstance().tokenLogin(freeAuthToken, this);
            }
        }
        // 获取七牛token
        FileUploadApi.getInstance().fetchUploadToken();
    }

    //随机生成一个头像标记
    private void randomPortrait() {
        Random random = new Random();
        int portraits[] = new int[]{R.mipmap.ic_default_portrait1
                , R.mipmap.ic_default_portrait2
                , R.mipmap.ic_default_portrait3
                , R.mipmap.ic_default_portrait4
                , R.mipmap.ic_default_portrait5
                , R.mipmap.ic_default_portrait6
                , R.mipmap.ic_default_portrait7
                , R.mipmap.ic_default_portrait8
                , R.mipmap.ic_default_portrait9
                , R.mipmap.ic_default_portrait10};
        int portrait = portraits[random.nextInt(portraits.length)];
        SPUtils.put(mContext,SPUtils.VISIT_FILE,Consts.KEY_DEFAULT_PORTRAIT, portrait);
    }

    public void onEventMainThread(BaseEvent.UserLoginEvent obj) {
        connectIM(obj.user.getChild_info() == null);
    }

    public void onEventMainThread(ApiErrorResult obj) {
        connectIM();
    }
}
