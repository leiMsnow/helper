package com.tongban.im.api;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tongban.corelib.base.api.ApiCallback;
import com.tongban.corelib.model.ApiListResult;
import com.tongban.corelib.model.ApiResult;
import com.tongban.corelib.utils.LogUtil;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.im.App;
import com.tongban.im.common.Consts;
import com.tongban.im.common.ModelToTable;
import com.tongban.im.db.helper.UserDaoHelper;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.Group;
import com.tongban.im.model.Theme;
import com.tongban.im.model.ProductBook;
import com.tongban.im.model.Topic;
import com.tongban.im.model.TopicComment;
import com.tongban.im.model.User;

import java.util.HashMap;

/**
 * 用户中心api
 * Created by fushudi on 2015/8/19.
 */
public class UserCenterApi extends BaseApi {
    private static UserCenterApi mApi;
    /**
     * 获取用户个人中心数据
     */
    public static final String FETCH_PERSONAL_CENTER_INFO = "user/center/info";
    /**
     * 获取个人资料
     */
    public static final String FETCH_USER_DETAIL_INFO = "user/info";
    /**
     * 获取个人群组列表-创建的群
     */
    public static final String FETCH_MY_GROUP_LIST = "user/join/group/list";
    /**
     * 获取我关注的人员列表
     */
    public static final String FETCH_FOCUS_USER_LIST = "user/focus/user/list";
    /**
     * 获取我的粉丝人员列表
     */
    public static final String FETCH_FANS_USER_LIST = "user/befocus/user/list";

    /**
     * 获取我的收藏 - 单品列表
     */
    public static final String FETCH_SINGLE_PRODUCT_LIST = "user/collect/product/list";
    /**
     * 获取我的收藏 - 话题列表
     */
    public static final String FETCH_COLLECT_TOPIC_LIST = "user/collect/topic/list";
    /**
     * 获取我的收藏 - 专题列表
     */
    public static final String FETCH_COLLECT_MULTIPLE_PRODUCT_LIST = "user/collect/theme/list";
    /**
     * 获取我的话题 - 回复我的话题列表
     */
    public static final String FETCH_COLLECT_REPLY_TOPIC_LIST = "user/bereply/comment/list";

    /**
     * 获取我的话题 - 我发起的话题列表
     */
    public static final String FETCH_LAUNCH_TOPIC_LIST = "user/launch/topic/list";
    /**
     * 关注用户
     */
    public static final String FOCUS_USER = "user/focus/user";
    /**
     * 取消关注用户
     */
    public static final String CANCEL_FOCUS_USER = "user/nofocus/user";

    /**
     * 获取用户（他人）资料
     */
    public static final String FETCH_USER_CENTER_INFO = "user/card";

    public UserCenterApi(Context context) {
        super(context);
    }

