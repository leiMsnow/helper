package com.tongban.corelib.base.activity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;

import com.tongban.corelib.R;
import com.tongban.corelib.base.ActivityContainer;
import com.tongban.corelib.base.activity.swipeback.SwipeBackActivity;
import com.tongban.corelib.base.activity.swipeback.SwipeBackLayout;
import com.tongban.corelib.utils.KeyBoardUtils;
import com.tongban.corelib.utils.ScreenUtils;

/**
 * 基础activity，处理通用功能：
 * 1.记录打开的activity
 * 2.控制activity右滑关闭
 */
public abstract class BasisActivity extends SwipeBackActivity {

    protected Context mContext;
    protected SwipeBackLayout mSwipeBackLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityContainer.getInstance().addActivity(this);
        mContext = this;
        // 使得音量键控制媒体声音
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        // 设置竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);

        setupWindowAnimations();
    }


    private void setupWindowAnimations() {
        if (Build.VERSION.SDK_INT > 21) {

            Fade fade = new Fade();
            fade.setDuration(1000);
            getWindow().setEnterTransition(fade);

            Slide slide = new Slide();
            slide.setDuration(1000);
            getWindow().setReturnTransition(slide);

        }
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