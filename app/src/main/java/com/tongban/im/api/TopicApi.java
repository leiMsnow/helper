package com.tongban.im.api;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tongban.corelib.base.api.IApiCallback;
import com.tongban.corelib.model.ApiErrorResult;
import com.tongban.corelib.model.ApiListResult;
import com.tongban.corelib.model.ApiResult;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.im.App;
import com.tongban.im.api.base.BaseApi;
import com.tongban.im.common.Consts;
import com.tongban.im.common.TransferCenter;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.ImageUrl;
import com.tongban.im.model.discover.ProductBook;
import com.tongban.im.model.topic.Topic;
import com.tongban.im.model.topic.TopicComment;

import java.util.HashMap;
import java.util.List;

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
                            final IApiCallback callback) {


        if (!TransferCenter.getInstance().startLogin())
            return;

        mParams = new HashMap<>();
        mParams.put("user_id", SPUtils.get(mContext, Consts.USER_ID, ""));
        mParams.put("topic_title", title);
        mParams.put("topic_content", content);
        mParams.put("topic_img_url", JSON.toJSON(urls));

        simpleRequest(CREATE_TOPIC, mParams, new IApiCallback() {
            @Override
            public void onStartApi() {
                if (callback != null)
                    callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                setDisableCache(TOPIC_CACHE_TIME);
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
    public void recommendTopicList(int cursor, int pageSize, final IApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("user_id", SPUtils.get(mContext, Consts.USER_ID, ""));
        mParams.put("cursor", cursor < 0 ? 0 : cursor);
        mParams.put("page_size", pageSize < 1 ? 10 : pageSize);

        simpleRequest(RECOMMEND_TOPIC_LIST, mParams, new IApiCallback() {
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
        mParams.put("user_id", SPUtils.get(mContext, Consts.USER_ID, ""));
        mParams.put("cursor", cursor < 0 ? 0 : cursor);
        mParams.put("page_size", pageSize < 1 ? 10 : pageSize);

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
        mParams.put("user_id", SPUtils.get(mContext, Consts.USER_ID, ""));

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
    public void getTopicCommentList(String topicId, int cursor, int pageSize, final IApiCallback callback) {
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
                ApiListResult<TopicComment> result = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiListResult<TopicComment>>() {
                        });
                BaseEvent.TopicCommentListEvent topicCommentListEvent = new BaseEvent.TopicCommentListEvent();
                topicCommentListEvent.topicCommentList = (result.getData().getResult());
                if (callback != null)
                    callback.onComplete(topicCommentListEvent);

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
    public void createCommentForTopic(final String topicId, String commentContent,
                                      @Nullable String repliedCommentId,
                                      @Nullable String repliedName,
                                      @Nullable String repliedUserId,
                                      List<ImageUrl> commentImgUrl,
                                      final IApiCallback callback) {

        if (!TransferCenter.getInstance().startLogin())
            return;

        mParams = new HashMap<>();
        mParams.put("user_id", SPUtils.get(mContext, Consts.USER_ID, ""));
        mParams.put("nick_name", SPUtils.get(mContext, Consts.NICK_NAME, ""));
        mParams.put("topic_id", topicId);
        mParams.put("comment_content", commentContent);
        mParams.put("comment_img_url", JSON.toJSON(commentImgUrl));
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
                        setDisableCache(TOPIC_CACHE_TIME);
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
        mParams.put("user_id", SPUtils.get(mContext, Consts.USER_ID, ""));
        mParams.put("topic_id", topicId);

        simpleRequest(collect ? COLLECT_CREATE : NO_COLLECT_CREATE, mParams, new IApiCallback() {
                    @Override
                    public void onStartApi() {
                        if (callback != null)
                            callback.onStartApi();
                    }

                    @Override
                    public void onComplete(Object obj) {
                        setDisableCache(TOPIC_CACHE_TIME);
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
