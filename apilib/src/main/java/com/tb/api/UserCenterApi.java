package com.tb.api;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tb.api.base.ApiCache;
import com.tb.api.base.BaseApi;
import com.tb.api.model.BaseEvent;
import com.tb.api.model.discover.ProductBook;
import com.tb.api.model.discover.Theme;
import com.tb.api.model.group.Group;
import com.tb.api.model.topic.Comment;
import com.tb.api.model.topic.Topic;
import com.tb.api.model.user.AddChildInfo;
import com.tb.api.model.user.EditUser;
import com.tb.api.model.user.User;
import com.tongban.corelib.base.BaseApplication;
import com.tongban.corelib.base.api.IApiCallback;
import com.tongban.corelib.model.ApiErrorResult;
import com.tongban.corelib.model.ApiListResult;
import com.tongban.corelib.model.ApiResult;
import com.tongban.corelib.utils.Constants;
import com.tongban.corelib.utils.LogUtil;
import com.tongban.corelib.utils.SPUtils;

import java.util.HashMap;
import java.util.List;

/**
 * 用户中心api
 * Created by fushudi on 2015/8/19.
 */
public class UserCenterApi extends BaseApi {
    private static UserCenterApi mApi;
//---------------------------------------输入接口----------------------------------------------------
    /**
     * 关注用户
     */
    public static final String FOCUS_USER = "/user/focus/user";
    /**
     * 取消关注用户
     */
    public static final String CANCEL_FOCUS_USER = "/user/nofocus/user";
    /**
     * 设置宝宝信息
     */
    public static final String SET_CHILD_INFO = "/user/childinfo/update";
    /**
     * 用户信息修改
     */
    public static final String USER_UPDATE = "/user/update";

//---------------------------------------输出接口----------------------------------------------------

//---------------------------------------用户接口----------------------------------------------------

    /**
     * 获取个人资料
     */
    public static final String USER_INFO = "/user/info";
    /**
     * 获取我关注的人员列表
     */
    public static final String FETCH_FOCUS_USER_LIST = "/user/focus/user/list";
    /**
     * 获取用户个人中心数据
     */
    public static final String FETCH_PERSONAL_CENTER_INFO = "/user/center/info";
    /**
     * 获取用户（他人）资料
     */
    public static final String FETCH_USER_CENTER_INFO = "/user/card";
    /**
     * 获取我的粉丝人员列表
     */
    public static final String FETCH_FANS_USER_LIST = "/user/befocus/user/list";

//---------------------------------------专题/商品接口------------------------------------------------
    /**
     * 获取我的收藏 - 专题列表
     */
    public static final String FETCH_COLLECT_MULTIPLE_PRODUCT_LIST = "/user/collect/theme/list";
    /**
     * 获取我的收藏 - 单品列表
     */
    public static final String FETCH_SINGLE_PRODUCT_LIST = "/user/collect/product/list";

//---------------------------------------话题接口----------------------------------------------------
    /**
     * 获取我的收藏 - 话题列表
     */
    public static final String FETCH_COLLECT_TOPIC_LIST = "/user/collect/topic/list";
    /**
     * 获取我的话题 - 回复我的话题列表
     */
    public static final String FETCH_COLLECT_REPLY_TOPIC_LIST = "/user/bereply/comment/list";
    /**
     * 获取我的话题 - 我发起的话题列表
     */
    public static final String FETCH_LAUNCH_TOPIC_LIST = "/user/launch/topic/list";

//---------------------------------------圈子接口----------------------------------------------------
    /**
     * 获取个人群组列表-我创建/加入的群
     */
    public static final String FETCH_MY_GROUPS_LIST = "/user/groups/list";


    public UserCenterApi(Context context) {
        super(context);
    }

    public static UserCenterApi getInstance() {
        if (mApi == null) {
            synchronized (UserCenterApi.class) {
                if (mApi == null) {
                    mApi = new UserCenterApi(BaseApplication.getInstance());
                }
            }
        }
        return mApi;
    }

