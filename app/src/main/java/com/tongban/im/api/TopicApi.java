package com.tongban.im.api;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tongban.corelib.base.api.ApiCallback;
import com.tongban.corelib.model.ApiListResult;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.im.App;
import com.tongban.im.common.Consts;
import com.tongban.im.model.Topic;

import java.util.HashMap;
import java.util.Map;

/**
 * 话题api
 * Created by zhangleilei on 15/8/15.
 */
public class TopicApi extends BaseApi {

    private static TopicApi mApi;

    /**
     * 创建话题接口
     */
    public static final String CREATE_TOPIC = "topic/create";
    /**
     * 话题推荐接口
     */
    public static final String RECOMMEND_TOPIC_LIST = "topic/recommend/list";
    /**
     * 话题搜索接口
     */
    public static final String SEARCH_TOPIC_LIST = "topic/search/list";

    private TopicApi(Context context) {
        super(context);
    }

    public static TopicApi getInstance() {
        if (mApi == null) {
            synchronized (TopicApi.class) {
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
     * 话题推荐
     *
     * @param callback
     */
    public void recommendTopicList(int cursor, int pageSize, final ApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("user_id", SPUtils.get(mContext, Consts.USER_ID, ""));
        mParams.put("cursor", cursor < 0 ? 0 : cursor);
        mParams.put("page_size", pageSize < 1 ? 10 : pageSize);

        simpleRequest(RECOMMEND_TOPIC_LIST, mParams, new ApiCallback() {
            @Override
            public void onStartApi() {
                if (callback != null)
                    callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                ApiListResult<Topic> listResult = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiListResult<Topic>>() {
                        });

                if (listResult.getData().getResult().size() > 0) {
                    if (callback != null)
                        callback.onComplete(listResult);
                } else {
                    if (callback != null)
                        callback.onFailure(DisplayType.View, "暂无话题信息,快来创建第一条话题吧");
                }
            }

            @Override
            public void onFailure(DisplayType displayType, Object errorMessage) {
                if (callback != null)
                    callback.onFailure(displayType, errorMessage);
            }
        });
    }

    /**
     * 话题搜索
     *
     * @param keyword  关键字
     * @param cursor   第几页，默认0开始
     * @param pageSize 每页数量 最少1条
     * @param callback
     */
    public void searchTopicList(String keyword, int cursor, int pageSize, final ApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("keyword", keyword);
        mParams.put("user_id", SPUtils.get(mContext, Consts.USER_ID, ""));
        mParams.put("cursor", cursor < 0 ? 0 : cursor);
        mParams.put("page_size", pageSize < 1 ? 10 : pageSize);

        simpleRequest(SEARCH_TOPIC_LIST, mParams, new ApiCallback() {
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
