package com.tongban.im.activity.base;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tongban.corelib.utils.ScreenUtils;
import com.tongban.corelib.widget.view.indicator.CirclePageIndicator;
import com.tongban.corelib.widget.view.ptz.PullToZoomBase;
import com.tongban.corelib.widget.view.ptz.PullToZoomScrollViewEx;
import com.tongban.corelib.widget.view.transformer.ScalePageTransformer;
import com.tongban.im.R;
import com.tongban.im.activity.user.ChildInfoActivity;
import com.tongban.im.adapter.UserInfoAdapter;
import com.tongban.im.common.Consts;
import com.tongban.im.common.TransferCenter;
import com.tongban.im.model.User;

/**
 * 通用的用户中心父类
 * Created by zhangleilei on 2015/09/01.
 */
public abstract class UserBaseActivity extends BaseToolBarActivity implements View.OnClickListener {

    private ImageView ivClose;

    private PullToZoomScrollViewEx lvUserCenter;
    private View vHeaderBottom;
    private ImageView ivZoomTop;
    private ImageView ivZoomBottom;
    private View rlFansNum, rlFollowNum, rlGroupNum;
    private ImageView ivUserPortrait;
    private ViewPager vpChildInfo;
    private CirclePageIndicator indicator;
    private UserInfoAdapter mAdapter;

    protected TextView tvFansCount, tvFollowCount, tvGroupCount;
    protected View headView, zoomView, contentView;
    protected TextView tvSetChildInfo;

    protected User mUserInfo = new User();

    private float alphaValue = 1.0f;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_personal_center;
    }

    @Override
    protected void initView() {

        ivClose = (ImageView) findViewById(R.id.iv_close);

        lvUserCenter = (PullToZoomScrollViewEx) findViewById(R.id.sv_user_center);
        headView = LayoutInflater.from(this).inflate(R.layout.ptz_head_view_personal_center, null, false);
        zoomView = LayoutInflater.from(this).inflate(R.layout.ptz_zoom_view, null, false);
        contentView = LayoutInflater.from(this).inflate(R.layout.ptz_content_view, null, false);

        lvUserCenter.setHeaderView(headView);
        lvUserCenter.setZoomView(zoomView);
        lvUserCenter.setScrollContentView(contentView);
        //headView
        vpChildInfo = (ViewPager) headView.findViewById(R.id.vp_container);
        indicator = (CirclePageIndicator) headView.findViewById(R.id.lpi_indicator);
        tvSetChildInfo = (TextView) headView.findViewById(R.id.tv_set_child_info);
        vHeaderBottom = headView.findViewById(R.id.ll_relationship);
        tvFansCount = (TextView) headView.findViewById(R.id.tv_fans_num);
        tvFollowCount = (TextView) headView.findViewById(R.id.tv_follow_num);
        tvGroupCount = (TextView) headView.findViewById(R.id.tv_group_num);
        rlFansNum = headView.findViewById(R.id.rl_fans_num);
        rlFollowNum = headView.findViewById(R.id.rl_follow_num);
        rlGroupNum = headView.findViewById(R.id.rl_group_num);

        rlFansNum.setEnabled(false);
        rlFollowNum.setEnabled(false);
        rlGroupNum.setEnabled(false);

        ivUserPortrait = (ImageView) headView.findViewById(R.id.iv_user_portrait);
        //zoomView
        ivZoomTop = (ImageView) zoomView.findViewById(R.id.iv_zoom_top);
        ivZoomBottom = (ImageView) zoomView.findViewById(R.id.iv_zoom_bottom);

        int mScreenWidth = ScreenUtils.getScreenWidth(mContext);
        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth,
                (int) (3.0F * (mScreenWidth / 4.0F)));
        lvUserCenter.setHeaderLayoutParams(localObject);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        ivClose.setOnClickListener(this);

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

        rlFansNum.setOnClickListener(this);
        rlFollowNum.setOnClickListener(this);
        rlGroupNum.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //关闭个人中心
        if (v == ivClose) {
            finish();
        }
        //跳转到粉丝界面
        else if (v == rlFansNum) {
            TransferCenter.getInstance().startRelationship(Consts.TAG_FANS,
                    mUserInfo.getUser_id());
        }
        //跳转到关注列表界面
        else if (v == rlFollowNum) {
            TransferCenter.getInstance().startRelationship(Consts.TAG_Follow,
                    mUserInfo.getUser_id());
        }
        //我的圈子
        if (v == rlGroupNum) {
            TransferCenter.getInstance().startMyGroupList(mUserInfo.getUser_id());
        } else {
            if (v.getId() == R.id.tv_name) {
                startActivity(new Intent(this, ChildInfoActivity.class));
            }
        }
    }

    protected void setDataInfo(User user) {
        mUserInfo = user;

        rlFansNum.setEnabled(true);
        rlFollowNum.setEnabled(true);
        rlGroupNum.setEnabled(true);

        if (mUserInfo.getPortrait_url() != null) {
            Glide.with(mContext).load(mUserInfo.getPortrait_url().getMin()).into(ivUserPortrait);
            Glide.with(mContext).load(mUserInfo.getPortrait_url().getMid()).into(ivZoomBottom);
        } else {
            ivZoomBottom.setImageResource(R.drawable.rc_default_portrait);
            ivUserPortrait.setImageResource(R.drawable.rc_default_portrait);
        }
        if (mUserInfo.getChild_info() != null &&
                mUserInfo.getChild_info().size() > 0) {
            mAdapter = new UserInfoAdapter(mContext, mUserInfo.getChild_info(),mUserInfo.getUser_id());
            vpChildInfo.setAdapter(mAdapter);
            indicator.setViewPager(vpChildInfo);
            vpChildInfo.setPageTransformer(true, new ScalePageTransformer());
        } else {
            tvSetChildInfo.setVisibility(View.VISIBLE);
        }
        tvFansCount.setText(String.valueOf(mUserInfo.getFans_amount()));
        tvFollowCount.setText(String.valueOf(mUserInfo.getFocused_amount()));
        tvGroupCount.setText(mUserInfo.getGroupAmount());
    }

}
