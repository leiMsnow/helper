package com.tongban.im.utils;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.im.common.Consts;

import de.greenrobot.event.EventBus;

/**
 * 定位工具类-使用的百度地图
 * Created by zhangleilei on 15/7/30.
 */
public class LocationUtils {

    private static LocationUtils mLocationUtils = null;
    private LocationClient mLocationClient = null;
    private BDLocationListener myListener = null;
    private Context mContext;
    LocationUtils(Context context) {
        this.mContext = context;
        mLocationClient = new LocationClient(context.getApplicationContext());
        myListener = new MyLocationListener();
        //注册监听函数
        mLocationClient.registerLocationListener(myListener);
        initLocation();
    }

    public static LocationUtils get(Context context) {

        if (mLocationUtils == null) {

            synchronized (LocationUtils.class) {

                if (mLocationUtils == null) {
                    mLocationUtils = new LocationUtils(context);
                }
            }
        }

        return mLocationUtils;
    }


    /**
     * 开始定位
     */
    public void start() {
        mLocationClient.start();
    }

    /**
     * 停止定位
     */
    public void stop() {
        mLocationClient.stop();
    }

    /**
     * 定位回调接口
     */
    class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation dbLocation) {

            double longitude = dbLocation.getLongitude();
            double latitude = dbLocation.getLatitude();
            String province = dbLocation.getProvince();
            String city = dbLocation.getCity();
            String county = dbLocation.getDistrict();
            String address = dbLocation.getAddrStr();

            SPUtils.put(mContext, Consts.LATITUDE,latitude);
            SPUtils.put(mContext, Consts.LONGITUDE,longitude);
            SPUtils.put(mContext, Consts.PROVINCE,province);
            SPUtils.put(mContext, Consts.CITY,city);
            SPUtils.put(mContext, Consts.COUNTY,county);
            SPUtils.put(mContext, Consts.ADDRESS,address);

            EventBus.getDefault().post(dbLocation);
        }

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
        //可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
        option.setIgnoreKillProcess(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集
        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否需要过滤gps仿真结果，默认需要
//        option.setEnableSimulateGps(false);
        option.setTimeOut(5 * 1000);
        mLocationClient.setLocOption(option);
    }
}
