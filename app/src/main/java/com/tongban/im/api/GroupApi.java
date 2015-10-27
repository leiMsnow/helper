package com.tongban.im.api;

import android.content.Context;
import android.support.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tongban.corelib.base.api.IApiCallback;
import com.tongban.corelib.model.ApiErrorResult;
import com.tongban.corelib.model.ApiListResult;
import com.tongban.corelib.model.ApiResult;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.im.App;
import com.tongban.im.api.base.ApiCache;
import com.tongban.im.api.base.BaseApi;
import com.tongban.im.common.Consts;
import com.tongban.im.common.ModelToTable;
import com.tongban.im.common.TransferCenter;
import com.tongban.im.db.helper.GroupDaoHelper;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.group.Group;
import com.tongban.im.model.group.GroupType;
import com.tongban.im.model.ImageUrl;
import com.tongban.im.model.user.User;

import java.util.HashMap;

import de.greenrobot.event.EventBus;

/**
 * 群组操作api
 * Created by zhangleilei on 15/7/15.
 */
public class GroupApi extends BaseApi {

    private static GroupApi mApi;
//---------------------------------------输入接口----------------------------------------------------

    /**
     * 创建圈子接口
     */
    public static final String CREATE_GROUP = "/group/create";
    /**
     * 加入圈子接口
     */
    public static final String JOIN_GROUP = "/user/join/group";
    /**
     * 用户退出圈子
     */
    public static final String USER_QUIT_GROUP = "/user/quit/group";

//---------------------------------------输出接口----------------------------------------------------
    /**
     * 圈子推荐接口
     */
    public static final String RECOMMEND_GROUP_LIST = "/group/recommend/list";
    /**
     * 圈子搜索接口
     */
    public static final String SEARCH_GROUP_LIST = "/group/search/list";
    /**
     * 圈子详情接口
     */
    public static final String GROUP_INFO = "/group/card";
    /**
     * 获取圈子成员列表接口
     */
    public static final String GROUP_MEMBERS_INFO = "/group/members/list";


    private GroupApi(Context context) {
        super(context);
    }

    public static GroupApi getInstance() {
        if (mApi == null) {
            synchronized (GroupApi.class) {
                if (mApi == null) {
                    mApi = new GroupApi(App.getInstance());
                }
            }
        }
        return mApi;
    }

