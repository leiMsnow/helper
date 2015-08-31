package com.tongban.corelib.base.activity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tongban.corelib.base.ActivityContainer;
import com.tongban.corelib.utils.KeyBoardUtils;

/**
 * 基础activity，处理通用功能：
 * 1.统一的模板方法
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityContainer.getInstance().addActivity(this);
        mContext = this;
        // 使得音量键控制媒体声音
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        // 设置竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    protected void onPause() {
        super.onPause();
        KeyBoardUtils.hideSoftKeyboard(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityContainer.getInstance().removeActivity(this);
    }
}





