package com.tongban.im.api;

import android.content.Context;
import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tongban.corelib.base.api.IApiCallback;
import com.tongban.corelib.model.ApiErrorResult;
import com.tongban.corelib.model.ApiListResult;
import com.tongban.corelib.model.ApiResult;
import com.tongban.corelib.utils.AppUtils;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.im.App;
import com.tongban.im.api.base.BaseApi;
import com.tongban.im.common.Consts;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.discover.Discover;
import com.tongban.im.model.discover.ProductBook;
import com.tongban.im.model.discover.Theme;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * 商品相关的Api
 * Created by Cheney on 15/8/18.
 */
public class ProductApi extends BaseApi {
    private static ProductApi mApi;
//---------------------------------------输入接口----------------------------------------------------

    // 收藏专题
    public static final String COLLECT_MULTI_PRODUCT = "/user/collect/theme";
    // 取消收藏专题
    public static final String NO_COLLECT_MULTI_PRODUCT = "/user/nocollect/theme";
    // 收藏商品
    public static final String COLLECT_PRODUCT = "/user/collect/product";
    // 取消收藏商品
    public static final String NO_COLLECT_PRODUCT = "/user/nocollect/product";


    //---------------------------------------输出接口----------------------------------------------------
    // 获取首页数据
    public static final String FETCH_HOME_INFO = "/home/template/require";

    // 获取专题收藏数量
    public static final String FETCH_THEME_COLLECTED_AMOUNT = "/theme/collected/amount";

    // 获取专题详情信息
    public static final String FETCH_THEME_INFO = "/theme/info";

    // 专题搜索
    public static final String SEARCH_THEME = "/theme/search/list";

    // 获取专题下属的商品列表
    public static final String FETCH_THEME_PRODUCTS = "/theme/products";

    // 商品的详情信息
    public static final String FETCH_PRODUCT_DETAIL_INFO = "/product/detail/info";

