package com.tongban.im.api;

import android.content.Context;
import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tongban.corelib.base.api.ApiCallback;
import com.tongban.corelib.model.ApiListResult;
import com.tongban.corelib.model.ApiResult;
import com.tongban.corelib.utils.AppUtils;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.im.App;
import com.tongban.im.common.Consts;
import com.tongban.im.model.BaseEvent;
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
    private static final String FETCH_THEME_INFO = "theme/info";

    // 收藏专题
    private static final String COLLECT_MULTI_PRODUCT = "user/collect/theme";

    // 取消收藏专题
    private static final String NO_COLLECT_MULTI_PRODUCT = "user/nocollect/theme";

    // 获取专题下属的商品列表
    private static final String FETCH_THEME_PRODUCTS = "theme/products";

    // 商品的详情信息
    private static final String FETCH_PRODUCT_DETAIL_INFO = "product/detail/info";

    // 收藏商品
    private static final String COLLECT_PRODUCT = "user/collect/product";

    // 取消收藏商品
    private static final String NO_COLLECT_PRODUCT = "user/nocollect/product";

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

            }

            @Override
            public void onComplete(Object obj) {
                ApiResult<List<Discover>> result = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiResult<List<Discover>>>() {
                        });
                List<Discover> discoverList = result.getData();
                BaseEvent.FetchHomeInfo homeInfo = new BaseEvent.FetchHomeInfo();
                homeInfo.setList(discoverList);
                callback.onComplete(homeInfo);
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
        mParams.put("user_id", SPUtils.get(mContext, Consts.USER_ID, ""));
        simpleRequest(FETCH_THEME_INFO, mParams, new ApiCallback() {
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
    public void fetchProductListByMultiId(@NonNull String multiProductId, int cursor, int pageSize,
                                          final ApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("theme_id", multiProductId);
        mParams.put("cursor", cursor);
        mParams.put("page_size", pageSize);
        simpleRequest(FETCH_THEME_PRODUCTS, mParams, new ApiCallback() {
            @Override
            public void onStartApi() {

            }

            @Override
            public void onComplete(Object obj) {
                ApiListResult<ProductBook> result = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiListResult<ProductBook>>() {
                        });
                List<ProductBook> productList = result.getData().getResult();
                BaseEvent.FetchThemeProducts themeProducts = new BaseEvent.FetchThemeProducts();
                themeProducts.setList(productList);
                callback.onComplete(themeProducts);
            }

            @Override
            public void onFailure(DisplayType displayType, Object errorObj) {
                callback.onFailure(displayType, errorObj);
            }
        });
    }

    /**
     * 收藏专题
     *
     * @param multiId  专题id
     * @param callback 回调
     */
    public void collectMultiProduct(String multiId, final ApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("theme_id", multiId);
        mParams.put("user_id", SPUtils.get(mContext, Consts.USER_ID, ""));
        simpleRequest(COLLECT_MULTI_PRODUCT, mParams, new ApiCallback() {
            @Override
            public void onStartApi() {
                callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                callback.onComplete(new BaseEvent.CollectThemeEvent());
            }

            @Override
            public void onFailure(DisplayType displayType, Object errorObj) {
                callback.onFailure(DisplayType.Toast, "收藏失败");
            }
        });
    }

    /**
     * 取消收藏专题
     *
     * @param multiId  专题id
     * @param callback 回调
     */
    public void noCollectMultiProduct(String multiId, final ApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("theme_id", multiId);
        mParams.put("user_id", SPUtils.get(mContext, Consts.USER_ID, ""));
        simpleRequest(NO_COLLECT_MULTI_PRODUCT, mParams, new ApiCallback() {
            @Override
            public void onStartApi() {
                callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                callback.onComplete(new BaseEvent.NoCollectThemeEvent());
            }

            @Override
            public void onFailure(DisplayType displayType, Object errorObj) {
                callback.onFailure(DisplayType.Toast, "收藏失败");
            }
        });
    }

    /**
     * 获取图书单品信息
     *
     * @param productId 单品id
     * @param callback  回调
     */
    public void fetchProductDetailInfo(String productId, final ApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("product_id", productId);
        mParams.put("user_id", SPUtils.get(mContext, Consts.USER_ID, ""));
        simpleRequest(FETCH_PRODUCT_DETAIL_INFO, mParams, new ApiCallback() {
            @Override
            public void onStartApi() {
                callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                ApiResult<ProductBook> result = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiResult<ProductBook>>() {
                        });
                ProductBook productBook = result.getData();
                callback.onComplete(productBook);
            }

            @Override
            public void onFailure(DisplayType displayType, Object errorObj) {
                callback.onFailure(DisplayType.ALL, "请重试");
            }
        });
    }

    /**
     * 收藏商品
     *
     * @param productId 商品id
     * @param callback  回调
     */
    public void collectProduct(String productId, final ApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("product_id", productId);
        mParams.put("user_id", SPUtils.get(mContext, Consts.USER_ID, ""));
        simpleRequest(COLLECT_PRODUCT, mParams, new ApiCallback() {
            @Override
            public void onStartApi() {
                callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                callback.onComplete(new BaseEvent.CollectProductEvent());
            }

            @Override
            public void onFailure(DisplayType displayType, Object errorObj) {
                callback.onFailure(DisplayType.Toast, "收藏失败");
            }
        });
    }

    /**
     * 取消收藏专题
     *
     * @param multiId  专题id
     * @param callback 回调
     */
    public void noCollectProduct(String multiId, final ApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("product_id", multiId);
        mParams.put("user_id", SPUtils.get(mContext, Consts.USER_ID, ""));
        simpleRequest(NO_COLLECT_PRODUCT, mParams, new ApiCallback() {
            @Override
            public void onStartApi() {
                callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                callback.onComplete(new BaseEvent.NoCollectProductEvent());
            }

            @Override
            public void onFailure(DisplayType displayType, Object errorObj) {
                callback.onFailure(DisplayType.Toast, "收藏失败");
            }
        });
    }

}
