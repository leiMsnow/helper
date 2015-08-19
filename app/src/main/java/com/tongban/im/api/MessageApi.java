package com.tongban.im.api;

import android.content.Context;

import com.tongban.corelib.base.api.ApiCallback;
import com.tongban.im.App;
import com.tongban.im.model.BaseEvent;

import java.util.HashMap;

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
    public static final String JOIN_GROUP = "msg/system/joingroup";

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

    /**
     * 获取圈子列表数据
     *
     * @param toUserIds 接收方ID
     * @param callback  回调
     */
    public void joinGroup(String[] toUserIds, String content, final ApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("to_user_ids", toUserIds);
        mParams.put("content", content);

        simpleRequest(JOIN_GROUP, mParams, new ApiCallback() {
            @Override
            public void onStartApi() {
                callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                BaseEvent.JoinGroupEvent joinGroupEvent = new BaseEvent.JoinGroupEvent();
                joinGroupEvent.setMessage("已经向该圈子发送申请");
                callback.onComplete(joinGroupEvent);
            }

            @Override
            public void onFailure(DisplayType displayType, Object errorMessage) {
                callback.onFailure(displayType, errorMessage);
            }
        });
    }

}
