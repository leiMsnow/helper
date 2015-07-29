package com.tongban.im.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.tongban.corelib.model.ApiResult;
import com.tongban.corelib.utils.LogUtil;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.im.R;
import com.tongban.im.RongCloudEvent;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.api.LocationApi;
import com.tongban.im.api.UserApi;
import com.tongban.im.common.Consts;
import com.tongban.im.model.User;

/**
 * 加载界面
 * * @author zhangleilei
 *
 * @createTime 2015/7/16
 */
public class LoadingActivity extends BaseToolBarActivity {

    private LocationClient mLocationClient = null;
    private BDLocationListener myListener = null;
    private String mToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient = null;
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

            mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
            myListener = new MyLocationListener();
            mLocationClient.registerLocationListener(myListener);    //注册监听函数
            initLocation();
            mLocationClient.start();
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
        }
        finish();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认gcj02，设置返回的定位结果坐标系
        option.setCoorType("bd09ll");
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
//        option.setScanSpan(1);
        //可选，设置是否需要地址信息，默认不需要
        option.setIsNeedAddress(true);
        //可选，默认false,设置是否使用gps
        option.setOpenGps(true);
        //可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setLocationNotify(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
//        option.setIsNeedLocationPoiList(true);
        //可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
        option.setIgnoreKillProcess(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集
        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否需要过滤gps仿真结果，默认需要
//        option.setEnableSimulateGps(false);
        option.setTimeOut(5 * 1000);
        mLocationClient.setLocOption(option);
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation dbLocation) {
            //Receive Location
            StringBuffer sb = new StringBuffer();

            sb.append("time : ");
            sb.append(dbLocation.getTime());

            sb.append("\nlatitude : ");
            sb.append(dbLocation.getLatitude());

            sb.append("\nlongitude : ");
            sb.append(dbLocation.getLongitude());

            sb.append("\ncounty : ");
            sb.append(dbLocation.getCountry());

            sb.append("\nprovince : ");
            sb.append(dbLocation.getProvince());

            sb.append("\ncity : ");
            sb.append(dbLocation.getCity());

            sb.append("\ndistrict : ");
            sb.append(dbLocation.getDistrict());

            sb.append("\naddress : ");
            sb.append(dbLocation.getAddrStr());

            sb.append("\nstreet : ");
            sb.append(dbLocation.getStreet());

            LogUtil.d("-----location-success:-----", sb.toString());
            double longitude = dbLocation.getLongitude();
            double latitude = dbLocation.getLatitude();
            String province = dbLocation.getProvince();
            String city = dbLocation.getCity();
            String county = dbLocation.getDistrict();
            String location = dbLocation.getAddrStr();
            int addressType = 0;
            LocationApi.getInstance().createLocation(longitude, latitude, province, city, county,
                    location, addressType, LoadingActivity.this);

        }

    }

}