    // 单品搜索
    public static final String SEARCH_PRODUCT = "/product/search/list";

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
    public void fetchHomeInfo(final IApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("client_version", AppUtils.getVersionName(mContext));
        mParams.put("paltform", "0");
        simpleRequest(FETCH_HOME_INFO, mParams, new IApiCallback() {
            @Override
            public void onStartApi() {
                if (callback != null)
                    callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                ApiResult<List<Discover>> result = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiResult<List<Discover>>>() {
                        });
                List<Discover> discoverList = result.getData();
                if (discoverList != null && discoverList.size() > 0) {
                    BaseEvent.FetchHomeInfo homeInfo = new BaseEvent.FetchHomeInfo();
                    homeInfo.list = (discoverList);
                    if (callback != null)
                        callback.onComplete(homeInfo);
                } else {
                    onFailure(createEmptyResult(FETCH_HOME_INFO));
                }
            }

            @Override
            public void onFailure(ApiErrorResult result) {
                result.setDisplayType(DisplayType.View);
                if (callback != null)
                    callback.onFailure(result);
            }
        });
    }

    /**
     * 获取专题的收藏数量
     *
     * @param floor    楼层
     * @param themeId  专题id
     * @param callback 回调
     */
    public void fetchThemeCollectedAmount(final int floor, @NonNull String themeId
            , final IApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("theme_id", themeId);
        simpleRequest(FETCH_THEME_COLLECTED_AMOUNT, mParams, new IApiCallback() {
            @Override
            public void onStartApi() {

            }

            @Override
            public void onComplete(Object obj) {
                int amount = ((JSONObject) obj).optJSONObject("data").optInt("collect_amount");
                BaseEvent.FetchThemeCollectedAmount event = new BaseEvent.FetchThemeCollectedAmount();
                event.floor = floor;
                event.amount = amount;
                callback.onComplete(event);
            }

            @Override
            public void onFailure(ApiErrorResult result) {
                result.setDisplayType(DisplayType.None);
                if (callback != null)
                    callback.onFailure(result);
            }
        });
    }

    /**
     * 获取专题详情
     *
     * @param themeId  专题id
     * @param callback 回调
     */
    public void fetchThemeInfo(@NonNull String themeId, final IApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("theme_id", themeId);
        mParams.put("user_id", SPUtils.get(mContext, Consts.USER_ID, ""));
        simpleRequest(FETCH_THEME_INFO, mParams, new IApiCallback() {
            @Override
            public void onStartApi() {
                if (callback != null)
                    callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                ApiResult<Theme> result = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiResult<Theme>>() {
                        });
                Theme theme = result.getData();
                callback.onComplete(theme);
            }

            @Override
            public void onFailure(ApiErrorResult result) {
                if (callback != null)
                    callback.onFailure(result);
            }
        });
    }

    /**
     * 根据专题id获取专题下的单品列表
     *
     * @param themeID  专题id
     * @param cursor   游标
     * @param pageSize 每页的数量
     * @param callback 回调
     */
    public void fetchProductListByThemeId(@NonNull String themeID, int cursor, int pageSize,
                                          final IApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("theme_id", themeID);
        mParams.put("cursor", cursor < 0 ? 0 : cursor);
        mParams.put("page_size", pageSize < 0 ? 10 : pageSize);
        simpleRequest(FETCH_THEME_PRODUCTS, mParams, new IApiCallback() {
            @Override
            public void onStartApi() {
                if (callback != null)
                    callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                ApiListResult<ProductBook> result = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiListResult<ProductBook>>() {
                        });
                List<ProductBook> productList = result.getData().getResult();
                BaseEvent.FetchProductBooksInTheme themeProducts = new BaseEvent.FetchProductBooksInTheme();
                themeProducts.list = (productList);
                callback.onComplete(themeProducts);
            }

            @Override
            public void onFailure(ApiErrorResult result) {
                if (callback != null)
                    callback.onFailure(result);
            }
        });
    }

    /**
     * 收藏专题
     *
     * @param themeId  专题id
     * @param callback 回调
     */
    public void collectTheme(String themeId, final IApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("theme_id", themeId);
        mParams.put("user_id", SPUtils.get(mContext, Consts.USER_ID, ""));
        simpleRequest(COLLECT_MULTI_PRODUCT, mParams, new IApiCallback() {
            @Override
            public void onStartApi() {
            }

            @Override
            public void onComplete(Object obj) {
                setDisableCache(THEME_CACHE_TIME);
                callback.onComplete(new BaseEvent.CollectThemeEvent());
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
     * 取消收藏专题
     *
     * @param themeId  专题id
     * @param callback 回调
     */
    public void noCollectTheme(String themeId, final IApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("theme_id", themeId);
        mParams.put("user_id", SPUtils.get(mContext, Consts.USER_ID, ""));
        simpleRequest(NO_COLLECT_MULTI_PRODUCT, mParams, new IApiCallback() {
            @Override
            public void onStartApi() {
            }

            @Override
            public void onComplete(Object obj) {
                setDisableCache(THEME_CACHE_TIME);
                callback.onComplete(new BaseEvent.NoCollectThemeEvent());
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
     * 获取图书单品信息
     *
     * @param productId 单品id
     * @param callback  回调
     */
    public void fetchProductDetailInfo(String productId, final IApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("product_id", productId);
        mParams.put("user_id", SPUtils.get(mContext, Consts.USER_ID, ""));
        simpleRequest(FETCH_PRODUCT_DETAIL_INFO, mParams, new IApiCallback() {
            @Override
            public void onStartApi() {
                if (callback != null)
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
            public void onFailure(ApiErrorResult result) {
                if (callback != null)
                    callback.onFailure(result);
            }
        });
    }

    /**
     * 收藏商品
     *
     * @param productId 商品id
     * @param callback  回调
     */
    public void collectProduct(String productId, final IApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("product_id", productId);
        mParams.put("user_id", SPUtils.get(mContext, Consts.USER_ID, ""));
        simpleRequest(COLLECT_PRODUCT, mParams, new IApiCallback() {
            @Override
            public void onStartApi() {

            }

            @Override
            public void onComplete(Object obj) {
                setDisableCache(PRODUCT_CACHE_TIME);
                callback.onComplete(new BaseEvent.CollectProductEvent());
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
     * 取消收藏商品
     *
     * @param productId 专题id
     * @param callback  回调
     */
    public void noCollectProduct(String productId, final IApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("product_id", productId);
        mParams.put("user_id", SPUtils.get(mContext, Consts.USER_ID, ""));
        simpleRequest(NO_COLLECT_PRODUCT, mParams, new IApiCallback() {
            @Override
            public void onStartApi() {
                callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                setDisableCache(PRODUCT_CACHE_TIME);
                callback.onComplete(new BaseEvent.NoCollectProductEvent());
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
     * 搜索专题的接口
     *
     * @param keyword  关键字
     * @param cursor   游标
     * @param pageSize 每页大小
     * @param callback 回调
     */
    public void searchTheme(String keyword, int cursor, int pageSize, final IApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("keyword", keyword);
        mParams.put("user_id", SPUtils.get(mContext, Consts.USER_ID, ""));
        mParams.put("cursor", cursor < 0 ? 0 : cursor);
        mParams.put("page_size", pageSize < 0 ? 10 : pageSize);
        simpleRequest(SEARCH_THEME, mParams, new IApiCallback() {
            @Override
            public void onStartApi() {
                if (callback != null)
                    callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                ApiListResult<Theme> result = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiListResult<Theme>>() {
                        });

                if (result.getData().getResult().size() > 0) {
                    List<Theme> themeList = result.getData().getResult();
                    BaseEvent.SearchThemeResultEvent event = new BaseEvent.SearchThemeResultEvent();
                    event.mThemes = themeList;
                    callback.onComplete(event);
                } else {
                    onFailure(createEmptyResult(SEARCH_THEME));
                }
            }

            @Override
            public void onFailure(ApiErrorResult result) {
                callback.onFailure(result);
            }
        });
    }

    /**
     * 搜索单品的接口
     *
     * @param keyword  关键字
     * @param cursor   游标
     * @param pageSize 每页大小
     * @param callback 回调
     */
    public void searchProduct(String keyword, int cursor, int pageSize, final IApiCallback callback) {
        mParams = new HashMap<>();
        mParams.put("keyword", keyword);
        mParams.put("user_id", SPUtils.get(mContext, Consts.USER_ID, ""));
        mParams.put("cursor", cursor < 0 ? 0 : cursor);
        mParams.put("page_size", pageSize < 0 ? 10 : pageSize);
        simpleRequest(SEARCH_PRODUCT, mParams, new IApiCallback() {
            @Override
            public void onStartApi() {
                if (callback != null)
                    callback.onStartApi();
            }

            @Override
            public void onComplete(Object obj) {
                ApiListResult<ProductBook> result = JSON.parseObject(obj.toString(),
                        new TypeReference<ApiListResult<ProductBook>>() {
                        });
                if (result.getData().getResult().size() > 0) {
                    List<ProductBook> productBooks = result.getData().getResult();
                    BaseEvent.SearchProductResultEvent event = new BaseEvent.SearchProductResultEvent();
                    event.mProductBooks = productBooks;
                    callback.onComplete(event);
                } else {
                    onFailure(createEmptyResult(SEARCH_PRODUCT));
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
