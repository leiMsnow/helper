package com.tongban.im;

import com.tongban.corelib.base.BaseApplication;
import com.tongban.corelib.utils.LogUtil;

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

        RongIM.init(this);
        /** Log开关 */
        LogUtil.isDebug = true;
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
