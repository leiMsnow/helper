package com.tongban.im.api;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tongban.corelib.base.BaseApplication;
import com.tongban.corelib.base.activity.BaseApiActivity;
import com.tongban.corelib.base.api.ApiCallback;
import com.tongban.corelib.model.ApiResult;
import com.tongban.corelib.utils.AppUtils;
import com.tongban.corelib.utils.LogUtil;
import com.tongban.corelib.utils.NetUtils;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.corelib.utils.ToastUtil;
import com.tongban.im.R;
import com.tongban.im.common.Consts;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.GroupType;
import com.tongban.im.utils.CheckID;

import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.greenrobot.event.EventBus;

/**
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
     * 接口请求失败
     */
    public final static int API_FAILD = -1;
    /**
     * 获取Volley请求队列
     */
    protected static RequestQueue mRequestQueue;

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    protected Map<String, Object> mParams;

    /**
     * 声明Request请求
     */
    private JsonObjectRequest request = null;
    // 默认服务器地址，实际地址根据getHostUrl来获取；
    private static String DEFAULT_HOST = "http://10.255.209.67:8080/ddim/";
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
     * @param callback 请求结果的回调
     * @return request--当前请求对象
     */
    protected void simpleRequest(String url, Map params, final ApiCallback callback) {
        if (!NetUtils.isConnected(mContext)) {
            callback.onFailure(ApiCallback.DisplayType.Toast, "网络连接失败,请稍后重试");
            return;
        }
        if (url == null || params == Collections.emptyMap()) {
            return;
        }
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = getRequestUrl(url);
        }
        final String requestUrl = url;
        LogUtil.d("request-url:", requestUrl);
        LogUtil.d("request-url:", "request-params: \n " + new JSONObject(params).toString());
        // 创建request
        request = new JsonObjectRequest(requestUrl, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        LogUtil.d("onResponse-url:", requestUrl);
                        LogUtil.d("onResponse-url:", "onResponse-data: \n " + jsonObject.toString());
                        int apiResult = jsonObject.optInt("statusCode");
                        if (apiResult == API_SUCCESS) {
                            // 请求成功,数据回调给调用方
                            callback.onComplete(jsonObject);
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
                LogUtil.d("onErrorResponse-url:", requestUrl);
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
                headers.put("_R_C", CheckID.encode(
                        TextUtils.isEmpty(SPUtils.get(mContext,Consts.USER_ID,"").toString())));
                headers.put("_U", SPUtils.get(mContext,Consts.USER_ID,"").toString());
                return headers;
            }
        };
        // 禁用缓存
        request.setShouldCache(false);
        // 添加请求到Volley队列
        mRequestQueue.add(request);
        // 回调到方法调用方,通知请求已经开始
        callback.onStartApi();
    }

    protected String getErrorMessage() {
        Random random = new Random();
        int count = mContext.getResources().
                getStringArray(R.array.error_message).length;
        return mContext.getResources().getStringArray(R.array.error_message)
                [random.nextInt(count)].toString();
    }

    protected String getTypeStr(boolean type) {
        return String.valueOf(type ? 1 : 0);
    }
}