    public static UserCenterApi getInstance() {
        if (mApi == null) {
            synchronized (UserCenterApi.class) {
                if (mApi == null) {
                    mApi = new UserCenterApi(App.getInstance());
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
    public void fetchPersonalCenterInfo(final ApiCallback callback) {

        mParams = new HashMap<>();
        mParams.put("user_id", SPUtils.get(mContext, Consts.USER_ID, ""));

        simpleRequest(FETCH_PERSONAL_CENTER_INFO, mParams, new ApiCallback() {
            @Override
            public void onStartApi() {
                callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                ApiResult<User> apiResponse = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiResult<User>>() {
                        });
                BaseEvent.PersonalCenterEvent personalCenterEvent=new BaseEvent.PersonalCenterEvent();
                personalCenterEvent.user=apiResponse.getData();
                callback.onComplete(personalCenterEvent);
            }

            @Override
            public void onFailure(DisplayType displayType, Object errorMessage) {
                callback.onFailure(displayType, errorMessage);
            }
        });
    }

    /**
     * 获取用户(他人)资料
     *
     * @param visiterId 被关注者的ID
     * @param callback
     */
    public void fetchUserCenterInfo(String visiterId, final ApiCallback callback) {

        mParams = new HashMap<>();
        mParams.put("user_id", SPUtils.get(mContext, Consts.USER_ID, ""));
        mParams.put("visiter_id", visiterId);

        simpleRequest(FETCH_USER_CENTER_INFO, mParams, new ApiCallback() {
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
                    // 将用户信息保存到本地数据库
                    UserDaoHelper.get(mContext).addData(ModelToTable.userToTable(apiResponse.getData()));
                    BaseEvent.UserCenterEvent userCenterEvent = new BaseEvent.UserCenterEvent();
                    userCenterEvent.user = (apiResponse.getData());
                    if (callback != null)
                        callback.onComplete(userCenterEvent);
                } catch (Throwable throwable) {
                    LogUtil.e("fetchUserCenterInfo-throwable", throwable.toString());
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
     * 获取个人资料
     *
     * @param callback
     */
    public void fetchUserDetailInfo(final ApiCallback callback) {

        mParams = new HashMap<>();
        mParams.put("user_id", SPUtils.get(mContext, Consts.USER_ID, ""));
        //TODO缺少参数  参数不对应
        simpleRequest(FETCH_USER_DETAIL_INFO, mParams, new ApiCallback() {
            @Override
            public void onStartApi() {
                callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                ApiResult<User> apiResponse = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiResult<User>>() {
                        });
                BaseEvent.UserInfoEvent userInfoEvent=new BaseEvent.UserInfoEvent();
                userInfoEvent.user=apiResponse.getData();
                callback.onComplete(userInfoEvent);
            }

            @Override
            public void onFailure(DisplayType displayType, Object errorMessage) {
                callback.onFailure(displayType, errorMessage);
            }
        });
    }

    /**
     * 获取个人中心群组列表（圈子）
     *
     * @param cursor
     * @param pageSize
     * @param callback
     */
    public void fetchMyGroupList(int cursor, int pageSize, String userId, final ApiCallback callback) {

        mParams = new HashMap<>();
        mParams.put("user_id", userId);
        mParams.put("longitude", SPUtils.get(mContext, Consts.LONGITUDE, -1.0D));
        mParams.put("latitude", SPUtils.get(mContext, Consts.LATITUDE, -1.0D));
        mParams.put("cursor", cursor < 0 ? 0 : cursor);
        mParams.put("page_size", pageSize < 1 ? 10 : pageSize);

        simpleRequest(FETCH_MY_GROUP_LIST, mParams, new ApiCallback() {
            @Override
            public void onStartApi() {
                callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                ApiListResult<Group> apiResponse = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiListResult<Group>>() {
                        });
                BaseEvent.MyGroupListEvent myGroupListEvent = new BaseEvent.MyGroupListEvent();
                myGroupListEvent.myGroupList = (apiResponse.getData().getResult());
                callback.onComplete(myGroupListEvent);
            }

            @Override
            public void onFailure(DisplayType displayType, Object errorMessage) {
                callback.onFailure(displayType, errorMessage);
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
    public void fetchFocusUserList(int cursor, int pageSize, String userId, final ApiCallback callback) {

        mParams = new HashMap<>();
        mParams.put("user_id", TextUtils.isEmpty(userId) ?
                SPUtils.get(mContext, Consts.USER_ID, "") : userId);
        mParams.put("cursor", cursor < 0 ? 0 : cursor);
        mParams.put("page_size", pageSize < 1 ? 10 : pageSize);

        // TODO: 2015/8/19 接口缺少测试数据
        simpleRequest(FETCH_FOCUS_USER_LIST, mParams, new ApiCallback() {
            @Override
            public void onStartApi() {
                callback.onStartApi();
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
            public void onFailure(DisplayType displayType, Object errorMessage) {
                callback.onFailure(displayType, errorMessage);
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
    public void fetchFansUserList(int cursor, int pageSize, String userId, final ApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("user_id", TextUtils.isEmpty(userId) ? SPUtils.get(mContext, Consts.USER_ID, "") :
                userId);
        mParams.put("cursor", cursor < 0 ? 0 : cursor);
        mParams.put("page_size", pageSize < 1 ? 10 : pageSize);

        simpleRequest(FETCH_FANS_USER_LIST, mParams, new ApiCallback() {
            @Override
            public void onStartApi() {
                callback.onStartApi();
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
            public void onFailure(DisplayType displayType, Object errorMessage) {
                callback.onFailure(displayType, errorMessage);
            }
        });
    }

    /**
     * 获取我的收藏 - 单品列表
     *
     * @param callback
     */
    public void fetchCollectedProductList(final ApiCallback callback) {

        mParams = new HashMap<>();
        mParams.put("user_id", SPUtils.get(mContext, Consts.USER_ID, ""));

        simpleRequest(FETCH_SINGLE_PRODUCT_LIST, mParams, new ApiCallback() {
            @Override
            public void onStartApi() {
                callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                ApiListResult<ProductBook> apiResponse = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiListResult<ProductBook>>() {
                        });
                BaseEvent.FetchCollectedProductEvent collectSingleProductEvent = new BaseEvent.FetchCollectedProductEvent();
                collectSingleProductEvent.setProductBookList(apiResponse.getData().getResult());
                callback.onComplete(collectSingleProductEvent);
            }

            @Override
            public void onFailure(DisplayType displayType, Object errorMessage) {
                callback.onFailure(displayType, errorMessage);
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
    public void fetchCollectTopicList(int cursor, int pageSize, final ApiCallback callback) {

        mParams = new HashMap<>();
        mParams.put("user_id", SPUtils.get(mContext, Consts.USER_ID, ""));
        mParams.put("cursor", cursor < 0 ? 0 : cursor);
        mParams.put("page_size", pageSize < 1 ? 10 : pageSize);

        simpleRequest(FETCH_COLLECT_TOPIC_LIST, mParams, new ApiCallback() {
            @Override
            public void onStartApi() {
                callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                // // TODO: 2015/8/22 数据格式错误
                ApiListResult<Topic> result = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiListResult<Topic>>() {
                        });
                BaseEvent.TopicListEvent topicListEvent = new BaseEvent.TopicListEvent();
                topicListEvent.topicList = (result.getData().getResult());
                if (callback != null)
                    callback.onComplete(topicListEvent);
            }

            @Override
            public void onFailure(DisplayType displayType, Object errorMessage) {
                callback.onFailure(displayType, errorMessage);
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
    public void fetchCollectMultipleTopicList(int cursor, int pageSize, final ApiCallback callback) {

        mParams = new HashMap<>();
        mParams.put("user_id", SPUtils.get(mContext, Consts.USER_ID, ""));
        mParams.put("cursor", cursor < 0 ? 0 : cursor);
        mParams.put("page_size", pageSize < 1 ? 10 : pageSize);

        simpleRequest(FETCH_COLLECT_MULTIPLE_PRODUCT_LIST, mParams, new ApiCallback() {
            @Override
            public void onStartApi() {
                callback.onStartApi();
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
            public void onFailure(DisplayType displayType, Object errorMessage) {
                callback.onFailure(displayType, errorMessage);
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
    public void fetchReplyTopicList(int cursor, int pageSize, final ApiCallback callback) {

        mParams = new HashMap<>();
        mParams.put("user_id", SPUtils.get(mContext, Consts.USER_ID, ""));
        mParams.put("cursor", cursor < 0 ? 0 : cursor);
        mParams.put("page_size", pageSize < 1 ? 10 : pageSize);

        simpleRequest(FETCH_COLLECT_REPLY_TOPIC_LIST, mParams, new ApiCallback() {
            @Override
            public void onStartApi() {
                callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                ApiListResult<TopicComment> apiResponse = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiListResult<TopicComment>>() {
                        });
                BaseEvent.CommentTopicListEvent commentTopicListEvent = new BaseEvent.CommentTopicListEvent();
                commentTopicListEvent.commentTopicList = (apiResponse.getData().getResult());
                callback.onComplete(commentTopicListEvent);
            }

            @Override
            public void onFailure(DisplayType displayType, Object errorMessage) {
                callback.onFailure(displayType, errorMessage);
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
    public void fetchLaunchTopicList(int cursor, int pageSize, final ApiCallback callback) {

        mParams = new HashMap<>();
        mParams.put("user_id", SPUtils.get(mContext, Consts.USER_ID, ""));
        mParams.put("cursor", cursor < 0 ? 0 : cursor);
        mParams.put("page_size", pageSize < 1 ? 10 : pageSize);

        simpleRequest(FETCH_LAUNCH_TOPIC_LIST, mParams, new ApiCallback() {
            @Override
            public void onStartApi() {
                callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {

                ApiListResult<Topic> result = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiListResult<Topic>>() {
                        });
                BaseEvent.TopicListEvent topicListEvent = new BaseEvent.TopicListEvent();
                topicListEvent.topicList = (result.getData().getResult());
                callback.onComplete(topicListEvent);

            }

            @Override
            public void onFailure(DisplayType displayType, Object errorMessage) {
                callback.onFailure(displayType, errorMessage);
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
    public void focusUser(final boolean isFocus, final String[] focusUserId, final ApiCallback callback) {

        mParams = new HashMap<>();
        mParams.put("be_focused_user_id", focusUserId);
        mParams.put("user_id", SPUtils.get(mContext, Consts.USER_ID, ""));


        simpleRequest(isFocus ? FOCUS_USER : CANCEL_FOCUS_USER, mParams, new ApiCallback() {
            @Override
            public void onStartApi() {
                callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {

                BaseEvent.FocusEvent focusEvent = new BaseEvent.FocusEvent();
                focusEvent.isFocus = isFocus;
                focusEvent.userIds = focusUserId;
                callback.onComplete(focusEvent);
            }

            @Override
            public void onFailure(DisplayType displayType, Object errorMessage) {
                callback.onFailure(displayType, errorMessage);
            }
        });
    }

}
