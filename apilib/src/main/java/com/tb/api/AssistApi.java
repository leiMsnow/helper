package com.tb.api;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tb.api.base.BaseApi;
import com.tb.api.model.AssistAnswer;
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
    /**
     * 切换到达人的时候，调用接口
     */
    public final static String ASSIST_UPDATE = "/assist/session/update";

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
        mParams.put("user_id", getUserId());

        simpleRequest(ASSIST_ASK, mParams, new IApiCallback() {
            @Override
            public void onStartApi() {

            }

            @Override
            public void onComplete(Object obj) {
                ApiResult<AssistAnswer> result = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiResult<AssistAnswer>>() {
                        });

                BaseEvent.AssistAnswerEvent answerEvent = new BaseEvent.AssistAnswerEvent();
                answerEvent.answers = result.getData();

                if (callback != null)
                    callback.onComplete(answerEvent);
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
    public void getAssistTopn(final IApiCallback callback) {

        mParams = new HashMap<>();
        mParams.put("cursor", 0);
        mParams.put("page_size", 5);
        mParams.put("limit", 5);

        simpleRequest(ASSIST_TOPN, mParams, new IApiCallback() {
            @Override
            public void onStartApi() {

            }

            @Override
            public void onComplete(Object obj) {
                ApiListResult<AssistTopn> apiResponse = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiListResult<AssistTopn>>() {
                        });

                BaseEvent.AssistTopnEvent assistTopnEvent = new BaseEvent.AssistTopnEvent();
                assistTopnEvent.talentInfo = apiResponse.getData().getResult();

                if (callback != null)
                    callback.onComplete(assistTopnEvent);
            }

            @Override
            public void onFailure(ApiErrorResult result) {
                result.setDisplayType(DisplayType.Toast);
                if (callback != null)
                    callback.onFailure(result);
            }

        });
    }

    public void setAssistUpdate(String session_id, String dealer_id, final IApiCallback callback) {

        mParams = new HashMap<>();
        mParams.put("session_id", session_id);
        mParams.put("deal_status", 1);
        mParams.put("dealer_id", dealer_id);

        simpleRequest(ASSIST_UPDATE, mParams, new IApiCallback() {
            @Override
            public void onStartApi() {

            }

            @Override
            public void onComplete(Object obj) {

                if (callback != null)
                    callback.onComplete(obj);
            }

            @Override
            public void onFailure(ApiErrorResult result) {
                result.setDisplayType(DisplayType.None);
                if (callback != null)
                    callback.onFailure(result);
            }

        });
    }

}
