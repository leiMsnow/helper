package com.tongban.im.api;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tongban.corelib.base.api.ApiCallback;
import com.tongban.corelib.model.ApiResult;
import com.tongban.im.App;
import com.tongban.im.model.BaseEvent;

import java.util.HashMap;
import java.util.List;

/**
 * 通用相关api
 * Created by zhangleilei on 15/7/15.
 */
public class CommonApi extends BaseApi {

    private static CommonApi mApi;

    /**
     * 创建地理位置
     */
    public static final String HOT_WORDS_LIST = "hotwords/tipHotWords/list";

    private CommonApi(Context context) {
        super(context);
    }

    public static CommonApi getInstance() {
        if (mApi == null) {
            synchronized (CommonApi.class) {
                if (mApi == null) {
                    mApi = new CommonApi(App.getInstance());
                }
            }
        }
        return mApi;
    }

    /**
     * 热词接口
     *
     * @param keyword  关键字
     * @param callback 回调
     */
    public void getHotWordsList(final String keyword, final ApiCallback callback) {

        mParams = new HashMap<>();
        mParams.put("keyword", keyword);

        simpleRequest(HOT_WORDS_LIST, mParams, new ApiCallback() {
            @Override
            public void onStartApi() {
            }

            @Override
            public void onComplete(Object obj) {
                ApiResult<List<String>> result = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiResult<List<String>>>() {
                        });

                BaseEvent.SuggestionsEvent suggestions = new BaseEvent.SuggestionsEvent();
                suggestions.keyword = keyword;
                suggestions.keywords = result.getData();
                callback.onComplete(suggestions);
            }

            @Override
            public void onFailure(DisplayType displayType, Object errorMessage) {
                callback.onFailure(displayType, errorMessage);
            }
        });
    }

}
