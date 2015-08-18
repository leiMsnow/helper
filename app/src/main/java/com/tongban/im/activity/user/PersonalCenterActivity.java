package com.tongban.im.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tongban.corelib.utils.ScreenUtils;
import com.tongban.corelib.widget.view.BadgeView;
import com.tongban.corelib.widget.view.ptz.PullToZoomScrollViewEx;
import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;

/**
 * 个人中心
 */
public class PersonalCenterActivity extends BaseToolBarActivity implements View.OnClickListener {

    private PullToZoomScrollViewEx lvUserCenter;
    private BadgeView fansBadgeView;
    private LinearLayout llUserInfo;
    private RelativeLayout rlFansNum, rlFollowNum, rlTopicNum;
    private TextView tvFans, tvFollow, tvTopic;
    private TextView tvMyGroup, tvMyCollect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutRes() {

        return R.layout.activity_personal_center;
    }

    @Override
    protected void initView() {
        lvUserCenter = (PullToZoomScrollViewEx) findViewById(R.id.sv_user_center);
        View headView = LayoutInflater.from(this).inflate(R.layout.ptz_head_view, null, false);
        View zoomView = LayoutInflater.from(this).inflate(R.layout.ptz_zoom_view, null, false);
        View contentView = LayoutInflater.from(this).inflate(R.layout.ptz_content_view, null, false);
        lvUserCenter.setHeaderView(headView);
        lvUserCenter.setZoomView(zoomView);
        lvUserCenter.setScrollContentView(contentView);

        llUserInfo = (LinearLayout) headView.findViewById(R.id.ll_user_info);
        rlFansNum = (RelativeLayout) headView.findViewById(R.id.rl_fans_num);
        rlFollowNum = (RelativeLayout) headView.findViewById(R.id.rl_follow_num);
        rlTopicNum = (RelativeLayout) headView.findViewById(R.id.rl_topic_num);

        tvFans = (TextView) headView.findViewById(R.id.tv_fans);
        tvFollow = (TextView) headView.findViewById(R.id.tv_follow);
        tvTopic = (TextView) headView.findViewById(R.id.tv_topic);

        tvMyGroup = (TextView) contentView.findViewById(R.id.tv_my_group);
        tvMyCollect = (TextView) contentView.findViewById(R.id.tv_my_collect);

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
        llUserInfo.setOnClickListener(this);
        rlFansNum.setOnClickListener(this);
        rlFollowNum.setOnClickListener(this);
        rlTopicNum.setOnClickListener(this);

        tvMyGroup.setOnClickListener(this);
        tvMyCollect.setOnClickListener(this);

        tvFans.setOnClickListener(this);
        tvFollow.setOnClickListener(this);
        tvTopic.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        //跳转到个人资料界面
        if (v == llUserInfo) {
            startActivity(new Intent(this, PersonalInfoActivity.class));
        }
        //跳转到粉丝界面
        else if (v == rlFansNum || v == tvFans) {
            Intent intent = new Intent(this, MyRelationshipActivity.class);
            intent.putExtra("Tag", "Fans");
            startActivity(intent);
        }
        //跳转到关注界面
        else if (v == rlFollowNum || v == tvFollow) {
            Intent intent = new Intent(this, MyRelationshipActivity.class);
            intent.putExtra("Tag", "Follow");
            startActivity(intent);
        }
        //跳转到话题界面
        else if (v == rlTopicNum || v == tvTopic) {
            startActivity(new Intent(this, MyTopicActivity.class));
        }
        //跳转到我的圈子界面
        else if (v == tvMyGroup) {
            startActivity(new Intent(this, MyGroupActivity.class));
        }
        //跳转到我的收藏界面
        else if (v == tvMyCollect) {
            startActivity(new Intent(this, MyCollectActivity.class));
        }
    }
}
