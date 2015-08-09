package com.tongban.im.activity;

import android.content.Intent;
import android.os.Bundle;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.tongban.corelib.model.ApiResult;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.im.R;
import com.tongban.im.RongCloudEvent;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.api.LocationApi;
import com.tongban.im.api.UserApi;
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
            UserApi.getInstance().tokenLogin(mToken, LoadingActivity.this);
            LocationUtils.get(mContext).start();
        }
    }

    public void onEventMainThread(User user) {
        RongCloudEvent.getInstance().connectIM(user.getIm_bind_token());
        startActivity(new Intent(mContext, MainActivity.class));
        finish();
    }

    public void onEventMainThread(Object obj) {
        if (obj instanceof ApiResult) {
            SPUtils.put(mContext, Consts.FREEAUTH_TOKEN, "");
            startActivity(new Intent(mContext, LoginActivity.class));
            finish();
        } else if (obj instanceof BDLocation) {
            BDLocation dbLocation = (BDLocation) obj;
            double longitude = dbLocation.getLongitude();
            double latitude = dbLocation.getLatitude();
            String province = dbLocation.getProvince();
            String city = dbLocation.getCity();
            String county = dbLocation.getDistrict();
            String location = dbLocation.getAddrStr();
            int addressType = 0;
            LocationApi.getInstance().createLocation(longitude, latitude, province, city, county,
                    location, addressType, LoadingActivity.this);
        }else if(obj instanceof String){
            startActivity(new Intent(mContext, MainActivity.class));
            finish();
        }
    }
}