    /**
     * 创建圈子
     * modified by chen
     *
     * @param groupName   圈子名字
     * @param groupType   圈子类型{@link GroupType}
     * @param longitude   经度
     * @param latitude    纬度
     * @param address     详细地址
     * @param birthday    出生日期(y-m-d H:i:s)
     * @param tags        群组的标签,多个用逗号分隔
     * @param declaration 圈子简介
     * @param groupAvatar 圈子头像
     * @param isSearch    (true-1:允许搜索;false-0：不允许搜索)
     * @param callback    回调
     */
    public void createGroup(final String groupName, int groupType, double longitude,
                            double latitude, final String address, @Nullable String birthday,
                            @Nullable String tags, @Nullable final String declaration,
                            ImageUrl groupAvatar, boolean isSearch,
                            final IApiCallback callback) {

        if (!TransferCenter.getInstance().startLogin())
            return;

        mParams = new HashMap<>();
        mParams.put("user_id", SPUtils.get(mContext, Consts.USER_ID, ""));
        mParams.put("group_name", groupName);
        //(默认0，1：学校)
        mParams.put("address_type", getTypeStr(groupType == GroupType.CLASSMATE));
        mParams.put("longitude", longitude);
        mParams.put("latitude", latitude);
        mParams.put("address", address);

        if (birthday != null)
            mParams.put("birthday", birthday);
        if (tags != null)
            mParams.put("tags", tags);
        if (declaration != null)
            mParams.put("declaration", declaration);

        if (groupAvatar == null) {
            mParams.put("group_avatar", "");
        } else {
            mParams.put("group_avatar", JSON.toJSONString(groupAvatar));
        }
        mParams.put("flag_allow_search", getTypeStr(isSearch));
        //此处接口名称根据groupType变化
        simpleRequest(CREATE_GROUP + "/" + groupType, mParams, new IApiCallback() {
            @Override
            public void onStartApi() {
                if (callback != null)
                    callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                ApiCache.getInstance().setDisableCache(ApiCache.GROUP_CACHE_TIME);
                ApiResult<Group> result = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiResult<Group>>() {
                        });
                BaseEvent.CreateGroupEvent createGroupEvent = new BaseEvent.CreateGroupEvent();
                createGroupEvent.groupId = (result.getData().toString());
                callback.onComplete(createGroupEvent);
            }

            @Override
            public void onFailure(ApiErrorResult result) {

                result.setDisplayType(DisplayType.Toast);
                callback.onFailure(result);
            }
        });
    }

    /**
     * 加入圈子
     *
     * @param groupId   圈子Id
     * @param groupName 圈子名称
     * @param masterId  圈子主Id
     * @param isVerify  是否需要验证 true需要 false 不需要
     * @param callback
     */
    public void joinGroup(String groupId, String groupName, String masterId, final boolean isVerify,
                          final IApiCallback callback) {


        if (!TransferCenter.getInstance().startLogin())
            return;

        mParams = new HashMap<>();
        mParams.put("group_id", groupId);
        mParams.put("group_name", groupName);
        mParams.put("user_id", SPUtils.get(mContext, Consts.USER_ID, ""));
        mParams.put("group_owner_id", masterId);

        simpleRequest(JOIN_GROUP, mParams, new IApiCallback() {
            @Override
            public void onStartApi() {

                if (callback != null)
                    callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                ApiCache.getInstance().setDisableCache(ApiCache.GROUP_CACHE_TIME);
                ApiResult<BaseEvent.JoinGroupEvent> result = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiResult<BaseEvent.JoinGroupEvent>>() {
                        });
                result.getData().is_verify = isVerify;
                if (callback != null)
                    callback.onComplete(result.getData());
                else
                    EventBus.getDefault().post(result.getData());
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
     * 获取圈子推荐列表
     *
     * @param cursor   第几页，默认0开始
     * @param pageSize 每页多少数据
     * @param callback
     */
    public void recommendGroupList(final int cursor, int pageSize, final IApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("longitude", SPUtils.get(mContext, Consts.LONGITUDE, Consts.DEFAULT_DOUBLE));
        mParams.put("latitude", SPUtils.get(mContext, Consts.LATITUDE, Consts.DEFAULT_DOUBLE));
        mParams.put("user_id", SPUtils.get(mContext, Consts.USER_ID, ""));
        mParams.put("cursor", cursor < 0 ? 0 : cursor);
        mParams.put("page_size", pageSize);

        simpleRequest(RECOMMEND_GROUP_LIST, mParams, new IApiCallback() {
            @Override
            public void onStartApi() {
                if (cursor == 0) {
                    if (callback != null)
                        callback.onStartApi();
                }
            }

            @Override
            public void onComplete(Object obj) {
                ApiListResult<Group> listResult = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiListResult<Group>>() {
                        });

                if (listResult.getData().getResult().size() > 0) {
                    BaseEvent.RecommendGroupListEvent listEvent = new BaseEvent.RecommendGroupListEvent();
                    listEvent.groupList = listResult.getData().getResult();
                    if (callback != null)
                        callback.onComplete(listEvent);
                } else {
                    onFailure(createEmptyResult(RECOMMEND_GROUP_LIST));
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
     * 搜索圈子
     *
     * @param keyword  关键字
     * @param cursor   第几页，默认0开始
     * @param pageSize 每页多少数据
     * @param callback
     */
    public void searchGroupList(String keyword, int cursor, int pageSize, final IApiCallback callback) {

        mParams = new HashMap<>();
        mParams.put("keyword", keyword);
        mParams.put("user_id", SPUtils.get(mContext, Consts.USER_ID, ""));
        mParams.put("cursor", cursor < 0 ? 0 : cursor);
        mParams.put("page_size", pageSize);
        mParams.put("longitude", SPUtils.get(mContext, Consts.LONGITUDE, Consts.DEFAULT_DOUBLE));
        mParams.put("latitude", SPUtils.get(mContext, Consts.LATITUDE, Consts.DEFAULT_DOUBLE));

        simpleRequest(SEARCH_GROUP_LIST, mParams, new IApiCallback() {
            @Override
            public void onStartApi() {
                if (callback != null)
                    callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                ApiListResult<Group> apiResponse = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiListResult<Group>>() {
                        });
                if (apiResponse.getData().getResult().size() > 0) {
                    BaseEvent.SearchGroupListEvent searchGroupEvent = new BaseEvent.SearchGroupListEvent();
                    searchGroupEvent.groups = (apiResponse.getData().getResult());
                    if (callback != null)
                        callback.onComplete(searchGroupEvent);
                } else {
                    onFailure(createEmptyResult(SEARCH_GROUP_LIST));
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
     * 获取圈子信息
     *
     * @param groupId  圈子Id
     * @param callback
     */
    public void getGroupInfo(String groupId, final IApiCallback callback) {

        mParams = new HashMap<>();
        mParams.put("user_id", SPUtils.get(mContext, Consts.USER_ID, ""));
        mParams.put("group_id", groupId);

        simpleRequest(GROUP_INFO, mParams, new IApiCallback() {
            @Override
            public void onStartApi() {
                if (callback != null)
                    callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                ApiResult<Group> result = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiResult<Group>>() {
                        });
                // 将圈子信息保存到本地数据库
                GroupDaoHelper.get(mContext).addData(ModelToTable.groupToTable(result.getData()));
                BaseEvent.GroupInfoEvent groupInfoEvent = new BaseEvent.GroupInfoEvent();
                groupInfoEvent.group = result.getData();
                if (callback != null)
                    callback.onComplete(groupInfoEvent);
            }

            @Override
            public void onFailure(ApiErrorResult result) {
                if (callback != null)
                    callback.onFailure(result);
            }
        });
    }

    /**
     * 获取圈子成员列表
     *
     * @param groupId  圈子Id
     * @param cursor   第几页，默认0开始
     * @param pageSize 每页多少数据
     * @param callback
     */
    public void getGroupMembersList(String groupId, int cursor, int pageSize, final IApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("group_id", groupId);
        mParams.put("cursor", cursor < 0 ? 0 : cursor);
        mParams.put("page_size", pageSize);

        simpleRequest(GROUP_MEMBERS_INFO, mParams, new IApiCallback() {
            @Override
            public void onStartApi() {
                if (callback != null)
                    callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {

                ApiListResult<User> result = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiListResult<User>>() {
                        });
                BaseEvent.GroupMemberEvent memberEvent = new BaseEvent.GroupMemberEvent();
                memberEvent.users = result.getData().getResult();
                if (callback != null)
                    callback.onComplete(memberEvent);
            }

            @Override
            public void onFailure(ApiErrorResult result) {
                if (callback != null)
                    callback.onFailure(result);
            }
        });
    }

    /**
     * 退出圈子
     *
     * @param groupId  圈子
     * @param callback
     */
    public void quitGroup(String groupId, final IApiCallback callback) {

        if (!TransferCenter.getInstance().startLogin())
            return;

        mParams = new HashMap<>();
        mParams.put("user_id", SPUtils.get(mContext, Consts.USER_ID, ""));
        mParams.put("group_id", groupId);

        simpleRequest(USER_QUIT_GROUP, mParams, new IApiCallback() {
            @Override
            public void onStartApi() {
                if (callback != null)
                    callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                ApiCache.getInstance().setDisableCache(ApiCache.GROUP_CACHE_TIME);
                if (callback != null)
                    callback.onComplete(new BaseEvent.QuitGroupEvent());
            }

            @Override
            public void onFailure(ApiErrorResult result) {
                result.setDisplayType(DisplayType.Toast);
                if (callback != null)
                    callback.onFailure(result);
            }
        });
    }

}
