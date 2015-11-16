package com.tongban.corelib.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.tongban.corelib.R;

import java.util.Random;


/**
 * 动画效果工具类
 * Created by zhangleilei on 15/7/17.
 */
public class AnimatorUtils {

    /**
     * 动画透明/不透明
     *
     * @param obj 对象
     */
    public static void animatorToAlpha(final View obj, float start, float end, int duration) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(obj, "alpha", start, end);
        objectAnimator.setDuration(duration);
        objectAnimator.start();
    }

    /**
     * 垂直显示/隐藏
     *
     * @param obj
     * @param visible
     */
    public static void animatorToY(final View obj, final int visible) {
        float start = visible == View.VISIBLE ? 0.0f : 1.0f;
        float end = visible == View.GONE ? 1.0f : 0.0f;
        if (visible == View.VISIBLE) {
            obj.setVisibility(visible);
        }
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(obj, "translationY", start, end);
        objectAnimator.setDuration(500);
        objectAnimator.start();
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                obj.setVisibility(visible);
            }
        });
    }

    public static void animatorPlane(final ImageView view, AnimatorListenerAdapter listenerAdapter) {

        int height = ScreenUtils.getScreenHeight(view.getContext());
        int width = ScreenUtils.getScreenWidth(view.getContext());
        int wh = DensityUtils.dp2px(view.getContext(), 36);
        Random random = new Random();
        int startX = 0;//random.nextInt(2);
        int startY = random.nextInt(height / 3 * 2) + height / 3;
        int endRotationX = 45;
        int endRotationY = -45;
        view.setImageResource(R.mipmap.ic_plane);
        if (startX > 0) {

            view.setImageResource(R.mipmap.ic_new_msg_x);
            startX = width - wh;
            startY = height;
            endRotationX = 0;
            endRotationY = 0;
        }


        ObjectAnimator animatorX = ObjectAnimator.ofFloat(view, View.X, startX, width - wh);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(view, View.Y, startY, 0);

        ObjectAnimator animatorRotationX = ObjectAnimator.ofFloat(view, View.ROTATION_X, 0, endRotationX);
        ObjectAnimator animatorRotationY = ObjectAnimator.ofFloat(view, View.ROTATION_Y, 0, endRotationY);

        AnimatorSet set = new AnimatorSet();
        set.setInterpolator(new LinearInterpolator());
        int durationRand = random.nextInt(30) + 5;
        set.setDuration(durationRand * 100);
        set.play(animatorX)
                .with(animatorY)
                .with(animatorRotationY)
                .with(animatorRotationX);
        set.start();
        set.addListener(listenerAdapter);

    }

    public static void animatorPlane1(View view) {
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(view, View.Y, view.getY(), 0);
        AnimatorSet set = new AnimatorSet();
        set.setInterpolator(new LinearInterpolator());
        set.setDuration(1500);
        set.play(animatorY);
        set.start();
    }

    /**
     * 抛物线
     *
     * @param view
     */
//    public static void paowuxian(final View view) {
//
//        view.setVisibility(View.VISIBLE);
//        ValueAnimator valueAnimator = new ValueAnimator();
//        valueAnimator.setDuration(3 * 1000);
//
//        valueAnimator.setObjectValues(new PointF(view.getLeft()
//                , ScreenUtils.getScreenWidth(view.getContext())));
//
//        valueAnimator.setInterpolator(new LinearInterpolator());
//        valueAnimator.setEvaluator(new TypeEvaluator<PointF>() {
//            // fraction = t / duration
//            @Override
//            public PointF evaluate(float fraction, PointF startValue,
//                                   PointF endValue) {
//                // x方向200px/s ，则y方向0.5 * 10 * t
//                PointF point = new PointF();
//                point.x = 200 * (1 - fraction) * 3;
//                point.y = 0.5f * 200 * ((1 - fraction) * 3) * ((1 - fraction) * 3);
//                LogUtil.d("pointX:" + point.x);
//                LogUtil.d("pointY:" + point.y);
//                return point;
//            }
//        });
//
//        valueAnimator.start();
//        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                PointF point = (PointF) animation.getAnimatedValue();
//                view.setX(point.x);
//                view.setY(point.y);
//
//            }
//
//        });
//        valueAnimator.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                view.setVisibility(View.GONE);
//            }
//        });
//    }

}
