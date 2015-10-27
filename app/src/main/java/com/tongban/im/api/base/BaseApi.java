package com.tongban.im.api.base;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tongban.corelib.base.BaseApplication;
import com.tongban.corelib.base.api.IApiCallback;
import com.tongban.corelib.model.ApiErrorResult;
import com.tongban.corelib.model.ImageFolder;
import com.tongban.corelib.utils.AppUtils;
import com.tongban.corelib.utils.LogUtil;
import com.tongban.corelib.utils.NetUtils;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.im.App;
import com.tongban.im.R;
import com.tongban.im.api.GroupApi;
import com.tongban.im.api.ProductApi;
import com.tongban.im.api.TopicApi;
import com.tongban.im.api.UserCenterApi;
import com.tongban.im.common.Consts;
import com.tongban.im.utils.CheckID;

import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import io.rong.imkit.RongIM;

/**
 * 输入接口：修改、创建的接口；使用完这些接口后，需要重置disableCache，使输出接口可以收到非缓存结果；
 * 输出接口：列表、详情接口；
 * <p>
 * Created by zhangleilei on 15/7/8.
 */
public class BaseApi {

    protected Context mContext;
    private static BaseApi mApi;
    // 服务器地址存储标示
    private static final String HOST_FLAG = "HOST_FLAG";
    /**
     * 接口请求成功
     */
    public final static int API_SUCCESS = 0;
    /**
     * 无网络
     */
    public final static int API_NO_NETWORK = -1;
    /**
     * 服务器地址错误/无数据
     */
    public final static int API_URL_ERROR = -404;
    /**
     * 客户端与服务器时间不同步
     */
    public final static int TIME_DIS_MATCH = 10069;

    /**
     * 获取Volley请求队列
     */
    private static RequestQueue mRequestQueue;

    protected Map<String, Object> mParams;


    public static BaseApi getInstance() {
        if (mApi == null) {
            synchronized (BaseApi.class) {
                if (mApi == null) {
                    mApi = new BaseApi(App.getInstance());
                }
            }
        }
        return mApi;
    }

    /**
     * 声明Request请求
     */
    private JsonObjectRequest request = null;
    // 正式环境
    public static String DEFAULT_HOST = "http://101.200.83.100/ddim/";
    // 测试环境
    public static String TEST_HOST = "http://10.255.209.66:8080/ddim/";
    // 67测试环境
    public static String TEST_HOST_67 = "http://10.255.209.67:8080/ddim/";

    public static String TEST_HOST_6 = "http://192.168.81.6:8080/ddim/";

    protected BaseApi(Context context) {
        this.mContext = context;
        this.mRequestQueue = BaseApplication.getInstance().getRequestQueue();
    }

    /**
     * 获得服务器地址
     *
     * @return 服务器地址
     */
    public String getHostUrl() {
        return SPUtils.get(mContext, SPUtils.NO_CLEAR_FILE, HOST_FLAG, DEFAULT_HOST).toString();
    }

    /**
     * 切换服务器地址
     *
     * @param mContext
     * @param url
     */
    public void setHostUrl(Context mContext, String url) {
        // 如果与上次地址不一样，将清除用户信息
        if (!url.equals(getHostUrl())) {
            //切换服务器，清除登录信息
            if (RongIM.getInstance() != null
                    && RongIM.getInstance().getRongIMClient() != null)
                RongIM.getInstance().logout();
            SPUtils.clear(mContext);
        }
        SPUtils.put(mContext, SPUtils.NO_CLEAR_FILE, HOST_FLAG, url);
    }

    /**
     * 接口请求地址
     */
    protected String getRequestUrl(String apiName) {
        return getHostUrl() + apiName;
    }

