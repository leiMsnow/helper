package com.tongban.im.api;

import android.content.Context;
import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tongban.corelib.base.api.ApiCallback;
import com.tongban.corelib.model.ApiResult;
import com.tongban.im.App;
import com.tongban.im.model.MultiProduct;

import java.util.HashMap;

/**
 * 商品相关的Api
 * Created by Cheney on 15/8/18.
 */
public class ProductApi extends BaseApi {
    private static ProductApi mApi;

    /**
     * 获取专题详情信息
     */
    public static final String FETCH_MULTI_PRODUCT_INFO = "ddim/theme/info";

    private ProductApi(Context context) {
        super(context);
    }

    public static ProductApi getInstance() {
        if (mApi == null) {
            synchronized (ProductApi.class) {
                if (mApi == null) {
                    mApi = new ProductApi(App.getInstance());
                }
            }
        }
        return mApi;
    }

    /**
     * 获取专题详情
     *
     * @param multiProductId 专题id
     * @param callback       回调
     */
    public void fetchMultiProductInfo(@NonNull String multiProductId, final ApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("theme_id", multiProductId);
        simpleRequest(FETCH_MULTI_PRODUCT_INFO, mParams, new ApiCallback() {
            @Override
            public void onStartApi() {
                callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                ApiResult<MultiProduct> result = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiResult<MultiProduct>>() {
                        });
                MultiProduct multiProduct = result.getData();
                callback.onComplete(multiProduct);
            }

            @Override
            public void onFailure(DisplayType displayType, Object errorObj) {
                callback.onFailure(displayType, errorObj);
            }
        });
    }

}
