package com.tongban.im.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.corelib.utils.ScreenUtils;
import com.tongban.corelib.widget.view.DepthPageTransformer;
import com.tongban.corelib.widget.view.indicator.CirclePageIndicator;
import com.tongban.corelib.widget.view.ptz.PullToZoomScrollViewEx;
import com.tongban.im.R;
import com.tongban.im.activity.base.UserBaseActivity;
import com.tongban.im.adapter.UserInfoAdapter;
import com.tongban.im.api.UserCenterApi;
import com.tongban.im.common.Consts;
import com.tongban.im.common.TransferCenter;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.User;

/**
 * 个人中心
 */
public class PersonalCenterActivity extends UserBaseActivity implements View.OnClickListener {

    private ImageView ivClose;
    private RelativeLayout rlFansNum, rlFollowNum, rlGroupNum;
    private TextView tvFansCount, tvFollowCount, tvGroupCount;
    private TextView tvFans, tvFollow, tvGroup, tvMyTopic, tvMyCollect, tvSettings;
    private ImageView ivUserPortrait;
    private ViewPager vpChildInfo;
    private TextView tvSetChildInfo;
    private CirclePageIndicator indicator;
    private UserInfoAdapter mAdapter;

    private User user;



    @Override
    protected int getLayoutRes() {
        return R.layout.activity_personal_center;
    }

    @Override
    protected void initView() {
        lvUserCenter = (PullToZoomScrollViewEx) findViewById(R.id.sv_user_center);
        headView = LayoutInflater.from(this).inflate(R.layout.ptz_head_view_personal_center, null, false);
        zoomView = LayoutInflater.from(this).inflate(R.layout.ptz_zoom_view, null, false);
        contentView = LayoutInflater.from(this).inflate(R.layout.ptz_content_view, null, false);

        lvUserCenter.setHeaderView(headView);
        lvUserCenter.setZoomView(zoomView);
        lvUserCenter.setScrollContentView(contentView);

        //headView
        vHeaderBottom = headView.findViewById(R.id.ll_relationship);
        //zoomView
        ivZoomTop = (ImageView) zoomView.findViewById(R.id.iv_zoom_top);

        int mScreenWidth = ScreenUtils.getScreenWidth(mContext);
        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth,
                (int) (3.0F * (mScreenWidth / 4.0F)));
        lvUserCenter.setHeaderLayoutParams(localObject);        ivClose = (ImageView) findViewById(R.id.iv_close);

        vpChildInfo = (ViewPager) findViewById(R.id.vp_container);
        tvSetChildInfo = (TextView) findViewById(R.id.tv_set_child_info);
        indicator = (CirclePageIndicator) findViewById(R.id.lpi_indicator);
        //headView
        ivUserPortrait = (ImageView) headView.findViewById(R.id.iv_user_portrait);
        rlFansNum = (RelativeLayout) headView.findViewById(R.id.rl_fans_num);
        rlFollowNum = (RelativeLayout) headView.findViewById(R.id.rl_follow_num);
        rlGroupNum = (RelativeLayout) headView.findViewById(R.id.rl_group_num);
        tvFansCount = (TextView) headView.findViewById(R.id.tv_fans_num);
        tvFollowCount = (TextView) headView.findViewById(R.id.tv_follow_num);
        tvGroupCount = (TextView) headView.findViewById(R.id.tv_group_num);
        tvFans = (TextView) headView.findViewById(R.id.tv_fans);
        tvFollow = (TextView) headView.findViewById(R.id.tv_follow);
        tvGroup = (TextView) headView.findViewById(R.id.tv_group);
        //zoomView
        ivZoomTop = (ImageView) zoomView.findViewById(R.id.iv_zoom_top);
        //contentView
        tvMyTopic = (TextView) contentView.findViewById(R.id.tv_my_topic);
        tvMyCollect = (TextView) contentView.findViewById(R.id.tv_my_collect);
        tvSettings = (TextView) contentView.findViewById(R.id.tv_settings);
        tvMyTopic.setVisibility(View.VISIBLE);
        tvMyCollect.setVisibility(View.VISIBLE);
        tvSettings.setVisibility(View.VISIBLE);

    }

    @Override
    protected void initData() {
        UserCenterApi.getInstance().fetchPersonalCenterInfo(this);
    }

    @Override
    protected void initListener() {
        super.initListener();
        ivClose.setOnClickListener(this);
        tvSetChildInfo.setOnClickListener(this);

        rlFansNum.setOnClickListener(this);
        rlFollowNum.setOnClickListener(this);
        rlGroupNum.setOnClickListener(this);

        tvMyCollect.setOnClickListener(this);
        tvMyTopic.setOnClickListener(this);
        tvSettings.setOnClickListener(this);

        tvFans.setOnClickListener(this);
        tvFollow.setOnClickListener(this);
        tvGroup.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        //跳转到粉丝界面
        if (v == rlFansNum || v == tvFans) {
            Intent intent = new Intent(this, MyRelationshipActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("Tag", "Fans");
            intent.putExtras(bundle);
            startActivity(intent);
        }
        //跳转到关注列表界面
        else if (v == rlFollowNum || v == tvFollow) {
            Intent intent = new Intent(this, MyRelationshipActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("Tag", "Follow");
            intent.putExtras(bundle);
            startActivity(intent);
        }
        //跳转到我的圈子界面
        else if (v == rlGroupNum || v == tvGroup) {
            startActivity(new Intent(this, MyGroupActivity.class));
        }
        //跳转到话题界面
        else if (v == tvMyTopic) {
            startActivity(new Intent(this, MyTopicActivity.class));
        }
        //跳转到我的收藏界面
        else if (v == tvMyCollect) {
            startActivity(new Intent(this, MyCollectActivity.class));
        }
        //关闭个人中心
        else if (v == ivClose) {
            finish();
        }
        //设置 // TODO: 9/1/15 暂时写为注销登录
        else if (v == tvSettings) {
            SPUtils.put(mContext, Consts.USER_ID, "");
            TransferCenter.getInstance().startLogin(true);
        }
        //跳转到设置宝宝信息界面
        else if (v == tvSetChildInfo) {
            startActivity(new Intent(this, ChildInfoActivity.class));
        }
    }


    /**
     * 返回个人中心数据Event
     */
    public void onEventMainThread(BaseEvent.PersonalCenterEvent obj) {
        this.user = obj.user;
        if (user.getPortrait_url() != null) {
            Glide.with(mContext).load(user.getPortrait_url().getMin()).into(ivUserPortrait);
        } else {
            ivUserPortrait.setImageResource(R.drawable.rc_default_portrait);
        }
        if (user.getChild_info() != null) {
            mAdapter = new UserInfoAdapter(mContext, user.getChild_info());
            vpChildInfo.setAdapter(mAdapter);
            indicator.setViewPager(vpChildInfo);
            vpChildInfo.setPageTransformer(true, new DepthPageTransformer());
        } else {
            tvSetChildInfo.setVisibility(View.VISIBLE);
        }
        tvFansCount.setText(String.valueOf(user.getFans_amount()));
        tvFollowCount.setText(String.valueOf(user.getFocused_amount()));
        tvGroupCount.setText(user.getGroupAmount());
    }

    /**
     * 关注Event
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.FocusEvent obj) {

        if (obj.isFocus) {
            tvFollowCount.setText(String.valueOf(Integer.parseInt(tvFollowCount.getText().toString()) + 1));
        } else {
            tvFollowCount.setText(String.valueOf(Integer.parseInt(tvFollowCount.getText().toString()) - 1));
        }
    }

}
