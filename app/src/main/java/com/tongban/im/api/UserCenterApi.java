package com.tongban.im.api;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tongban.corelib.base.api.ApiCallback;
import com.tongban.corelib.model.ApiListResult;
import com.tongban.corelib.model.ApiResult;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.im.App;
import com.tongban.im.common.Consts;
import com.tongban.im.model.Group;
import com.tongban.im.model.User;

import java.util.HashMap;
import java.util.List;

/**
 * 用户中心api
 * Created by fushudi on 2015/8/19.
 */
public class UserCenterApi extends BaseApi {
    private static UserCenterApi mApi;
    /**
     * 获取用户个人中心数据
     */
    public static final String FETCH_USER_CENTER_INFO = "user/center/info";
    /**
     * 获取个人资料
     */
    public static final String FETCH_USER_DETAIL_INFO = "/user/info";
    /**
     * 获取个人群组列表-创建的群
     */
    public static final String FETCH_MY_GROUP_LIST = "user/join/group/list";
    /**
     * 获取我关注的人员列表
     */
    public static final String FETCH_FOCUS_USER_LIST = "/user/focus/user/list";

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
    public void fetchUserCenterInfo(final ApiCallback callback) {

        mParams = new HashMap<>();
        mParams.put("user_id", SPUtils.get(mContext, Consts.USER_ID, ""));

        simpleRequest(FETCH_USER_CENTER_INFO, mParams, new ApiCallback() {
            @Override
            public void onStartApi() {
                callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                ApiResult<User> apiResponse = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiResult<User>>() {
                        });
                User user = apiResponse.getData();
                callback.onComplete(user);
            }

            @Override
            public void onFailure(DisplayType displayType, Object errorMessage) {
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
        //TODO缺少参数
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
                User user = apiResponse.getData();
                callback.onComplete(user);
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
    public void fetchMyGroupList(int cursor, int pageSize, final ApiCallback callback) {

        mParams = new HashMap<>();
        mParams.put("user_id", SPUtils.get(mContext, Consts.USER_ID, ""));
        mParams.put("cursor", cursor < 0 ? 0 : cursor);
        mParams.put("page_size", pageSize < 4 ? 4 : pageSize);

        // TODO: 2015/8/19 借口返回参数错误，缺少页码
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
                List<Group> groups = apiResponse.getData().getResult();
                callback.onComplete(groups);
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
     * @param callback
     */
    public void fetchFocusUserList(int cursor, int pageSize, final ApiCallback callback) {

        mParams = new HashMap<>();
        mParams.put("user_id", SPUtils.get(mContext, Consts.USER_ID, ""));
        mParams.put("cursor", cursor < 0 ? 0 : cursor);
        mParams.put("page_size", pageSize < 1 ? 10 : pageSize);

        // TODO: 2015/8/19 借口返回参数错误，缺少页码
        simpleRequest(FETCH_FOCUS_USER_LIST, mParams, new ApiCallback() {
            @Override
            public void onStartApi() {
                callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {

            }

            @Override
            public void onFailure(DisplayType displayType, Object errorMessage) {
                callback.onFailure(displayType, errorMessage);
            }
        });
    }
}
