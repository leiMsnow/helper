package com.tongban.im.api;

import android.content.Context;
import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tongban.corelib.base.api.ApiCallback;
import com.tongban.corelib.model.ApiResult;
import com.tongban.corelib.utils.AppUtils;
import com.tongban.im.App;
import com.tongban.im.model.Discover;
import com.tongban.im.model.MultiProduct;
import com.tongban.im.model.ProductBook;

import java.util.HashMap;
import java.util.List;

/**
 * 商品相关的Api
 * Created by Cheney on 15/8/18.
 */
public class ProductApi extends BaseApi {
    private static ProductApi mApi;

    // 获取首页数据
    private static final String FETCH_HOME_INFO = "home/template/require";

    // 获取专题详情信息
    private static final String FETCH_MULTI_PRODUCT_INFO = "theme/info";

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
     * 获取首页数据
     *
     * @param callback 回调
     */
    public void fetchHomeInfo(final ApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("client_version", AppUtils.getVersionName(mContext));
        mParams.put("paltform", "0");
        simpleRequest(FETCH_HOME_INFO, mParams, new ApiCallback() {
            @Override
            public void onStartApi() {
                callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                ApiResult<List<Discover>> result = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiResult<List<Discover>>>() {
                        });
                List<Discover> discoverList = result.getData();
                callback.onComplete(discoverList);
            }

            @Override
            public void onFailure(DisplayType displayType, Object errorObj) {
                callback.onFailure(displayType, errorObj);
            }
        });
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

    /**
     * 根据专题id获取专题下的单品列表
     *
     * @param multiProductId 专题id
     * @param cursor         游标
     * @param pageSize       每页的数量
     * @param callback       回调
     */
    public void fetchSimpleByMultiId(@NonNull String multiProductId, int cursor, int pageSize,
                                     final ApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("theme_id", multiProductId);
        mParams.put("cursor", cursor);
        mParams.put("page_size", pageSize);
        simpleRequest(FETCH_MULTI_PRODUCT_INFO, mParams, new ApiCallback() {
            @Override
            public void onStartApi() {

            }

            @Override
            public void onComplete(Object obj) {
                ApiResult<List<ProductBook>> result = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiResult<List<ProductBook>>>() {
                        });
                List<ProductBook> productList = result.getData();
                callback.onComplete(productList);
            }

            @Override
            public void onFailure(DisplayType displayType, Object errorObj) {
                callback.onFailure(displayType, errorObj);
            }
        });
    }

}
