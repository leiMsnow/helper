package com.tongban.im.api;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tongban.corelib.base.api.ApiCallback;
import com.tongban.im.App;
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
public class GroupApi extends  BaseApi{

    private static GroupApi mApi;

    private Map<String, String> mParams;

    /** 获取个人群组列表 */
    public static final String FETCH_PERSONAL_GROUP_LIST = "http://10.255.209.67:8080/ddim/im/group/fetch/1";
    /** 加入群组 */
    public static final String JOIN_GROUP = "http://10.255.209.67:8080/ddim/im/group/join";

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
     * @param user_id   用户id
     * @param callback 回调
     */
    public void fetchPersonalGroupList(String user_id, final ApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("user_id", user_id);

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
     * @param group_id 群组id
     * @param user_id 用户id
     * @param callback 结果回调
     */
    public void joinGroup(String group_id, String user_id, final ApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("group_id", group_id);
        mParams.put("user_id", user_id);

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

}
