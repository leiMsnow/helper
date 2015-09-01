package com.tongban.im.activity.base;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tongban.corelib.utils.ScreenUtils;
import com.tongban.corelib.widget.view.ptz.PullToZoomBase;
import com.tongban.corelib.widget.view.ptz.PullToZoomScrollViewEx;
import com.tongban.im.R;
import com.tongban.im.model.User;

/**
 * 通用的用户中心父类
 * Created by zhangleilei on 2015/09/01.
 */
public abstract class UserBaseActivity extends BaseToolBarActivity {

    protected PullToZoomScrollViewEx lvUserCenter;
    protected View vHeaderBottom;
    protected ImageView ivZoomTop;

    protected View headView, zoomView, contentView;

    private float alphaValue = 1.0f;

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

        lvUserCenter.setOnPullZoomListener(new PullToZoomBase.OnPullZoomListener() {
            @Override
            public void onPullZooming(int newScrollValue) {
                float scrollValue = -newScrollValue;
                float headerBottomHeight = vHeaderBottom.getHeight() * 2;
                float startValue = alphaValue;
                if (scrollValue < headerBottomHeight) {
                    alphaValue = (headerBottomHeight - scrollValue) / headerBottomHeight;

                    ObjectAnimator zoomTopAnim = ObjectAnimator.
                            ofFloat(ivZoomTop, "alpha", startValue, alphaValue);
                    AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.play(zoomTopAnim);
                    animatorSet.setDuration(10);
                    animatorSet.start();
                }
            }

            @Override
            public void onPullZoomEnd() {
                ObjectAnimator zoomTopAnim = ObjectAnimator.
                        ofFloat(ivZoomTop, "alpha", alphaValue, 1.0f);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.play(zoomTopAnim);
                animatorSet.setDuration(200);
                animatorSet.start();

                alphaValue = 1.0f;
            }
        });
    }


}
