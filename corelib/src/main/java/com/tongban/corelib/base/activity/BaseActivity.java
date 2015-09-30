package com.tongban.corelib.base.activity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.VelocityTracker;

import com.tongban.corelib.base.ActivityContainer;
import com.tongban.corelib.model.ImageFolder;
import com.tongban.corelib.utils.KeyBoardUtils;
import com.tongban.corelib.utils.LogUtil;
import com.tongban.corelib.utils.ScreenUtils;

/**
 * 基础activity，处理通用功能：
 * 1.记录打开的activity
 * 2.控制activity右滑关闭
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected Context mContext;

    private boolean isFinish = true;

    /**
     * 是否可以滑动删除
     *
     * @param isFinish
     */
    public void setTouchFinish(boolean isFinish) {
        this.isFinish = isFinish;
    }

    //手指上下滑动时的最小速度
    private static final int Y_SPEED_MIN = 1000;
    //手指上下滑时的最小距离
    private static final int Y_DISTANCE_MIN = 100;
    //手指向右滑动时的最小距离
    private int X_DISTANCE_MIN = 0;

    //记录手指按下时的横坐标。
    private float xD;
    //记录手指按下时的纵坐标。
    private float yD;
    //记录手指移动时的横坐标。
    private float xM;
    //记录手指移动时的纵坐标。
    private float yM;

    //用于计算手指滑动的速度。
    private VelocityTracker mVelocityTracker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityContainer.getInstance().addActivity(this);
        mContext = this;
        // 使得音量键控制媒体声音
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        // 设置竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // 设置滑动的距离为屏幕的1/2
        X_DISTANCE_MIN = ScreenUtils.getScreenWidth(mContext) / 2;
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


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        createVelocityTracker(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xD = event.getRawX();
                yD = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                xM = event.getRawX();
                yM = event.getRawY();
                //滑动的距离
                int distanceX = (int) (xM - xD);
                int distanceY = (int) (yM - yD);
                //获取顺时速度
                int ySpeed = getScrollVelocity();
                // 关闭Activity需满足以下条件：
                // x轴滑动的距离 > 滑动的距离
                // y轴滑动的距离 < 滑动的距离
                // y轴滑动的速度 < 设定的速度
                // 是否需要关闭activity
                if (distanceX > X_DISTANCE_MIN
                        && (distanceY < Y_DISTANCE_MIN
                        && distanceY > -Y_DISTANCE_MIN)
                        && ySpeed < Y_SPEED_MIN) {

                    if (isFinish)
                        finish();

                }
                break;
            case MotionEvent.ACTION_UP:
                recycleVelocityTracker();
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * 创建VelocityTracker对象，并将触摸界面的滑动事件加入到VelocityTracker当中。
     *
     * @param event
     */
    private void createVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    /**
     * 回收VelocityTracker对象。
     */
    private void recycleVelocityTracker() {
        mVelocityTracker.recycle();
        mVelocityTracker = null;
    }

    /**
     * @return 滑动速度，以每秒钟移动了多少像素值为单位。
     */
    private int getScrollVelocity() {
        mVelocityTracker.computeCurrentVelocity(1000);
        int velocity = (int) mVelocityTracker.getYVelocity();
        return Math.abs(velocity);
    }
}





