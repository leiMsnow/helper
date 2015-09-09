package com.tongban.im.model;

/**
 * Api错误码
 * Created by zhangleilei on 15/7/16.
 */
public class ApiErrorCode {

    public static class User {
        /**
         * 用户已经注册
         */
        public static int USER_REGISTERED = 10001;

        /**
         * 重置密码老密码错误
         */
        public static int RESET_OLD_PWD_ERROR = 10030;
        /**
         * 重置密码包含空参数
         */
        public static int RESET_PWD_CONTAIN_EMPTY = 10031;
        /**
         * 重置密码失败
         */
        public static int RESET_PWD_FAIL = 10032;
        /**
         * 新密码和确认新密码不一致
         */
        public static int NEW_PWD_NOT_SANME_CONFIRM_PWD = 10033;
        /**
         * 重置密码获取用户旧密码失败
         */
        public static int  OBTAIN_OLD_PWD_FAIL= 10038;
        /**
         * 用户登录密码或账户名为空
         */
        public static int  USERNAME_OR_PWD_EMPTY= 10025;
        /**
         * 用户登录包含非法参数
         */
        public static int  CONTAIN_ILLEGAL_PARAMETER= 10024;
        /**
         * 使用手机号和密码登录失败
         */
        public static int  USE_PHONE_OR_PWD_FAIL= 10029;
        /**
         * 手机号码不正确
         */
        public static int  PHONE_ERROR= 10004;
        /**
         * 手机验证码不一致或失效
         */
        public static int  VCODE_NOT_SAME_OR_OUT_OF_DATE= 10014;
        /**
         * 手机号码已经注册
         */
        public static int  PHONE_HAS_BEEN_REGISTED= 11502;

    }

}
