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
import com.tongban.corelib.base.api.ApiCallback;
import com.tongban.corelib.model.ApiResult;
import com.tongban.corelib.utils.AppUtils;
import com.tongban.corelib.utils.LogUtil;
import com.tongban.corelib.utils.NetUtils;
import com.tongban.corelib.utils.SPUtils;
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

/**
 * 输入接口：修改、创建的接口；使用完这些接口后，需要重置disableCache，使输出接口可以收到非缓存结果；
 * 输出接口：列表、详情接口；
 * <p/>
 * Created by zhangleilei on 15/7/8.
 */
public class BaseApi {

    protected Context mContext;
    // 服务器地址存储标示
    private static final String HOST_FLAG = "HOST_FLAG";
    /**
     * 接口请求成功
     */
    public final static int API_SUCCESS = 0;
    /**
     * 客户端与服务器时间不同步
     */
    public final static int TIME_DIS_MATCH = 10069;
    //-----------------------------接口缓存时间key----------------------------------------------------
    //------------------------存储的时间大于0，直接调用DB数据-------------------------------------------
    protected final static String ALL_CACHE_URL = "ALL_CACHE_URL";
    //    //专题时间-10min
    protected final static String THEME_CACHE_TIME = "THEME_CACHE_TIME";
    //    protected final static String THEME_CACHE_URL = "THEME_CACHE_URL";
//    //单品时间-10min
    protected final static String PRODUCT_CACHE_TIME = "PRODUCT_CACHE_TIME";
    //    protected final static String PRODUCT_CACHE_URL = "PRODUCT_CACHE_URL";
//    //话题时间-10min
    protected final static String TOPIC_CACHE_TIME = "TOPIC_CACHE_TIME";
    //    protected final static String TOPIC_CACHE_URL = "TOPIC_CACHE_URL";
//    //圈子时间-30min
    protected final static String GROUP_CACHE_TIME = "GROUP_CACHE_TIME";
    //    protected final static String GROUP_CACHE_URL = "GROUP_CACHE_URL";
//    //用户时间-5min
    protected final static String USER_CACHE_TIME = "USER_CACHE_TIME";
//    protected final static String USER_CACHE_URL = "USER_CACHE_URL";
    /**
     * 获取Volley请求队列
     */
    protected static RequestQueue mRequestQueue;

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    protected Map<String, Object> mParams;

    private Set<String> mDisableCacheUrls;


    /**
     * 声明Request请求
     */
    private JsonObjectRequest request = null;
    // 默认服务器地址，实际地址根据getHostUrl来获取；
    private static String DEFAULT_HOST = "http://10.255.209.66:8080/ddim/";
    //测试环境
    private static String TEST_HOST = "http://192.168.81.9:8080/ddim/";

    public BaseApi(Context context) {
        this.mRequestQueue = BaseApplication.getInstance().getRequestQueue();
        this.mContext = context;
    }

    /**
     * 获得服务器地址
     *
     * @return 服务器地址
     */
    protected String getHostUrl() {
        return SPUtils.get(mContext, HOST_FLAG, DEFAULT_HOST).toString();
    }

