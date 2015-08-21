package com.tongban.im.api;

import android.content.Context;
import android.media.Image;
import android.support.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tongban.corelib.base.api.ApiCallback;
import com.tongban.corelib.model.ApiListResult;
import com.tongban.corelib.model.ApiResult;
import com.tongban.corelib.utils.LogUtil;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.im.App;
import com.tongban.im.common.ModelToTable;
import com.tongban.im.common.Consts;
import com.tongban.im.db.helper.GroupDaoHelper;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.Group;
import com.tongban.im.model.GroupType;
import com.tongban.im.model.ImageUrl;
import com.tongban.im.model.User;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 群组操作api
 * Created by zhangleilei on 15/7/15.
 */
public class GroupApi extends BaseApi {

    private static GroupApi mApi;

    /**
     * 创建群组
     */
    public static final String CREATE_GROUP = "group/create";
    /**
     * 加入群组
     */
    public static final String JOIN_GROUP = "/user/join/group";
    /**
     * 根据群组类型获取搜索群组
     */
    public static final String SEARCH_GROUP_BY_TYPE = "group/fetch/3";
    /**
     * 根据地理位置获取推荐群组(附近的群)
     */
    public static final String FETCH_RECOMMEND_GROUP_LIST = "group/fetch/5";
    /**
     * 获取群组详情
     */
    public static final String FETCH_MY_JOIN_GROUP_INFO = "group/info";
    /**
     * 获取群组成员信息
     */
    public static final String FETCH_MY_JOIN_GROUP_MEMBER = "group/members";
    /**
     * 圈子推荐接口
     */
    public static final String RECOMMEND_GROUP_LIST = "group/recommend/list";
    /**
     * 圈子搜索接口
     */
    public static final String SEARCH_GROUP_LIST = "group/search/list";


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
     * 创建群组
     * modified by chen
     *
     * @param groupName   群组名字
     * @param groupType   群组类型{@link GroupType}
     * @param longitude   经度
     * @param latitude    纬度
     * @param address     详细地址
     * @param birthday    出生日期(y-m-d H:i:s)
     * @param tags        群组的标签,多个用逗号分隔
     * @param declaration 群简介
     * @param groupAvatar 群头像
     * @param isSearch    (true-1:允许搜索;false-0：不允许搜索)
     * @param callback    回调
     */
    public void createGroup(final String groupName, int groupType, double longitude,
                            double latitude, final String address, @Nullable String birthday,
                            @Nullable String tags, @Nullable final String declaration,
                            ImageUrl groupAvatar, boolean isSearch,
                            final ApiCallback callback) {

        mParams = new HashMap<>();
        mParams.put("user_id", SPUtils.get(mContext, Consts.USER_ID, ""));
        mParams.put("group_name", groupName);
        //(默认0，1：学校)
        mParams.put("address_type", groupType == GroupType.CLASSMATE ? 1 : 0);
        mParams.put("longitude", longitude);
        mParams.put("latitude", latitude);
        mParams.put("address", address);

        if (birthday != null)
            mParams.put("birthday", birthday);
        if (tags != null)
            mParams.put("tags", tags);
        if (declaration != null)
            mParams.put("declaration", declaration);

        mParams.put("group_avatar", JSON.toJSON(groupAvatar));
        mParams.put("flag_allow_search", isSearch ? 1 : 0);
        //此处接口名称根据groupType变化
        simpleRequest(CREATE_GROUP + "/" + groupType, mParams, new ApiCallback() {
            @Override
            public void onStartApi() {
                callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                ApiResult<Group> result = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiResult<Group>>() {
                        });
                BaseEvent.CreateGroupEvent createGroupEvent = new BaseEvent.CreateGroupEvent();
                createGroupEvent.setGroupId(result.getData().toString());
//                // TODO: 8/19/15  同步到查询表，后面会删除
//                createGroup1(groupName, result.getData().toString(), address, declaration, null);
                callback.onComplete(createGroupEvent);
            }

            @Override
            public void onFailure(DisplayType displayType, Object errorMessage) {
                callback.onFailure(displayType, errorMessage);
            }
        });
    }

    @Deprecated
    public void createGroup1(String groupName, String groupId, String address, String declaration,
                             final ApiCallback callback) {

        mParams = new HashMap<>();
        mParams.put("group_name", groupName);
        mParams.put("address", address);
        mParams.put("declaration", declaration);

        //此处接口名称根据groupType变化
        simpleRequest("http://10.255.209.72/group_test/group_info/1", mParams, new ApiCallback() {
            @Override
            public void onStartApi() {
            }

            @Override
            public void onComplete(Object obj) {
                LogUtil.d("createGroup1-onComplete", obj.toString());
            }

            @Override
            public void onFailure(DisplayType displayType, Object errorMessage) {
                LogUtil.d("createGroup1-onFailure", errorMessage.toString());

            }
        });
    }

    /**
     * 加入群组
     *
     * @param groupId   群组ID
     * @param groupName 群名称
     * @param masterId  群主ID
     * @param callback  结果回调
     */
    public void joinGroup(String groupId, String groupName, String masterId, final ApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("group_id", groupId);
        mParams.put("group_name", groupName);
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
     * 获取群组详情
     *
     * @param groupId  群组ID
     * @param callback
     */
    public void fetchMyGroupInfo(String groupId, final ApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("group_id", groupId);
        //获取数量，默认15条用户信息
        mParams.put("member_amount", 15);

        simpleRequest(FETCH_MY_JOIN_GROUP_INFO, mParams, new ApiCallback() {
            @Override
            public void onStartApi() {
                if (callback != null)
                    callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                ApiResult<Group> apiResponse = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiResult<Group>>() {
                        });
                BaseEvent.GroupInfoEvent groupInfoEvent = new BaseEvent.GroupInfoEvent();
                groupInfoEvent.setGroup(apiResponse.getData());
                //将数据保存在本地数据库
                GroupDaoHelper.get(mContext).addData(ModelToTable.groupToTable(apiResponse.getData()));
                if (callback != null)
                    callback.onComplete(groupInfoEvent);
            }

            @Override
            public void onFailure(DisplayType displayType, Object errorMessage) {
                if (callback != null)
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

    /**
     * 获取圈子推荐列表
     *
     * @param cursor   第几页，默认0开始
     * @param pageSize 每页数量 最少1条
     * @param callback
     */
    public void recommendGroupList(int cursor, int pageSize, final ApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("longitude", SPUtils.get(mContext, Consts.LONGITUDE, -1.0D));
        mParams.put("latitude", SPUtils.get(mContext, Consts.LATITUDE, -1.0D));
        mParams.put("user_id", SPUtils.get(mContext, Consts.USER_ID, ""));
        mParams.put("cursor", cursor < 0 ? 0 : cursor);
        mParams.put("page_size", pageSize < 1 ? 10 : pageSize);

        simpleRequest(RECOMMEND_GROUP_LIST, mParams, new ApiCallback() {
            @Override
            public void onStartApi() {
                if (callback != null)
                    callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                ApiListResult<Group> listResult = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiListResult<Group>>() {
                        });
                if (listResult.getData().getResult().size() > 0) {
                    if (callback != null)
                        callback.onComplete(listResult);
                } else {
                    if (callback != null)
                        callback.onFailure(DisplayType.View, "暂无圈子信息,快来创建第一个圈子吧");
                }
            }

            @Override
            public void onFailure(DisplayType displayType, Object errorMessage) {
                if (callback != null)
                    callback.onFailure(DisplayType.View, errorMessage);
            }
        });
    }

    /**
     * 搜索圈子
     *
     * @param keyword  关键字
     * @param cursor   第几页，默认0开始
     * @param pageSize 每页数量 最少1条
     * @param callback
     */
    public void searchGroupList(String keyword, int cursor, int pageSize, final ApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("keyword", keyword);
        mParams.put("user_id", SPUtils.get(mContext, Consts.USER_ID, ""));
        mParams.put("cursor", cursor < 0 ? 0 : cursor);
        mParams.put("page_size", pageSize < 1 ? 10 : pageSize);

        simpleRequest(SEARCH_GROUP_LIST, mParams, new ApiCallback() {
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
                BaseEvent.SearchGroupListEvent searchGroupEvent = new BaseEvent.SearchGroupListEvent();
                searchGroupEvent.setGroups(apiResponse.getData().getResult());
                if (callback != null)
                    callback.onComplete(searchGroupEvent);
            }

            @Override
            public void onFailure(DisplayType displayType, Object errorMessage) {
                if (callback != null)
                    callback.onFailure(DisplayType.View, errorMessage);
            }
        });
    }


}
