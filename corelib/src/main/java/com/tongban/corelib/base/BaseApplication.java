package com.tongban.corelib.base;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tongban.corelib.utils.SPUtils;

/**
 * Created by zhangleilei on 15/7/8.
 */
public class BaseApplication extends Application {

    private static BaseApplication mApp;
    /**
     * Volley请求队列
     */
    private RequestQueue mRequestQueue;

    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        if (isSaveCrash())
            MCrashHandler.getInstance().init(this);
        super.onCreate();
        mApp = this;
        refWatcher = LeakCanary.install(this);
        /** Volley队列 */
        mRequestQueue = Volley.newRequestQueue(this);
    }

    /**
     * 获取Application的单例
     *
     * @return BaseApplication
     */
    public static BaseApplication getInstance() {
        return mApp;
    }

    /**
     * 获取Volley队列
     *
     * @return RequestQueue
     */
    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public static RefWatcher getRefWatcher() {
        return getInstance().refWatcher;
    }

    public boolean isSaveCrash() {
        return (boolean) SPUtils.get(this, "crash", false);
    }

    public void saveCrash(boolean isSave) {
        SPUtils.put(this, "crash", isSave);
    }
}
