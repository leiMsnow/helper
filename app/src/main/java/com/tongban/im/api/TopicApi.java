package com.tongban.im.api;

import android.content.Context;

import com.tongban.corelib.base.api.ApiCallback;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.im.App;
import com.tongban.im.common.Consts;
import com.tongban.im.model.BaseEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * 话题api
 * Created by zhangleilei on 15/8/15.
 */
public class TopicApi extends BaseApi {

    private static TopicApi mApi;

    /**
     * 创建话题
     */
    public static final String CREATE_TOPIC = "topic/create";
    /**
     * 话题推荐接口
     */
    public static final String RECOMMEND_TOPIC_LIST = "topic/recommend/list";

    private TopicApi(Context context) {
        super(context);
    }

    public static TopicApi getInstance() {
        if (mApi == null) {
            synchronized (UserApi.class) {
                if (mApi == null) {
                    mApi = new TopicApi(App.getInstance());
                }
            }
        }
        return mApi;
    }


    /**
     * 创建话题
     *
     * @param title    标题
     * @param content  内容
     * @param urls     图片集合
     * @param callback
     */
    public void createTopic(String title, String content, Map<String, String[]> urls,
                            final ApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("user_id", SPUtils.get(mContext, Consts.USER_ID, ""));
        mParams.put("nick_name", SPUtils.get(mContext, Consts.NICK_NAME, ""));
        mParams.put("topic_title", title);
        mParams.put("topic_content", content);
        mParams.put("topic_img_url", urls);

        simpleRequest(CREATE_TOPIC, mParams, new ApiCallback() {
            @Override
            public void onStartApi() {
                if (callback != null)
                    callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                if (callback != null)
                    callback.onComplete(obj);
            }

            @Override
            public void onFailure(DisplayType displayType, Object errorMessage) {
                if (callback != null)
                    callback.onFailure(displayType, errorMessage);
            }
        });
    }

    /**
     *
     * @param callback
     */
    public void recommendTopicList(final ApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("user_id", SPUtils.get(mContext, Consts.USER_ID, ""));

        simpleRequest(RECOMMEND_TOPIC_LIST, mParams, new ApiCallback() {
            @Override
            public void onStartApi() {
                if (callback != null)
                    callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                if (callback != null)
                    callback.onComplete(obj);
            }

            @Override
            public void onFailure(DisplayType displayType, Object errorMessage) {
                if (callback != null)
                    callback.onFailure(displayType, errorMessage);
            }
        });
    }
}
