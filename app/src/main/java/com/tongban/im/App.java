package com.tongban.im;

import com.baidu.mapapi.SDKInitializer;
import com.tongban.corelib.base.BaseApplication;
import com.tongban.corelib.utils.LogUtil;
import com.tongban.im.api.BaseApi;

import io.rong.imkit.RongIM;

/**
 * 主程序入口
 */
public class App extends BaseApplication {

    private static App mApp;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;

        BaseApi.setHostUrl(mApp,1);

        RongIM.init(this);
        RongCloudEvent.init(this);
        /** Log开关 */
        LogUtil.isDebug = true;
        // 百度地图初始化
        SDKInitializer.initialize(getApplicationContext());
    }

    /**
     * 获取Application的单例
     *
     * @return IMApplication
     */
    public static App getInstance() {
        return mApp;
    }
}
