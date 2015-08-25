package com.tongban.im.api;

import android.content.Context;
import android.support.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tongban.corelib.base.api.ApiCallback;
import com.tongban.corelib.model.ApiListResult;
import com.tongban.corelib.model.ApiResult;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.im.App;
import com.tongban.im.R;
import com.tongban.im.common.Consts;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.ImageUrl;
import com.tongban.im.model.Topic;
import com.tongban.im.model.TopicComment;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

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
    /**
     * 话题详情接口
     */
    public static final String TOPIC_INFO = "topic/detail/info";
    /**
     * 话题回复列表接口
     */
    public static final String TOPIC_COMMENT_LIST = "topic/contain/comment/list";
    /**
     * 回复话题
     */
    public static final String COMMENT_CREATE = "comment/create";
    /**
     * 收藏话题
     */
    public static final String COLLECT_CREATE = "user/collect/topic";
    /**
     * 取消收藏话题
     */
    public static final String NO_COLLECT_CREATE = "user/nocollect/topic";

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
    public void createTopic(String title, String content, List<ImageUrl> urls,
                            final ApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("user_id", SPUtils.get(mContext, Consts.USER_ID, ""));
        mParams.put("topic_title", title);
        mParams.put("topic_content", content);
        mParams.put("topic_img_url", JSON.toJSON(urls));

        simpleRequest(CREATE_TOPIC, mParams, new ApiCallback() {
            @Override
            public void onStartApi() {
                if (callback != null)
                    callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                ApiResult<String> result = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiResult<String>>() {
                        });

                BaseEvent.CreateTopicEvent createTopicEvent = new BaseEvent.CreateTopicEvent();
                createTopicEvent.setTopicId(result.getData());
                if (callback != null)
                    callback.onComplete(createTopicEvent);
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
                ApiListResult<Topic> result = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiListResult<Topic>>() {
                        });

                if (result.getData().getResult().size() > 0) {
                    BaseEvent.TopicListEvent topicListEvent = new BaseEvent.TopicListEvent();
                    topicListEvent.setIsMain(true);
                    topicListEvent.setTopicList(result.getData().getResult());
                    if (callback != null)
                        callback.onComplete(topicListEvent);
                } else {
                    if (callback != null)
                        callback.onFailure(DisplayType.View, "暂无话题信息,快来创建第一条话题吧");
                }
            }

            @Override
            public void onFailure(DisplayType displayType, Object errorMessage) {

                if (callback != null)
                    callback.onFailure(DisplayType.View, getErrorMessage());
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
                ApiListResult<Topic> result = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiListResult<Topic>>() {
                        });
                BaseEvent.SearchTopicListEvent topicListEvent = new BaseEvent.SearchTopicListEvent();
                topicListEvent.setTopicList(result.getData().getResult());
                if (callback != null)
                    callback.onComplete(topicListEvent);
            }

            @Override
            public void onFailure(DisplayType displayType, Object errorMessage) {
                if (callback != null)
                    callback.onFailure(displayType, errorMessage);
            }


        });
    }

    /**
     * 话题详情接口
     *
     * @param topicId  话题Id
     * @param callback
     */
    public void getTopicInfo(String topicId, final ApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("topic_id", topicId);
        mParams.put("user_id", SPUtils.get(mContext, Consts.USER_ID, ""));

        simpleRequest(TOPIC_INFO, mParams, new ApiCallback() {
            @Override
            public void onStartApi() {
                if (callback != null)
                    callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                ApiResult<Topic> result = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiResult<Topic>>() {
                        });
                if (result.getData() != null) {
                    BaseEvent.TopicInfoEvent topicEvent = new BaseEvent.TopicInfoEvent();
                    topicEvent.setTopic(result.getData());
                    if (callback != null)
                        callback.onComplete(topicEvent);
                } else {
                    if (callback != null)
                        callback.onFailure(DisplayType.View, "暂无话题数据");
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
     * 获取话题回复列表
     *
     * @param topicId  话题Id
     * @param cursor   第几页，默认0开始
     * @param pageSize 每页数量 最少1条
     * @param callback
     */
    public void getTopicCommentList(String topicId, int cursor, int pageSize, final ApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("topic_id", topicId);
        mParams.put("cursor", cursor < 0 ? 0 : cursor);
        mParams.put("page_size", pageSize < 1 ? 10 : pageSize);

        simpleRequest(TOPIC_COMMENT_LIST, mParams, new ApiCallback() {
            @Override
            public void onStartApi() {
                if (callback != null)
                    callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                ApiListResult<TopicComment> result = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiListResult<TopicComment>>() {
                        });
                BaseEvent.TopicCommentListEvent topicCommentListEvent = new BaseEvent.TopicCommentListEvent();
                topicCommentListEvent.setTopicCommentList(result.getData().getResult());
                if (callback != null)
                    callback.onComplete(topicCommentListEvent);

            }

            @Override
            public void onFailure(DisplayType displayType, Object errorMessage) {
                if (callback != null)
                    callback.onFailure(displayType, errorMessage);
            }
        });
    }

    /**
     * 回复话题/某评论
     *
     * @param topicId          话题Id
     * @param commentContent   回复内容
     * @param repliedCommentId 被回复评论的Id
     * @param repliedName      被回复评论的用户昵称
     * @param repliedUserId    被回复评论的用户Id
     * @param callback
     */
    public void createCommentForTopic(String topicId, String commentContent,
                                      @Nullable String repliedCommentId,
                                      @Nullable String repliedName,
                                      @Nullable String repliedUserId,
                                      final ApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("user_id", SPUtils.get(mContext, Consts.USER_ID, ""));
        mParams.put("nick_name", SPUtils.get(mContext, Consts.NICK_NAME, ""));
        mParams.put("topic_id", topicId);
        mParams.put("comment_content", commentContent);
        //回复评论用到的字段，如果不传这三个值，将视为回复话题
        if (repliedName != null && repliedUserId != null && repliedCommentId != null) {
            mParams.put("replied_comment_id", repliedCommentId);
            mParams.put("replied_nick_name", repliedName);
            mParams.put("replied_user_id", repliedUserId);
        }


        simpleRequest(COMMENT_CREATE, mParams, new ApiCallback() {
                    @Override
                    public void onStartApi() {
                        if (callback != null)
                            callback.onStartApi();
                    }

                    @Override
                    public void onComplete(Object obj) {

                        BaseEvent.CreateTopicCommentEvent event = new BaseEvent.CreateTopicCommentEvent();
                        event.setMessage("回复成功");
                        if (callback != null)
                            callback.onComplete(event);
                    }

                    @Override
                    public void onFailure(DisplayType displayType, Object errorMessage) {
                        if (callback != null)
                            callback.onFailure(displayType, errorMessage);
                    }
                }

        );
    }

    /**
     * 收藏话题
     *
     * @param collect  true:收藏；false：取消收藏
     * @param topicId  话题Id
     * @param callback
     */
    public void collectTopic(final boolean collect, String topicId, final ApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("user_id", SPUtils.get(mContext, Consts.USER_ID, ""));
        mParams.put("topic_id", topicId);

        simpleRequest(collect ? COLLECT_CREATE : NO_COLLECT_CREATE, mParams, new ApiCallback() {
                    @Override
                    public void onStartApi() {
                        if (callback != null)
                            callback.onStartApi();
                    }

                    @Override
                    public void onComplete(Object obj) {

                        if (callback != null)
                            callback.onComplete(new BaseEvent.TopicCollect(collect));
                    }

                    @Override
                    public void onFailure(DisplayType displayType, Object errorMessage) {
                        if (callback != null)
                            callback.onFailure(displayType, errorMessage);
                    }
                }
        );
    }
}
