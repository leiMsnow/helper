package com.tongban.im.fragment.user;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tb.api.model.user.User;
import com.tb.api.utils.TransferCenter;
import com.tongban.corelib.utils.AnimatorUtils;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.corelib.utils.ScreenUtils;
import com.tongban.corelib.widget.view.CircleImageView;
import com.tongban.corelib.widget.view.indicator.CirclePageIndicator;
import com.tongban.corelib.widget.view.ptz.PullToZoomBase;
import com.tongban.corelib.widget.view.ptz.PullToZoomScrollViewEx;
import com.tongban.corelib.widget.view.transformer.ScalePageTransformer;
import com.tongban.im.R;
import com.tongban.im.activity.user.MyInfoActivity;
import com.tongban.im.adapter.UserInfoAdapter;
import com.tongban.im.common.Consts;
import com.tongban.im.fragment.base.BaseToolBarFragment;

import butterknife.Bind;
import butterknife.OnClick;

public class UserBaseFragment extends BaseToolBarFragment implements View.OnClickListener {
    @Bind(R.id.sv_user_center)
    PullToZoomScrollViewEx lvUserCenter;
    @Bind(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
//    @Bind(R.id.tv_title)
//    TextView tvUserName;
//    @Bind(R.id.iv_close)
//    ImageView ivClose;
    //headerView
//    @Bind(R.id.iv_user_portrait)
    CircleImageView ivUserPortrait;
    //    @Bind(R.id.vp_container)
    ViewPager vpChildInfo;
    //    @Bind(R.id.lpi_indicator)
    CirclePageIndicator indicator;

    //    @Bind(R.id.rl_action_parent)
    RelativeLayout rlActionParent;
    //    @Bind(R.id.tv_follow_num)
    protected TextView tvFollowCount;
    //    @Bind(R.id.rl_follow_num)
    LinearLayout rlFollowNum;
    //    @Bind(R.id.tv_fans_num)
    TextView tvFansCount;
    //    @Bind(R.id.rl_fans_num)
    LinearLayout rlFansNum;
    //    @Bind(R.id.tv_group_num)
    TextView tvGroupCount;
    //    @Bind(R.id.rl_group_num)
    LinearLayout rlGroupNum;
    //    @Bind(R.id.ll_relationship)
    LinearLayout vHeaderBottom;
    // contentView
//    @Bind(R.id.tv_my_topic)
    protected TextView tvMyTopic;
    //    @Bind(R.id.tv_my_collect)
    protected TextView tvMyCollect;
    //    @Bind(R.id.tv_settings)
    protected TextView tvSettings;
    //    @Bind(R.id.iv_focus)
    protected Button ivFocus;
    //    @Bind(R.id.iv_cancel_focus)
    protected Button ivCancelFocus;
    //    @Bind(R.id.iv_private_chat)
    protected Button ivPrivateChat;

    //zoomView
//    @Bind(R.id.iv_zoom_top)
    ImageView ivZoomTop;

    private UserInfoAdapter mAdapter;

    protected View headView, zoomView, contentView;

    protected User mUserInfo = new User();

    private float alphaValue = 1.0f;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_personal_center;
    }

