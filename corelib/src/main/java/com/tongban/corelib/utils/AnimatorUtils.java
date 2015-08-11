package com.tongban.corelib.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.view.View;


/**
 * 动画效果工具类
 * Created by zhangleilei on 15/7/17.
 */
public class AnimatorUtils {

    /**
     * 动画透明/不透明
     *
     * @param obj     对象
     * @param visible 显示/隐藏
     */
    public static void animatorToAlpha(final View obj, final int visible) {
        float start = visible == View.VISIBLE ? 0.0f : 0.0f;
        float end = visible == View.GONE ? 1.0f : 0.0f;
        if (visible == View.VISIBLE) {
            obj.setVisibility(visible);
        }
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(obj, "alpha", end, start);
        objectAnimator.setDuration(500);
        objectAnimator.start();
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                obj.setVisibility(visible);
            }
        });
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
}
