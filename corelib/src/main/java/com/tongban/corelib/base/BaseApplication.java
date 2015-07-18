package com.tongban.corelib.base;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by zhangleilei on 15/7/8.
 */
public class BaseApplication extends Application {
    private static BaseApplication mApp;
    /**
     * Volley请求队列
     */
    private RequestQueue mRequestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
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
}
