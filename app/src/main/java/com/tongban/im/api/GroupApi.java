package com.tongban.im.api;

import android.content.Context;
import android.support.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tongban.corelib.base.api.ApiCallback;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.im.App;
import com.tongban.im.common.Consts;
import com.tongban.im.model.ApiResult;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.Group;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 群组操作api
 * Created by zhangleilei on 15/7/15.
 */
public class GroupApi extends BaseApi {

    private static GroupApi mApi;

    private Map<String, String> mParams;

    private static final String GROUP_HOST = "http://10.255.209.67:8080/ddim/im/group/";

    /**
     * 获取个人群组列表
     */
    public static final String FETCH_PERSONAL_GROUP_LIST = GROUP_HOST + "fetch/1";
    /**
     * 加入群组
     */
    public static final String JOIN_GROUP = GROUP_HOST + "join";
    /**
     * 创建群组
     */
    public static final String CREATE_GROUP = GROUP_HOST + "create";

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
        mParams.put("user_id", (String) SPUtils.get(mContext, Consts.USER_ID, ""));

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
            public void onFailure(DisplayType displayType, String errorMessage) {
                callback.onFailure(displayType, errorMessage);
            }
        });
    }

    /**
     * 加入群组
     *
     * @param group_id 群组id
     * @param callback 结果回调
     */
    public void joinGroup(String group_id, final ApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("group_id", group_id);
        mParams.put("user_id", (String) SPUtils.get(mContext, Consts.USER_ID, ""));

        simpleRequest(JOIN_GROUP, mParams, new ApiCallback() {
            @Override
            public void onStartApi() {
                callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                callback.onComplete(new BaseEvent.JoinGroupEvent());
            }

            @Override
            public void onFailure(DisplayType displayType, String errorMessage) {
                callback.onFailure(displayType, errorMessage);
            }
        });
    }

    /**
     * 创建群组
     *
     * @param group_name 群组名字
     * @param callback   回调
     */
    public void createGroup(@Nullable String group_name, final ApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("user_id", (String) SPUtils.get(mContext, Consts.USER_ID, ""));
        if (group_name != null)
            mParams.put("group_name", group_name);

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
            public void onFailure(DisplayType displayType, String errorMessage) {
                callback.onFailure(displayType, errorMessage);
            }
        });
    }

}
