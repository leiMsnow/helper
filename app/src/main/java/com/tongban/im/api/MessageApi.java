package com.tongban.im.api;

import android.content.Context;
import android.support.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tongban.corelib.base.api.ApiCallback;
import com.tongban.corelib.model.ApiResult;
import com.tongban.corelib.utils.LogUtil;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.im.App;
import com.tongban.im.common.Consts;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.Group;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            synchronized (UserApi.class) {
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
                callback.onComplete(obj);
            }

            @Override
            public void onFailure(DisplayType displayType, Object errorMessage) {
                callback.onFailure(displayType, errorMessage);
            }
        });
    }

}
