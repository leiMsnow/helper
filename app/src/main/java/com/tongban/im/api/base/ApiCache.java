package com.tongban.im.api.base;

import android.content.Context;

import com.tongban.corelib.base.BaseApplication;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.im.api.GroupApi;
import com.tongban.im.api.ProductApi;
import com.tongban.im.api.TopicApi;
import com.tongban.im.api.UserCenterApi;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by zhangleilei on 10/27/15.
 */
public class ApiCache {

    private static ApiCache apiCache;

    private Context mContext;

    private Set<String> mDisableCacheUrls;
    //-----------------------------接口缓存时间key----------------------------------------------------
    //------------------------存储的时间大于0，直接调用DB数据-------------------------------------------
    public final static String ALL_CACHE_URL = "ALL_CACHE_URL";
    //专题时间-10min
    public final static String THEME_CACHE_TIME = "THEME_CACHE_TIME";
    //单品时间-10min
    public final static String PRODUCT_CACHE_TIME = "PRODUCT_CACHE_TIME";
    //话题时间-10min
    public final static String TOPIC_CACHE_TIME = "TOPIC_CACHE_TIME";
    //圈子时间-30min
    public final static String GROUP_CACHE_TIME = "GROUP_CACHE_TIME";
    //用户时间-5min
    public final static String USER_CACHE_TIME = "USER_CACHE_TIME";

    private ApiCache(Context context) {
        this.mContext = context;
    }

    public static ApiCache getInstance() {
        if (apiCache == null) {
            synchronized (ApiCache.class) {
                if (apiCache == null) {
                    apiCache = new ApiCache(BaseApplication.getInstance());
                }
            }
        }
        return apiCache;
    }


    /**
     * 删除所有的缓存url
     */
    public void clearDisableCacheUrls(String cacheName) {
        SPUtils.put(mContext, cacheName, null);
    }

    /**
     * 获得所有接口
     *
     * @return
     */
    public Set<String> getDisableCacheUrls() {
        mDisableCacheUrls = new HashSet<>();
        mDisableCacheUrls = (Set<String>) SPUtils.get(mContext, ALL_CACHE_URL, mDisableCacheUrls);
        return mDisableCacheUrls;
    }


    /**
     * 删除某个缓存url
     *
     * @param url
     */
    public void removeDisableCacheUrls(String url) {
        getDisableCacheUrls();
        this.mDisableCacheUrls.remove(url);
        SPUtils.put(mContext, ALL_CACHE_URL, mDisableCacheUrls);
    }


    /**
     * 缓存地址是否存在，如果存在将不再走缓存接口
     *
     * @param url
     * @return
     */
    public boolean isCurrentUrl(String url) {
        getDisableCacheUrls();
        return mDisableCacheUrls.contains(url);
    }

    /**
     * 记录所有url
     * 读取缓存的时候，将url存储下来
     *
     * @param url
     */
    public void setDisableCacheUrls(String url) {
        getDisableCacheUrls();
        this.mDisableCacheUrls.add(url);
        SPUtils.put(mContext, ALL_CACHE_URL, mDisableCacheUrls);
    }

    /**
     * 设置disableCache时间
     *
     * @param cacheName {@link BaseApi}
     */
    public void setDisableCache(String cacheName) {
        int disableCacheTime = 0;
        if (cacheName.equals(USER_CACHE_TIME)) {
            disableCacheTime = 5;
            setDisableCacheUrls(UserCenterApi.USER_INFO);
            setDisableCacheUrls(UserCenterApi.FETCH_USER_CENTER_INFO);
            setDisableCacheUrls(UserCenterApi.FETCH_FOCUS_USER_LIST);
            setDisableCacheUrls(UserCenterApi.FETCH_PERSONAL_CENTER_INFO);

        } else if (cacheName.equals(TOPIC_CACHE_TIME)) {
            disableCacheTime = 10;
            //话题相关
            setDisableCacheUrls(TopicApi.RECOMMEND_TOPIC_LIST);
            setDisableCacheUrls(TopicApi.SEARCH_TOPIC_LIST);
            setDisableCacheUrls(TopicApi.TOPIC_INFO);
            setDisableCacheUrls(TopicApi.OFFICIAL_TOPIC_INFO);
            setDisableCacheUrls(TopicApi.TOPIC_COMMENT_LIST);
            //用户相关
            setDisableCacheUrls(UserCenterApi.FETCH_COLLECT_REPLY_TOPIC_LIST);
            setDisableCacheUrls(UserCenterApi.FETCH_COLLECT_TOPIC_LIST);
            setDisableCacheUrls(UserCenterApi.FETCH_LAUNCH_TOPIC_LIST);
            setDisableCacheUrls(UserCenterApi.FETCH_PERSONAL_CENTER_INFO);

        } else if (cacheName.equals(GROUP_CACHE_TIME)) {
            disableCacheTime = 30;
            setDisableCacheUrls(GroupApi.RECOMMEND_GROUP_LIST);
            setDisableCacheUrls(GroupApi.SEARCH_GROUP_LIST);
            setDisableCacheUrls(GroupApi.GROUP_INFO);
            setDisableCacheUrls(GroupApi.GROUP_MEMBERS_INFO);

            setDisableCacheUrls(UserCenterApi.FETCH_MY_GROUPS_LIST);
            setDisableCacheUrls(UserCenterApi.FETCH_PERSONAL_CENTER_INFO);

        } else if (cacheName.equals(THEME_CACHE_TIME)) {
            disableCacheTime = 10;
            setDisableCacheUrls(ProductApi.FETCH_THEME_INFO);
            setDisableCacheUrls(ProductApi.SEARCH_THEME);
            setDisableCacheUrls(ProductApi.FETCH_THEME_COLLECTED_AMOUNT);

            setDisableCacheUrls(UserCenterApi.FETCH_COLLECT_MULTIPLE_PRODUCT_LIST);

        } else if (cacheName.equals(PRODUCT_CACHE_TIME)) {
            disableCacheTime = 10;
            setDisableCacheUrls(ProductApi.FETCH_THEME_PRODUCTS);
            setDisableCacheUrls(ProductApi.FETCH_PRODUCT_DETAIL_INFO);
            setDisableCacheUrls(ProductApi.SEARCH_PRODUCT);

            setDisableCacheUrls(UserCenterApi.FETCH_SINGLE_PRODUCT_LIST);

        }

        long cacheTimeMillis = System.currentTimeMillis() + 1000 * 60 * disableCacheTime;
        SPUtils.put(mContext, cacheName, cacheTimeMillis);
    }
}
