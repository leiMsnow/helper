package com.tb.api;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tb.api.base.ApiCache;
import com.tb.api.base.BaseApi;
import com.tb.api.model.BaseEvent;
import com.tb.api.model.discover.ProductBook;
import com.tb.api.model.topic.Comment;
import com.tb.api.model.topic.CommentContent;
import com.tb.api.model.topic.Topic;
import com.tb.api.model.topic.TopicContent;
import com.tb.api.model.topic.TopicType;
import com.tb.api.utils.TransferCenter;
import com.tongban.corelib.base.BaseApplication;
import com.tongban.corelib.base.api.IApiCallback;
import com.tongban.corelib.model.ApiErrorResult;
import com.tongban.corelib.model.ApiListResult;
import com.tongban.corelib.model.ApiResult;
import com.tongban.corelib.utils.Constants;
import com.tongban.corelib.utils.SPUtils;

import java.util.HashMap;

/**
 * 话题api
 * Created by zhangleilei on 15/8/15.
 */
public class TopicApi extends BaseApi {

    private static TopicApi mApi;
//---------------------------------------输入接口----------------------------------------------------
    /**
     * 创建话题接口
     */
    public static final String CREATE_TOPIC = "/topic/create";
    /**
     * 回复话题
     */
    public static final String COMMENT_CREATE = "/comment/create";
    /**
     * 收藏话题
     */
    public static final String COLLECT_CREATE = "/user/collect/topic";
    /**
     * 取消收藏话题
     */
    public static final String NO_COLLECT_CREATE = "/user/nocollect/topic";

//---------------------------------------输出接口----------------------------------------------------
    /**
     * 话题回复列表接口
     */
    public static final String TOPIC_COMMENT_LIST = "/topic/contain/comment/list";
    /**
     * 话题推荐接口
     */
    public static final String RECOMMEND_TOPIC_LIST = "/topic/recommend/list";
    /**
     * 话题搜索接口
     */
    public static final String SEARCH_TOPIC_LIST = "/topic/search/list";
    /**
     * 话题详情接口
     */
    public static final String TOPIC_INFO = "/topic/detail/info";
    /**
     * 官方话题评测详情
     */
    public static final String OFFICIAL_TOPIC_INFO = "/topic/products";

    private TopicApi(Context context) {
        super(context);
    }

