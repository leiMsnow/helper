package com.tongban.im.api;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tongban.corelib.base.api.ApiCallback;
import com.tongban.corelib.model.ApiResult;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.im.App;
import com.tongban.im.common.Consts;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.User;

import org.json.JSONObject;

import java.util.HashMap;

import de.greenrobot.event.EventBus;


/**
 * 用户操作API接口类
 * Created by zhangleilei on 15/7/3.
 */
public class UserApi extends BaseApi {

    private static UserApi mApi;

    /**
     * 注册
     */
    public final static String REGISTER = "user/register/1";
    /**
     * 第三方注册
     */
    public final static String THIRD_REGISTER = "user/register/2";
    /**
     * 获取手机验证码
     */
    public final static String FETCH = "sms/fetch";
    /**
     * 校验手机验证码
     */
    public final static String EXAM = "sms/exam";

    /**
     * 登录
     */
    public final static String LOGIN = "user/login/1";
    /**
     * token登录
     */
    public final static String TOKEN_LOGIN = "user/login/2";
    /**
     * 密码重置
     */
    public final static String PWD_RESET = "user/password/reset";

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
     * 注册,第一步
     *
     * @param nickName    昵称
     * @param mobilePhone 手机号
     * @param password    密码
     * @param callback    回调结果
     */
    public void register(String nickName, String mobilePhone, String password, final ApiCallback callback) {

        mParams = new HashMap<>();
        mParams.put("nick_name", nickName);
        mParams.put("mobile_phone", mobilePhone);
        mParams.put("password", password);

        simpleRequest(REGISTER, mParams, new ApiCallback() {
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
                registerEvent.setRegisterEnum(BaseEvent.RegisterEvent.RegisterEnum.REG);

                callback.onComplete(registerEvent);
            }

            @Override
            public void onFailure(DisplayType displayType, Object errorMessage) {
                callback.onFailure(DisplayType.None, errorMessage);
                if (errorMessage instanceof ApiResult) {
                    JSONObject jsonObject = (JSONObject) ((ApiResult) errorMessage).getData();
                    ((ApiResult) errorMessage).setData(jsonObject.opt("user_id"));
                    EventBus.getDefault().post(errorMessage);
                }
            }

        });
    }

    /**
     * 获取手机验证码，注册第二步
     *
     * @param userId      从注册第一步拿到的userid
     * @param mobilePhone 手机号
     * @param verifyType  验证类型
     * @param callback
     */
    public void fetch(String userId, String mobilePhone, String verifyType, final ApiCallback callback) {

        mParams = new HashMap<>();
        mParams.put("user_id", userId);
        mParams.put("mobile_phone", mobilePhone);
        mParams.put("verify_type", verifyType);

        simpleRequest(FETCH, mParams, new ApiCallback() {
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
                registerEvent.setRegisterEnum(BaseEvent.RegisterEvent.RegisterEnum.FETCH);
                callback.onComplete(registerEvent);
            }

            @Override
            public void onFailure(DisplayType displayType, Object errorMessage) {
                callback.onFailure(displayType, errorMessage);
            }

        });
    }

    /**
     * 校验手机验证码，注册第三步
     *
     * @param verifyId   验证码ID
     * @param verifyCode 验证码code
     * @param callback
     */
    public void exam(String verifyId, String verifyCode, final ApiCallback callback) {

        mParams = new HashMap<>();
        mParams.put("verify_id", verifyId);
        mParams.put("verify_code", verifyCode);

        simpleRequest(EXAM, mParams, new ApiCallback() {
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
                registerEvent.setRegisterEnum(BaseEvent.RegisterEvent.RegisterEnum.EXAM);
                callback.onComplete(registerEvent);
            }

            @Override
            public void onFailure(DisplayType displayType, Object errorMessage) {
                callback.onFailure(displayType, errorMessage);
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
    public void login(String phone, String password, final ApiCallback callback) {

        mParams = new HashMap<>();
        mParams.put("mobile_phone", phone);
        mParams.put("password", password);

        simpleRequest(LOGIN, mParams, new ApiCallback() {
            @Override
            public void onStartApi() {
                callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                ApiResult<User> apiResponse = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiResult<User>>() {
                        });
                User user = apiResponse.getData();
                saveUserInfo(user);
                callback.onComplete(user);
            }

            @Override
            public void onFailure(DisplayType displayType, Object errorMessage) {
                callback.onFailure(displayType, errorMessage);
            }

        });
    }


    /**
     * token登录
     *
     * @param token    令牌
     * @param callback
     */
    public void tokenLogin(String token, final ApiCallback callback) {

        mParams = new HashMap<>();
        mParams.put("freeauth_token", token);

        simpleRequest(TOKEN_LOGIN, mParams, new ApiCallback() {
            @Override
            public void onStartApi() {
            }

            @Override
            public void onComplete(Object obj) {
                ApiResult<User> apiResponse = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiResult<User>>() {
                        });
                User user = apiResponse.getData();
                saveUserInfo(user);
                callback.onComplete(user);
            }

            @Override
            public void onFailure(DisplayType displayType, Object errorMessage) {
                callback.onFailure(displayType, errorMessage);
                EventBus.getDefault().post(errorMessage);
            }

        });
    }

    /**
     * 密码重置
     *
     * @param oldPwd        旧密码
     * @param newPwd        新密码
     * @param callback
     */
    public void pwdReset(String oldPwd, String newPwd, final ApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("user_id", SPUtils.get(mContext, Consts.USER_ID, ""));
        mParams.put("old_pass", oldPwd);
        mParams.put("new_pass", newPwd);
        // TODO 使用新接口，删除此字段
        mParams.put("confirm_new_pass", newPwd);

        simpleRequest(PWD_RESET, mParams, new ApiCallback() {
            @Override
            public void onStartApi() {
                callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                BaseEvent.PwdResetEvent pwdResetEvent=new BaseEvent.PwdResetEvent();
                pwdResetEvent.setResult("重置密码成功");
                callback.onComplete(pwdResetEvent);
            }

            @Override
            public void onFailure(DisplayType displayType, Object errorMessage) {
                callback.onFailure(displayType, errorMessage);
                EventBus.getDefault().post(errorMessage);
            }


        });

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
    }


}
