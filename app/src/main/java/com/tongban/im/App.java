package com.tongban.im;

import com.baidu.mapapi.SDKInitializer;
import com.iflytek.cloud.Setting;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.tb.api.base.BaseApi;
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
        // Log开关
        LogUtil.isDebug = true;
        // 设置服务器地址
        BaseApi.getInstance().setHostUrl(this, BaseApi.DEFAULT_HOST);
        // 融云初始化
        RongIM.init(this);
        // 融云事件初始化
        RongCloudEvent.init(this);
        // 百度地图初始化
        SDKInitializer.initialize(this);
        // 科大讯飞语音初始化
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=56188312");
        Setting.setShowLog(false);
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
