package com.tongban.im.api;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.tongban.corelib.base.api.ApiCallback;
import com.tongban.im.App;

import java.util.HashMap;

/**
 * 地理位置相关api
 * Created by zhangleilei on 15/7/15.
 */
public class LocationApi extends BaseApi {

    private static LocationApi mApi;

    /**
     * 创建地理位置
     */
    public static final String LOCATION_CREATE = "config/address/create";
    /**
     * 获取附近的位置
     */
    public static final String FETCH_LOCATION_LIST = "config/address/fetch";

    private LocationApi(Context context) {
        super(context);
    }

    public static LocationApi getInstance() {
        if (mApi == null) {
            synchronized (LocationApi.class) {
                if (mApi == null) {
                    mApi = new LocationApi(App.getInstance());
                }
            }
        }
        return mApi;
    }

    /**
     * 创建地理位置
     *
     * @param longitude   经度
     * @param latitude    维度
     * @param province    省
     * @param city        市
     * @param county      区/县
     * @param location    详细位置
     * @param addressType 地址类型；(默认0，1：学校)
     * @param callback    回调
     */
    @Deprecated
    public void createLocation(double longitude, double latitude, @Nullable String province,
                               @Nullable String city, @Nullable String county,
                               String location, int addressType, final ApiCallback callback) {

        mParams = new HashMap<>();
        mParams.put("longitude", longitude);
        mParams.put("latitude", latitude);
        if (!TextUtils.isEmpty(province))
            mParams.put("province", province);
        if (!TextUtils.isEmpty(city))
            mParams.put("city", city);
        if (!TextUtils.isEmpty(city))
            mParams.put("county", county);
        mParams.put("location", location);
        mParams.put("address_type", addressType);

        simpleRequest(LOCATION_CREATE, mParams, new ApiCallback() {
            @Override
            public void onStartApi() {
//                callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {

//                callback.onComplete(obj);
            }

            @Override
            public void onFailure(DisplayType displayType, Object errorMessage) {
//                callback.onFailure(displayType, errorMessage);
            }
        });
    }

    /**
     * 获取附近的位置
     *
     * @param longitude 经度
     * @param latitude  维度
     * @param cursor    第几页，默认0开始
     * @param pageSize  每页数量 默认10条
     * @param callback
     */
    public void fetchLocationList(double longitude, double latitude, int cursor, int pageSize,
                                  final ApiCallback callback) {

        mParams = new HashMap<>();
        mParams.put("longitude", longitude);
        mParams.put("latitude", latitude);
        mParams.put("cursor", cursor < 0 ? 0 : cursor);
        mParams.put("page_size", pageSize < 10 ? 10 : pageSize);

        simpleRequest(FETCH_LOCATION_LIST, mParams, new ApiCallback() {
            @Override
            public void onStartApi() {
                callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                callback.onComplete(obj);
            }

            @Override
            public void onFailure(DisplayType displayType, Object errorMessage) {
                callback.onFailure(displayType, errorMessage);
            }
        });
    }

}
