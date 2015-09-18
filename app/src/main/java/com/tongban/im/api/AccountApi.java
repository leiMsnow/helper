package com.tongban.im.api;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tongban.corelib.base.api.IApiCallback;
import com.tongban.corelib.model.ApiErrorResult;
import com.tongban.corelib.model.ApiResult;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.im.App;
import com.tongban.im.R;
import com.tongban.im.api.base.BaseApi;
import com.tongban.im.common.Consts;
import com.tongban.im.common.ModelToTable;
import com.tongban.im.db.helper.UserDaoHelper;
import com.tongban.im.model.ApiErrorCode;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.User;

import java.util.HashMap;

import de.greenrobot.event.EventBus;


/**
 * 账号相关API接口类
 * Created by zhangleilei on 15/7/3.
 */
public class AccountApi extends BaseApi {

    private static AccountApi mApi;

    /**
     * 登录
     */
    public final static String LOGIN = "/user/login/1";
    /**
     * token登录
     */
    public final static String TOKEN_LOGIN = "/user/login/2";
    /**
     * 密码重置
     */
    public final static String PWD_RESET = "/user/password/reset";
    /**
     * 获取用户信息
     */
    public final static String GET_USER_INFO = "/user/info";
    /**
     * 注册第一步，获取手机验证码
     */
    public final static String SMS_REQUIRE = "/verifycode/sms/require";
    /**
     * 注册第二步，输入手机号验证码
     */
    public final static String REGISTER = "/user/register/1";
    /**
     * 第三方注册
     */
    public final static String OTHER_REGISTER = "/user/register/2";
    /**
     * 检查手机号是否已经注册
     */
    public final static String CHECK_PHONE = "/user/regcheck/phone";
    /**
     * 第三方登录
     */
    public final static String OTHER_LOGIN = "/user/login/3";


    private AccountApi(Context context) {
        super(context);
    }

    public static AccountApi getInstance() {
        if (mApi == null) {
            synchronized (AccountApi.class) {
                if (mApi == null) {
                    mApi = new AccountApi(App.getInstance());
                }
            }
        }
        return mApi;
    }

