package com.tb.api;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tb.api.base.BaseApi;
import com.tb.api.model.AssistTopn;
import com.tb.api.model.BaseEvent;
import com.tb.api.model.user.User;
import com.tongban.corelib.base.BaseApplication;
import com.tongban.corelib.base.api.IApiCallback;
import com.tongban.corelib.model.ApiErrorResult;
import com.tongban.corelib.model.ApiListResult;
import com.tongban.corelib.model.ApiResult;
import com.tongban.corelib.utils.Constants;
import com.tongban.corelib.utils.SPUtils;

import java.util.HashMap;
import java.util.List;


/**
 * 协助API接口类
 * Created by zhangleilei on 15/11/10.
 */
public class AssistApi extends BaseApi {

    private static AssistApi mApi;
    /**
     * 提问接口
     */
    public final static String ASSIST_ASK = "/assist/ask";
    /**
     * 最热提问热词
     */
    public final static String ASSIST_TOPN = "/assist/issues/topn";

    private AssistApi(Context context) {
        super(context);
    }

    public static AssistApi getInstance() {
        if (mApi == null) {
            synchronized (AssistApi.class) {
                if (mApi == null) {
                    mApi = new AssistApi(BaseApplication.getInstance());
                }
            }
        }
        return mApi;
    }

    /**
     * 提问接口
     *
     * @param session
     * @param issue
     * @param callback
     */
    public void createAssistQuery(String session, String issue, final IApiCallback callback) {

        mParams = new HashMap<>();
        mParams.put("session", session);
        mParams.put("issue", issue);
        mParams.put("user_id", "0_5620b9e52284007c4a418e87");

        simpleRequest(ASSIST_ASK, mParams, new IApiCallback() {
            @Override
            public void onStartApi() {
//                callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
//                ApiResult<BaseEvent.RegisterEvent> apiResponse = JSON.parseObject(obj.toString(),
//                        new TypeReference<ApiResult<BaseEvent.RegisterEvent>>() {
//                        });
//                BaseEvent.RegisterEvent registerEvent = apiResponse.getData();
//                registerEvent.registerEnum = (BaseEvent.RegisterEvent.RegisterEnum.SMS_CODE);
                if (callback != null)
                    callback.onComplete(obj);
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
     * 查询最热提问词
     *
     * @param callback
     */
    public void getIssuesTopn(final IApiCallback callback) {

        mParams = new HashMap<>();
        mParams.put("cursor", 0);
        mParams.put("page_size", 5);
        mParams.put("limit", 5);

        simpleRequest(ASSIST_TOPN, mParams, new IApiCallback() {
            @Override
            public void onStartApi() {
//                callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                ApiListResult<AssistTopn> apiResponse = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiListResult<AssistTopn>>() {
                        });

                if (callback != null)
                    callback.onComplete(apiResponse.getData().getResult());
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