    /**
     * 获取用户个人中心数据
     *
     * @param callback
     */
    public void fetchPersonalCenterInfo(final IApiCallback callback) {

        mParams = new HashMap<>();
                mParams.put("user_id", getUserId());

        simpleRequest(FETCH_PERSONAL_CENTER_INFO, mParams, new IApiCallback() {
            @Override
            public void onStartApi() {
                callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                ApiResult<User> apiResponse = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiResult<User>>() {
                        });
                BaseEvent.PersonalCenterEvent personalCenterEvent = new BaseEvent.PersonalCenterEvent();
                personalCenterEvent.user = apiResponse.getData();
                callback.onComplete(personalCenterEvent);
            }

            @Override
            public void onFailure(ApiErrorResult result) {
                if (callback != null)
                    callback.onFailure(result);
            }
        });
    }

    /**
     * 获取用户(他人)资料
     *
     * @param visiterId 被关注者的ID
     * @param callback
     */
    public void fetchUserCenterInfo(String visiterId, final IApiCallback callback) {

        mParams = new HashMap<>();
        mParams.put("user_id", SPUtils.get(mContext, Constants.USER_ID, ""));
        mParams.put("visiter_id", visiterId);

        simpleRequest(FETCH_USER_CENTER_INFO, mParams, new IApiCallback() {
            @Override
            public void onStartApi() {
                if (callback != null)
                    callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {

                try {
                    ApiResult<User> apiResponse = JSON.parseObject(obj.toString(),
                            new TypeReference<ApiResult<User>>() {
                            });
                    BaseEvent.UserCenterEvent userCenterEvent = new BaseEvent.UserCenterEvent();
                    userCenterEvent.user = (apiResponse.getData());
                    if (callback != null)
                        callback.onComplete(userCenterEvent);
                } catch (Throwable throwable) {
                    LogUtil.e("fetchUserCenterInfo-throwable", throwable.toString());
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
     * 获取个人资料
     *
     * @param callback
     */
    public void fetchUserDetailInfo(final IApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("user_id", SPUtils.get(mContext, Constants.USER_ID, ""));
        simpleRequest(USER_INFO, mParams, new IApiCallback() {
            @Override
            public void onStartApi() {
                callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                ApiResult<User> apiResponse = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiResult<User>>() {
                        });
                BaseEvent.UserInfoEvent userInfoEvent = new BaseEvent.UserInfoEvent();
                userInfoEvent.user = apiResponse.getData();
                callback.onComplete(userInfoEvent);
            }

            @Override
            public void onFailure(ApiErrorResult result) {
                if (callback != null)
                    callback.onFailure(result);
            }
        });
    }


    /**
     * 获取个人中心我创建/加入的群组列表（圈子）
     *
     * @param cursor
     * @param pageSize
     * @param callback
     */
    public void fetchMyGroupsList(final int cursor, int pageSize, String userId, final IApiCallback callback) {

        mParams = new HashMap<>();
        mParams.put("user_id", userId);
        mParams.put("longitude", SPUtils.get(mContext, Constants.LONGITUDE, Constants.DEFAULT_DOUBLE));
        mParams.put("latitude", SPUtils.get(mContext, Constants.LATITUDE, Constants.DEFAULT_DOUBLE));
        mParams.put("cursor", cursor < 0 ? 0 : cursor);
        mParams.put("page_size", pageSize < 1 ? 10 : pageSize);

        simpleRequest(FETCH_MY_GROUPS_LIST, mParams, new IApiCallback() {
            @Override
            public void onStartApi() {
                if (cursor == 0) {
                    if (callback != null)
                        callback.onStartApi();
                }
            }

            @Override
            public void onComplete(Object obj) {
                ApiListResult<Group> apiResponse = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiListResult<Group>>() {
                        });
                BaseEvent.MyGroupListEvent myGroupListEvent = new BaseEvent.MyGroupListEvent();
                myGroupListEvent.myGroupList = (apiResponse.getData().getResult());
                if (callback != null)
                    callback.onComplete(myGroupListEvent);
            }

            @Override
            public void onFailure(ApiErrorResult result) {
                if (callback != null)
                    callback.onFailure(result);
            }
        });
    }


    /**
     * 获取我关注的人员列表
     *
     * @param cursor
     * @param pageSize
     * @param callback
     */
    public void fetchFocusUserList(final int cursor, int pageSize, String userId, final IApiCallback callback) {

        mParams = new HashMap<>();
        mParams.put("user_id", TextUtils.isEmpty(userId) ?
                SPUtils.get(mContext, Constants.USER_ID, "") : userId);
        mParams.put("cursor", cursor < 0 ? 0 : cursor);
        mParams.put("page_size", pageSize < 1 ? 10 : pageSize);

        simpleRequest(FETCH_FOCUS_USER_LIST, mParams, new IApiCallback() {
            @Override
            public void onStartApi() {
                if (cursor == 0) {
                    if (callback != null)
                        callback.onStartApi();
                }
            }

            @Override
            public void onComplete(Object obj) {
                ApiListResult<User> apiResponse = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiListResult<User>>() {
                        });
                BaseEvent.MyFollowListEvent myFollowListEvent = new BaseEvent.MyFollowListEvent();
                myFollowListEvent.myFollowList = (apiResponse.getData().getResult());
                callback.onComplete(myFollowListEvent);
            }

            @Override
            public void onFailure(ApiErrorResult result) {
                if (callback != null)
                    callback.onFailure(result);
            }
        });
    }

    /**
     * 获取我的粉丝人员列表
     *
     * @param cursor
     * @param pageSize
     * @param callback
     */
    public void fetchFansUserList(final int cursor, int pageSize, String userId, final IApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("user_id", TextUtils.isEmpty(userId) ?
                SPUtils.get(mContext, Constants.USER_ID, "") : userId);
        mParams.put("cursor", cursor < 0 ? 0 : cursor);
        mParams.put("page_size", pageSize < 1 ? 10 : pageSize);

        simpleRequest(FETCH_FANS_USER_LIST, mParams, new IApiCallback() {
            @Override
            public void onStartApi() {
                if (cursor == 0) {
                    if (callback != null)
                        callback.onStartApi();
                }
            }

            @Override
            public void onComplete(Object obj) {
                ApiListResult<User> apiResponse = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiListResult<User>>() {
                        });
                BaseEvent.MyFansListEvent myFansListEvent = new BaseEvent.MyFansListEvent();
                myFansListEvent.myFansList = (apiResponse.getData().getResult());
                callback.onComplete(myFansListEvent);
            }

            @Override
            public void onFailure(ApiErrorResult result) {
                if (callback != null)
                    callback.onFailure(result);
            }
        });
    }

    /**
     * 获取我的收藏 - 单品列表
     *
     * @param callback
     */
    public void fetchCollectedProductList(final int cursor, int pageSize, final IApiCallback callback) {

        mParams = new HashMap<>();
        mParams.put("cursor", cursor < 0 ? 0 : cursor);
        mParams.put("page_size", pageSize < 1 ? 10 : pageSize);
        mParams.put("user_id", SPUtils.get(mContext, Constants.USER_ID, ""));

        simpleRequest(FETCH_SINGLE_PRODUCT_LIST, mParams, new IApiCallback() {
            @Override
            public void onStartApi() {
                if (cursor == 0) {
                    if (callback != null)
                        callback.onStartApi();
                }
            }

            @Override
            public void onComplete(Object obj) {
                ApiListResult<ProductBook> apiResponse = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiListResult<ProductBook>>() {
                        });
                BaseEvent.FetchCollectedProductEvent collectSingleProductEvent =
                        new BaseEvent.FetchCollectedProductEvent();
                collectSingleProductEvent.productBookList = (apiResponse.getData().getResult());
                callback.onComplete(collectSingleProductEvent);
            }

            @Override
            public void onFailure(ApiErrorResult result) {
                if (callback != null)
                    callback.onFailure(result);
            }
        });
    }

    /**
     * 获取我的收藏 - 话题列表
     *
     * @param cursor
     * @param pageSize
     * @param callback
     */
    public void fetchCollectTopicList(final int cursor, int pageSize, final IApiCallback callback) {

        mParams = new HashMap<>();
        mParams.put("user_id", SPUtils.get(mContext, Constants.USER_ID, ""));
        mParams.put("cursor", cursor < 0 ? 0 : cursor);
        mParams.put("page_size", pageSize < 1 ? 10 : pageSize);

        simpleRequest(FETCH_COLLECT_TOPIC_LIST, mParams, new IApiCallback() {
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
                BaseEvent.TopicListEvent topicListEvent = new BaseEvent.TopicListEvent();
                topicListEvent.topicList = (result.getData().getResult());
                if (callback != null)
                    callback.onComplete(topicListEvent);
            }

            @Override
            public void onFailure(ApiErrorResult result) {
                if (callback != null)
                    callback.onFailure(result);
            }
        });
    }

    /**
     * 获取我的收藏 - 专题列表
     *
     * @param cursor
     * @param pageSize
     * @param callback
     */
    public void fetchCollectMultipleTopicList(final int cursor, int pageSize, final IApiCallback callback) {

        mParams = new HashMap<>();
        mParams.put("user_id", SPUtils.get(mContext, Constants.USER_ID, ""));
        mParams.put("cursor", cursor < 0 ? 0 : cursor);
        mParams.put("page_size", pageSize < 1 ? 10 : pageSize);

        simpleRequest(FETCH_COLLECT_MULTIPLE_PRODUCT_LIST, mParams, new IApiCallback() {
            @Override
            public void onStartApi() {
                if (cursor == 0) {
                    if (callback != null)
                        callback.onStartApi();
                }
            }

            @Override
            public void onComplete(Object obj) {
                ApiListResult<Theme> result = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiListResult<Theme>>() {
                        });
                BaseEvent.FetchCollectedThemeEvent collectMultiProductEvent = new BaseEvent.FetchCollectedThemeEvent();
                collectMultiProductEvent.mThemeList = (result.getData().getResult());
                if (callback != null)
                    callback.onComplete(collectMultiProductEvent);
            }

            @Override
            public void onFailure(ApiErrorResult result) {
                if (callback != null)
                    callback.onFailure(result);
            }
        });
    }

    /**
     * 获取我的话题 - 回复我的话题列表
     *
     * @param cursor
     * @param pageSize
     * @param callback
     */
    public void fetchReplyTopicList(final int cursor, int pageSize, final IApiCallback callback) {

        mParams = new HashMap<>();
        mParams.put("user_id", SPUtils.get(mContext, Constants.USER_ID, ""));
        mParams.put("cursor", cursor < 0 ? 0 : cursor);
        mParams.put("page_size", pageSize < 1 ? 10 : pageSize);

        simpleRequest(FETCH_COLLECT_REPLY_TOPIC_LIST, mParams, new IApiCallback() {
            @Override
            public void onStartApi() {
                if (cursor == 0) {
                    if (callback != null)
                        callback.onStartApi();
                }
            }

            @Override
            public void onComplete(Object obj) {
                ApiListResult<Comment> apiResponse = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiListResult<Comment>>() {
                        });
                BaseEvent.CommentTopicListEvent commentTopicListEvent = new BaseEvent.CommentTopicListEvent();
                commentTopicListEvent.commentTopicList = (apiResponse.getData().getResult());
                callback.onComplete(commentTopicListEvent);
            }

            @Override
            public void onFailure(ApiErrorResult result) {
                if (callback != null)
                    callback.onFailure(result);
            }
        });
    }

    /**
     * 获取我的话题 - 我发起的话题列表
     *
     * @param cursor
     * @param pageSize
     * @param callback
     */
    public void fetchLaunchTopicList(final int cursor, int pageSize, final IApiCallback callback) {

        mParams = new HashMap<>();
        mParams.put("user_id", SPUtils.get(mContext, Constants.USER_ID, ""));
        mParams.put("cursor", cursor < 0 ? 0 : cursor);
        mParams.put("page_size", pageSize < 1 ? 10 : pageSize);

        simpleRequest(FETCH_LAUNCH_TOPIC_LIST, mParams, new IApiCallback() {
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
                    BaseEvent.TopicListEvent topicListEvent = new BaseEvent.TopicListEvent();
                    topicListEvent.topicList = (result.getData().getResult());
                    callback.onComplete(topicListEvent);
                } else {
                    onFailure(createEmptyResult(FETCH_LAUNCH_TOPIC_LIST));
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
     * 关注/取消关注用户
     *
     * @param isFocus     true:关注用户;false:取消关注
     * @param focusUserId 被关注的Id
     * @param callback
     */
    public void focusUser(final boolean isFocus, final String focusUserId, final IApiCallback callback) {

        mParams = new HashMap<>();
        mParams.put("be_focused_user_id", focusUserId);
        mParams.put("user_id", SPUtils.get(mContext, Constants.USER_ID, ""));


        simpleRequest(isFocus ? FOCUS_USER : CANCEL_FOCUS_USER, mParams, new IApiCallback() {
            @Override
            public void onStartApi() {
                if (callback != null)
                    callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                ApiCache.getInstance().setDisableCache(ApiCache.USER_CACHE_TIME);
                BaseEvent.FocusEvent focusEvent = new BaseEvent.FocusEvent();
                focusEvent.isFocus = isFocus;
                focusEvent.userIds = focusUserId;
                callback.onComplete(focusEvent);
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
     * 设置宝宝信息
     *
     * @param userId   用户ID
     * @param children
     * @param callback
     */
    public void setChildInfo(String userId, List<AddChildInfo> children, final IApiCallback callback) {

        mParams = new HashMap<>();
        mParams.put("user_id", userId);
        mParams.put("child_info", JSON.toJSONString(children));

        simpleRequest(SET_CHILD_INFO, mParams, new IApiCallback() {
            @Override
            public void onStartApi() {
                if (callback != null)
                    callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                ApiCache.getInstance().setDisableCache(ApiCache.USER_CACHE_TIME);
                BaseEvent.ChildCreateSuccessEvent childSuccess =
                        new BaseEvent.ChildCreateSuccessEvent();
                childSuccess.isSetSuccess = true;
                if (callback != null)
                    callback.onComplete(childSuccess);
            }

            @Override
            public void onFailure(ApiErrorResult result) {
                BaseEvent.ChildCreateSuccessEvent childSuccess =
                        new BaseEvent.ChildCreateSuccessEvent();
                childSuccess.isSetSuccess = false;
                if (callback != null)
                    callback.onComplete(childSuccess);
            }
        });
    }

    /**
     * 设置用户信息
     *
     * @param userInfo 用户信息
     * @param callback
     */
    public void updateUserInfo(final EditUser userInfo, final IApiCallback callback) {

        mParams = new HashMap<>();
        mParams.put("user_id", SPUtils.get(mContext, Constants.USER_ID, "").toString());
        if (userInfo.getNick_name() != null) {
            String nickName = userInfo.getNick_name();
//            try {
//                nickName = new String(userInfo.getNick_name().getBytes("GBK"), "UTF-8");
//            } catch (UnsupportedEncodingException e) {
//                nickName = "";
//            }
            mParams.put("nick_name", nickName);
        }
        if (userInfo.getPortrait_url() != null)
            mParams.put("portrait_url", JSON.toJSONString(userInfo.getPortrait_url()));

        SPUtils.put(mContext, Constants.NICK_NAME, userInfo.getNick_name());

        simpleRequest(USER_UPDATE, mParams, new IApiCallback() {
            @Override
            public void onStartApi() {
                if (callback != null)
                    callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                ApiCache.getInstance().setDisableCache(ApiCache.USER_CACHE_TIME);
                if (callback != null)
                    callback.onComplete(new BaseEvent.EditUserEvent());
            }

            @Override
            public void onFailure(ApiErrorResult result) {
                if (callback != null)
                    callback.onComplete(new BaseEvent.EditUserEvent());
            }
        });
    }

}