    /**
     * 封装了JsonObjectRequest的网络请求方法
     *
     * @param url      请求接口名称||地址
     * @param params   请求参数
     *                 Ps:只有特定的几个接口需要新数据，大多数情况不要传递这个值
     * @param callback 请求结果的回调
     */
    protected void simpleRequest(final String url, Map params,
                                 final IApiCallback callback) {
        if (callback != null)
            callback.onStartApi();

        if (!NetUtils.isConnected(mContext)) {
            ApiErrorResult errorResult = new ApiErrorResult();
            errorResult.setDisplayType(IApiCallback.DisplayType.ALL);
            errorResult.setErrorMessage(mContext.getResources()
                    .getString(com.tongban.corelib.R.string.api_error));
            errorResult.setErrorCode(API_NO_NETWORK);
            errorResult.setApiName(url);
            if (callback != null)
                callback.onFailure(errorResult);
            return;
        }
        if (url == null || params == Collections.emptyMap()) {
            return;
        }
        String apiUrl = url;
        if (!apiUrl.startsWith("http://") && !apiUrl.startsWith("https://")) {
            apiUrl = getRequestUrl(apiUrl);
        }
        final String requestUrl = apiUrl;
        //是否获取缓存数据标示 true获取实时数据；false获取缓存数据 默认为false，
        final boolean disableCache = ApiCache.getInstance().isCurrentUrl(url);
        final String requestJson = JSON.toJSON(params).toString();

        LogUtil.d("request-url:", requestUrl
                + "  \t disableCache: " + disableCache);
        LogUtil.d("request-url:", "request-params: \n " + requestJson);

        // 创建request
        try {
            JSONObject jsonObject = new JSONObject(requestJson);
            request = new JsonObjectRequest(requestUrl, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {

                            LogUtil.d("onResponse-url:", requestUrl
                                    + "  \t disableCache:" + disableCache);
                            LogUtil.d("onResponse-url:", "onResponse-data: \n "
                                    + jsonObject.toString());

                            //获取完成后，将取消缓存的接口删掉
                            if (disableCache) {
                                ApiCache.getInstance().removeDisableCacheUrls(url);
                            }
                            int statusCode = jsonObject.optInt("statusCode");
                            // 请求成功,数据回调给调用方
                            if (statusCode == API_SUCCESS) {
                                if (callback != null)
                                    callback.onComplete(jsonObject);
                            }
                            // 请求成功，但有错误信息返回
                            else {
                                // 请求成功,与服务器时间有差异
                                if (statusCode == TIME_DIS_MATCH) {
                                    long dif = ((JSONObject) jsonObject.opt("data")).optLong("mark");
                                    CheckID.difMills = dif;
                                    LogUtil.d("onResponse-TIME_DIS_MATCH", String.valueOf(dif));
                                }
                                ApiErrorResult errorResult = new ApiErrorResult();
                                errorResult.setDisplayType(IApiCallback.DisplayType.ALL);
                                errorResult.setErrorMessage(jsonObject.optString("statusDesc"));
                                errorResult.setErrorCode(statusCode);
                                errorResult.setApiName(url);
                                if (callback != null)
                                    callback.onFailure(errorResult);
                            }
                        }
                    }

                    , new Response.ErrorListener()

            {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                    LogUtil.d("onErrorResponse-url:", requestUrl
                            + "  \t disableCache:" + disableCache);
                    LogUtil.d("onErrorResponse-info:", "volleyError-ServerError");

                    // 请求失败,错误信息回调给调用方
                    String errorMessage = getErrorMessage();
                    ApiErrorResult errorResult = new ApiErrorResult();
                    errorResult.setDisplayType(IApiCallback.DisplayType.ALL);
                    errorResult.setErrorMessage(errorMessage);
                    errorResult.setErrorCode(API_URL_ERROR);
                    errorResult.setApiName(url);
                    if (callback != null)
                        callback.onFailure(errorResult);

                }
            }

            )

            {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("_P", "Android");
                    headers.put("_V", AppUtils.getVersionName(mContext));
                    boolean isLogin = !TextUtils.isEmpty(SPUtils.get(mContext,
                            Consts.USER_ID, "").toString());
                    headers.put("_R_C", CheckID.encode(isLogin, disableCache));
                    if (isLogin)
                        headers.put("_U", SPUtils.get(mContext, Consts.USER_ID, "").toString());
                    return headers;
                }
            }

            ;
            // 禁用缓存
            request.setShouldCache(false);
            // 添加请求到Volley队列
            mRequestQueue.add(request);
            // 回调到方法调用方,通知请求已经开始
            if (callback != null)
                callback.onStartApi();
        } catch (Exception e) {
            LogUtil.e("onError-request-json:", "解析json异常");
            ApiErrorResult errorResult = new ApiErrorResult();
            errorResult.setDisplayType(IApiCallback.DisplayType.Toast);
            errorResult.setErrorMessage("解析json异常");
            errorResult.setApiName(url);
            if (callback != null)
                callback.onFailure(errorResult);
        }
    }

    /**
     * 服务器异常的错误提示，随机提示
     *
     * @return
     */

    protected String getErrorMessage() {
        Random random = new Random();
        int count = mContext.getResources().
                getStringArray(R.array.error_message).length;
        return mContext.getResources().getStringArray(R.array.error_message)
                [random.nextInt(count)].toString();
    }

    /**
     * 判断当前时间是否在disableCache内
     *
     * @param cacheName {@link BaseApi}
     * @return
     */
    protected boolean disableCache(String cacheName) {
        long time = (long) SPUtils.get(mContext, cacheName, 0L);
        if (time - System.currentTimeMillis() > 0L) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 将boolean转换成string的值，系统默认转换的是int值
     *
     * @param type
     * @return
     */
    protected String getTypeStr(boolean type) {
        return String.valueOf(type ? 1 : 0);
    }

    protected ApiErrorResult createEmptyResult(String apiName) {
        ApiErrorResult errorResult = new ApiErrorResult();
        errorResult.setDisplayType(IApiCallback.DisplayType.ALL);
        errorResult.setErrorCode(API_URL_ERROR);
        errorResult.setErrorMessage("这里什么也没有");
        errorResult.setApiName(apiName);
        return errorResult;
    }




}
