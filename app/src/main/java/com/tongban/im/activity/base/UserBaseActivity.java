package com.tongban.im.activity.base;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tongban.corelib.utils.AnimatorUtils;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.corelib.utils.ScreenUtils;
import com.tongban.corelib.widget.view.indicator.CirclePageIndicator;
import com.tongban.corelib.widget.view.ptz.PullToZoomBase;
import com.tongban.corelib.widget.view.ptz.PullToZoomScrollViewEx;
import com.tongban.corelib.widget.view.transformer.ScalePageTransformer;
import com.tongban.im.R;
import com.tongban.im.activity.user.PersonalInfoActivity;
import com.tongban.im.adapter.UserInfoAdapter;
import com.tongban.im.common.Consts;
import com.tongban.im.common.TransferCenter;
import com.tongban.im.model.user.User;

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
    private TextView tvUserName;
    private ImageView ivUserPortrait;
    private ViewPager vpChildInfo;
    private CirclePageIndicator indicator;
    private UserInfoAdapter mAdapter;

    protected TextView tvFansCount, tvFollowCount, tvGroupCount;
    protected View headView, zoomView, contentView;
    protected TextView tvSetChildInfo;
    private View vActionParent;

    protected User mUserInfo = new User();

    private float alphaValue = 1.0f;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_personal_center;
    }

    @Override
    protected void initView() {

        ivClose = (ImageView) findViewById(R.id.iv_close);
        tvUserName = (TextView) findViewById(R.id.tv_name);

        lvUserCenter = (PullToZoomScrollViewEx) findViewById(R.id.sv_user_center);
        headView = LayoutInflater.from(this).inflate(R.layout.ptz_head_view_personal_center, null, false);
        zoomView = LayoutInflater.from(this).inflate(R.layout.ptz_zoom_view, null, false);
        contentView = LayoutInflater.from(this).inflate(R.layout.ptz_content_view, null, false);

        lvUserCenter.setHeaderView(headView);
        lvUserCenter.setZoomView(zoomView);
        lvUserCenter.setScrollContentView(contentView);
        //headView
        vpChildInfo = (ViewPager) headView.findViewById(R.id.vp_container);
        vActionParent = findViewById(R.id.rl_action_parent);
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
    protected void initListener() {
        ivClose.setOnClickListener(this);
        ivUserPortrait.setOnClickListener(this);

        lvUserCenter.setOnPullZoomListener(new PullToZoomBase.OnPullZoomListener() {
            @Override
            public void onPullZooming(int newScrollValue) {
                float scrollValue = -newScrollValue;
                float headerBottomHeight = vHeaderBottom.getHeight() * 2;
                float startValue = alphaValue;
                if (scrollValue < headerBottomHeight) {
                    alphaValue = (headerBottomHeight - scrollValue) / headerBottomHeight;

                    AnimatorUtils.animatorToAlpha(ivZoomTop, startValue, alphaValue, 10);
                    AnimatorUtils.animatorToAlpha(ivUserPortrait, startValue, alphaValue, 10);
                    AnimatorUtils.animatorToAlpha(vpChildInfo, startValue, alphaValue, 10);
                    AnimatorUtils.animatorToAlpha(vActionParent, startValue, alphaValue, 10);
                }
            }

            @Override
            public void onPullZoomEnd() {
                AnimatorUtils.animatorToAlpha(ivZoomTop, alphaValue, 1.0f, 300);
                AnimatorUtils.animatorToAlpha(ivUserPortrait, alphaValue, 1.0f, 300);
                AnimatorUtils.animatorToAlpha(vpChildInfo, alphaValue, 1.0f, 300);
                AnimatorUtils.animatorToAlpha(vActionParent, alphaValue, 1.0f, 300);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        alphaValue = 1.0f;
                    }
                }, 300);
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
            TransferCenter.getInstance().startRelationship(Consts.TAG_FOLLOW,
                    mUserInfo.getUser_id());
        }
        //我的圈子
        else if (v == rlGroupNum) {
            TransferCenter.getInstance().startMyGroupList(mUserInfo.getUser_id());
        }
        //跳转到个人资料页
        else if (v == ivUserPortrait) {
            if (SPUtils.get(mContext, Consts.USER_ID, "").equals(mUserInfo.getUser_id()))
                mContext.startActivity(new Intent(mContext, PersonalInfoActivity.class));
        }

    }

    protected void setDataInfo(User user) {
        mUserInfo = user;

        rlFansNum.setEnabled(true);
        rlFollowNum.setEnabled(true);
        rlGroupNum.setEnabled(true);

        if (!TextUtils.isEmpty(mUserInfo.getNick_name())) {
            tvUserName.setText(mUserInfo.getNick_name());
        }

        if (mUserInfo.getPortrait_url() != null) {
            setUserPortrait(mUserInfo.getPortrait_url().getMin(), ivUserPortrait);
//            setUserPortrait(mUserInfo.getPortrait_url().getMax(), ivZoomBottom);
        } else {
            int resId = (Integer) SPUtils.
                    get(mContext, SPUtils.NO_CLEAR_FILE, Consts.KEY_DEFAULT_PORTRAIT, 0);
//            ivZoomBottom.setImageResource(resId);
            ivUserPortrait.setImageResource(resId);
        }
        if (mUserInfo.getChild_info() != null &&
                mUserInfo.getChild_info().size() > 0) {
            mAdapter = new UserInfoAdapter(mContext, mUserInfo.getChild_info(), mUserInfo.getUser_id());
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