    /**
     * 获取手机验证码,注册第一步
     *
     * @param mobilePhone 手机号
     * @param callback    回调结果
     */
    public void getSMSCode(String mobilePhone, final IApiCallback callback) {

        mParams = new HashMap<>();
        mParams.put("mobile_phone", mobilePhone);
        mParams.put("verify_type", 1);

        simpleRequest(SMS_REQUIRE, mParams, new IApiCallback() {
            @Override
            public void onStartApi() {
                callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                ApiResult<BaseEvent.RegisterEvent> apiResponse = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiResult<BaseEvent.RegisterEvent>>() {
                        });
                BaseEvent.RegisterEvent registerEvent = apiResponse.getData();
                registerEvent.registerEnum = (BaseEvent.RegisterEvent.RegisterEnum.SMS_CODE);
                callback.onComplete(registerEvent);
            }

            @Override
            public void onFailure(ApiErrorResult result) {
                callback.onFailure(result);
            }

        });
    }

    /**
     * 注册
     *
     * @param mobilePhone 手机号
     * @param password    密码
     * @param verifyId    验证码Id
     * @param verifyCode  验证码
     * @param callback
     */
    public void register(String mobilePhone, String password, String verifyId, String verifyCode,
                         final IApiCallback callback) {

        mParams = new HashMap<>();
        mParams.put("mobile_phone", mobilePhone);
        mParams.put("password", password);
        mParams.put("verify_id", verifyId);
        mParams.put("verify_code", verifyCode);

        simpleRequest(REGISTER, mParams, new IApiCallback() {
            @Override
            public void onStartApi() {
                callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                ApiResult<BaseEvent.RegisterEvent> apiResponse = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiResult<BaseEvent.RegisterEvent>>() {
                        });
                BaseEvent.RegisterEvent registerEvent = apiResponse.getData();
                registerEvent.registerEnum = (BaseEvent.RegisterEvent.RegisterEnum.REGISTER);
                callback.onComplete(registerEvent);
            }

            @Override
            public void onFailure(ApiErrorResult result) {
                callback.onFailure(result);
            }

        });
    }

    /**
     * 第三方注册
     *
     * @param callback
     */
    public void otherRegister(String mobilePhone,
                              String password,
                              String thirdToken,
                              String thirdType,
                              String verifyId,
                              String verifyCode,
                              final IApiCallback callback) {

        mParams = new HashMap<>();
        mParams.put("mobile_phone", mobilePhone);
        mParams.put("password", password);
        mParams.put("thirdparty_token", thirdToken);
        mParams.put("thirdparty_type", thirdType);
        mParams.put("verify_id", verifyId);
        mParams.put("verify_code", verifyCode);

        simpleRequest(OTHER_REGISTER, mParams, new IApiCallback() {
            @Override
            public void onStartApi() {
                if (callback != null)
                    callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                ApiResult<BaseEvent.RegisterEvent> apiResponse = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiResult<BaseEvent.RegisterEvent>>() {
                        });
                BaseEvent.RegisterEvent registerEvent = apiResponse.getData();
                registerEvent.registerEnum = (BaseEvent.RegisterEvent.RegisterEnum.REGISTER);
                if (callback != null)
                    callback.onComplete(registerEvent);
            }

            @Override
            public void onFailure(ApiErrorResult result) {
                callback.onFailure(result);
            }

        });
    }

    /**
     * 检测手机是否存在
     *
     * @param mobilePhone
     * @param callback
     */
    public void checkPhone(String mobilePhone, final IApiCallback callback) {

        mParams = new HashMap<>();
        mParams.put("mobile_phone", mobilePhone);

        simpleRequest(CHECK_PHONE, mParams, new IApiCallback() {
            @Override
            public void onStartApi() {

            }

            @Override
            public void onComplete(Object obj) {
                if (callback != null)
                    callback.onComplete(new BaseEvent.CheckPhoneEvent(true));
            }

            @Override
            public void onFailure(ApiErrorResult result) {
                if (callback != null)
                    callback.onComplete(new BaseEvent.CheckPhoneEvent(false));
            }

        });
    }

    /**
     * 第三方登录
     *
     * @param thirdToken
     * @param thirdType
     * @param callback
     */
    public void otherLogin(String thirdToken, String thirdType, final IApiCallback callback) {

        mParams = new HashMap<>();
        mParams.put("thirdparty_token", thirdToken);
        mParams.put("thirdparty_type", thirdType);

        simpleRequest(OTHER_LOGIN, mParams, new IApiCallback() {
            @Override
            public void onStartApi() {

            }

            @Override
            public void onComplete(Object obj) {
                if (callback != null)
                    loginSuccess(obj, callback);
            }

            @Override
            public void onFailure(ApiErrorResult result) {
                if (callback != null)
                    callback.onFailure(result);
            }

        });
    }


    /**
     * 登录
     *
     * @param phone    手机号
     * @param password 密码
     * @param callback 回调结果
     */
    public void login(String phone, String password, final IApiCallback callback) {

        mParams = new HashMap<>();
        mParams.put("mobile_phone", phone);
        mParams.put("password", password);

        simpleRequest(LOGIN, mParams, new IApiCallback() {
            @Override
            public void onStartApi() {
                if (callback != null)
                    callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                if (callback != null)
                    loginSuccess(obj, callback);
            }

            @Override
            public void onFailure(ApiErrorResult result) {
                if (callback != null)
                    callback.onFailure(result);
            }

        });
    }


    /**
     * token登录
     *
     * @param token    令牌
     * @param callback
     */
    public void tokenLogin(String token, final IApiCallback callback) {

        mParams = new HashMap<>();
        mParams.put("freeauth_token", token);

        simpleRequest(TOKEN_LOGIN, mParams, new IApiCallback() {
            @Override
            public void onStartApi() {
            }

            @Override
            public void onComplete(Object obj) {
                if (callback != null)
                    loginSuccess(obj, callback);
            }

            @Override
            public void onFailure(ApiErrorResult result) {
                if (callback != null)
                    callback.onFailure(result);
            }

        });
    }


    /**
     * 密码重置
     *
     * @param verifyCode
     * @param verifyId
     * @param mobilePhone
     * @param password    新密码
     * @param callback
     */
    public void pwdReset(String verifyCode, String verifyId, String mobilePhone, String password,
                         final IApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("verify_code", verifyCode);
        mParams.put("verify_id", verifyId);
        mParams.put("password", password);
        mParams.put("mobile_phone", mobilePhone);

        simpleRequest(PWD_RESET, mParams, new IApiCallback() {
            @Override
            public void onStartApi() {
                callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                BaseEvent.PwdResetEvent pwdResetEvent = new BaseEvent.PwdResetEvent();
                pwdResetEvent.result = (mContext.getResources().getString(R.string.pwd_reset_success));
                callback.onComplete(pwdResetEvent);
            }

            @Override
            public void onFailure(ApiErrorResult result) {
                callback.onFailure(result);
            }

        });

    }

    /**
     * 获取用户信息
     *
     * @param userId   用户ID
     * @param callback 回调
     */
    public void getUserInfoByUserId(final String userId, final IApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("user_id", userId);

        simpleRequest(GET_USER_INFO, mParams, new IApiCallback() {
            @Override
            public void onStartApi() {

            }

            @Override
            public void onComplete(Object obj) {
                ApiResult<User> apiResponse = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiResult<User>>() {
                        });
                User user = apiResponse.getData();
                // 将用户信息保存到本地数据库
                UserDaoHelper.get(mContext).addData(ModelToTable.userToTable(user));
                if (callback != null)
                    callback.onComplete(user);
            }

            @Override
            public void onFailure(ApiErrorResult result) {
                if (callback != null)
                    callback.onFailure(result);
            }

        });

    }

    // 三种登录成功后，统一处理
    public BaseEvent.UserLoginEvent loginSuccess(Object obj, IApiCallback callback) {
        ApiResult<User> apiResponse = JSON.parseObject(obj.toString(),
                new TypeReference<ApiResult<User>>() {
                });
        BaseEvent.UserLoginEvent userEvent = new BaseEvent.UserLoginEvent();
        userEvent.user = apiResponse.getData();
        saveUserInfo(userEvent.user);
        if (callback != null)
            callback.onComplete(userEvent);

        return userEvent;
    }

    /**
     * 登录成功后，保存用户信息
     *
     * @param userInfo
     */
    private void saveUserInfo(User userInfo) {
        SPUtils.put(mContext, Consts.IM_BIND_TOKEN, userInfo.getIm_bind_token());
        SPUtils.put(mContext, Consts.FREEAUTH_TOKEN, userInfo.getFreeauth_token());
        SPUtils.put(mContext, Consts.USER_ID, userInfo.getUser_id());
        SPUtils.put(mContext, Consts.NICK_NAME, userInfo.getNick_name());
    }

}
