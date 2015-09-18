package com.tongban.im.api;

import android.content.Context;

import com.tongban.im.App;
import com.tongban.im.api.base.BaseApi;

/**
 * 消息api
 * Created by zhangleilei on 15/7/15.
 */
public class MessageApi extends BaseApi {

    private static MessageApi mApi;


//    public static final String MESSAGE_HOST = "http://10.255.209.67:8080/ddim/msg";
    /**
     * 加入群组
     */
    public static final String JOIN_GROUP = "/msg/system/joingroup";

    private MessageApi(Context context) {
        super(context);
    }

    public static MessageApi getInstance() {
        if (mApi == null) {
            synchronized (MessageApi.class) {
                if (mApi == null) {
                    mApi = new MessageApi(App.getInstance());
                }
            }
        }
        return mApi;
    }
}
