package com.tongban.im.api;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.RequestQueue;
import com.tongban.corelib.base.api.ApiCallback;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.im.App;
import com.tongban.im.model.ApiResult;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.Group;
import com.tongban.im.model.UserInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * IM系统API接口类
 * Created by zhangleilei on 15/7/3.
 */
public class IMApi extends BaseApi {

    private static IMApi mApi;

    private Map<String, String> mParams;
    /**
     * 注册
     */
    public final static String REGISTER = "register/1";
    /**
     * 第三方注册
     */
    public final static String THIRD_REGISTER = "register/2";
    /**
     * 获取免登录token
     */
    public final static String GET_TOKEN = "user/login";
    /** 获取个人群组列表 */
    public static final String FETCH_PERSONAL_GROUP_LIST = "http://10.255.209.67:8080/ddim/im/group/fetch/1";
    /** 加入群组 */
    public static final String JOIN_GROUP = "http://10.255.209.67:8080/ddim/im/group/join";

    private IMApi(Context context) {
        super(context);
    }

    public static IMApi getInstance() {
        if (mApi == null) {
            synchronized (IMApi.class) {
                if (mApi == null) {
                    mApi = new IMApi(App.getInstance());
                }
            }
        }
        return mApi;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    /**
     * token登录
     *
     * @param token    令牌
     * @param callback
     */
    public void getToken(String token, final ApiCallback callback) {

        mParams = new HashMap<String, String>();
        mParams.put("freeauth_token", token);

        simpleRequest(GET_TOKEN, mParams, new ApiCallback() {
            @Override
            public void onStartApi() {
                callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                ApiResult<UserInfo> apiResponse = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiResult<UserInfo>>() {
                        });
                UserInfo userInfo = apiResponse.getData();
                SPUtils.put(mContext, "USER_TOKEN", userInfo.getIm_bind_token() + "");
                callback.onComplete(userInfo);
            }

            @Override
            public void onFailure(DisplayType displayType, String errorMessage) {
                callback.onFailure(displayType, errorMessage);
            }

        });
    }

    /**
     * 获取圈子列表数据
     *
     * @param user_id   用户id
     * @param callback 回调
     */
    public void fetchPersonalGroupList(String user_id, final ApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("user_id", user_id);

        simpleRequest(FETCH_PERSONAL_GROUP_LIST, mParams, new ApiCallback() {
            @Override
            public void onStartApi() {
                callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                ApiResult<List<Group>> apiResponse = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiResult<List<Group>>>() {
                        });
                List<Group> groups = apiResponse.getData();
                callback.onComplete(groups);
            }

            @Override
            public void onFailure(DisplayType displayType, String errorMessage) {
                callback.onFailure(displayType, errorMessage);
            }
        });
    }

    /**
     * 加入群组
     * @param group_id 群组id
     * @param user_id 用户id
     * @param callback 结果回调
     */
    public void joinGroup(String group_id, String user_id, final ApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("group_id", group_id);
        mParams.put("user_id", user_id);

        simpleRequest(JOIN_GROUP, mParams, new ApiCallback() {
            @Override
            public void onStartApi() {
                callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                callback.onComplete(new BaseEvent.JoinGroupEvent());
            }

            @Override
            public void onFailure(DisplayType displayType, String errorMessage) {
                callback.onFailure(displayType, errorMessage);
            }
        });
    }
}
