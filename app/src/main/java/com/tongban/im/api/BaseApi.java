package com.tongban.im.api;

import android.content.Context;

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
import com.tongban.corelib.utils.LogUtil;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.im.common.Consts;
import com.tongban.im.utils.CheckID;

import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangleilei on 15/7/8.
 */
public class BaseApi {

    protected Context mContext;
    // 服务器地址存储标示 0线上；1st；2test；3+其他开发人员地址
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
    private static String DEFAULT_HOST = "http://10.255.209.66:8080/ddim/";
    //正式环境
    private static String MAIN_HOST = "";
    //测试环境
    private static String TEST_HOST = "";

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
        String host = SPUtils.get(mContext, HOST_FLAG, DEFAULT_HOST).toString();
        if (host != null) {
            return host;
        }
        return DEFAULT_HOST;
    }

    /**
     * 设置服务器地址
     *
     * @param flag 0线上；1st；2test；3+其他开发人员地址
     */
    public void setHostUrl(int flag) {
        String saveUrl = "";
        switch (flag) {
            case 0:
            default:
                saveUrl = MAIN_HOST;
                break;
            case 1:
                break;
            case 2:
                saveUrl = TEST_HOST;
                break;
            case 3:
                saveUrl = "http://10.255.209.67:8080/ddim/";
                break;
            case 4:
                saveUrl = "http://10.255.209.67:8080/ddim/";
                break;
        }
        SPUtils.put(mContext, saveUrl, flag);
    }

    /**
     * 接口请求地址
     */
    protected String getRequestUrl(String apiName) {
        return getHostUrl() + apiName;
    }

    /**
     * 封装了JsonObjectRequest的网络请求方法
     * cey:目前失败的请求都存到BaseApiActivity中,暂未考虑BaseApiFragment~~~
     *
     * @param url      请求地址
     * @param params   请求参数
     * @param callback 请求结果的回调
     * @return request--当前请求对象
     */
    protected Request simpleRequest(String url, Map params, final ApiCallback callback) {
        if (url == null || params == Collections.emptyMap()) {
            return null;
        }
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = getRequestUrl(url);
        }
        LogUtil.d("request-url:", url);
        LogUtil.d("request-params:", new JSONObject(params).toString());
        // 创建request
        request = new JsonObjectRequest(url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        LogUtil.d("response-success:" + jsonObject.toString());
                        // 如果当前请求位于失败请求的队列中,则移除
                        if (BaseApiActivity.getFailedRequest().contains(request)) {
                            BaseApiActivity.getFailedRequest().remove(request);
                        }
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
                LogUtil.e("response-error:" + volleyError.toString());

                // 将当前的请求添加到失败队列中
                if (!BaseApiActivity.getFailedRequest().contains(request)) {
                    BaseApiActivity.getFailedRequest().add(request);
                }
                // 请求失败,错误信息回调给调用方
                String errorMessage = "";
                if (volleyError.getMessage() != null) {
                    errorMessage = volleyError.getMessage().toString();
                }
                callback.onFailure(ApiCallback.DisplayType.Toast, errorMessage);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                //headers.put("_R_C", CheckID.encode(SPUtils.contains(mContext, Consts.USER_ID)));
                return headers;
            }
        };
        // 禁用缓存
        request.setShouldCache(false);
        // 添加请求到Volley队列
        mRequestQueue.add(request);
        // 回调到方法调用方,通知请求已经开始
        callback.onStartApi();

        return request;
    }
}
