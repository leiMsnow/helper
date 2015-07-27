package com.tongban.im.api;

import android.content.Context;
import android.support.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tongban.corelib.base.api.ApiCallback;
import com.tongban.corelib.model.ApiResult;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.im.App;
import com.tongban.im.common.Consts;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.Group;
import com.tongban.im.model.User;

import java.util.HashMap;
import java.util.List;

/**
 * 群组操作api
 * Created by zhangleilei on 15/7/15.
 */
public class GroupApi extends BaseApi {

    private static GroupApi mApi;

    /**
     * 创建群组
     */
    public static final String CREATE_GROUP = "im/group/create";
    /**
     * 加入群组
     */
    public static final String JOIN_GROUP = "im/group/join";

    /**
     * 获取个人群组列表-创建的群
     */
    public static final String FETCH_MY_CREATE_GROUP_LIST = "im/group/fetch/1";

    /**
     * 根据群组名获取搜索群组
     */
    public static final String SEARCH_GROUP_BY_NAME = "im/group/fetch/2";

    /**
     * 根据群组类型获取搜索群组
     */
    public static final String SEARCH_GROUP_BY_TYPE = "im/group/fetch/3";

    /**
     * 获取用户加入的群组-全部的群
     */
    public static final String FETCH_MY_All_GROUP_LIST = "im/group/fetch/4";
    /**
     * 获取群组详情
     */
    public static final String FETCH_MY_JOIN_GROUP_INFO = "im/group/info";
    /**
     * 获取群组成员信息
     */
    public static final String FETCH_MY_JOIN_GROUP_MEMBER = "im/group/members";


    private GroupApi(Context context) {
        super(context);
    }

    public static GroupApi getInstance() {
        if (mApi == null) {
            synchronized (UserApi.class) {
                if (mApi == null) {
                    mApi = new GroupApi(App.getInstance());
                }
            }
        }
        return mApi;
    }