    @Override
    protected void initData() {

        headView = LayoutInflater.from(mContext).inflate(R.layout.ptz_head_view_personal_center, null, false);
        zoomView = LayoutInflater.from(mContext).inflate(R.layout.ptz_zoom_view, null, false);
        contentView = LayoutInflater.from(mContext).inflate(R.layout.ptz_content_view, null, false);

        lvUserCenter.setHeaderView(headView);
        lvUserCenter.setZoomView(zoomView);
        lvUserCenter.setScrollContentView(contentView);

        //headView
        ivUserPortrait = (CircleImageView) headView.findViewById(R.id.iv_user_portrait);
        vpChildInfo = (ViewPager) headView.findViewById(R.id.vp_container);
        rlActionParent = (RelativeLayout) getActivity().findViewById(R.id.rl_action_parent);
        indicator = (CirclePageIndicator) headView.findViewById(R.id.lpi_indicator);
        vHeaderBottom = (LinearLayout) headView.findViewById(R.id.ll_relationship);
        tvFansCount = (TextView) headView.findViewById(R.id.tv_fans_num);
        tvFollowCount = (TextView) headView.findViewById(R.id.tv_follow_num);
        tvGroupCount = (TextView) headView.findViewById(R.id.tv_group_num);
        rlFansNum = (LinearLayout) headView.findViewById(R.id.rl_fans_num);
        rlFollowNum = (LinearLayout) headView.findViewById(R.id.rl_follow_num);
        rlGroupNum = (LinearLayout) headView.findViewById(R.id.rl_group_num);
        ivFocus = (Button) headView.findViewById(R.id.iv_focus);
        ivPrivateChat = (Button) headView.findViewById(R.id.iv_private_chat);
        ivCancelFocus = (Button) headView.findViewById(R.id.iv_cancel_focus);
        // contentView
        tvMyTopic = (TextView) contentView.findViewById(R.id.tv_my_topic);
        tvMyCollect = (TextView) contentView.findViewById(R.id.tv_my_collect);
        tvSettings = (TextView) contentView.findViewById(R.id.tv_settings);
        // zoomView
        ivZoomTop = (ImageView) zoomView.findViewById(R.id.iv_zoom_top);

        rlFansNum.setEnabled(false);
        rlFollowNum.setEnabled(false);
        rlGroupNum.setEnabled(false);

        int mScreenWidth = ScreenUtils.getScreenWidth(mContext);
        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth,
                (int) (3.0F * (mScreenWidth / 4.0F)));
        lvUserCenter.setHeaderLayoutParams(localObject);

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
                    AnimatorUtils.animatorToAlpha(rlActionParent, startValue, alphaValue, 10);
                }
            }

            @Override
            public void onPullZoomEnd() {
                AnimatorUtils.animatorToAlpha(ivZoomTop, alphaValue, 1.0f, 300);
                AnimatorUtils.animatorToAlpha(ivUserPortrait, alphaValue, 1.0f, 300);
                AnimatorUtils.animatorToAlpha(vpChildInfo, alphaValue, 1.0f, 300);
                AnimatorUtils.animatorToAlpha(rlActionParent, alphaValue, 1.0f, 300);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        alphaValue = 1.0f;
                    }
                }, 300);
            }
        });

        ivUserPortrait.setOnClickListener(this);

        rlFansNum.setOnClickListener(this);
        rlFollowNum.setOnClickListener(this);
        rlGroupNum.setOnClickListener(this);

    }

//    @OnClick({R.id.iv_close})
    public void onClick(View v) {
        //关闭个人中心
//        if (v == ivClose) {
//            getActivity().finish();
//        }
        //跳转到粉丝界面
         if (v == rlFansNum) {
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
                mContext.startActivity(new Intent(mContext, MyInfoActivity.class));
        }

    }

    protected void setDataInfo(User user) {
        mUserInfo = user;

        rlFansNum.setEnabled(true);
        rlFollowNum.setEnabled(true);
        rlGroupNum.setEnabled(true);

        if (!TextUtils.isEmpty(mUserInfo.getNick_name())) {
//            tvUserName.setText(mUserInfo.getNick_name());
            mCollapsingToolbarLayout.setTitle(mUserInfo.getNick_name());
        }

        if (mUserInfo.getPortraitUrl() != null) {
            setUserPortrait(mUserInfo.getPortraitUrl().getMin(), ivUserPortrait);
        } else {
            int resId = (Integer) SPUtils.
                    get(mContext, SPUtils.NO_CLEAR_FILE, Consts.KEY_DEFAULT_PORTRAIT, 0);
            ivUserPortrait.setImageResource(resId);
        }
        if (mUserInfo.getChildInfo() != null &&
                mUserInfo.getChildInfo().size() > 0) {
            mAdapter = new UserInfoAdapter(mContext, mUserInfo.getChildInfo(), mUserInfo.getUser_id());
            vpChildInfo.setAdapter(mAdapter);
            indicator.setViewPager(vpChildInfo);
            vpChildInfo.setPageTransformer(true, new ScalePageTransformer());
        }
        tvFansCount.setText(String.valueOf(mUserInfo.getFans_amount()));
        tvFollowCount.setText(String.valueOf(mUserInfo.getFocused_amount()));
        tvGroupCount.setText(mUserInfo.getGroupAmount());
    }
}
