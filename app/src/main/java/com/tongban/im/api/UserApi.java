package com.tongban.im.api;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tongban.corelib.base.api.ApiCallback;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.im.App;
import com.tongban.im.model.ApiResult;
import com.tongban.im.model.UserInfo;

import java.util.HashMap;


/**
 * 用户操作API接口类
 * Created by zhangleilei on 15/7/3.
 */
public class UserApi extends BaseApi {

    private static UserApi mApi;

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


    private UserApi(Context context) {
        super(context);
    }

    public static UserApi getInstance() {
        if (mApi == null) {
            synchronized (UserApi.class) {
                if (mApi == null) {
                    mApi = new UserApi(App.getInstance());
                }
            }
        }
        return mApi;
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


}