    /**
     * 创建群组
     *
     * @param groupName 群组名字
     * @param callback  回调
     */
    public void createGroup(@Nullable String groupName, int groupType, final ApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("user_id", SPUtils.get(mContext, Consts.USER_ID, ""));
        if (groupName != null)
            mParams.put("group_name", groupName);
        mParams.put("group_type", String.valueOf(groupType));

        simpleRequest(CREATE_GROUP, mParams, new ApiCallback() {
            @Override
            public void onStartApi() {
                callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                ApiResult<Group> result = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiResult<Group>>() {
                        });
                Group group = result.getData();
                callback.onComplete(group);
            }

            @Override
            public void onFailure(DisplayType displayType, Object errorMessage) {
                callback.onFailure(displayType, errorMessage);
            }
        });
    }

    /**
     * 加入群组
     *
     * @param groupId  群组ID
     * @param masterId 群主ID
     * @param callback 结果回调
     */
    public void joinGroup(String groupId, String masterId, final ApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("group_id", groupId);
        mParams.put("user_id", SPUtils.get(mContext, Consts.USER_ID, ""));
        mParams.put("group_owner_id", masterId);

        simpleRequest(JOIN_GROUP, mParams, new ApiCallback() {
            @Override
            public void onStartApi() {
                callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                BaseEvent.JoinGroupEvent joinGroupEvent = new BaseEvent.JoinGroupEvent();
                joinGroupEvent.setMessage("加入成功");
                callback.onComplete(joinGroupEvent);
            }

            @Override
            public void onFailure(DisplayType displayType, Object errorMessage) {
                callback.onFailure(displayType, errorMessage);
            }
        });
    }

    /**
     * 获取个人群组列表-创建的群
     *
     * @param callback
     */
    public void fetchMyCreateGroupList(final ApiCallback callback) {

        mParams = new HashMap<>();
        mParams.put("user_id", SPUtils.get(mContext, Consts.USER_ID, ""));

        simpleRequest(FETCH_MY_CREATE_GROUP_LIST, mParams, new ApiCallback() {
            @Override
            public void onStartApi() {
                callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                ApiResult<List<Group>> apiResponse = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiResult<List<Group>>>() {
                        });
                List<Group> groups = apiResponse.getData();
                callback.onComplete(groups);
            }

            @Override
            public void onFailure(DisplayType displayType, Object errorMessage) {
                callback.onFailure(displayType, errorMessage);
            }
        });
    }


    /**
     * 根据群组名获取搜索群组
     *
     * @param groupName 群名称
     * @param cursor    第几页，默认0开始
     * @param pageSize  每页数量 默认10条
     * @param callback
     */
    public void searchGroupByName(String groupName, int cursor, int pageSize, final ApiCallback callback) {

        mParams = new HashMap<>();
        mParams.put("user_id", SPUtils.get(mContext, Consts.USER_ID, ""));
        mParams.put("group_name", groupName);
        mParams.put("cursor", cursor < 0 ? 0 : cursor);
        mParams.put("page_size", pageSize < 10 ? 10 : pageSize);

        simpleRequest(SEARCH_GROUP_BY_NAME, mParams, new ApiCallback() {
            @Override
            public void onStartApi() {
                callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                ApiResult<List<Group>> apiResponse = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiResult<List<Group>>>() {
                        });
                BaseEvent.SearchGroupEvent searchGroupEvent = new BaseEvent.SearchGroupEvent();
                searchGroupEvent.setGroups(apiResponse.getData());
                callback.onComplete(searchGroupEvent);
            }

            @Override
            public void onFailure(DisplayType displayType, Object errorMessage) {
                callback.onFailure(displayType, errorMessage);
            }
        });
    }

    /**
     * 根据群组类型获取搜索群组
     *
     * @param groupType 群组类型,注意：这里使用GroupType类的常量
     * @param cursor    第几页，默认0开始
     * @param pageSize  每页数量 默认10条
     * @param callback
     */
    public void searchGroupByType(int groupType, int cursor, int pageSize, final ApiCallback callback) {

        mParams = new HashMap<>();
        mParams.put("user_id", SPUtils.get(mContext, Consts.USER_ID, ""));
        mParams.put("group_type", groupType);
        mParams.put("cursor", cursor < 0 ? 0 : cursor);
        mParams.put("page_size", pageSize < 10 ? 10 : pageSize);

        simpleRequest(SEARCH_GROUP_BY_TYPE, mParams, new ApiCallback() {
            @Override
            public void onStartApi() {
                callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                ApiResult<List<Group>> apiResponse = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiResult<List<Group>>>() {
                        });
                BaseEvent.SearchGroupEvent searchGroupEvent = new BaseEvent.SearchGroupEvent();
                searchGroupEvent.setGroups(apiResponse.getData());
                callback.onComplete(searchGroupEvent);
            }

            @Override
            public void onFailure(DisplayType displayType, Object errorMessage) {
                callback.onFailure(displayType, errorMessage);
            }
        });
    }

    /**
     * 获取圈子列表数据-全部的群
     *
     * @param callback 回调
     */
    public void fetchMyAllGroupList(final ApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("user_id", SPUtils.get(mContext, Consts.USER_ID, ""));

        simpleRequest(FETCH_MY_All_GROUP_LIST, mParams, new ApiCallback() {
            @Override
            public void onStartApi() {
                callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                ApiResult<List<Group>> apiResponse = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiResult<List<Group>>>() {
                        });
                List<Group> groups = apiResponse.getData();
                callback.onComplete(groups);
            }

            @Override
            public void onFailure(DisplayType displayType, Object errorMessage) {
                callback.onFailure(displayType, errorMessage);
            }
        });
    }

    /**
     * 获取群组详情
     * member_amount-获取数量，默认15条用户信息
     *
     * @param callback
     */
    public void fetchMyGroupInfo(final ApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("user_id", SPUtils.get(mContext, Consts.USER_ID, ""));
        mParams.put("member_amount", 15);

        simpleRequest(FETCH_MY_JOIN_GROUP_INFO, mParams, new ApiCallback() {
            @Override
            public void onStartApi() {
                callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                ApiResult<Group> apiResponse = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiResult<Group>>() {
                        });
                BaseEvent.GroupInfoEvent groupInfoEvent = new BaseEvent.GroupInfoEvent();
                groupInfoEvent.setGroup(apiResponse.getData());
                callback.onComplete(groupInfoEvent);
            }

            @Override
            public void onFailure(DisplayType displayType, Object errorMessage) {
                callback.onFailure(displayType, errorMessage);
            }
        });
    }

    /**
     * 获取群组成员信息
     *
     * @param groupId  群组ID
     * @param cursor   第几页，默认0开始
     * @param pageSize 每页数量 默认15条
     * @param callback
     */
    public void fetchMyGroupMember(String groupId, int cursor, int pageSize, final ApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("group_id", groupId);
        mParams.put("cursor", cursor < 0 ? 0 : cursor);
        mParams.put("page_size", pageSize < 15 ? 15 : pageSize);

        simpleRequest(FETCH_MY_JOIN_GROUP_MEMBER, mParams, new ApiCallback() {
            @Override
            public void onStartApi() {
                callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                ApiResult<List<User>> apiResponse = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiResult<List<User>>>() {
                        });
                BaseEvent.GroupMemberEvent groupInfoEvent = new BaseEvent.GroupMemberEvent();
                groupInfoEvent.setUsers(apiResponse.getData());
                callback.onComplete(groupInfoEvent);
            }

            @Override
            public void onFailure(DisplayType displayType, Object errorMessage) {
                callback.onFailure(displayType, errorMessage);
            }
        });
    }

}
