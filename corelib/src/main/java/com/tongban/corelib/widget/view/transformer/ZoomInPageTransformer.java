package com.tongban.corelib.widget.view.transformer;

import android.annotation.SuppressLint;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

/**
 * viewpager过场动画
 */
public class ZoomInPageTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.7f;
    private static final float MIN_ALPHA = 0.2f;

    @SuppressLint("NewApi")
    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();
        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setAlpha(0);

        } else if (position <= 0) {
            // Use the default slide transition when moving to the left page
            view.setAlpha(1 + position);
            float scaleFactor = MIN_SCALE
                    + (1 - MIN_SCALE) * (1 - Math.abs(position));
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
        }
        //a页滑动至b页 ； a页从 0.0 -1 ；b页从1 ~ 0.0
        else if (position <= 1) {

            view.setAlpha(1 - position);

            // [-1,1]
            // Modify the default slide transition to shrink the page as well
            float scaleFactor = MIN_SCALE
                    + (1 - MIN_SCALE) * (1 - Math.abs(position));
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);

            // Fade the page relative to its size.
            view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE)
                    / (1 - MIN_SCALE) * (1 - MIN_ALPHA));

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setAlpha(0);
        }
    }
}