package com.tb.api;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tb.api.base.BaseApi;
import com.tb.api.model.AssistTopn;
import com.tb.api.model.TalentInfo;
import com.tongban.corelib.base.BaseApplication;
import com.tongban.corelib.base.api.IApiCallback;
import com.tongban.corelib.model.ApiErrorResult;
import com.tongban.corelib.model.ApiListResult;

import java.util.HashMap;


/**
 * 协助API接口类
 * Created by zhangleilei on 15/11/10.
 */
public class TalentApi extends BaseApi {

    private static TalentApi mApi;
    /**
     * 列出所有的达人
     */
    public final static String TALENT_USER_LIST = "/producer/query/list";


    private TalentApi(Context context) {
        super(context);
    }

    public static TalentApi getInstance() {
        if (mApi == null) {
            synchronized (TalentApi.class) {
                if (mApi == null) {
                    mApi = new TalentApi(BaseApplication.getInstance());
                }
            }
        }
        return mApi;
    }

    /**
     * 获取达人list接口
     *
     * @param cursor
     * @param page_size
     * @param callback
     */
    public void getTalentUserList(int cursor, int page_size, final IApiCallback callback) {

        mParams = new HashMap<>();
        mParams.put("cursor", cursor);
        mParams.put("page_size", page_size);

        simpleRequest(TALENT_USER_LIST, mParams, new IApiCallback() {
            @Override
            public void onStartApi() {
            }

            @Override
            public void onComplete(Object obj) {

                ApiListResult<TalentInfo> apiResponse = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiListResult<TalentInfo>>() {
                        });

                if (apiResponse.getData().getResult().size() > 0) {
                    if (callback != null)
                        callback.onComplete(apiResponse.getData().getResult());
                } else {
                    if (callback != null)
                        callback.onFailure(createEmptyResult(TALENT_USER_LIST));
                }
            }

            @Override
            public void onFailure(ApiErrorResult result) {
                if (callback != null)
                    callback.onFailure(result);
            }

        });

    }

}
