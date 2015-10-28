package com.tb.api;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tb.api.base.BaseApi;
import com.tb.api.model.BaseEvent;
import com.tb.api.model.Tag;
import com.tongban.corelib.base.BaseApplication;
import com.tongban.corelib.base.api.IApiCallback;
import com.tongban.corelib.model.ApiErrorResult;
import com.tongban.corelib.model.ApiListResult;
import com.tongban.corelib.model.ApiResult;

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
    public static final String HOT_WORDS_LIST = "/hotwords/tipHotWords/list";

    // 获取发现搜索页的标签
    public static final String FETCH_DISCOVER_TAG = "/tag/list";

    private CommonApi(Context context) {
        super(context);
    }

    public static CommonApi getInstance() {
        if (mApi == null) {
            synchronized (CommonApi.class) {
                if (mApi == null) {
                    mApi = new CommonApi(BaseApplication.getInstance());
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
    public void getHotWordsList(final String keyword, final IApiCallback callback) {

        mParams = new HashMap<>();
        mParams.put("keyword", keyword);

        simpleRequest(HOT_WORDS_LIST, mParams, new IApiCallback() {
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
            public void onFailure(ApiErrorResult result) {

            }
        });
    }

    /**
     * 获取tag
     *
     * @param cursor   游标
     * @param pageSize 页容
     * @param type     tag类型{@link TagType}
     * @param callback 回调
     */
    public void fetchTags(int cursor, int pageSize, final String type, final IApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("cursor", cursor);
        mParams.put("page_size", pageSize);
        mParams.put("tag_type", type);
        simpleRequest(FETCH_DISCOVER_TAG, mParams, new IApiCallback() {
            @Override
            public void onStartApi() {

            }

            @Override
            public void onComplete(Object obj) {
                ApiListResult<Tag> result = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiListResult<Tag>>() {
                        });
                List<Tag> tagList = result.getData().getResult();
                BaseEvent.FetchTags event = new BaseEvent.FetchTags();
                event.type = type;
                event.tags = tagList;
                callback.onComplete(event);
            }

            @Override
            public void onFailure(ApiErrorResult result) {
                callback.onFailure(result);
            }
        });
    }

}
