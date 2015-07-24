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

import java.util.HashMap;
import java.util.List;

/**
 * 群组操作api
 * Created by zhangleilei on 15/7/15.
 */
public class GroupApi extends BaseApi {

    private static GroupApi mApi;

    /**
     * 获取用户已加入的群组列表
     */
    public static final String FETCH_PERSONAL_GROUP_LIST = "im/group/fetch/4";
    /**
     * 加入群组
     */
    public static final String JOIN_GROUP = "im/group/join";
    /**
     * 创建群组
     */
    public static final String CREATE_GROUP = "im/group/create";
    /**
     * 搜索群组-关键字
     */
    public static final String SEARCH_GROUP_BY_NAME = "im/group/fetch/2";

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
     * 获取圈子列表数据
     *
     * @param callback 回调
     */
    public void fetchPersonalGroupList(final ApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("user_id", SPUtils.get(mContext, Consts.USER_ID, ""));

        simpleRequest(FETCH_PERSONAL_GROUP_LIST, mParams, new ApiCallback() {
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
     * 加入群组
     *
     * @param groupId  群组ID
     * @param masterId 群主ID
     * @param callback 结果回调
     */
    public void joinGroup(String groupId,String masterId, final ApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("group_id", groupId);
        mParams.put("user_id", (String) SPUtils.get(mContext, Consts.USER_ID, ""));
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
     * 创建群组
     *
     * @param groupName 群组名字
     * @param callback  回调
     */
    public void createGroup(@Nullable String groupName, int groupType, final ApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("user_id", (String) SPUtils.get(mContext, Consts.USER_ID, ""));
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
     * 搜索群组-根据群名称
     *
     * @param groupName 群名称
     * @param cursor    第几页，从0开始
     * @param pageSize  每页数量 默认10条
     * @param callback
     */
    public void searchGroupByName(String groupName, int cursor, int pageSize, final ApiCallback callback) {

        mParams = new HashMap<>();
        mParams.put("user_id", (String) SPUtils.get(mContext, Consts.USER_ID, ""));
        mParams.put("group_name", groupName);
        mParams.put("cursor", String.valueOf(cursor));
        mParams.put("page_size", String.valueOf(pageSize));

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

}
