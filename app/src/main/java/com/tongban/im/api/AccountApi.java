package com.tongban.im.api;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tongban.corelib.base.api.ApiCallback;
import com.tongban.corelib.model.ApiResult;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.im.App;
import com.tongban.im.R;
import com.tongban.im.common.ModelToTable;
import com.tongban.im.common.Consts;
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
    public final static String LOGIN = "user/login/1";
    /**
     * token登录
     */
    public final static String TOKEN_LOGIN = "user/login/2";
    /**
     * 密码重置
     */
    public final static String PWD_RESET = "user/password/reset";

    /**
     * 获取用户信息
     */
    public final static String GET_USER_INFO = "user/info";
    /**
     * 注册第一步，获取手机验证码
     */
    public final static String SMS_REQUIRE = "verifycode/sms/require";
//    /**
//     * 注册第二步，验证手机验证码
//     */
//    public final static String VERIFY_CODE = "verifycode/sms/verify";
    /**
     * 注册第二步，输入手机号验证码
     */
    public final static String REGISTER = "user/register/1";

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
    public void getSMSCode(String mobilePhone, final ApiCallback callback) {

        mParams = new HashMap<>();
        mParams.put("mobile_phone", mobilePhone);
        mParams.put("verify_type", 1);

        simpleRequest(SMS_REQUIRE, mParams, new ApiCallback() {
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
                registerEvent.setRegisterEnum(BaseEvent.RegisterEvent.RegisterEnum.SMS_CODE);
                callback.onComplete(registerEvent);
            }

            @Override
            public void onFailure(DisplayType displayType, Object errorMessage) {
                callback.onFailure(DisplayType.None, errorMessage);
                if (errorMessage instanceof ApiResult) {
                    EventBus.getDefault().post(((ApiResult) errorMessage).getStatusDesc());
                }
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
                         final ApiCallback callback) {

        mParams = new HashMap<>();
        mParams.put("mobile_phone", mobilePhone);
        mParams.put("nick_name", mobilePhone);
        mParams.put("password", password);
        mParams.put("verify_id", verifyId);
        mParams.put("verify_code", verifyCode);

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
                registerEvent.setRegisterEnum(BaseEvent.RegisterEvent.RegisterEnum.REGISTER);
                callback.onComplete(registerEvent);
            }

            @Override
            public void onFailure(DisplayType displayType, Object errorMessage) {
                ApiResult apiResult = (ApiResult) errorMessage;
                if (apiResult.getStatusCode() == ApiErrorCode.User.VCODE_NOT_SAME_OR_OUT_OF_DATE) {
                    apiResult.setStatusDesc(mContext.getResources().getString(R.string.verify_code_illegal));
                }
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
                ApiResult apiResult = (ApiResult) errorMessage;
                if (apiResult.getStatusCode() == ApiErrorCode.User.USERNAME_OR_PWD_EMPTY) {
                    apiResult.setStatusDesc(mContext.getResources().getString(R.string.username_or_pwd_empty));
                } else if (apiResult.getStatusCode() == ApiErrorCode.User.CONTAIN_ILLEGAL_PARAMETER) {
                    apiResult.setStatusDesc(mContext.getResources().getString(R.string.contain_illegal_parameter));
                } else if (apiResult.getStatusCode() == ApiErrorCode.User.USE_PHONE_OR_PWD_FAIL) {
                    apiResult.setStatusDesc(mContext.getResources().getString(R.string.use_phone_pwd_fail));
                } else if (apiResult.getStatusCode() == ApiErrorCode.User.PHONE_ERROR) {
                    apiResult.setStatusDesc(mContext.getResources().getString(R.string.phone_error));
                }
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
     * @param oldPwd   旧密码
     * @param newPwd   新密码
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
                BaseEvent.PwdResetEvent pwdResetEvent = new BaseEvent.PwdResetEvent();
                pwdResetEvent.setResult(mContext.getResources().getString(R.string.pwd_reset_success));
                callback.onComplete(pwdResetEvent);
            }

            @Override
            public void onFailure(DisplayType displayType, Object errorMessage) {
                ApiResult apiResult = (ApiResult) errorMessage;
                if (apiResult.getStatusCode() == ApiErrorCode.User.RESET_OLD_PWD_ERROR) {
                    apiResult.setStatusDesc(mContext.getResources().getString(R.string.old_pwd_error));
                } else if (apiResult.getStatusCode() == ApiErrorCode.User.RESET_PWD_CONTAIN_EMPTY) {
                    apiResult.setStatusDesc(mContext.getResources().getString(R.string.pwd_contain_empty));
                } else if (apiResult.getStatusCode() == ApiErrorCode.User.RESET_PWD_FAIL) {
                    apiResult.setStatusDesc(mContext.getResources().getString(R.string.pwd_reset_fail));
                } else if (apiResult.getStatusCode() == ApiErrorCode.User.NEW_PWD_NOT_SANME_CONFIRM_PWD) {
                    apiResult.setStatusDesc(mContext.getResources().getString(R.string.twice_pwd_same));
                } else if (apiResult.getStatusCode() == ApiErrorCode.User.OBTAIN_OLD_PWD_FAIL) {
                    apiResult.setStatusDesc(mContext.getResources().getString(R.string.obtain_old_pwd_fail));
                }
                callback.onFailure(displayType, apiResult);
            }


        });

    }

    /**
     * 获取用户信息
     *
     * @param userId   用户ID
     * @param callback 回调
     */
    public void getUserInfoByUserId(final String userId, final ApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("user_id", userId);

        simpleRequest(GET_USER_INFO, mParams, new ApiCallback() {
            @Override
            public void onStartApi() {
                if (callback != null)
                    callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                ApiResult<User> apiResponse = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiResult<User>>() {
                        });
                BaseEvent.UserInfoEvent userInfoEvent = new BaseEvent.UserInfoEvent();
                userInfoEvent.setUser(apiResponse.getData());
                userInfoEvent.getUser().setUser_id(userId);
                //将用户信息保存到本地数据库
                UserDaoHelper.get(mContext).addData(ModelToTable.userToTable(userInfoEvent.getUser()));
                if (callback != null)
                    callback.onComplete(userInfoEvent);
            }

            @Override
            public void onFailure(DisplayType displayType, Object errorMessage) {
                if (callback != null)
                    callback.onFailure(displayType, errorMessage);
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
        SPUtils.put(mContext, Consts.NICK_NAME, userInfo.getNick_name());
    }

}