    /**
     * 设置服务器地址
     *
     * @param flag 0线上；1test；2+其他开发人员地址
     */
    public static void setHostUrl(Context mContext, int flag) {
        String saveUrl;
        switch (flag) {
            case 0:
            default:
                saveUrl = DEFAULT_HOST;
                break;
            case 1:
                saveUrl = TEST_HOST;
                break;
        }
        SPUtils.put(mContext, HOST_FLAG, saveUrl);
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
                                 final ApiCallback callback) {
        if (!NetUtils.isConnected(mContext)) {
            callback.onFailure(ApiCallback.DisplayType.Toast, "网络连接失败,请稍后重试");
            return;
        }
        if (url == null || params == Collections.emptyMap()) {
            return;
        }
        String apiUrl = url;
        if (!apiUrl.startsWith("http://") && !apiUrl.startsWith("https://")) {
            apiUrl = getRequestUrl(apiUrl);
        }
        //是否获取缓存数据标示 默认为false，
        final String requestUrl = apiUrl;
        final boolean disableCache = isCurrentUrl(url);
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
                                removeDisableCacheUrls(url);
                            }
                            int apiResult = jsonObject.optInt("statusCode");
                            if (apiResult == API_SUCCESS) {
                                // 请求成功,数据回调给调用方
                                callback.onComplete(jsonObject);
                            } else if (apiResult == TIME_DIS_MATCH) {
                                // 保存客户端与服务器的时间差
                                long dif = ((JSONObject) jsonObject.opt("data")).optLong("mark");
                                LogUtil.d("TIME_DIS_MATCH", dif + "");
                                CheckID.difMills = dif;
                            } else {
                                ApiResult apiResponse = new ApiResult();
                                apiResponse.setStatusDesc(jsonObject.optString("statusDesc"));
                                apiResponse.setData(jsonObject.opt("data"));
                                apiResponse.setStatusCode(apiResult);
                                callback.onFailure(ApiCallback.DisplayType.Toast, apiResponse);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    LogUtil.d("onErrorResponse-url:", requestUrl
                            + "  \t disableCache:" + disableCache);
                    LogUtil.d("onErrorResponse-url:", "onErrorResponse-info: volleyError-ServerError");
                    // 请求失败,错误信息回调给调用方
                    String errorMessage = getErrorMessage();
                    callback.onFailure(ApiCallback.DisplayType.Toast, errorMessage);
                }
            }) {
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
            };
            // 禁用缓存
            request.setShouldCache(false);
            // 添加请求到Volley队列
            mRequestQueue.add(request);
            // 回调到方法调用方,通知请求已经开始
            callback.onStartApi();
        } catch (Exception e) {
            LogUtil.e("onError-request-json:", "解析json异常");
            callback.onFailure(ApiCallback.DisplayType.Toast, "解析json异常");
        }
    }


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
     * 设置disableCache时间
     *
     * @param cacheName {@link BaseApi}
     */
    protected void setDisableCache(String cacheName) {
        int disableCacheTime = 0;
        if (cacheName.equals(USER_CACHE_TIME)) {
            disableCacheTime = 5;
            setDisableCacheUrls(UserCenterApi.USER_INFO);
            setDisableCacheUrls(UserCenterApi.FETCH_FOCUS_USER_LIST);
            setDisableCacheUrls(UserCenterApi.FETCH_PERSONAL_CENTER_INFO);

        } else if (cacheName.equals(TOPIC_CACHE_TIME)) {
            disableCacheTime = 10;
            //话题相关
            setDisableCacheUrls(TopicApi.RECOMMEND_TOPIC_LIST);
            setDisableCacheUrls(TopicApi.SEARCH_TOPIC_LIST);
            setDisableCacheUrls(TopicApi.TOPIC_INFO);
            setDisableCacheUrls(TopicApi.OFFICIAL_TOPIC_INFO);
            setDisableCacheUrls(TopicApi.TOPIC_COMMENT_LIST);
            //用户相关
            setDisableCacheUrls(UserCenterApi.FETCH_COLLECT_REPLY_TOPIC_LIST);
            setDisableCacheUrls(UserCenterApi.FETCH_COLLECT_TOPIC_LIST);
            setDisableCacheUrls(UserCenterApi.FETCH_LAUNCH_TOPIC_LIST);

        } else if (cacheName.equals(GROUP_CACHE_TIME)) {
            disableCacheTime = 30;
            setDisableCacheUrls(GroupApi.RECOMMEND_GROUP_LIST);
            setDisableCacheUrls(GroupApi.SEARCH_GROUP_LIST);
            setDisableCacheUrls(GroupApi.GROUP_INFO);
            setDisableCacheUrls(GroupApi.GROUP_MEMBERS_INFO);

            setDisableCacheUrls(UserCenterApi.FETCH_MY_GROUPS_LIST);

        } else if (cacheName.equals(THEME_CACHE_TIME)) {
            disableCacheTime = 10;
            setDisableCacheUrls(ProductApi.FETCH_THEME_INFO);
            setDisableCacheUrls(ProductApi.SEARCH_THEME);
            setDisableCacheUrls(ProductApi.FETCH_THEME_COLLECTED_AMOUNT);

            setDisableCacheUrls(UserCenterApi.FETCH_COLLECT_MULTIPLE_PRODUCT_LIST);

        } else if (cacheName.equals(PRODUCT_CACHE_TIME)) {
            disableCacheTime = 10;
            setDisableCacheUrls(ProductApi.FETCH_THEME_PRODUCTS);
            setDisableCacheUrls(ProductApi.FETCH_PRODUCT_DETAIL_INFO);
            setDisableCacheUrls(ProductApi.SEARCH_PRODUCT);

            setDisableCacheUrls(UserCenterApi.FETCH_SINGLE_PRODUCT_LIST);
        }

        long cacheTimeMillis = System.currentTimeMillis() + 1000 * 60 * disableCacheTime;
        SPUtils.put(mContext, cacheName, cacheTimeMillis);
    }


    /**
     * 获得所有接口
     *
     * @return
     */
    public Set<String> getDisableCacheUrls() {
        mDisableCacheUrls = new HashSet<>();
        mDisableCacheUrls = (Set<String>) SPUtils.get(mContext, ALL_CACHE_URL, mDisableCacheUrls);
        return mDisableCacheUrls;
    }

    /**
     * 缓存地址是否存在，如果存在将不再走缓存接口
     *
     * @param url
     * @return
     */
    public boolean isCurrentUrl(String url) {
        getDisableCacheUrls();
//        disableCache();
        return mDisableCacheUrls.contains(url);
    }


    /**
     * 记录所有url
     * 读取缓存的时候，将url存储下来
     *
     * @param url
     */
    public void setDisableCacheUrls(String url) {
        getDisableCacheUrls();
        this.mDisableCacheUrls.add(url);
        SPUtils.put(mContext, ALL_CACHE_URL, mDisableCacheUrls);
    }

    /**
     * 删除某个缓存url
     *
     * @param url
     */
    public void removeDisableCacheUrls(String url) {
        getDisableCacheUrls();
        this.mDisableCacheUrls.remove(url);
        SPUtils.put(mContext, ALL_CACHE_URL, mDisableCacheUrls);
    }

    /**
     * 删除所有的缓存url
     */
    public void clearDisableCacheUrls(String cacheName) {
        SPUtils.put(mContext, cacheName, null);
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
}