    public static TopicApi getInstance() {
        if (mApi == null) {
            synchronized (TopicApi.class) {
                if (mApi == null) {
                    mApi = new TopicApi(BaseApplication.getInstance());
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
     * @param callback
     */
    public void createTopic(String title, TopicContent content,
                            final IApiCallback callback) {

        if (!TransferCenter.getInstance().startLogin())
            return;

        mParams = new HashMap<>();
        mParams.put("user_id", getUserId());
        mParams.put("topic_title", title);
        mParams.put("topic_content", JSON.toJSONString(content));
        mParams.put("topic_type_list", TopicType.PRIVATE);

        simpleRequest(CREATE_TOPIC, mParams, new IApiCallback() {
            @Override
            public void onStartApi() {
                if (callback != null)
                    callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                ApiCache.getInstance().setDisableCache(ApiCache.TOPIC_CACHE_TIME);
                ApiResult<String> result = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiResult<String>>() {
                        });

                BaseEvent.CreateTopicEvent createTopicEvent = new BaseEvent.CreateTopicEvent();
                createTopicEvent.topicId = (result.getData());
                if (callback != null)
                    callback.onComplete(createTopicEvent);
            }

            @Override
            public void onFailure(ApiErrorResult result) {
                result.setDisplayType(DisplayType.Toast);
                if (callback != null)
                    callback.onFailure(result);
            }
        });
    }

    /**
     * 话题推荐
     *
     * @param callback
     */
    public void recommendTopicList(final int cursor, int pageSize, final IApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("user_id", getUserId());
        mParams.put("cursor", cursor < 0 ? 0 : cursor);
        mParams.put("page_size", pageSize < 1 ? 10 : pageSize);
        // 类型为官方话题和个人发表的话题
        mParams.put("topic_type_list", new String[]{TopicType.EVALUATION, TopicType.PRIVATE});

        simpleRequest(RECOMMEND_TOPIC_LIST, mParams, new IApiCallback() {
            @Override
            public void onStartApi() {
                if (cursor == 0) {
                    if (callback != null)
                        callback.onStartApi();
                }
            }

            @Override
            public void onComplete(Object obj) {
                ApiListResult<Topic> result = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiListResult<Topic>>() {
                        });
                if (result.getData().getResult().size() > 0) {
                    BaseEvent.RecommendTopicListEvent topicListEvent = new BaseEvent.RecommendTopicListEvent();
                    topicListEvent.topicList = (result.getData().getResult());
                    if (callback != null)
                        callback.onComplete(topicListEvent);
                } else {
                    onFailure(createEmptyResult(RECOMMEND_TOPIC_LIST));
                }
            }

            @Override
            public void onFailure(ApiErrorResult result) {
                result.setDisplayType(DisplayType.View);
                if (callback != null)
                    callback.onFailure(result);
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
    public void searchTopicList(String keyword, int cursor, int pageSize, final IApiCallback callback) {

        mParams = new HashMap<>();
        mParams.put("keyword", keyword);
        mParams.put("user_id", getUserId());
        mParams.put("cursor", cursor < 0 ? 0 : cursor);
        mParams.put("page_size", pageSize < 1 ? 10 : pageSize);
        // 类型为官方话题和个人发表的话题
        mParams.put("topic_type_list", new String[]{TopicType.EVALUATION, TopicType.PRIVATE});
        simpleRequest(SEARCH_TOPIC_LIST, mParams, new IApiCallback() {
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
                    BaseEvent.SearchTopicListEvent topicListEvent = new BaseEvent.SearchTopicListEvent();
                    topicListEvent.topicList = (result.getData().getResult());
                    if (callback != null)
                        callback.onComplete(topicListEvent);
                } else {
                    onFailure(createEmptyResult(SEARCH_TOPIC_LIST));
                }
            }

            @Override
            public void onFailure(ApiErrorResult result) {
                if (callback != null)
                    callback.onFailure(result);
            }

        });
    }


    /**
     * 话题详情接口
     *
     * @param topicId  话题Id
     * @param callback
     */
    public void getTopicInfo(String topicId, final IApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("topic_id", topicId);
        mParams.put("user_id", getUserId());

        simpleRequest(TOPIC_INFO, mParams, new IApiCallback() {
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
                BaseEvent.TopicInfoEvent topicEvent = new BaseEvent.TopicInfoEvent();
                topicEvent.topic = (result.getData());
                if (callback != null)
                    callback.onComplete(topicEvent);

            }

            @Override
            public void onFailure(ApiErrorResult result) {
                if (callback != null)
                    callback.onFailure(result);
            }
        });
    }

    /**
     * 官方话题评测详情接口
     *
     * @param topicId  话题Id
     * @param callback
     */
    public void getOfficialTopicInfo(String topicId, int cursor, int pageSize, final IApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("topic_id", topicId);
        mParams.put("cursor", cursor < 0 ? 0 : cursor);
        mParams.put("page_size", pageSize < 1 ? 10 : pageSize);

        simpleRequest(OFFICIAL_TOPIC_INFO, mParams, new IApiCallback() {
            @Override
            public void onStartApi() {
                if (callback != null)
                    callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                ApiListResult<ProductBook> result = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiListResult<ProductBook>>() {
                        });
                BaseEvent.OfficialTopicInfoEvent officialTopicInfoEvent = new BaseEvent.OfficialTopicInfoEvent();
                officialTopicInfoEvent.productBookList = (result.getData().getResult());
                if (callback != null) {
                    Log.d("onComplete", "onComplete1");
                    callback.onComplete(officialTopicInfoEvent);

                }

            }

            @Override
            public void onFailure(ApiErrorResult result) {
                if (callback != null)
                    callback.onFailure(result);
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
    public void getTopicCommentList(String topicId,
                                    int cursor,
                                    int pageSize,
                                    final IApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("topic_id", topicId);
        mParams.put("cursor", cursor < 0 ? 0 : cursor);
        mParams.put("page_size", pageSize < 1 ? 10 : pageSize);

        simpleRequest(TOPIC_COMMENT_LIST, mParams, new IApiCallback() {
            @Override
            public void onStartApi() {
                if (callback != null)
                    callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                ApiListResult<Comment> result = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiListResult<Comment>>() {
                        });
                BaseEvent.TopicCommentListEvent commentList =
                        new BaseEvent.TopicCommentListEvent();
                commentList.topicCommentList = (result.getData().getResult());
                if (callback != null)
                    callback.onComplete(commentList);
            }

            @Override
            public void onFailure(ApiErrorResult result) {
                result.setDisplayType(DisplayType.None);
                if (callback != null)
                    callback.onFailure(result);
            }
        });
    }

    /**
     * 评论话题/回复评论
     *
     * @param topicId          话题Id
     * @param commentContent   评论/回复内容
     * @param repliedCommentId 被回复评论的Id
     * @param repliedName      被回复评论的用户昵称
     * @param repliedUserId    被回复评论的用户Id
     * @param callback
     */
    public void createCommentForTopic(final String topicId, CommentContent commentContent,
                                      @Nullable String repliedCommentId,
                                      @Nullable String repliedName,
                                      @Nullable String repliedUserId,
                                      final IApiCallback callback) {

        if (!TransferCenter.getInstance().startLogin())
            return;

        mParams = new HashMap<>();
        mParams.put("user_id", getUserId());
        mParams.put("nick_name", SPUtils.get(mContext, Constants.NICK_NAME, ""));
        mParams.put("topic_id", topicId);
        mParams.put("comment_content", JSON.toJSONString(commentContent));
        //回复评论用到的字段，如果不传这三个值，将视为评论话题
        if (repliedName != null && repliedUserId != null && repliedCommentId != null) {
            mParams.put("replied_comment_id", repliedCommentId);
            mParams.put("replied_nick_name", repliedName);
            mParams.put("replied_user_id", repliedUserId);
        }


        simpleRequest(COMMENT_CREATE, mParams, new IApiCallback() {
                    @Override
                    public void onStartApi() {
                        if (callback != null)
                            callback.onStartApi();
                    }

                    @Override
                    public void onComplete(Object obj) {
                        ApiCache.getInstance().setDisableCache(ApiCache.TOPIC_CACHE_TIME);
                        BaseEvent.CreateTopicCommentEvent event = new BaseEvent.CreateTopicCommentEvent();
                        event.topic_id = topicId;
                        if (callback != null)
                            callback.onComplete(event);
                    }

                    @Override
                    public void onFailure(ApiErrorResult result) {
                        result.setDisplayType(DisplayType.Toast);
                        if (callback != null)
                            callback.onFailure(result);
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
    public void collectTopic(final boolean collect, final String topicId, final IApiCallback callback) {

        if (!TransferCenter.getInstance().startLogin())
            return;

        mParams = new HashMap<>();
        mParams.put("user_id", getUserId());
        mParams.put("topic_id", topicId);

        simpleRequest(collect ? COLLECT_CREATE : NO_COLLECT_CREATE, mParams, new IApiCallback() {
                    @Override
                    public void onStartApi() {
                        if (callback != null)
                            callback.onStartApi();
                    }

                    @Override
                    public void onComplete(Object obj) {
                        ApiCache.getInstance().setDisableCache(ApiCache.TOPIC_CACHE_TIME);
                        BaseEvent.TopicCollect topicCollect = new BaseEvent.TopicCollect();
                        topicCollect.topic_id = topicId;
                        topicCollect.status = collect;
                        if (callback != null) {
                            callback.onComplete(topicCollect);
                        }
                    }

                    @Override
                    public void onFailure(ApiErrorResult result) {
                        result.setDisplayType(DisplayType.Toast);
                        if (callback != null)
                            callback.onFailure(result);
                    }
                }
        );
    }
}
