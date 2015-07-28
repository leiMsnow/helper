package com.tongban.im.model;

/**
 * Api错误码
 * Created by zhangleilei on 15/7/16.
 */
public class ApiErrorCode {

    public static class User{
        /**
         * 用户已经注册
         */
        public static int USER_REGISTERED = 10001;

        /**
         * 重置密码老密码错误
         */
        public static int RESET_OLD_PWD_ERROR = 10030;



    }

}
